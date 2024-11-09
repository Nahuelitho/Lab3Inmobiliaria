using System.ComponentModel.DataAnnotations.Schema;


namespace InmobiliariaAPI_Vargas.Models.Datos
{
[Table("propietario")]
public class PropietarioUpdate {

public string Dni { get; set; } = "";

public string Apellido { get; set; } = "";

public string Nombre { get; set; } = "";

public string Telefono { get; set; } = "";

public string Mail { get; set; }
    }
}