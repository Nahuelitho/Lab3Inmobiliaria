package com.softnahu.modelo.ui.pago;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentPagosBinding;
import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Pago;

import java.util.ArrayList;
import java.util.List;

public class PagosFragment extends Fragment {

    private PagosViewModel mViewModel;
    private FragmentPagosBinding binding;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Contrato contrato = (Contrato) getArguments().getSerializable("contrato");
        if (contrato != null && contrato.getPagos() != null) {
            mViewModel.recuperarPagos(contrato);
        } else {
            Log.d("Pago", "No hay pagos para mostrar en el contrato recibido.");
        }

        PagoAdapter pagoAdapter = new PagoAdapter(new ArrayList<>(), inflater,contrato.getId());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL,false);

        binding.fragmentPagos.setAdapter(pagoAdapter);
        binding.fragmentPagos.setLayoutManager(gridLayoutManager);


        // Observa los cambios en la lista de pagos y actualiza el RecyclerView
        mViewModel.getmPagos().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                if (pagos != null && !pagos.isEmpty()) {
                    Log.d("PagosFragment", "Cantidad de pagos recibidos: " + pagos.size());
                    pagoAdapter.updateList(pagos);
                } else {
                    Log.d("PagosFragment", "No hay pagos para mostrar.");
                }
            }
        });




        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
