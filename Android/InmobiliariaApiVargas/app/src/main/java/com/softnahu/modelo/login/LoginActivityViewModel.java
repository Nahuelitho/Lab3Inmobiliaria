package com.softnahu.modelo.login;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.MainActivity;
import com.softnahu.modelo.request.ApiClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    private Context context;

    private MutableLiveData<String> mUsuario;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getUsuario(){
        if(mUsuario==null){
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }


    public void llamarLogin(String mail, String clave){
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();
        Call<String> llamada = api.login(mail, clave);

        Context context = getApplication().getApplicationContext();
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body();

                    // Verificar si el token no está vacío
                    if (!token.isEmpty()) {
                        Log.d("salida", "Token recibido: " + token);

                        // Guardar el token o proceder con la autenticación
                        mUsuario.setValue("Clave correcta");
                        ApiClient.Guardar(context,token);
                        // Iniciar la nueva actividad
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else {
                        // Token vacío o no válido
                        mUsuario.setValue("Token no válido");
                    }
                } else {
                    // Mostrar mensaje de error si los datos son incorrectos
                    mUsuario.setValue("Datos incorrectos");
                    Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.e("salida", "Error del servidor: " + throwable.getMessage());
                mUsuario.setValue("Error en la conexión");
            }
        });
    }
    public void llamarRestaurarClave(String email){
        if (email.isEmpty()) {
            Toast.makeText(context, "Por favor, ingrese el email que desea restablecer.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validar que el usuario tenga formato de email valido
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "El formato del correo no es válido.", Toast.LENGTH_LONG).show();
            return;
        }

        Call<String> llamada = ApiClient.getApiInmobiliaria().recuperarClave(email);

        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String token = response.body();
                if(response.isSuccessful() && token != null){
                    Toast.makeText(context, "Email de restablecimiento enviado con exito, revise su correo electronico.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "Error: el correo no esta registrado.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, "Error en conexion", Toast.LENGTH_LONG).show();
            }
        });
    }

}
