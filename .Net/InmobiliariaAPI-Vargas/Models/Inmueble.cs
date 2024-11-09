using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace InmobiliariaAPI_Vargas.Models

{
    public class Inmueble
    {
        public int Id { get; set; }

        public String Direccion { get; set; } = "";

        public int Ambientes { get; set; }

        public String Tipo { get; set; } = "";

        public String Uso { get; set; } = "";

        public int Precio { get; set; }

        public bool Disponible { get; set; }      

        public Propietario? Propietario { get; set; }

        public String Foto { get; set; } = "";
        
        [ForeignKey("Propietario")]
        public int Id_Propietario { get; set; }

        public ICollection<Contrato>? Contratos { get; set; }

    }
}