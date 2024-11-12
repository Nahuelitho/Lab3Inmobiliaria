package com.softnahu.modelo.ui.inquilino;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentInquilinoDetalleBinding;

public class DetalleInquilinoFragment extends Fragment {

    private InquilinoDetalleViewModel mViewModel;
    private FragmentInquilinoDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);
        View root = binding.getRoot();

        // Observa los cambios en el inquilino seleccionado
        mViewModel.getMInquilino().observe(getViewLifecycleOwner(), inquilino -> {

                binding.tvDni.setText(String.valueOf(inquilino.getDni()));
                binding.tvApellido.setText(inquilino.getApellido());
                binding.tvNombre.setText(inquilino.getNombre());
                binding.tvDireccion.setText(inquilino.getDireccion());
                binding.tvTelefono.setText(String.valueOf(inquilino.getTelefono()));


        });

        // Pasa el argumento al ViewModel
        mViewModel.recibeInquilino(getArguments());

        return root;
    }
}
