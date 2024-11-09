package com.softnahu.modelo.ui.contratos;

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
import com.softnahu.modelo.databinding.FragmentContratosBinding;
import com.softnahu.modelo.databinding.FragmentInquilinoBinding;
import com.softnahu.modelo.ui.inquilino.InquilinoAdapter;
import com.softnahu.modelo.ui.inquilino.InquilinoViewModel;

import java.util.ArrayList;

public class ContratosFragment extends Fragment {

    private ContratosViewModel contratosViewModel;
    private FragmentContratosBinding binding;
    private ContratosAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("FRAGMENT_LIFECYCLE", "onCreateView called for InmuebleFragment");
        contratosViewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y Adapter
        adapter = new ContratosAdapter(new ArrayList<>(), inflater);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.listaInmueblesXProp2.setLayoutManager(gridLayoutManager);
        binding.listaInmueblesXProp2.setAdapter(adapter);

        // Observa cambios en los datos
        contratosViewModel.getmInmueblesXContrato().observe(getViewLifecycleOwner(), inmuebles -> {
            if (inmuebles != null) {
                Log.d("RECYCLER_VIEW", "Inmuebles cargados en el RecyclerView: " + inmuebles.size());
                adapter.updateList(inmuebles); // Usar updateList en lugar de crear un nuevo adaptador
            } else {
                Log.e("RECYCLER_VIEW_ERROR", "La lista de inmuebles es nula");
            }
        });


        // Llamar a la API
        contratosViewModel.llamarInmuebleXContrato();

        return root;
    }
}