package com.softnahu.modelo.ui.contratos;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.model.Pago;

import java.util.List;

public class ContratoDetalleViewModel extends AndroidViewModel {
    private MutableLiveData<Contrato> mContrato;
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<Pago> mPago;
    public ContratoDetalleViewModel(@NonNull Application application) {
        super(application);  // Se debe pasar la referencia a la clase Application al super constructor.
        mContrato = new MutableLiveData<>();
        mInmueble = new MutableLiveData<>();
    }

    // Método para recibir ambos objetos: Contrato e Inmueble
    public void recibeContrato(Contrato contrato, Inmueble inmueble) {
        mContrato.setValue(contrato);
        mInmueble.setValue(inmueble);
    }

    public MutableLiveData<Contrato> getmContrato() {
        return mContrato;
    }

    public MutableLiveData<Inmueble> getInmueble() {
        return mInmueble;
    }
}


