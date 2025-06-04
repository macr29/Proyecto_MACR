package dam.macr.proyecto_macr.activities;

import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import java.util.Date;
import java.util.Locale;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.bd.DBHelper;

public class MensajeActivity extends AppCompatActivity {

    private String remitente;
    private String destinatario;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        // Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dbHelper = new DBHelper(this);

        // Obtener extras del intent
        remitente = getIntent().getStringExtra("remitente");
        destinatario = getIntent().getStringExtra("destinatario");

        // Referencias a los elementos del layout
        TextView tRemitente = findViewById(R.id.t_remitente);
        TextView tDestinatario = findViewById(R.id.t_destinatario);
        EditText editAsunto = findViewById(R.id.editAsunto);
        EditText editMensaje = findViewById(R.id.editMensaje);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        // Mostrar remitente y destinatario
        tRemitente.setText("De: " + remitente);
        tDestinatario.setText("Para: " + destinatario);

        // Acción del botón
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asunto = editAsunto.getText().toString().trim();
                String mensaje = editMensaje.getText().toString().trim();

                if (asunto.isEmpty() || mensaje.isEmpty()) {
                    Toast.makeText(MensajeActivity.this, "Por favor, rellena el asunto y el mensaje.", Toast.LENGTH_SHORT).show();
                } else {

                    String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

                    boolean insertado = dbHelper.insertarMensaje(remitente, destinatario, fecha, asunto, mensaje);

                    if (insertado) {
                        Toast.makeText(MensajeActivity.this, "Se ha enviado el mensaje al usuario @" + destinatario + " ✅", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MensajeActivity.this, "Error al enviar el mensaje.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
