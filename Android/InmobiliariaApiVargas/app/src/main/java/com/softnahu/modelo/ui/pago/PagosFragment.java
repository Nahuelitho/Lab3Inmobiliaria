package com.softnahu.modelo.ui.pago;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softnahu.modelo.R;
import com.softnahu.modelo.databinding.FragmentPagosBinding;
import com.softnahu.modelo.model.Pago;

import java.util.List;

public class PagosFragment extends Fragment {

    private PagosViewModel mViewModel;
    private FragmentPagosBinding binding;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observa los cambios en la lista de pagos y actualiza el RecyclerView
        mViewModel.getmPagos().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                if (pagos != null && !pagos.isEmpty()) {
                    // Asegúrate de tener un adaptador configurado para el RecyclerView
                    PagoAdapter adapter = new PagoAdapter(pagos,inflater);
                    binding.listaPagos.setAdapter(adapter);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
