package com.softnahu.modelo.ui.inmueble;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentDetalleInmuebleBinding;
import com.softnahu.modelo.model.Inmueble;

public class DetalleItemFragment extends Fragment {

    private DetalleItemViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleItemViewModel.class);
        View root = binding.getRoot();

        // Observa los cambios en el inmueble seleccionado
        mViewModel.getMInmueble().observe(getViewLifecycleOwner(), inmueble -> {

                binding.tvDireccion.setText(inmueble.getDireccion());
                binding.tvAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvTipoUso.setText(inmueble.getTipo());
                binding.tvPrecio.setText(String.valueOf(inmueble.getPrecio()));

                String url = "http://192.168.1.106:5001" + inmueble.getFoto();
                Glide.with(binding.ivFoto.getContext())
                        .load(url)
                        .error(R.drawable.error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.ivFoto);

                binding.switchDisponible.setChecked(
                        inmueble.isDisponible());

                // Listener para cambiar el estado del inmueble
                binding.switchDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    mViewModel.modificarInmuebleEstado();
                });

        });

        // Pasa el argumento al ViewModel
        mViewModel.recibeInmueble(getArguments());

        return root;
    }
}
