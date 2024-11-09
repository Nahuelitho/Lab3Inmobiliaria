package com.softnahu.modelo.ui.inmueble;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.MainActivity;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> mInmuebles;
    private Context context;

    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mInmuebles = new MutableLiveData<>();
    }

    // Getter para LiveData de la lista de inmuebles
    public LiveData<List<Inmueble>> getInmuebles() {
        return mInmuebles;
    }

    // Método para recuperar los inmuebles desde la API usando el token de autenticación
    public void cargarInmueblesConToken() {
        String token = "Bearer " + ApiClient.Leer(context); // Ajusta según cómo se obtiene el token

        Call<List<Inmueble>> inmueblesCall = ApiClient.getApiInmobiliaria().getInmuebles(token);

        inmueblesCall.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmuebles = response.body();
                    // Actualizar LiveData con los datos de los inmuebles recibidos
                    mInmuebles.postValue(inmuebles);

                    // Sincronizar lista en MainActivity
                    MainActivity.listaInmueble.clear();
                    MainActivity.listaInmueble.addAll(inmuebles);

                    Toast.makeText(context, "Datos de inmuebles cargados correctamente.", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("InmuebleViewModel", "Error en la respuesta: " + response.message());
                    if (response.code() == 401) {
                        Toast.makeText(context, "No autorizado. Verifica tu token.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al cargar los datos: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.d("InmuebleViewModel", "Error de conexión: " + t.getMessage());
                Toast.makeText(context, "Error de conexión. Intente nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
