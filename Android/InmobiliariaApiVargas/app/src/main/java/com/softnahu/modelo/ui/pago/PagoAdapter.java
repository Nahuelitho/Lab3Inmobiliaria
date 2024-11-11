package com.softnahu.modelo.ui.pago;

import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softnahu.modelo.R;
import com.softnahu.modelo.model.Contrato;
import com.softnahu.modelo.model.Pago;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolderPago>{

    private List<Pago> pagos;
    private LayoutInflater li;
    private int idContrato;

    public PagoAdapter( List<Pago> pagos, LayoutInflater li, int id) {
        this.pagos = pagos != null ? pagos : new ArrayList<>();
        this.li = li;
        this.idContrato = id;
    }

    public void updateList(List<Pago> pagosLista) {
        pagos.clear();
        pagos.addAll(pagosLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagoAdapter.ViewHolderPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.tarjeta_pago, parent, false);
        return new ViewHolderPago(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPago holder, int position) {
        Pago pago = pagos.get(position);
        pago.setIdContrato(idContrato);
        // Establecer el texto para cada campo en el ViewHolder
        holder.tvCodigoPago.setText("Código de pago: " + pago.getId());
        holder.tvNumeroPago.setText("Número de pago: " + pago.getNumeroPago());
        holder.tvCodigoContrato.setText("Código de contrato: " + pago.getIdContrato());
        holder.tvImporte.setText("Importe: $" + pago.getImporte());
        Log.d("datoPago",pago.getDetalle()+"");
        Log.d("datoPago",pago.getFechaPago()+"");
        Log.d("datoPago",pago.getNumeroPago()+"");
        Log.d("datoPago",pago.getIdContrato()+"");
        // Parsear y formatear la fecha del pago
        String fechaPago = pago.getFechaPago();
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaPagoParsed;

        try {
            // Parsear la fecha
            fechaPagoParsed = formatoEntrada.parse(fechaPago);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Formatear la fecha y establecerla en el TextView
        String fechaPagoFormateada = formatoSalida.format(fechaPagoParsed);
        holder.tvFechaPago.setText("Fecha de pago: " + fechaPagoFormateada);


    }


    @Override
    public int getItemCount() {

        return pagos != null ? pagos.size() : 0;
    }


    public static class ViewHolderPago extends RecyclerView.ViewHolder {
        TextView tvCodigoPago, tvNumeroPago, tvCodigoContrato, tvImporte, tvFechaPago;

        public ViewHolderPago(@NonNull View itemView) {
            super(itemView);

            // Inicialización de cada campo con los IDs correspondientes de la tarjeta de pago
            tvCodigoPago = itemView.findViewById(R.id.tvCodigoPago);
            tvNumeroPago = itemView.findViewById(R.id.tvNumeroPago);
            tvCodigoContrato = itemView.findViewById(R.id.tvCodigoContrato);
            tvImporte = itemView.findViewById(R.id.tvImporte);
            tvFechaPago = itemView.findViewById(R.id.tvFechaPago);
        }
    }

}
