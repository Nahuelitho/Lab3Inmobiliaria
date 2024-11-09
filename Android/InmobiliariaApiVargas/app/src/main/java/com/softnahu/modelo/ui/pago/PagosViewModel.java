package com.softnahu.modelo.ui.pago;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Pago;

import java.util.List;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>>mPagos;

    Context context;

    public PagosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mPagos = new MutableLiveData<>();
    }

    public MutableLiveData<List<Pago>> getmPagos() {
        return mPagos;
    }

    public void recuperarPagos(Contrato contrato){
        mPagos.setValue(contrato.getPagos());
    }

}