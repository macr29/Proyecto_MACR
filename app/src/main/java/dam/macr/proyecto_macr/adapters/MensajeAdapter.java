package dam.macr.proyecto_macr.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Mensaje;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {

    private List<Mensaje> listaMensajes;
    private String usuario;
    private OnMensajeClickListener listener;

    public MensajeAdapter(List<Mensaje> mensajes, String usuario) {
        this.listaMensajes = mensajes;
        this.usuario = usuario;
    }

    public interface OnMensajeClickListener {
        void onMensajeClick(Mensaje mensaje);
    }

    public void setOnMensajeClickListener(OnMensajeClickListener listener) {
        this.listener = listener;
    }

    public void actualizarLista(List<Mensaje> nuevosMensajes) {
        this.listaMensajes = nuevosMensajes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_item, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        Mensaje mensaje = listaMensajes.get(position);

        holder.tFecha.setText(mensaje.getFecha());
        holder.tAsunto.setText(mensaje.getAsunto());
        holder.tMensaje.setText(mensaje.getMensaje());

        if (mensaje.getRemitente().equals(usuario)) {
            holder.tRemitente.setText("@" + mensaje.getDestinatario());
            holder.ivFlecha.setImageResource(R.drawable.flecha_derecha);
            holder.layoutMensaje.setBackgroundColor(Color.parseColor("#046BFA"));
            holder.tRemitente.setTextColor(Color.WHITE);
            holder.tFecha.setTextColor(Color.WHITE);
            holder.tAsunto.setTextColor(Color.WHITE);
            holder.tMensaje.setTextColor(Color.WHITE);
        } else {
            holder.tRemitente.setText("@" + mensaje.getRemitente());
            holder.ivFlecha.setImageResource(R.drawable.flecha_izquierda);
            holder.layoutMensaje.setBackgroundColor(Color.WHITE);
            holder.tRemitente.setTextColor(Color.BLACK);
            holder.tFecha.setTextColor(Color.parseColor("#888888"));
            holder.tAsunto.setTextColor(Color.BLACK);
            holder.tMensaje.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMensajeClick(mensaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    static class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView tRemitente, tFecha, tAsunto, tMensaje;
        View layoutMensaje;
        ImageView ivFlecha;

        MensajeViewHolder(View itemView) {
            super(itemView);
            tRemitente = itemView.findViewById(R.id.tRemitente);
            tFecha = itemView.findViewById(R.id.tFecha);
            tAsunto = itemView.findViewById(R.id.tAsunto);
            tMensaje = itemView.findViewById(R.id.tMensaje);
            ivFlecha = itemView.findViewById(R.id.ivFlecha);
            layoutMensaje = itemView.findViewById(R.id.layoutMensaje);
        }
    }
}
