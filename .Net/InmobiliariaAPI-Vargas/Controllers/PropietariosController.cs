using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using InmobiliariaAPI_Vargas.Services;
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

        private readonly EmailService _emailService;

        private readonly IConfiguration _configuration;
        public PropietariosController(BDContext context, IConfiguration configuration, IWebHostEnvironment environment, EmailService emailService)
        {
            _environment = environment;
            _context = context;
            _configuration = configuration;
            _passwordHasher = new PasswordHasher<Propietario>();
            _emailService = emailService;
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
        [Authorize]
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


        ////////////////////////////////RECUPERAR CLAVE CON CORREO//////////////////////////////////////////////
        [HttpPost("recuperarClave")]
        [AllowAnonymous]
        public async Task<IActionResult> RecuperarClave([FromForm] string email)
        {
            try
            {
                // Buscar al propietario por email
                var propietario = await _context.Propietarios.FirstOrDefaultAsync(p => p.Mail == email);
                if (propietario == null)
                {
                    return BadRequest("El correo no está registrado en la base de datos.");
                }

                if (propietario == null)
                {
                    return BadRequest("El correo electrónico no está registrado.");
                }

                // Generar el token JWT para restablecimiento de contraseña
                var key = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(_configuration["TokenAuthentication:SecretKey"]));
                var credenciales = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
                var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, propietario.Mail),
            new Claim("PropietarioId", propietario.Id.ToString()),
            new Claim("Purpose", "PasswordReset") // Propósito del token
        };



                var token = new JwtSecurityToken(
                    issuer: _configuration["TokenAuthentication:Issuer"],
                    audience: _configuration["TokenAuthentication:Audience"],
                    claims: claims,
                    expires: DateTime.Now.AddMinutes(5), // Token válido por 5 minutos
                    signingCredentials: credenciales
                );

                // Obtener el dominio o la IP
                var dominio = _environment.IsDevelopment() ? "192.168.1.106" : "www.misitio.com";


                var tokenString = new JwtSecurityTokenHandler().WriteToken(token);

                // Contenido del correo de restablecimiento
                var mensajeHtml = $@"
                        <div style='font-family: Arial, sans-serif; color: #333; line-height: 1.5;'>
                            <p style='margin-bottom: 16px;'>
                                Si usted <strong>NO</strong> solicitó recuperar su contraseña en la App de la Inmobiliaria, ignore este correo. 
                                Si desea continuar con la recuperación, haga clic en el siguiente enlace para establecer una nueva contraseña. 
                                <strong>Va a recibir un nuevo email con su nueva contraseña</strong>.
                            </p>
                            <form action='http://{dominio}:5001/api/propietarios/confirmarRestaurarClave' method='POST'>
                                <input type='hidden' name='token' value='{tokenString}'>
                                <button type='submit' style='background-color: #007bff; color: #fff; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer;'>
                                    Recuperar contraseña
                                </button>
                            </form>
                        </div>";

                // Enviar el correo
                await _emailService.EnviarCorreoAsync(email, "Recuperar contraseña", mensajeHtml);

                return Ok("Correo de restablecimiento de contraseña enviado.");
            }
            catch (Exception ex)
            {
                return BadRequest($"Error al generar el token o enviar el correo: {ex.Message}");
            }
        }



        ////////////////////////////////////CONFIRMAR RECUPERAR CLAVE//////////////////////////////////////////
        [HttpPost("confirmarRestaurarClave")]
        [AllowAnonymous]
        public async Task<IActionResult> ConfirmarRestaurarClave([FromForm] string token)
        {
            try
            {
                // Validar el token
                var tokenHandler = new JwtSecurityTokenHandler();
                var key = Encoding.ASCII.GetBytes(_configuration["TokenAuthentication:SecretKey"]);

                var tokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateLifetime = true,
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(key),
                    ValidIssuer = _configuration["TokenAuthentication:Issuer"],
                    ValidAudience = _configuration["TokenAuthentication:Audience"]
                };

                var principal = tokenHandler.ValidateToken(token, tokenValidationParameters, out SecurityToken validatedToken);

                // Extraer el Id del propietario del token
                var idClaim = principal.FindFirst("PropietarioId");
                if (idClaim == null)
                {
                    return BadRequest("Token no válido.");
                }

                // Verificar el formato del ID como un int
                if (!int.TryParse(idClaim.Value, out int propietarioId))
                {
                    return BadRequest("Formato de ID no válido.");
                }

                // Buscar al propietario por Id
                var propietario = await _context.Propietarios.FindAsync(propietarioId);
                if (propietario == null)
                {
                    return BadRequest("El propietario no existe.");
                }

                // Generar una nueva clave aleatoria de 4 letras
                string nuevaClave = GenerarClaveAleatoria(4);

                // Hashear la nueva clave usando la misma lógica que en el método de login

                string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                    password: nuevaClave,
                    salt: Encoding.ASCII.GetBytes(_configuration["Salt"]),
                    prf: KeyDerivationPrf.HMACSHA256,
                    iterationCount: 10000,
                    numBytesRequested: 256 / 8));
                // Actualizar la clave en la base de datos
                propietario.Password = hashed;
                _context.Propietarios.Update(propietario);
                try
                {
                    await _context.SaveChangesAsync();
                }
                catch (Exception ex)
                {
                    return BadRequest($"Error al guardar la nueva contraseña: {ex.Message}");
                }

                // Contenido del correo de confirmación de restablecimiento
                var mensajeHtml = $@"
                        <table width='100%' style='font-family: Arial, sans-serif; color: #333333; line-height: 1.5;'>
                            <tr>
                                <td style='padding: 16px 0;'>
                                    <p style='margin: 0 0 16px 0;'>
                                        Su contraseña ha sido restablecida. La nueva contraseña es: 
                                        <strong style='color: #d9534f;'>{nuevaClave}</strong>.
                                    </p>
                                    <p style='margin: 0;'>
                                        Puede <strong>Iniciar sesión</strong> y <strong>Modificarla</strong> desde su 
                                        <strong>Menú &rarr; Perfil &rarr; Modificar Clave</strong>.
                                    </p>
                                </td>
                            </tr>
                        </table>";
                // Enviar el correo
                await _emailService.EnviarCorreoAsync(propietario.Mail, "Contraseña restablecida", mensajeHtml);

                return Ok("Contraseña restablecida con éxito. Se ha enviado un correo con la nueva contraseña.");
            }
            catch (Exception ex)
            {
                return BadRequest($"Error al restablecer la contraseña: {ex.Message}");
            }
        }

        //////////////////////////////////////////////////////////////////////////////

        public string GenerarClaveAleatoria(int longitud)
        {
            const string letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            Random random = new Random();

            return new string(Enumerable.Repeat(letras, longitud)
                .Select(s => s[random.Next(s.Length)]).ToArray());
        }
        //////////////////////////////////////////////////////////////////////////////

        private bool PropietarioExists(int id)
        {
            return _context.Propietarios.Any(e => e.Id == id);
        }
        //////////////////////////////////////////////////////////////////////////////

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