using System.Net;
using System.Net.Mail;
using Microsoft.Extensions.Configuration;
using System.Threading.Tasks;

namespace InmobiliariaAPI_Vargas.Services
{
    public class EmailService
    {
        private readonly IConfiguration _configuration;

        public EmailService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task EnviarCorreoAsync(string destinatario, string asunto, string mensajeHtml)
        {
            try
            {
                // Configuración del cliente SMTP
                var smtpClient = new SmtpClient(_configuration["Smtp:Host"], Convert.ToInt32(_configuration["Smtp:Port"]))
                {
                    Credentials = new NetworkCredential(_configuration["Smtp:UserName"], _configuration["Smtp:Password"]),
                    EnableSsl = Convert.ToBoolean(_configuration["Smtp:EnableSsl"])
                };

                // Creación del mensaje
                var mailMessage = new MailMessage
                {
                    From = new MailAddress(_configuration["Smtp:UserName"]), // Debería ser el mismo que el UserName
                    Subject = asunto,
                    Body = mensajeHtml,
                    IsBodyHtml = true
                };

                mailMessage.To.Add(destinatario);

                // Enviar el correo
                await smtpClient.SendMailAsync(mailMessage);
            }
            catch (SmtpException smtpEx)
            {
                // Registrar o mostrar más detalles sobre el error SMTP
                Console.WriteLine($"Error SMTP: {smtpEx.Message}");
                throw;
            }
            catch (Exception ex)
            {
                // Capturar otros errores
                Console.WriteLine($"Error general: {ex.Message}");
                throw;
            }
        }

    }
}



