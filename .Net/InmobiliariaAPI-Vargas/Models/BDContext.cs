using Microsoft.EntityFrameworkCore;

namespace InmobiliariaAPI_Vargas.Models
{
    public class BDContext : DbContext
    {
        public BDContext(DbContextOptions<BDContext> options) : base(options) { }

        public DbSet<Propietario> Propietarios { get; set; }

        public DbSet<Inquilino> Inquilino { get; set; }

        public DbSet<Contrato> Contrato { get; set; }

        public DbSet<Inmueble> Inmueble { get; set; }

        public DbSet<Pago> Pago { get; set; }

    }
    
}
