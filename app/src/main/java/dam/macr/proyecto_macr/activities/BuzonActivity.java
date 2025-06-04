package dam.macr.proyecto_macr.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.adapters.MensajeAdapter;
import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.pojos.Mensaje;

public class BuzonActivity extends AppCompatActivity {

    private RecyclerView recyclerMensajes;
    private DBHelper dbHelper;
    private MensajeAdapter adapter;
    private String usuario;
    private TextView textSinMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        usuario = getIntent().getStringExtra("usuario");

        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        textSinMensajes = findViewById(R.id.textSinMensajes);
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        cargarMensajes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMensajes();
    }

    private void cargarMensajes() {
        List<Mensaje> mensajes = dbHelper.obtenerMensajesUsuario(usuario);

        if (mensajes.isEmpty()) {
            textSinMensajes.setVisibility(View.VISIBLE);
            recyclerMensajes.setVisibility(View.GONE);
        } else {
            textSinMensajes.setVisibility(View.GONE);
            recyclerMensajes.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new MensajeAdapter(mensajes, usuario);
                recyclerMensajes.setAdapter(adapter);
                adapter.setOnMensajeClickListener(mensaje -> mostrarOpcionesMensaje(mensaje));
            } else {
                adapter.actualizarLista(mensajes);
            }
        }
    }

    private void mostrarOpcionesMensaje(Mensaje mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (mensaje.getRemitente().equals(usuario)) {
            builder.setTitle("¿Qué quieres hacer?")
                    .setItems(new CharSequence[]{"❌  Cancelar envío"}, (dialog, which) -> {
                        if (which == 0) {
                            // Confirmación antes de cancelar
                            new AlertDialog.Builder(this)
                                    .setTitle("❌  Cancelar Envío")
                                    .setMessage("¿Seguro que quieres cancelar el envío de este mensaje?")
                                    .setPositiveButton("SÍ", (d, w) -> {
                                        dbHelper.eliminarMensaje(
                                                mensaje.getRemitente(),
                                                mensaje.getDestinatario(),
                                                mensaje.getFecha(),
                                                mensaje.getAsunto()
                                        );

                                        Toast.makeText(this, "Envío cancelado ✅", Toast.LENGTH_SHORT).show();
                                        cargarMensajes();
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    })
                    .show();
        } else {
            builder.setTitle("¿Qué quieres hacer?")
                    .setItems(new CharSequence[]{"💬  Contestar", "❌  Eliminar mensaje"}, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(this, MensajeActivity.class);
                            intent.putExtra("remitente", usuario);
                            intent.putExtra("destinatario", mensaje.getRemitente());
                            startActivity(intent);
                        } else if (which == 1) {
                            // Confirmación antes de eliminar
                            new AlertDialog.Builder(this)
                                    .setTitle("❌  Eliminar Mensaje")
                                    .setMessage("¿Seguro que quieres eliminar este mensaje?")
                                    .setPositiveButton("ELIMINAR", (d, w) -> {
                                        dbHelper.eliminarMensaje(
                                                mensaje.getRemitente(),
                                                mensaje.getDestinatario(),
                                                mensaje.getFecha(),
                                                mensaje.getAsunto()
                                        );

                                        Toast.makeText(this, "Mensaje eliminado ✅", Toast.LENGTH_SHORT).show();
                                        cargarMensajes();
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    })
                    .show();
        }
    }
}
