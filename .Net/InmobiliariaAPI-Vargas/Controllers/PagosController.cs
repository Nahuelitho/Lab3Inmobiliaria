using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;

namespace  InmobiliariaAPI_Vargas.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PagosController : ControllerBase
    {
        private readonly BDContext _context;

        public PagosController(BDContext context)
        {
            _context = context;
        }

        
    }
}	