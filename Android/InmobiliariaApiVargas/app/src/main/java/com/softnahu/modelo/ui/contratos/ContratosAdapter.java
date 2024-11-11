package com.softnahu.modelo.ui.contratos;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.softnahu.modelo.R;
import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Inmueble;
import com.softnahu.modelo.model.Inquilino;
import com.softnahu.modelo.model.Pago;
import com.softnahu.modelo.ui.inquilino.InquilinoAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ContratosAdapter extends RecyclerView.Adapter<ContratosAdapter.ViewHolderItem> {

    private List<Inmueble> listaInmueblesXPropietario;
    private List<Contrato> contratos;
    private LayoutInflater inflater;

    public ContratosAdapter(List<Inmueble> list, LayoutInflater inflater) {
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
    public ContratosAdapter.ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tarjeta2, parent, false);
        return new ContratosAdapter.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContratosAdapter.ViewHolderItem holder, int position) {
        Inmueble inmueble = listaInmueblesXPropietario.get(position);
        contratos = inmueble.getContratos();



        if (contratos != null && !contratos.isEmpty()) {
            Contrato contrato = contratos.get(0);

            if (contrato != null) {
                String fechaInicio = contrato.getFechaInicio();
                String fechaFin = contrato.getFechaFin();
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat formatoSalida = new SimpleDateFormat("dd-MM-yyyy");
                Date fechaInicioParsed = null;
                Date fechaFinParsed = null;
                try {
                    // Parsear fechas
                    fechaInicioParsed = formatoEntrada.parse(fechaInicio);
                    fechaFinParsed = formatoEntrada.parse(fechaFin);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String fechaInicioFormateada = formatoSalida.format(fechaInicioParsed);
                String fechaFinFormateada = formatoSalida.format(fechaFinParsed);
                Inquilino inquilino = contrato.getInquilino();
                List<Pago> pagos = contrato.getPagos();

                String url = "http://192.168.1.106:5001" + inmueble.getFoto();
                Glide.with(holder.ivFoto.getContext())
                        .load(url)
                        .placeholder(R.drawable.defaultprofile)
                        .error(R.drawable.error)
                        .into(holder.ivFoto);

                holder.tvDireccion.setText(inmueble.getDireccion());
                holder.tvFechaInicio.setText("Inicio: " + fechaInicioFormateada);
                holder.tvFechaFin.setText("Fin: " + fechaFinFormateada);

                holder.itemView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contrato", contrato);
                    bundle.putSerializable("inmueble", inmueble);
                    Log.d("ContratosAdapter", "Contrato enviado con ID: " + contrato.getId());
                    Log.d("ContratosAdapter", "Contrato enviado con ID: " + contrato.getInquilino());
                    Log.d("ContratosAdapter", "Contrato enviado con ID: " + contrato.getInmueble());
                    Navigation.findNavController(view).navigate(R.id.detalle_contrato, bundle);
                });

            } else {
                holder.tvFechaInicio.setText("Contrato no disponible");
                holder.tvFechaFin.setText("Contrato no disponible");
            }
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