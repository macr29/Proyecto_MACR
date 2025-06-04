package dam.macr.proyecto_macr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Reserva;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {
    private List<Reserva> reservas;
    private Context context;
    private OnPartidoClickListener listener;

    public ReservaAdapter(Context context, List<Reserva> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView nombreClub, pistaFecha, Hora;

        public ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.imgLogoClub);
            nombreClub = itemView.findViewById(R.id.txtNombreClub);
            pistaFecha = itemView.findViewById(R.id.txtPistaFecha);
            Hora = itemView.findViewById(R.id.txtHora);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reserva_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        // Aquí manejamos el click dentro de onCreateViewHolder
        view.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPartidoClick(reservas.get(holder.getAdapterPosition()));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        holder.logo.setImageResource(reserva.getLogoResId());
        holder.nombreClub.setText(reserva.getNombreClub());
        holder.pistaFecha.setText(reserva.getPista() + "   " + reserva.getFecha());
        holder.Hora.setText(reserva.getHora());
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    // Interfaz para manejar el clic en el partido
    public interface OnPartidoClickListener {
        void onPartidoClick(Reserva reserva);
    }

    // Método para configurar el listener
    public void setOnPartidoClickListener(OnPartidoClickListener listener) {
        this.listener = listener;
    }
}

