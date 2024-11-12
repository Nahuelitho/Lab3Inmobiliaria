package com.softnahu.modelo.ui.inquilino;

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


import com.softnahu.modelo.databinding.FragmentInquilinoBinding;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import java.util.ArrayList;

public class InquilinoFragment extends Fragment {

    private InquilinoViewModel inquilinoViewModel;
    private FragmentInquilinoBinding binding;
    private InquilinoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("FRAGMENT_LIFECYCLE", "onCreateView called for InmuebleFragment");
        inquilinoViewModel = new ViewModelProvider(this).get(InquilinoViewModel.class);
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y Adapter
        adapter = new InquilinoAdapter(new ArrayList<>(), inflater);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.listaInmueblesXProp.setLayoutManager(gridLayoutManager);
        binding.listaInmueblesXProp.setAdapter(adapter);

        // Observa cambios en los datos
        inquilinoViewModel.getmInmueblesXContrato().observe(getViewLifecycleOwner(), inmuebles -> {

                Log.d("RECYCLER_VIEW", "Inmuebles cargados en el RecyclerView: " + inmuebles.size());
                adapter.updateList(inmuebles); // Usar updateList en lugar de crear un nuevo adaptador

        });

        // Llamar a la API
        inquilinoViewModel.llamarInmuebleXContrato();

        return root;
    }
}
