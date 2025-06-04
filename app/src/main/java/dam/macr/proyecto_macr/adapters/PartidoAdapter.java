package dam.macr.proyecto_macr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Partido;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private Context context;
    private List<Partido> listaPartidos;

    public PartidoAdapter(Context context, List<Partido> listaPartidos) {
        this.context = context;
        this.listaPartidos = listaPartidos;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.partido_item, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = listaPartidos.get(position);

        holder.textFecha.setText(partido.getFecha());

        holder.textUsuarioPareja.setText(partido.getNombreUsuario() + " / " + partido.getPareja());
        holder.textRivales.setText(partido.getRival1() + " / " + partido.getRival2());

        // Mostrar resultados por sets
        String resultadoUsuario = partido.getResultadoP1S1() + " | " + partido.getResultadoP1S2() + " | " + partido.getResultadoP1S3();
        String resultadoRivales = partido.getResultadoP2S1() + " | " + partido.getResultadoP2S2() + " | " + partido.getResultadoP2S3();

        holder.textResultadoUsuario.setText(resultadoUsuario);
        holder.textResultadoRivales.setText(resultadoRivales);

        // Color del panel lateral según victoria (verde) o derrota (rojo)
        if (partido.getResultado() == 1) {
            holder.viewResultado.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.textResultado.setText("VICTORIA");
            holder.textResultado.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if (partido.getResultado() == 0){
            holder.viewResultado.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.textResultado.setText("DERROTA");
            holder.textResultado.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.viewResultado.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.textResultado.setText("EMPATE");
            holder.textResultado.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return listaPartidos.size();
    }

    public static class PartidoViewHolder extends RecyclerView.ViewHolder {

        TextView textResultado, textFecha, textUsuarioPareja, textRivales, textResultadoUsuario, textResultadoRivales;
        View viewResultado;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            textResultado = itemView.findViewById(R.id.textResultado);
            textFecha = itemView.findViewById(R.id.textFecha);
            textUsuarioPareja = itemView.findViewById(R.id.textUsuarioPareja);
            textRivales = itemView.findViewById(R.id.textRivales);
            textResultadoUsuario = itemView.findViewById(R.id.textResultadoUsuario);
            textResultadoRivales = itemView.findViewById(R.id.textResultadoRivales);
            viewResultado = itemView.findViewById(R.id.viewResultado);
        }
    }
}
