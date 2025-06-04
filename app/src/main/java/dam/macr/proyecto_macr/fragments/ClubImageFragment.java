package dam.macr.proyecto_macr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dam.macr.proyecto_macr.R;

public class ClubImageFragment extends Fragment {
    private ImageView imagenClub;

    public ClubImageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_club_image, container, false);
        imagenClub = vista.findViewById(R.id.imagen_club);

        // No establecer ninguna imagen al principio
        imagenClub.setImageResource(0);

        return vista;
    }

    public void actualizarImagen(int recursoImagen) {
        imagenClub.setImageResource(recursoImagen);
    }
}
