package dam.macr.proyecto_macr.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.fragments.ClubListFragment;
import dam.macr.proyecto_macr.fragments.ClubImageFragment;
import dam.macr.proyecto_macr.pojos.Club;

public class ClubesActivity extends AppCompatActivity implements ClubListFragment.OnClubSelectedListener {
    private ClubImageFragment clubImageFragment;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        usuario = getIntent().getStringExtra("usuario");

        //Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Cargar Fragmento de la Lista
        if (savedInstanceState == null) {
            ClubListFragment clubListFragment = new ClubListFragment();
            clubImageFragment = new ClubImageFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_lista, clubListFragment);
            //transaction.replace(R.id.fragment_imagen, clubImageFragment);
            transaction.commit();
        }
    }

    @Override
    public void onClubSelected(Club club) {
        /*
        if (clubImageFragment != null) {
            clubImageFragment.actualizarImagen(club.getImagenRecursoId());
        }*/

        Intent intent = new Intent(this, ReservaActivity.class);
        intent.putExtra("club_name", club.getNombre());
        intent.putExtra("club_city", club.getCiudad());
        intent.putExtra("club_logo", club.getLogoRecursoId());
        intent.putExtra("club_image", club.getImagenRecursoId());
        intent.putExtra("usuario", usuario);

        // Iniciar la nueva actividad
        startActivity(intent);
    }
}
