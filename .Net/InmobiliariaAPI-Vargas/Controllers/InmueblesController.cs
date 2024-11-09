using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authorization;

namespace InmobiliariaAPI_Vargas.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class InmueblesController : ControllerBase
    {
        private readonly BDContext _context;
        private readonly IWebHostEnvironment _environment;

        public String mensaje;
        public InmueblesController(BDContext context, IWebHostEnvironment environment)
        {
            _environment = environment;
            _context = context;
        }
        ////////////////////Listar Inmuebles
        [HttpGet("listaInmuebles")]
        [Authorize]
        public async Task<ActionResult<IEnumerable<Inmueble>>> GetListaInmuebles()
        {
            // Obtener el ID del propietario del token
            var propietarioIdClaim = User.FindFirst("PropietarioId");
            if (propietarioIdClaim == null)
            {
                return Unauthorized(); // Retorna 401 si no se encuentra el ID
            }

            if (!int.TryParse(propietarioIdClaim.Value, out int id))
            {
                return BadRequest("ID de propietario no válido."); // Retorna 400 si el ID no es válido
            }

            var inmuebles = await _context.Inmueble
                                           .Where(i => i.Id_Propietario == id)
                                           .ToListAsync();
            return Ok(inmuebles);
        }
        ////////////////////Agregar Inmueble
        [HttpPost("agregarInmueble")]
        [Authorize]
        public async Task<IActionResult> AgregarInmueble([FromForm] Inmueble inmueble)
        {
            // Obtener el ID del propietario desde el token JWT
            var propietarioIdClaim = User.FindFirst("PropietarioId")?.Value;

            if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioIdInt))
            {
                return Unauthorized("Token no contiene un ID válido.");
            }

            var propietario = await _context.Propietarios.FindAsync(propietarioIdInt);
            if (propietario == null)
            {
                return NotFound("El propietario asociado al token no existe.");
            }

            inmueble.Id_Propietario = propietarioIdInt;

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.Inmueble.Add(inmueble);
            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Inmueble agregado exitosamente.",
                inmuebleId = inmueble.Id.ToString()
            });
        }
        ////////////////////Agregar FOTO al Inmueble
        [HttpPut("modificarAvatar")]
        [Authorize]
        [Consumes("multipart/form-data")]
        public async Task<IActionResult> ModificarAvatarInmueble(
        [FromForm] int idInmueble,
        [FromForm] IFormFile avatarFile)
        {
            if (avatarFile == null || avatarFile.Length == 0)
            {
                return BadRequest("No se ha proporcionado una imagen válida.");
            }

            // Aquí podrías verificar si el inmueble con el `idInmueble` existe en la base de datos.

            var inmueble = _context.Inmueble.Find(idInmueble);
            if (inmueble == null) return NotFound("Inmueble no encontrado");

            try
            {
                // Define la ruta donde se guardará la imagen (podrías usar una carpeta "Avatars" en "wwwroot")
                string uploadsFolder = Path.Combine(_environment.WebRootPath, "avatars");
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

                // Aquí podrías guardar la ruta del archivo en la base de datos asociada al inmueble
                inmueble.Foto = $"/avatars/{fileName}";
                await _context.SaveChangesAsync();

                return Ok(new { Message = "Avatar actualizado con éxito", FilePath = $"/avatars/{fileName}" });
            }
            catch (Exception ex)
            {
                // Loguea el error si es necesario
                return StatusCode(500, $"Error al guardar la imagen: {ex.Message}");
            }
        }
        ///////////////////////////////////////////////////////
        
        [Authorize]
        [HttpPut("modificarEstado")]
        public async Task<IActionResult> ModificarEstado([FromForm] int id)
        {
            try{
                var i = await _context.Inmueble.FirstOrDefaultAsync(x => x.Id == id);

                if (i == null) return NotFound();

                i.Disponible = !i.Disponible;
                _context.SaveChanges();

                string message = i.Disponible ? "Disponible" : "No disponible";
                return Ok(mensaje);
        
            } catch (Exception ex) {
                return BadRequest(ex.Message);
            }

            
        }

        ///////////////////////////////////////////////////////
        /*[HttpGet("inmueblesConContrato")]
        public async Task<IActionResult> InmueblesConContrato()
        {
            var propietarioIdClaim = User.FindFirst("PropietarioId")?.Value;

            if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioIdInt))
            {
                return Unauthorized("Token no contiene un ID válido.");
            }

            var inmuebles = await _context.Inmueble
                .Where(i => i.Id_Propietario == propietarioIdInt &&
                            i.Contratos.Any(c => c.FechaTerminacion == null))
                .Select(i => new
                {
                    Id = i.Id,
                    Direccion = i.Direccion,
                    Ambientes = i.Ambientes,
                    Tipo = i.Tipo,
                    Uso = i.Uso,
                    Precio = i.Precio,
                    Foto = i.Foto
                })
                .ToListAsync();

            return Ok(inmuebles);
        }*/
        ///////////////////////////////////////////////////////
    }

}