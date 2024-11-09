package com.softnahu.modelo.ui.inquilino;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.model.Inquilino;

public class InquilinoDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<Inquilino> mInquilino;
    private Context context;

    public InquilinoDetalleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mInquilino = new MutableLiveData<>();
    }

    public LiveData<Inquilino> getMInquilino() {
        return mInquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        mInquilino.setValue(inquilino);
    }

    public void recibeInquilino(Bundle bundle) {
        if (bundle != null) {
            Inquilino inquilino = (Inquilino) bundle.getSerializable("inquilino");
            if (inquilino != null) {
                mInquilino.setValue(inquilino);
                Log.d("InquilinoDetalle", "Inquilino recibido: " + inquilino.getId());
            } else {
                mostrarMensaje("Error: No se pudo recuperar el inquilino.");
            }
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
