package com.softnahu.modelo.ui.inquilino;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.MainActivity;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Inmueble>> mInmueblesXContrato;
    private Context context;

    public InquilinoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mInmueblesXContrato = new MutableLiveData<>();
    }

    public MutableLiveData<List<Inmueble>> getmInmueblesXContrato() {
        return mInmueblesXContrato;
    }

    public void llamarInmuebleXContrato() {
        String token = "Bearer " + ApiClient.Leer(context);
        Call<List<Inmueble>> call = ApiClient.getApiInmobiliaria().getContratosEnCurso(token);

        Log.d("API_CALL", "Iniciando llamada a la API para obtener inmuebles en contrato...");

        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Inmueble> inmueblesList = response.body();
                    Log.d("API_RESPONSE", "Datos recibidos correctamente: " + inmueblesList.size() + " inmuebles");

                    for (Inmueble inmueble : inmueblesList) {
                        Log.d("API_RESPONSE_DETAIL", "Inmueble ID: " + inmueble.getId() +
                                ", Dirección: " + inmueble.getDireccion() +
                                ", Precio: " + inmueble.getPrecio());
                    }

                    // Asignamos los datos al LiveData
                    mInmueblesXContrato.postValue(inmueblesList);
                    MainActivity.listaInmueble.clear();
                    MainActivity.listaInmueble.addAll(inmueblesList);
                } else {
                    Log.e("API_RESPONSE_ERROR", "Error en la respuesta: " + response.message());
                    Toast.makeText(context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("API_CALL_FAILURE", "Error de conexión al API: " + t.getMessage());
                Toast.makeText(context, "Error de conexión al API", Toast.LENGTH_SHORT).show();
            }
        });

        // Este log no tiene acceso a inmueblesList directamente, ya que este se define dentro del onResponse.
        // Si deseas imprimir el valor de mInmueblesXContrato fuera de la llamada, utiliza un observador en la vista.
        Log.d("VIEW_MODEL", "Llamada a la API para cargar inmuebles en contrato finalizada.");
    }

}
