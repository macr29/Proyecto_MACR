package dam.macr.proyecto_macr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.adapters.ClubAdapter;
import dam.macr.proyecto_macr.pojos.Club;

public class ClubListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ClubAdapter clubAdapter;
    private List<Club> listaClubes;
    private OnClubSelectedListener listener;

    // Interfaz que la actividad debe implementar para manejar la selección del club
    public interface OnClubSelectedListener {
        void onClubSelected(Club club);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnClubSelectedListener) {
            listener = (OnClubSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " debe implementar OnClubSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_club_list, container, false);
        recyclerView = vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de clubes
        listaClubes = new ArrayList<>();
        listaClubes.add(new Club("Polideportivo San Jerónimo", "Petrer, Alicante", R.drawable.pp_logo1, R.drawable.pp_imagen1));
        listaClubes.add(new Club("Pádel Lacy", "Elda, Alicante", R.drawable.pp_logo2, R.drawable.pp_imagen2));
        listaClubes.add(new Club("Alonka Pádel", "Elda, Alicante", R.drawable.pp_logo3, R.drawable.pp_imagen3));
        listaClubes.add(new Club("Centro Excursionista Eldense", "Elda, Alicante", R.drawable.pp_logo4, R.drawable.pp_imagen4));
        listaClubes.add(new Club("Energy Pádel", "Elda, Alicante", R.drawable.pp_logo5, R.drawable.pp_imagen5));
        listaClubes.add(new Club("Red Pádel", "Alicante", R.drawable.pp_logo6, R.drawable.pp_imagen6));
        listaClubes.add(new Club("Area Padel Club", "Alicante", R.drawable.pp_logo7, R.drawable.pp_imagen7));

        // Pasar el listener al adaptador
        clubAdapter = new ClubAdapter(listaClubes, new ClubAdapter.OnClubSelectedListener() {
            @Override
            public void onClubSelected(Club club) {
                if (listener != null) {
                    listener.onClubSelected(club);
                }
            }
        });

        recyclerView.setAdapter(clubAdapter);

        return vista;
    }
}
