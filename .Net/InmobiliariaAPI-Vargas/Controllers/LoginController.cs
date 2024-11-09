using Microsoft.AspNetCore.Mvc;
using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;
using System.IdentityModel.Tokens.Jwt;
using System.Text;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;

namespace InmobiliariaAPI_Vargas.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class LoginController : ControllerBase
    {
        private readonly BDContext _context;
        private readonly IConfiguration _configuration;
        private readonly PasswordHasher<Propietario> _passwordHasher;

        public LoginController(BDContext context, IConfiguration configuration)
        {
            _context = context;
            _configuration = configuration;
        }

        // POST: api/Login
        [HttpPost("login")]
        [AllowAnonymous]

        public async Task<ActionResult> Login([FromForm] LoginRequest request)
        {   
            Console.WriteLine("Entro al login");
            try
            {
                // Hashear la contraseña ingresada
                string hash = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: request.Password,
                salt: Encoding.ASCII.GetBytes(_configuration["Salt"]),
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 10000,
                numBytesRequested: 256 / 8));

                // Buscar el propietario por correo
                var propietario = await _context.Propietarios.FirstOrDefaultAsync(p => p.Mail == request.Mail);


                Console.WriteLine("salida 1 " + propietario.Password);
                Console.WriteLine("salida 2 " + hash);


                // Comparar el hash con el almacenado en la base de datos
                if (propietario == null || propietario.Password != hash)
                {
                    return BadRequest("Nombre o clave incorrecto NO COINSIDE");
                }

                // Generar el token JWT (si el hash coincide)
                var key = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(_configuration["TokenAuthentication:SecretKey"]));
                var credenciales = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
                var claims = new List<Claim>
        {
            new Claim(ClaimTypes.Name, propietario.Id.ToString()),
            new Claim("FullName", $"{propietario.Nombre} {propietario.Apellido}"),
            new Claim(ClaimTypes.Role, "Propietario"),
            new Claim("PropietarioId", propietario.Id.ToString()),
        };
                var token = new JwtSecurityToken(
                    issuer: _configuration["TokenAuthentication:Issuer"],
                    audience: _configuration["TokenAuthentication:Audience"],
                    claims: claims,
                    expires: DateTime.Now.AddMinutes(60),
                    signingCredentials: credenciales
                );


                return Ok(new JwtSecurityTokenHandler().WriteToken(token));
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }



        
        //////////////////////////////////////////////////////////////////////////////
        
    }

    public class LoginRequest
    {
        public string Mail { get; set; }
        public string Password { get; set; }
    }

   
}
