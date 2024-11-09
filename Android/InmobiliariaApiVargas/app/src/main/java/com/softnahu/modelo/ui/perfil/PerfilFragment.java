package com.softnahu.modelo.ui.perfil;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentPerfilBinding;


import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.widget.Toast;


import com.softnahu.modelo.model.Propietario;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;
    private ActivityResultLauncher<Intent> launcher;

    public PerfilFragment() {}

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa el launcher para abrir la galería
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> mViewModel.recibirFoto(result)
        );
        observarImagenAvatar();
        // Configuración del botón para seleccionar una imagen de avatar
        binding.btnEditarAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            launcher.launch(intent);
        });

        // Configuración del botón para subir el avatar
        binding.btnActualizarAvatar.setOnClickListener(v -> {
            mViewModel.actualizarAvatar();
        });

        // Observa los datos del perfil del ViewModel
        observarDatosPerfil();

        return root;
    }

    private void observarDatosPerfil() {
        mViewModel.getmPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                // Actualizar la UI con los datos del propietario
                binding.tvDni.setText("DNI: " + propietario.getDni());
                binding.tvApellido.setText("Apellido: " + propietario.getApellido());
                binding.tvNombre.setText("Nombre: " + propietario.getNombre());
                binding.tvTelefono.setText("Teléfono: " + propietario.getTelefono());
                binding.tvEmail.setText("Email: " + propietario.getMail());

                // Carga la imagen del avatar usando Glide
                String url = "http://192.168.1.106:5001" + propietario.getAvatar();
                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.defaultprofile)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.error)
                        .into(binding.ivAvatar);
            } else {
                Toast.makeText(getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnEditar.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.editarPerfilFragment);
        });

        binding.btnModificarClave.setOnClickListener( v -> {
            Navigation.findNavController(requireView()).navigate(R.id.fragment_modificar_clave);
        });
        // Iniciar la carga de los datos del perfil
        mViewModel.datosPropietarios();
    }
    private void observarImagenAvatar() {
        mViewModel.getMAvatar().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
                    // Usa Glide para cargar la imagen seleccionada en el ImageView
                    Glide.with(requireContext())
                            .load(uri)
                            .placeholder(R.drawable.defaultprofile) // Imagen de carga inicial
                            .error(R.drawable.error) // Imagen de error si falla la carga
                            .into(binding.ivAvatar);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Limpiar el binding para evitar fugas de memoria
    }
}

