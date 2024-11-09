package com.softnahu.modelo;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.model.Propietario;
import com.softnahu.modelo.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    Context context;
    private MutableLiveData<Propietario> mPropietario;



    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public MutableLiveData<Propietario> getmPropietario(){
        if (mPropietario == null) {
            mPropietario = new MutableLiveData<Propietario>();
        }
        return mPropietario;
}

    public void recuperarDatosPerfil() {
        Call<Propietario> propietarioCall = ApiClient.getApiInmobiliaria().ObtenerPerfil("Bearer " + ApiClient.Leer(context));

        propietarioCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Propietario propietario = response.body();
                    mPropietario.postValue(propietario);  // Actualizar el LiveData
                } else {
                    Log.d("SALIDA", "Error en respuesta: " + response.message());
                    Toast.makeText(context, "Error de respuesta API en obtener perfil del propietario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("SALIDA", "Error en llamada API: " + t.getMessage());
                Toast.makeText(context, "Error al conectarse a la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

