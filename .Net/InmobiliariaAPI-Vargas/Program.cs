using InmobiliariaAPI_Vargas.Models;
using Microsoft.EntityFrameworkCore;
//token
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

using System.Text;

var builder = WebApplication.CreateBuilder(args);
var configuration = builder.Configuration;

builder.WebHost.UseUrls("http://192.168.1.106:5001", "http://*:5001");

// Configuración de la base de datos
builder.Services.AddDbContext<BDContext>(
	options => options.UseMySql(
		configuration["ConnectionStrings:DefaultConnection"],
		ServerVersion.AutoDetect(configuration["ConnectionStrings:DefaultConnection"])
	)
);

// Configuración de la autenticación JWT
builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options => // La API web valida con token
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            ValidIssuer = configuration["TokenAuthentication:Issuer"], // Usar el issuer desde appsettings.json
            ValidAudience = configuration["TokenAuthentication:Audience"], // Usar el audience desde appsettings.json
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(configuration["TokenAuthentication:SecretKey"])), // Usar la clave desde appsettings.json
        };
    });
    
// Agregar servicios a la aplicación
builder.Services.AddControllers();
/*
builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        // Configuración para ignorar referencias cíclicas y evitar incluir $id/$ref
        options.JsonSerializerOptions.ReferenceHandler = ReferenceHandler.IgnoreCycles;
        options.JsonSerializerOptions.WriteIndented = true;  // Opcional, para formato más legible
    });*/
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Configuración de CORS
builder.Services.AddCors(options =>
{
    options.AddPolicy("CorsPolicy", builder =>
    {
        builder.WithOrigins("http://localhost", "http://localhost:5001","http://192.168.1.105:5001")
               .AllowAnyMethod()
               .AllowAnyHeader();
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseCors("CorsPolicy");

//app.UseHttpsRedirection();

app.UseStaticFiles();

app.UseAuthentication();

app.UseAuthorization();

app.MapControllers();

app.Run();
