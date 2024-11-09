package com.softnahu.modelo.ui.inmueble;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleItemViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> mInmueble;
    private Context context;

    public DetalleItemViewModel(@NonNull Application app) {
        super(app);
        context = app.getApplicationContext();
    }

    public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public void modificarInmuebleEstado() {
        ApiClient.InmobiliariaService api = ApiClient.getApiInmobiliaria();

            int id = mInmueble.getValue().getId();

            Log.d("salida",mInmueble.getValue().getId()+"");

            String token = "Bearer " + ApiClient.Leer(context);

            // Llamada al backend para modificar el estado
            Call<String> call = api.modificarEstado(token, id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {


                        Toast.makeText(context, "Estado del inmueble modificado con éxito!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error al modificar estado: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void recibeInmueble(Bundle bundle) {
        if (bundle != null) {
            Inmueble inmueble = (Inmueble) bundle.getSerializable("inmueble");
            if (inmueble != null) {
                mInmueble.setValue(inmueble);
                Log.d("salidaId",inmueble.getId()+"");
            } else {
                mostrarMensaje("Error: No se pudo recuperar el inmueble.");
            }
        }
    }


    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
