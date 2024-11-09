namespace InmobiliariaAPI_Vargas.Models
{
    public class Inquilino
    {
        public int Id { get; set; }
        public int Dni { get; set; } 
        public string Apellido { get; set; } = "" ;
        public string Nombre { get; set; } = "" ;
        public string Direccion { get; set; } = "" ;
        public int Telefono { get; set; }
        
    }
}
