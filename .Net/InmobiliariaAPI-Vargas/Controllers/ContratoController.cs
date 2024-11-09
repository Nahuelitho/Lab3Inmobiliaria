using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;

namespace  InmobiliariaAPI_Vargas.Controllers 
{
    [Route("api/[controller]")]
    [ApiController]
    public class ContratoController : ControllerBase
    {
        private readonly BDContext _context;

        public ContratoController(BDContext context)
        {
            _context = context;
        }

        
    }
}