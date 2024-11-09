package com.softnahu.modelo.ui.contratos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentContratoDetalleBinding;
import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Inmueble;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContratoDetalleFragment extends Fragment {
    private ContratoDetalleViewModel mViewModel;
    private FragmentContratoDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratoDetalleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ContratoDetalleViewModel.class);



        // Pasar los argumentos al ViewModel antes de observar los cambios
        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            Contrato contrato = (Contrato) getArguments().getSerializable("contrato");

            if (inmueble != null && contrato != null) {
                Log.d("ContratoDetalle", "Inmueble recibido: " + inmueble.getDireccion());
                Log.d("ContratoDetalle", "Contrato recibido con ID: " + contrato.getId());

                // Pasar ambos al ViewModel
                mViewModel.recibeContrato(contrato, inmueble);
            } else {
                Log.d("ContratoDetalle", "Datos no recibidos correctamente.");
            }
        }

        // Observa los cambios en el contrato y actualiza la interfaz
        mViewModel.getmContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                if (contrato != null) {
                    // Formateo de fechas
                    String fechaInicioFormateada = formatFecha(contrato.getFechaInicio());
                    String fechaFinFormateada = formatFecha(contrato.getFechaFin());

                    // Setea los datos del contrato
                    binding.tvCodigoContrato.setText(String.valueOf(contrato.getId()));
                    binding.tvFechaInicio.setText("Inicio: " + fechaInicioFormateada);
                    binding.tvFechaFin.setText("Fin: " + fechaFinFormateada);
                    binding.tvMontoAlquiler.setText(String.valueOf(contrato.getPrecio()));

                    // Verifica si Inquilino no es null
                    if (contrato.getInquilino() != null) {
                        binding.tvInquilinoNombre.setText(contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
                    } else {
                        binding.tvInquilinoNombre.setText("Inquilino no disponible");
                    }
                }
                binding.btnPagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Contrato contrato = mViewModel.getmContrato().getValue();
                        Log.d("Pago", contrato.getFechaInicio());
                        Log.d("Pago", contrato.getPagos().get(0).getImporte()+"");
                        Bundle bundle = new Bundle();
                         bundle = getArguments();
                        Navigation.findNavController(view).navigate(R.id.pagosFragment, bundle);
                    }
                });
            }
        });

        mViewModel.getInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble != null) {
                    // Verifica si Inmueble no es null
                    binding.tvInmuebleDireccion.setText(inmueble.getDireccion());
                } else {
                    binding.tvInmuebleDireccion.setText("Dirección no disponible");
                }
            }
        });

        return binding.getRoot();
    }


    // Método para formatear las fechas
    private String formatFecha(String fecha) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date fechaParsed = formatoEntrada.parse(fecha);
            return formatoSalida.format(fechaParsed);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Fecha no disponible";
        }
    }
}
