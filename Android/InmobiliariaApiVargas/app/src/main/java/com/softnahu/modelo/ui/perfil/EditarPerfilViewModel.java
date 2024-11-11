package com.softnahu.modelo.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.model.Propietario;
import com.softnahu.modelo.model.PropietarioModificar;
import com.softnahu.modelo.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> propietarioLiveData;

    private Context context;

    public EditarPerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        propietarioLiveData = new MutableLiveData<>();

    }

    public LiveData<Propietario> getPropietario() {
        return propietarioLiveData;
    }



    public void editarPerfil(PropietarioModificar propietario) {
        if (!validarPropietario(propietario)) {
            return; // Si los datos no son válidos, salir del método
        }

        String token = "Bearer " + ApiClient.Leer(context);

        Call<String> editarCall = ApiClient.getApiInmobiliaria().updatePerfil(
                token,
                propietario.getDni(),
                propietario.getNombre(),
                propietario.getApellido(),
                propietario.getTelefono(),
                propietario.getMail()
        );

        editarCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    cargarDatosPropietario();
                    Log.d("API_SUCCESS", "Perfil actualizado exitosamente.");
                } else {
                    Log.d("API_ERROR", "Error al actualizar perfil: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("API_ERROR", "Fallo en la actualización del perfil: " + t.getMessage());
            }
        });
    }

    private boolean validarPropietario(PropietarioModificar propietario) {
        // Validación de DNI
        if (propietario.getDni() == null || propietario.getDni().isEmpty() || propietario.getDni().length() > 10) {
            Toast.makeText(context, "DNI inválido. Debe tener hasta 10 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación de Nombre
        if (propietario.getNombre() == null || propietario.getNombre().isEmpty() || propietario.getNombre().length() > 20) {
            Toast.makeText(context, "El nombre es obligatorio y debe tener hasta 20 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación de Apellido
        if (propietario.getApellido() == null || propietario.getApellido().isEmpty() || propietario.getApellido().length() > 20) {
            Toast.makeText(context, "El apellido es obligatorio y debe tener hasta 20 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación de Teléfono
        if (propietario.getTelefono() != null && propietario.getTelefono().length() > 20) {
            Toast.makeText(context, "El teléfono no puede exceder los 20 caracteres.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validación de Email
        if (propietario.getMail() != null && !Patterns.EMAIL_ADDRESS.matcher(propietario.getMail()).matches()) {
            Toast.makeText(context, "El correo electrónico no es válido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Todos los campos son válidos
    }

    public void cargarDatosPropietario() {
        String token = "Bearer " + ApiClient.Leer(context);
        Call<Propietario> propietarioCall = ApiClient.getApiInmobiliaria().get(token);

        propietarioCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioLiveData.setValue(response.body()); // Refresca los datos en el LiveData
                } else {
                    Log.d("API_ERROR", "Error de respuesta API, GetPropietario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("API_ERROR", "Error en la llamada API, GetPropietario: " + t.getMessage());
            }
        });
    }
}
