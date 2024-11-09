using System.ComponentModel.DataAnnotations.Schema;
namespace InmobiliariaAPI_Vargas.Models;
public class Contrato
{

    public int Id { get; set; }
    public double Precio { get; set; }

    public DateTime FechaInicio { get; set; }

    public DateTime FechaFin { get; set; }

    public DateTime? FechaTerminacion { get; set; }

    public int Id_Inquilino { get; set; }

    [ForeignKey("Id_Inquilino")]
    public Inquilino inquilino { get; set; }
    public int Id_Inmueble { get; set; }

    [ForeignKey("Id_Inmueble")]
    public Inmueble inmueble { get; set; }

}