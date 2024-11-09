package com.softnahu.modelo.ui.perfil;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.model.Propietario;
import com.softnahu.modelo.request.ApiClient;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import android.app.Application;
import android.content.Context;
import android.widget.Toast;


import androidx.lifecycle.AndroidViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PerfilViewModel extends AndroidViewModel {
    private Propietario propietario;
    private MutableLiveData<Propietario> propietarioLiveData;
    private MutableLiveData<Uri> mAvatar;
    private Context context;
    private String avatar;

    public PerfilViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        propietarioLiveData = new MutableLiveData<>();
        mAvatar = new MutableLiveData<>();
    }

    public MutableLiveData<Propietario> getmPropietario() {
        if (propietarioLiveData == null) {
            propietarioLiveData = new MutableLiveData<>();
        }
        return propietarioLiveData;
    }

    public LiveData<Uri> getMAvatar() {
        if (mAvatar == null) {
            mAvatar = new MutableLiveData<>();
        }
        return mAvatar;
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                avatar = uri.toString();
                Log.d("resultado foto", avatar);

                // Otorga permisos persistentes
                context.getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
                mAvatar.setValue(uri); // Guarda el URI en el LiveData
            }
        }
    }

    public void actualizarAvatar() {
        // Asegura que mAvatar esté inicializado
        if (getMAvatar().getValue() == null) {
            Toast.makeText(context, "No se ha seleccionado ninguna imagen para actualizar.", Toast.LENGTH_LONG).show();
            return;
        }

        Uri avatarUri = getMAvatar().getValue();
        File avatarFile = createTemporaryFileFromUri(avatarUri);
        if (avatarFile == null) {
            Toast.makeText(context, "Error: no es posible enviar imagen al servidor.", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), avatarFile);
        MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatarFile", avatarFile.getName(), requestFile);

        if (propietario == null) {
            Toast.makeText(context, "El ID del propietario no está disponible.", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody idPropietario = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(propietario.getId()));

        Call<ResponseBody> call = ApiClient.getApiInmobiliaria().modificarAvatarPropietario(
                "Bearer " + ApiClient.Leer(context),
                idPropietario,
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
        if (uri == null) {
            return null;
        }
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            if (inputStream == null) {
                return null;
            }

            File tempFile = File.createTempFile("avatarPerfil", ".jpg", context.getCacheDir());
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

    public void datosPropietarios() {
        String token = "Bearer " + ApiClient.Leer(context);
        Call<Propietario> propietarioCall = ApiClient.getApiInmobiliaria().get(token);

        propietarioCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietario = response.body();
                    avatar = propietario.getAvatar();
                    propietarioLiveData.setValue(response.body());
                } else {
                    Log.d("API_ERROR", "Error de respuesta API, GetPropietario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable throwable) {
                Log.d("API_ERROR", "Error en la llamada API, GetPropietario: " + throwable.getMessage());
            }
        });
    }
}
