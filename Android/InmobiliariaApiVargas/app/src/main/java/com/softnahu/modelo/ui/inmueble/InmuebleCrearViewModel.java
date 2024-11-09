package com.softnahu.modelo.ui.inmueble;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.model.CrearInmuebleRespuesta;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.request.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleCrearViewModel extends AndroidViewModel {

    private final Context context;
    private final MutableLiveData<Uri> mAvatar = new MutableLiveData<>();
    private final MutableLiveData<String> mIdInmueble = new MutableLiveData<>();

    private String avatar;

    public InmuebleCrearViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public LiveData<Uri> getMAvatar() {
        return mAvatar;
    }

    public LiveData<String> getMIdInmueble() {
        return mIdInmueble;
    }

    public void crearInmueble(String token, String direccion, String tipo, String uso, String ambientesString, String precioString) {
        // Validación de datos vacíos
        if (token.isEmpty() || direccion.isEmpty() || tipo.isEmpty() || uso.isEmpty() || ambientesString.isEmpty() || precioString.isEmpty()) {
            Toast.makeText(context, "Datos vacíos: revise los datos ingresados", Toast.LENGTH_LONG).show();
            return;
        }

        // Validación de dirección
        if (direccion.length() < 5 || !direccion.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\s).{5,}$")) {
            Toast.makeText(context, "La dirección debe tener al menos 5 caracteres y contener letras, espacios y números.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validación de ambientes
        int ambientes;
        try {
            ambientes = Integer.parseInt(ambientesString);
            if (ambientes <= 0) {
                Toast.makeText(context, "El número de ambientes debe ser positivo.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, "El número de ambientes debe ser un entero válido.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validación de tipo y uso
        if (!tipo.matches("[a-zA-Z\\s]+") || !uso.matches("[a-zA-Z\\s]+")) {
            Toast.makeText(context, "El tipo y uso solo pueden contener letras y espacios.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validación de precio
        int precio = (int) Double.parseDouble(precioString.replace(",", "."));
        try {

            if (precio <= 0) {
                Toast.makeText(context, "El precio debe ser un número positivo.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, "El precio debe ser un número válido.", Toast.LENGTH_LONG).show();
            return;
        }

        // Llamada al API
        Call<CrearInmuebleRespuesta> call = ApiClient.getApiInmobiliaria().agregarInmueble(
                "Bearer " + token, direccion, String.valueOf(ambientes), tipo, uso, String.valueOf(precio));
        call.enqueue(new Callback<CrearInmuebleRespuesta>() {
            @Override
            public void onResponse(Call<CrearInmuebleRespuesta> call, Response<CrearInmuebleRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Inmueble agregado exitosamente.", Toast.LENGTH_LONG).show();
                    mIdInmueble.postValue(response.body().getInmuebleId());
                } else {
                    Toast.makeText(context, "Error al agregar inmueble: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CrearInmuebleRespuesta> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                avatar=uri.toString();
                Log.d("resultado foto",avatar);

                // Otorga permisos persistentes
                context.getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                mAvatar.setValue(uri);
            }

        }
    }

    public void actualizarAvatar() {
        if (mAvatar.getValue() == null) {
            Toast.makeText(context, "No se ha seleccionado ninguna imagen para actualizar.", Toast.LENGTH_LONG).show();
            return;
        }

        Uri avatarUri = mAvatar.getValue();
        File avatarFile = createTemporaryFileFromUri(avatarUri);
        if (avatarFile == null) {
            Toast.makeText(context, "Error: no es posible enviar imagen al servidor.", Toast.LENGTH_LONG).show();
            return;
        }

        // Convierte el archivo a un MultipartBody.Part para Retrofit
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), avatarFile);
        MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatarFile", avatarFile.getName(), requestFile);

        // Crea el RequestBody para idInmueble
        String idInmuebleValue = mIdInmueble.getValue(); // Asegúrate de que mIdInmueble esté inicializado y contenga el ID
        if (idInmuebleValue == null) {
            Toast.makeText(context, "El ID del inmueble no está disponible.", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody idInmueble = RequestBody.create(MediaType.parse("text/plain"), idInmuebleValue);

        // Llamada a Retrofit
        Call<ResponseBody> call = ApiClient.getApiInmobiliaria().modificarAvatarInmueble(
                "Bearer " + ApiClient.Leer(context),
                idInmueble,
                avatarPart
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Foto modificada con éxito.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("Error response:", errorMessage);
                        Toast.makeText(context, "Error al enviar la imagen al servidor.", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error al procesar el error de respuesta.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error de conexión al intentar actualizar la foto.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private File createTemporaryFileFromUri(Uri uri) {
        // Verificar si el URI es nulo
        if (uri == null) {
            return null;
        }

        // Intentar abrir el InputStream y crear el archivo temporal
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            // Verificar si el InputStream se abre correctamente
            if (inputStream == null) {
                return null;
            }

            // Crear un archivo temporal
            File tempFile = File.createTempFile("avatar", ".jpg", context.getCacheDir());

            // Usar try-with-resources para asegurarnos de cerrar el OutputStream
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            }

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
