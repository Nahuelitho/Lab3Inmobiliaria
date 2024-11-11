package com.softnahu.modelo.ui.inmueble;

import android.os.Bundle;
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
import com.softnahu.modelo.model.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolderItem> {
    private List<Inmueble> listaDeInmueble;
    private LayoutInflater inflater;

    public InmuebleAdapter(List<Inmueble> listaDeInmueble, LayoutInflater inflater) {
        this.listaDeInmueble = listaDeInmueble;
        this.inflater = inflater;
    }

    // Método para actualizar la lista de inmuebles
    public void updateInmuebles(List<Inmueble> nuevosInmuebles) {
        listaDeInmueble.clear();
        listaDeInmueble.addAll(nuevosInmuebles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tarjeta, parent, false);

        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position) {
        Inmueble inmueble = listaDeInmueble.get(position);

        // Cargar la imagen usando Glide
        String url = "http://192.168.1.106:5001" + inmueble.getFoto();
        Glide.with(holder.ivFoto.getContext())
                .load(url)
                .placeholder(R.drawable.defaultprofile)
                .error(R.drawable.error)
                .into(holder.ivFoto);

        // Asignar el texto a los TextViews
        holder.tvNombre.setText(inmueble.getDireccion());  // Puedes ajustar el campo que consideres como nombre
        holder.tvDireccion.setText("Ambientes: " + inmueble.getAmbientes());
        holder.tvInfo.setText("Precio: $" + inmueble.getPrecio());

        // Configurar el click para navegar al fragmento de detalles
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble); // Asegúrate de que Inmueble implemente Serializable
            Navigation.findNavController(v).navigate(R.id.detalleItemFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listaDeInmueble.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvDireccion, tvInfo;
        private ImageView ivFoto;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvDireccion);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvInfo = itemView.findViewById(R.id.tvPrecio);
            ivFoto = itemView.findViewById(R.id.ivFoto);
        }

    }
}
