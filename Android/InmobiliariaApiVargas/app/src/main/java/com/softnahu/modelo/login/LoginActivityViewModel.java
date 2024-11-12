package com.softnahu.modelo.login;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.MainActivity;
import com.softnahu.modelo.request.ApiClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    private Context context;
    private SensorManager sensorManager;
    private EscuchaDeLecturas escuchaDeLecturas;

    private MutableLiveData<String> mUsuario;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        escuchaDeLecturas = new EscuchaDeLecturas();
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
    public void obtenerLecturas() {
        List<Sensor> sensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
        if (!sensores.isEmpty()) {
            Sensor acelerometro = sensores.get(0);
            sensorManager.registerListener(escuchaDeLecturas, acelerometro, SensorManager.SENSOR_DELAY_GAME);
        }
    }
    public void pararLecturas() {
        sensorManager.unregisterListener(escuchaDeLecturas);
    }
    public class EscuchaDeLecturas implements SensorEventListener {
        private static final float SHAKE_THRESHOLD = 12.0f;  // Umbral para el movimiento
        private long lastUpdate = 0;
        private float last_x, last_y, last_z;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currentTime = System.currentTimeMillis();
                // Para evitar que el sensor se dispare muy rápido
                if ((currentTime - lastUpdate) > 100) {
                    long diffTime = currentTime - lastUpdate;
                    lastUpdate = currentTime;
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];
                    // Cálculo de la magnitud de la aceleración
                    float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
                    if (magnitude > SHAKE_THRESHOLD) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:2664162251"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
}
