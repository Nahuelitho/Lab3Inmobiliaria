package com.softnahu.modelo.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.softnahu.modelo.databinding.FragmentEditarPerfilBinding;
import com.softnahu.modelo.model.Propietario;
import com.softnahu.modelo.model.PropietarioModificar;

import androidx.lifecycle.Observer;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel viewModel;
    private FragmentEditarPerfilBinding binding;

    public EditarPerfilFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(EditarPerfilViewModel.class);

        // Inflar el layout con binding
        binding = FragmentEditarPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observar datos del ViewModel para cargar el perfil
        observarDatosPerfil2();
        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();
            }
        });
        return root;
    }

    private void observarDatosPerfil2() {
        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    // Cargar datos en los campos de edición
                    binding.etDni.setText(propietario.getDni());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etTelefono.setText(propietario.getTelefono());
                    binding.etMail.setText(propietario.getMail());
                }
            }
        });

        // Iniciar la carga de los datos del perfil
        viewModel.cargarDatosPropietario();
    }

    private void editarPerfil() {
        // Crear un objeto Propietario con los datos del formulario
        PropietarioModificar propietario = new PropietarioModificar();
        propietario.setDni(binding.etDni.getText().toString());
        propietario.setNombre(binding.etNombre.getText().toString());
        propietario.setApellido(binding.etApellido.getText().toString());
        propietario.setTelefono(binding.etTelefono.getText().toString());
        propietario.setMail(binding.etMail.getText().toString());

        // Llamar al método del ViewModel para editar el perfil
        viewModel.editarPerfil(propietario);
        viewModel.cargarDatosPropietario();
        // Observar el resultado de la edición
        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    Toast.makeText(getContext(), "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
