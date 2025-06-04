package dam.macr.proyecto_macr.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import dam.macr.proyecto_macr.R;

public class ConfigActivity extends AppCompatActivity {

    private TextView tvTamañoFuente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Obtener el usuario desde el Intent
        String usuario = getIntent().getStringExtra("usuario");

        TextView tvUsuario = findViewById(R.id.tv_usuario);
        if (usuario != null) {
            tvUsuario.setText("Usuario: " + usuario);
        }

        // Switch de notificaciones
        Switch switchNotificaciones = findViewById(R.id.switch_notificaciones);
        switchNotificaciones.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String estado = isChecked ? "Notificaciones activadas" : "Notificaciones desactivadas";
            Toast.makeText(ConfigActivity.this, estado, Toast.LENGTH_SHORT).show();
        });

        // CheckBox de modo oscuro
        CheckBox checkBoxModoOscuro = findViewById(R.id.checkbox_modo_oscuro);
        checkBoxModoOscuro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String mensaje = isChecked ? "Modo oscuro activado" : "Modo oscuro desactivado";
            Toast.makeText(ConfigActivity.this, mensaje, Toast.LENGTH_SHORT).show();
        });

        // SeekBar para ajustar el tamaño de la fuente
        tvTamañoFuente = findViewById(R.id.tv_tamaño_fuente);
        SeekBar seekBarFuente = findViewById(R.id.seekbar_fuente);
        seekBarFuente.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float tamaño = 14 + progress;
                tvTamañoFuente.setTextSize(tamaño);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Botón de guardar cambios
        Button btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(v ->
                Toast.makeText(ConfigActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        );
    }
}
