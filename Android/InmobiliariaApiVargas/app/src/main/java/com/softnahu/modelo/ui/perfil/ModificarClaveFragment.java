package com.softnahu.modelo.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentModificarClaveBinding;
import com.softnahu.modelo.request.ApiClient;

public class ModificarClaveFragment extends Fragment {

    private ModificarClaveViewModel mViewModel;
    private FragmentModificarClaveBinding binding;

    public static ModificarClaveFragment newInstance() {
        return new ModificarClaveFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentModificarClaveBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mViewModel = new ViewModelProvider(this).get(ModificarClaveViewModel.class);

        mViewModel.getmMsj().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msj) {
                binding.tvMensaje.setText(msj);
            }
        });

        Glide.with(getContext())
                .load(R.drawable.logoinmobiliaria)
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.error)
                .into(binding.ivLoginImage);

        binding.btnConfirmarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String claveAct = binding.etClaveActual.getText().toString().trim();
                String claveNue = binding.etNuevaClave.getText().toString().trim();
                String claveRep = binding.etRepetirClave.getText().toString().trim();

                String token = ApiClient.Leer(getContext());

                mViewModel.cambiarClave(token,claveAct,claveNue,claveRep);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ModificarClaveViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null; // Evitar pérdidas de memoria
    }

}