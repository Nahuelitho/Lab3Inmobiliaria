package com.softnahu.modelo.ui.perfil;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarClaveViewModel extends AndroidViewModel {
    private MutableLiveData <String> mMsj;

    public ModificarClaveViewModel (Application application) {
        super(application);
        mMsj = new MutableLiveData<>();
        ;
    }

    public MutableLiveData<String> getmMsj() {
        return mMsj;
    }

    public void cambiarClave(String token, String claveActual, String nuevaClave, String repetirClave){
        if (claveActual.isEmpty() || nuevaClave.isEmpty() || repetirClave.isEmpty()) {
            mMsj.postValue("Debe llenar todos los campos.");
            return;
        }


        if (nuevaClave.length() < 3 || nuevaClave.length() > 15) {
            mMsj.postValue("La nueva contraseña debe tener entre 3 y 15 caracteres.");
            return;
        }

        if (!nuevaClave.equals(repetirClave)) {
            mMsj.postValue("Las contraseñas nuevas no coinciden.");
            return;
        }
        // Llamada a la API para cambiar la contraseña
        Call<String> call = ApiClient.getApiInmobiliaria().cambiarClave("Bearer " + token, claveActual, nuevaClave, repetirClave);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Contraseña cambiada correctamente
                    mMsj.postValue("Contraseña cambiada correctamente.");
                } else {
                    // Error en la respuesta
                    mMsj.postValue("Contraseña actual incorrecta.");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Error en la solicitud
                mMsj.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}