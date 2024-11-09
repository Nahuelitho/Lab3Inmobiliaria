using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Identity;

using InmobiliariaAPI_Vargas.Models.Datos;

using Microsoft.AspNetCore.Authorization;

using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;

using System.Text;
//
namespace InmobiliariaAPI_Vargas.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class PropietariosController : ControllerBase
    {
        private readonly BDContext _context;
        private readonly PasswordHasher<Propietario> _passwordHasher;

        private readonly IWebHostEnvironment _environment;

        private readonly IConfiguration _configuration;
        public PropietariosController(BDContext context, IConfiguration configuration, IWebHostEnvironment environment)
        {
            _environment = environment;
            _context = context;
            _configuration = configuration;
            _passwordHasher = new PasswordHasher<Propietario>();
        }


        //////////////////////////////////ObtenerPropietario por Id////////////////////////////////////////////
        [HttpGet("{id}")]
        public async Task<ActionResult<Propietario>> GetPropietario(int id)
        {
            var propietario = await _context.Propietarios.FindAsync(id);

            if (propietario == null)
            {
                return NotFound();
            }

            return propietario;
        }
        /////////////////////////////////Obtener propietario////////////////////////////////////////////
        [HttpGet]
        public async Task<ActionResult<Propietario>> Get()
        {
            // Obtén el ID del propietario desde los claims del usuario logueado
            var propietarioIdClaim = User.FindFirstValue(ClaimTypes.Name);

            if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioId))
            {
                return Unauthorized("Usuario no logueado o ID no válido.");
            }

            // Busca al propietario por ID
            Propietario? propietario = await _context.Propietarios
                .SingleOrDefaultAsync(x => x.Id == propietarioId);

            if (propietario == null)
            {
                return NotFound("Propietario no encontrado.");
            }

            // Retorna la información del propietario
            return Ok(new
            {
                propietario.Id,
                propietario.Dni,
                propietario.Nombre,
                propietario.Apellido,
                propietario.Telefono,
                propietario.Mail,
                propietario.Avatar
            });
        }

        /////////////////////////////////Obtener Perfil Propietario/////////////////////////////////////////////

        [HttpGet("perfil")]
        [Authorize]
        public async Task<IActionResult> PerfilPropietario()
        {
            Console.WriteLine("entro al perfil");

            try
            {
                // Obtén el ID del propietario desde los claims del token JWT
                var propietarioIdClaim = User.Claims.FirstOrDefault(c => c.Type == "PropietarioId")?.Value;
                if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioId))
                {
                    return Unauthorized("Usuario no logueado.");
                }

                // Buscar al propietario por ID
                var propietario = await _context.Propietarios.FirstOrDefaultAsync(p => p.Id == propietarioId);
                if (propietario == null)
                {
                    return NotFound("Propietario no encontrado.");
                }

                return Ok(propietario);
            }
            catch (Exception ex)
            {
                return BadRequest($"Error: {ex.Message}");
            }
        }




        //////////////////////////////////Crear Propietario////////////////////////////////////////////


        [HttpPost]
        public async Task<ActionResult<Propietario>> PostPropietario([FromBody] Propietario propietario)
        {
            // Hashear la contraseña
            propietario.Password = HashPassword(propietario.Password);

            _context.Propietarios.Add(propietario);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetPropietario), new { id = propietario.Id }, propietario);
        }

        private string HashPassword(string password)
        {
            string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: password,
                salt: Encoding.ASCII.GetBytes(_configuration["Salt"]),
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 10000,
                numBytesRequested: 256 / 8));

            return hashed;
        }


        ///////////////////////////////////Editar Perfil Propietario///////////////////////////////////////////

        //El model de este endpoint se encuentra en Models-->Dtos-->PropietarioUpdateDto.cs
        [HttpPut("modificarPerfil")]
        [Authorize]
        public async Task<IActionResult> ModificarPerfil([FromForm] PropietarioUpdate updatedPropietario)
        {
            try
            {
                // Obtener el ID del propietario desde el token JWT
                var propietarioIdClaim = User.Claims.FirstOrDefault(c => c.Type == "PropietarioId")?.Value;

                if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioId))
                {
                    return Unauthorized("Token no contiene un ID válido");
                }

                // Buscar al propietario por su ID
                var propietario = await _context.Propietarios.FirstOrDefaultAsync(p => p.Id == propietarioId);

                if (propietario == null)
                {
                    return NotFound("Propietario no encontrado");
                }

                // Actualizar los datos con los valores recibidos en el DTO
                propietario.Dni = updatedPropietario.Dni;
                propietario.Nombre = updatedPropietario.Nombre;
                propietario.Apellido = updatedPropietario.Apellido;
                propietario.Telefono = updatedPropietario.Telefono;
                propietario.Mail = updatedPropietario.Mail;

                // Guardar los cambios en la base de datos
                await _context.SaveChangesAsync();

                // Regenerar el token JWT con los datos actualizados
                var key = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(_configuration["TokenAuthentication:SecretKey"]));
                var credenciales = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
                var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, propietario.Mail),
            new Claim("FullName", $"{propietario.Nombre} {propietario.Apellido}"),
            new Claim(ClaimTypes.Role, "Propietario"),
            new Claim("PropietarioId", propietario.Id.ToString()), // Asegúrate de incluir el ID
        };

                var token = new JwtSecurityToken(
                    issuer: _configuration["TokenAuthentication:Issuer"],
                    audience: _configuration["TokenAuthentication:Audience"],
                    claims: claims,
                    expires: DateTime.Now.AddMinutes(60),
                    signingCredentials: credenciales
                );

                // Retornar el nuevo token (si es necesario)
                return Ok(new { Message = "Perfil actualizado correctamente.", Token = new JwtSecurityTokenHandler().WriteToken(token) });
            }
            catch (Exception ex)
            {
                return BadRequest("Error al actualizar el perfil: " + ex.Message);
            }
        }


        ///////////////////////////////////Subir y editar Avatar Propietario///////////////////////////////////////////
        [HttpPut("modificarAvatar")]
        [Authorize]
        [Consumes("multipart/form-data")]
        public async Task<IActionResult> ModificarAvatarPropietario([FromForm] IFormFile avatarFile)
        {
            if (avatarFile == null || avatarFile.Length == 0)
            {
                return BadRequest("No se ha proporcionado una imagen válida.");
            }

            // Obtener el ID del propietario desde el token JWT
            var propietarioIdClaim = User.Claims.FirstOrDefault(c => c.Type == "PropietarioId")?.Value;
            if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioId))
            {
                return Unauthorized("Token no contiene un ID válido.");
            }

            var propietario = await _context.Propietarios.FindAsync(propietarioId);
            if (propietario == null)
            {
                return NotFound("Propietario no encontrado.");
            }

            try
            {
                // Define la ruta donde se guardará la imagen
                string uploadsFolder = Path.Combine(_environment.WebRootPath, "avatarPerfil");
                if (!Directory.Exists(uploadsFolder))
                {
                    Directory.CreateDirectory(uploadsFolder);
                }

                // Crea un nombre único para el archivo
                string fileName = $"{Guid.NewGuid()}_{avatarFile.FileName}";
                string filePath = Path.Combine(uploadsFolder, fileName);

                // Guarda el archivo en la carpeta especificada
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    await avatarFile.CopyToAsync(stream);
                }

                // Guarda la ruta del archivo en la base de datos asociada al propietario
                propietario.Avatar = $"/avatarPerfil/{fileName}";
                await _context.SaveChangesAsync();

                return Ok(new { Message = "Avatar actualizado con éxito", FilePath = $"/avatarPerfil/{fileName}" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error al guardar la imagen: {ex.Message}");
            }
        }

        //////////////////////////////////////////////////////////////////////////////
        [HttpPut("cambiarContraseña")]
        [Authorize]
        public async Task<IActionResult> CambiarContraseña([FromForm] CambiarContraseña model)
        {
            try
            {
                // Obtener el ID del propietario desde el token JWT
                var propietarioIdClaim = User.Claims.FirstOrDefault(c => c.Type == "PropietarioId")?.Value;

                if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioId))
                {
                    return Unauthorized("Token no contiene un ID válido");
                }

                // Buscar al propietario por su ID
                var propietario = await _context.Propietarios.FirstOrDefaultAsync(p => p.Id == propietarioId);

                if (propietario == null)
                {
                    return NotFound("Propietario no encontrado");
                }

                // Verificar si las contraseñas nuevas coinciden
                if (model.ContraseñaNueva != model.RepetirContraseña)
                {
                    return BadRequest("Las nuevas contraseñas no coinciden.");
                }

                // Hashear la clave ingresada
                string hashedCurrentPassword = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                    password: model.ContraseñaActual,
                    salt: Encoding.ASCII.GetBytes(_configuration["Salt"]),
                    prf: KeyDerivationPrf.HMACSHA256,
                    iterationCount: 10000,
                    numBytesRequested: 256 / 8));

                // Verificar si la contraseña actual es correcta
                if (propietario.Password != hashedCurrentPassword)
                {
                    return BadRequest("La contraseña actual es incorrecta.");
                }

                // Hashear la nueva clave
                string hashedNewPassword = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                    password: model.ContraseñaNueva,
                    salt: Encoding.ASCII.GetBytes(_configuration["Salt"]),
                    prf: KeyDerivationPrf.HMACSHA256,
                    iterationCount: 10000,
                    numBytesRequested: 256 / 8));

                // Actualizar la contraseña
                propietario.Password = hashedNewPassword;
                await _context.SaveChangesAsync();

                return Ok("Contraseña cambiada correctamente.");
            }
            catch (Exception ex)
            {
                return BadRequest("Error al cambiar la contraseña: " + ex.Message);
            }
        }
        //////////////////////////////////////////////////////////////////////////////

        private bool PropietarioExists(int id)
        {
            return _context.Propietarios.Any(e => e.Id == id);
        }

    }
    public class CambiarContraseña
    {
        required
        public string ContraseñaActual
        { get; set; }

        required
        public string ContraseñaNueva
        { get; set; }

        required

        public string RepetirContraseña
        { get; set; }



    }

}