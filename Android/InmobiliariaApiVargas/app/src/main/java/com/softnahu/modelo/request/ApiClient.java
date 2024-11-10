package com.softnahu.modelo.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softnahu.modelo.model.CrearInmuebleRespuesta;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.model.Propietario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class ApiClient {
    public static final String URLBASE =  "http://192.168.1.106:5001/api/";

    private static SharedPreferences sp;


    public static InmobiliariaService getApiInmobiliaria(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmobiliariaService.class);
    }
    private static SharedPreferences getSharedPreference(Context context){
        if (sp==null){
            sp=context.getSharedPreferences("usuario",0);
        }
        return sp;
    }
    public static void Guardar(Context context, String token){
        SharedPreferences sp = getSharedPreference(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
        Log.d("API_CLIENT", "Token guardado: " + token);
    };
    public static String Leer(Context context) {
        SharedPreferences sp = getSharedPreference(context);
        String token = sp.getString("token", null);
        Log.d("API_CLIENT", "Token leído: " + token);
        return token; // El segundo parámetro es el valor por defecto si no existe

    }
    public interface InmobiliariaService{
        //--------LOGIN--------
        @FormUrlEncoded
        @POST("login/login")
        Call<String> login(@Field("Mail") String mail, @Field ("Password") String pass);
        //--------PROPIETARIOS--------
        @GET("propietarios/perfil")
        Call<Propietario> ObtenerPerfil(@Header("Authorization") String token);

        @GET("propietarios")
        Call<Propietario> get(@Header("Authorization")String token);

        @FormUrlEncoded
        @PUT("propietarios/modificarPerfil")
        Call<String> updatePerfil(
                @Header("Authorization") String token,
                @Field("Dni") String dni,
                @Field("Nombre") String nombre,
                @Field("Apellido") String apellido,
                @Field("Telefono") String telefono,
                @Field("Mail") String mail
        );
        @Multipart
        @PUT("propietarios/modificarAvatar") // Cambia esto al endpoint adecuado de tu API
        Call<ResponseBody> modificarAvatarPropietario(
                @Header("Authorization") String token,
                @Part("idPropietario") RequestBody idPropietario,
                @Part MultipartBody.Part avatarFile
        );
        @FormUrlEncoded
        @PUT("propietarios/cambiarContraseña")
        Call<String> cambiarClave(
                @Header("Authorization") String token,
                @Field("ContraseñaActual") String contraseñaActual,
                @Field("ContraseñaNueva") String contraseñaNueva,
                @Field("RepetirContraseña") String repetirContraseña
        );
        @FormUrlEncoded
        @POST("propietarios/recuperarClave")
        Call<String> recuperarClave(
                @Field("email") String email
        );
        //--------INMUEBLES--------
        @GET("inmuebles/listaInmuebles")
        Call<List<Inmueble>> getInmuebles(@Header("Authorization") String token);


        @FormUrlEncoded
        @POST("inmuebles/agregarInmueble")
        Call<CrearInmuebleRespuesta> agregarInmueble(
                @Header("Authorization") String token,
                @Field("Direccion") String direccion,
                @Field("Ambientes") String ambientes,
                @Field("Tipo") String tipo,
                @Field("Uso") String uso,
                @Field("Precio") String precio
        );

        @Multipart
        @PUT("inmuebles/modificarAvatar")
        Call<ResponseBody> modificarAvatarInmueble(
                @Header("Authorization") String token,
                @Part("idInmueble") RequestBody idInmueble,
                @Part MultipartBody.Part avatarFile
        );

        @FormUrlEncoded
        @PUT("inmuebles/modificarEstado")
        Call<String> modificarEstado(
                @Header("Authorization")String token,
                @Field("Id") int id
        );
        //Inquilinos
        @GET("inquilinos/contratosEnCurso")
        Call<List<Inmueble>> getContratosEnCurso(
                @Header("Authorization") String token
    );




    /////////////FIN
    }

}
