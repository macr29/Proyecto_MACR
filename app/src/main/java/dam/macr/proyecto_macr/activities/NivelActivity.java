package dam.macr.proyecto_macr.activities;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.bd.DBHelper;

public class NivelActivity extends AppCompatActivity {

    private SeekBar seekFrecuencia;
    private TextView textFrecuenciaValor, textDescripcionNivel;
    private CheckBox checkEntrenos, checkTorneos;
    private RadioGroup radioGroupPosicion, radioGroupEstilo;
    private ProgressBar progressNivel;
    private Button btnCalcular;
    private String usuario;
    private int nivel;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nivel);

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

        seekFrecuencia = findViewById(R.id.seekFrecuencia);
        textFrecuenciaValor = findViewById(R.id.textFrecuenciaValor);
        checkEntrenos = findViewById(R.id.checkEntrenos);
        checkTorneos = findViewById(R.id.checkTorneos);
        radioGroupPosicion = findViewById(R.id.radioGroupPosicion);
        radioGroupEstilo = findViewById(R.id.radioGroupEstilo);
        progressNivel = findViewById(R.id.progressNivel);
        textDescripcionNivel = findViewById(R.id.textDescripcionNivel);
        btnCalcular = findViewById(R.id.btnCalcularNivel);

        usuario = getIntent().getStringExtra("usuario");
        nivel = dbHelper.obtenerNivelUsuario(usuario);

        // Muestra el nivel en la barra de progreso
        progressNivel.setProgress(nivel);
        textDescripcionNivel.setText(getDescripcionNivel(nivel));

        seekFrecuencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textFrecuenciaValor.setText(progress + " partidos");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnCalcular.setOnClickListener(v -> calcularNivel());
    }

    private void calcularNivel() {
        int puntos = 0;

        // Frecuencia: hasta 30 partidos → máx 5 puntos
        int frecuencia = seekFrecuencia.getProgress();
        if (frecuencia >= 20) puntos += 5;
        else if (frecuencia >= 15) puntos += 4;
        else if (frecuencia >= 10) puntos += 3;
        else if (frecuencia >= 5) puntos += 2;
        else if (frecuencia >= 2) puntos += 1;
        else if (frecuencia == 0) puntos -= 3;

        // Entrenos y torneos
        if (checkEntrenos.isChecked()) puntos += 2;
        if (checkTorneos.isChecked()) puntos += 2;

        // Posición en pista
        int idPosicion = radioGroupPosicion.getCheckedRadioButtonId();
        if (idPosicion == R.id.radioReves) puntos += 2;
        else if (idPosicion == R.id.radioDerecha) puntos += 1;
        // Indiferente suma 0

        // Estilo de juego
        int idEstilo = radioGroupEstilo.getCheckedRadioButtonId();
        if (idEstilo == R.id.radioOfensivo) puntos += 1;
        else if (idEstilo == R.id.radioDefensivo) puntos += 1;
        else if (idEstilo == R.id.radioEquilibrado) puntos += 2;

        // Nivel del 1 al 5
        int nivel = 1;
        if (puntos >= 13) nivel = 5;
        else if (puntos >= 11) nivel = 4;
        else if (puntos >= 7) nivel = 3;
        else if (puntos >= 4) nivel = 2;

        progressNivel.setProgress(nivel);

        String descripcion = getDescripcionNivel(nivel);

        textDescripcionNivel.setText(descripcion);

        // Actualizar nivel del usuario en la base de datos
        dbHelper.actualizarNivelUsuario(usuario, nivel);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Nivel Actualizado ✅")
                .setMessage("Tu nuevo nivel es " + descripcion)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish(); // Cierra el Activity
                })
                .setCancelable(false)
                .show();
    }

    private String getDescripcionNivel(int nivel) {
        switch (nivel) {
            case 1: return "PRINCIPIANTE";
            case 2: return "AMATEUR";
            case 3: return "AVANZADO";
            case 4: return "EXPERTO";
            case 5: return "MÁSTER";
            default: return "";
        }
    }

}
