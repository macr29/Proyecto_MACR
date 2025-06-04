package dam.macr.proyecto_macr.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.adapters.ReservaAdapter;
import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.pojos.Reserva;

public class MainActivity extends AppCompatActivity {

    private TextView et_Bienvenida;
    private LinearLayout contTitPartidos;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recycler;
    private DBHelper dbHelper;
    private String usuario;
    private LinearLayout panel_reservas, panel_jugadores, panel_partidos, panel_nivel, panel_mensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Referencias
        et_Bienvenida = findViewById(R.id.et_bienvenida);
        recycler = findViewById(R.id.recyclerPartidos);
        contTitPartidos = findViewById(R.id.contenedorTituloPartidos);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        panel_reservas = findViewById(R.id.panel_reservar);
        panel_jugadores = findViewById(R.id.panel_jugadores);
        panel_partidos = findViewById(R.id.panel_partidos);
        panel_nivel = findViewById(R.id.panel_nivel);
        panel_mensajes = findViewById(R.id.panel_mensajes);

        // Obtener usuario y mostrar nombre
        usuario = getIntent().getStringExtra("usuario");
        String nombre = dbHelper.obtenerNombre(usuario);
        et_Bienvenida.setText("Hola " + nombre);

        //Botones de funcionalidades
        //RESERVAS
        panel_reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClubesActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        //JUGADORES
        panel_jugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UsuariosActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        //HISTÓRICO DE PARTIDOS
        panel_partidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PartidosActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        //CALCULADORA DE NIVEL
        panel_nivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NivelActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        //BUZÓN DE MENSAJES
        panel_mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuzonActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });


        // Toolbar y Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Listener del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_clubes) {
                Intent intent = new Intent(MainActivity.this, ClubesActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_partidos) {
                Intent intent = new Intent(MainActivity.this, PartidosActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_usuarios) {
                Intent intent = new Intent(MainActivity.this, UsuariosActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_nivel) {
                Intent intent = new Intent(MainActivity.this, NivelActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_mensajes) {
                Intent intent = new Intent(MainActivity.this, BuzonActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            } else if (id == R.id.nav_cerrar) {
                mostrarDialogoCerrarSesion();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        cargarPartidos(); // Cargar al inicio

        //Cerrar sesión al pulsar atrás
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mostrarDialogoCerrarSesion();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPartidos(); // Recargar al volver a la actividad
    }

    private void cargarPartidos() {
        int idUsuario = dbHelper.obtenerIdUsuario(usuario);
        List<Reserva> listaReservas = dbHelper.obtenerReservasUsuario(idUsuario);

        if (!listaReservas.isEmpty()) {
            contTitPartidos.setVisibility(View.VISIBLE);
        } else {
            contTitPartidos.setVisibility(View.GONE);
        }

        ReservaAdapter adapter = new ReservaAdapter(this, listaReservas);
        adapter.setOnPartidoClickListener(reserva -> mostrarDialogoAcciones(reserva));
        recycler.setAdapter(adapter);
    }

    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Quieres cerrar tu sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void mostrarDialogoAcciones(Reserva reserva) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Qué quieres hacer?")
                .setItems(new CharSequence[]{"\uD83C\uDFAF  Registrar Resultado", "➕  Nueva Reserva",  "✏\uFE0F  Editar Reserva", "❌  Cancelar Reserva"}, (dialog, which) -> {
                    if (which == 0) {
                        // REGISTRAR RESULTADO
                        Intent intent = new Intent(this, ResultadosActivity.class);
                        intent.putExtra("club", reserva.getNombreClub());
                        intent.putExtra("fecha", reserva.getFecha());
                        intent.putExtra("hora", reserva.getHora());
                        intent.putExtra("pista", reserva.getPista());
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);
                    } else if (which == 1) {
                        // RESERVAR
                        Intent intent = new Intent(this, ReservaActivity.class);
                        intent.putExtra("club_name", reserva.getNombreClub());
                        intent.putExtra("club_logo", reserva.getLogoResId());
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);

                    } else if (which == 2) {
                        // EDITAR RESERVA
                        dbHelper.eliminarReserva(usuario, reserva.getNombreClub(), reserva.getFecha(), reserva.getHora(), reserva.getPista());

                        Intent intent = new Intent(this, ReservaActivity.class);
                        intent.putExtra("club_name", reserva.getNombreClub());
                        intent.putExtra("club_logo", reserva.getLogoResId());
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);

                    } else if (which == 3) {
                        // CANCELAR RESERVA
                        mostrarConfirmacionEliminar(reserva);
                    }
                })
                .show();
    }

    private void mostrarConfirmacionEliminar(Reserva reserva) {
        new AlertDialog.Builder(this)
                .setTitle("❌  Cancelar Reserva")
                .setMessage("¿Estás seguro de que quieres cancelar esta reserva?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dbHelper.eliminarReserva(usuario, reserva.getNombreClub(), reserva.getFecha(), reserva.getHora(), reserva.getPista());
                    cargarPartidos();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
