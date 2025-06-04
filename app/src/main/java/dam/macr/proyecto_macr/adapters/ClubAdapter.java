package dam.macr.proyecto_macr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Club;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {
    private List<Club> listaClubes;
    private OnClubSelectedListener listener; // Listener para notificar cuando se selecciona un club

    // Constructor que recibe la lista y el listener
    public ClubAdapter(List<Club> listaClubes, OnClubSelectedListener listener) {
        this.listaClubes = listaClubes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_list_item, parent, false);
        return new ClubViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = listaClubes.get(position);
        holder.nombreClub.setText(club.getNombre());
        holder.ciudadClub.setText(club.getCiudad());
        holder.logoClub.setImageResource(club.getLogoRecursoId());

        // Manejo del clic en un club
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClubSelected(club); // Notifica a la actividad cuando se selecciona un club
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaClubes.size();
    }

    static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView nombreClub, ciudadClub;
        ImageView logoClub;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreClub = itemView.findViewById(R.id.nombre_club);
            ciudadClub = itemView.findViewById(R.id.ciudad_club);
            logoClub = itemView.findViewById(R.id.logo_club);
        }
    }

    // Interfaz para notificar cuando se selecciona un club
    public interface OnClubSelectedListener {
        void onClubSelected(Club club);
    }
}
