package com.softnahu.modelo.ui.inmueble;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentInmuebleBinding;
import java.util.ArrayList;

public class InmuebleFragment extends Fragment {
    private InmuebleViewModel inmuebleViewModel;
    private FragmentInmuebleBinding binding;
    private InmuebleAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inicializar ViewModel y ViewBinding
        inmuebleViewModel = new ViewModelProvider(this).get(InmuebleViewModel.class);
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y Adaptador
        adapter = new InmuebleAdapter(new ArrayList<>(), inflater);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.listaInmuebles.setLayoutManager(gridLayoutManager);
        binding.listaInmuebles.setAdapter(adapter);

        // Observar los datos de LiveData
        inmuebleViewModel.getInmuebles().observe(getViewLifecycleOwner(), inmuebles -> {
            adapter.updateInmuebles(inmuebles);  // Método personalizado en el adaptador para actualizar los datos
        });
        binding.btnCrearInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.inmuebleCrearFragment);
            }
        });
        // Llamar al método para cargar los datos desde la API
        inmuebleViewModel.cargarInmueblesConToken();

        return root;
    }
}
