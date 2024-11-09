package com.softnahu.modelo.ui.inquilino;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softnahu.modelo.R;
import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Inmueble;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.softnahu.modelo.model.Inquilino;

import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.ViewHolderItem> {

    private List<Inmueble> listaInmueblesXPropietario;
    private LayoutInflater inflater;

    public InquilinoAdapter(List<Inmueble> list, LayoutInflater inflater) {
        this.listaInmueblesXPropietario = list;
        this.inflater = inflater;
    }

    public void updateList(List<Inmueble> inmuebles) {
        listaInmueblesXPropietario.clear();
        listaInmueblesXPropietario.addAll(inmuebles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tarjeta2, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position) {
        Inmueble inmueble = listaInmueblesXPropietario.get(position);

        // Comprobar los contratos del inmueble en esta posición
        if (inmueble.getContratos() != null && !inmueble.getContratos().isEmpty()) {
            Contrato contrato = inmueble.getContratos().get(0);
            Log.d("CONTRATO_DATA", "Inmueble: " + inmueble.getDireccion() +
                    ", Fecha Inicio: " + contrato.getFechaInicio() +
                    ", Fecha Fin: " + contrato.getFechaFin());

            String url = "http://192.168.1.106:5001" + inmueble.getFoto();
            Glide.with(holder.ivFoto.getContext())
                    .load(url)
                    .placeholder(R.drawable.defaultprofile)
                    .error(R.drawable.error)
                    .into(holder.ivFoto);

            holder.tvDireccion.setText(inmueble.getDireccion());
            holder.tvFechaInicio.setText("Inicio: " + contrato.getFechaInicio());
            holder.tvFechaFin.setText("Fin: " + contrato.getFechaFin());

            holder.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inquilino", contrato.getInquilino());
                Navigation.findNavController(view).navigate(R.id.detalleInquilinoFragment, bundle);
            });

        } else {
            holder.tvFechaInicio.setText("No disponible");
            holder.tvFechaFin.setText("No disponible");
        }
    }



    @Override
    public int getItemCount() {
        return listaInmueblesXPropietario.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvDireccion,tvFechaInicio,tvFechaFin;
        private ImageView ivFoto;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion2);
            ivFoto = itemView.findViewById(R.id.ivFoto2);
            tvFechaInicio = itemView.findViewById(R.id.tvFechaInicio);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);

        }
    }
}

