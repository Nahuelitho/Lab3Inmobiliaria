using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authorization;

namespace InmobiliariaAPI_Vargas.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class InquilinosController : ControllerBase
    {
        private readonly BDContext _context;

        public InquilinosController(BDContext context)
        {
            _context = context;
        }
        ///////////////////////////////////////
        [Authorize]
        [HttpGet("contratosEnCurso")]
        public async Task<IActionResult> contratosEnCurso()
        {
            var propietarioIdClaim = User.FindFirst("PropietarioId")?.Value;

            if (string.IsNullOrEmpty(propietarioIdClaim) || !int.TryParse(propietarioIdClaim, out int propietarioIdInt))
            {
                return Unauthorized("Token no contiene un ID válido.");
            }

            var inmuebleContratoInquilinoEnCurso = await _context.Inmueble
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
                    Foto = i.Foto,
                    Id_Propietario = i.Id_Propietario,
                    Contratos = i.Contratos
                        .Where(c => c.FechaTerminacion == null)
                        .Select(c => new
                        {
                            Id = c.Id,
                            Precio = c.Precio,
                            FechaInicio = c.FechaInicio,
                            FechaFin = c.FechaFin,
                            FechaTerminacion = c.FechaTerminacion,
                            Inquilino = new
                            {
                                Id = c.inquilino.Id,
                                Dni = c.inquilino.Dni,
                                Apellido = c.inquilino.Apellido,
                                Nombre = c.inquilino.Nombre,
                                Telefono = c.inquilino.Telefono,
                                Direccion = c.inquilino.Direccion
                            },
                            Pagos = _context.Pago
                                .Where(p => p.Id_Contrato == c.Id)
                                .Select(p => new
                                {
                                    Id = p.Id,
                                    NumeroPago = p.NumeroPago,
                                    FechaPago = p.FechaPago,
                                    Importe = p.Importe,
                                    Detalle = p.Detalle
                                })
                                .ToList()
                        })
                        .ToList()
                })
                .ToListAsync();

            return Ok(inmuebleContratoInquilinoEnCurso);
        }

        ///////////////////////////////////////




    }
}