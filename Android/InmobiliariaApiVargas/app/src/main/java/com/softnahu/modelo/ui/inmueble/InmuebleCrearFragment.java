package com.softnahu.modelo.ui.inmueble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentInmuebleCrearBinding;
import com.softnahu.modelo.request.ApiClient;

public class InmuebleCrearFragment extends Fragment {

    private FragmentInmuebleCrearBinding binding;
    private InmuebleCrearViewModel mViewModel;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleCrearBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InmuebleCrearViewModel.class);
        abrirGaleria();
        View view = binding.getRoot();

        binding.btnGuardarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direccion = binding.etDireccion.getText().toString().trim();
                String tipo = binding.etTipo.getText().toString().trim();
                String uso = binding.etUso.getText().toString().trim();
                String ambientes = binding.etAmbientes.getText().toString().trim();
                String precio = binding.etPrecio.getText().toString().trim();
                String token = ApiClient.Leer(getContext());
                mViewModel.crearInmueble(token, direccion, tipo, uso, ambientes, precio);
            }
        });

        binding.btnFotoCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });

        mViewModel.getMAvatar().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivFotoInmueble.setImageURI(uri);
                mViewModel.actualizarAvatar();
            }
        });

        mViewModel.getMIdInmueble().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String id) {
                binding.btnGuardarInmueble.setVisibility(View.GONE);
                binding.btnFotoCrear.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                mViewModel.recibirFoto(result);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
