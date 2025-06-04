package dam.macr.proyecto_macr.activities;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.bd.DBHelper;

public class ResultadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultados);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Recoger los datos del Intent
        String club = getIntent().getStringExtra("club");
        String fecha = getIntent().getStringExtra("fecha");
        String hora = getIntent().getStringExtra("hora");
        String pista = getIntent().getStringExtra("pista");
        String usuario = getIntent().getStringExtra("usuario");

        DBHelper dbhelper = new DBHelper(this);
        String nombre = dbhelper.obtenerNombre(usuario);

        EditText editUsuario = findViewById(R.id.editUsuario);
        editUsuario.setText(nombre);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            String pareja = ((EditText) findViewById(R.id.editPareja)).getText().toString();
            String rival1 = ((EditText) findViewById(R.id.editRival1)).getText().toString();
            String rival2 = ((EditText) findViewById(R.id.editRival2)).getText().toString();

            int set1User = parseScore(R.id.editText1);
            int set1Rival = parseScore(R.id.editText2);
            int set2User = parseScore(R.id.editText3);
            int set2Rival = parseScore(R.id.editText4);
            int set3User = parseScore(R.id.editText5);
            int set3Rival = parseScore(R.id.editText6);

            if (!esSetValido(set1User, set1Rival) ||
                !esSetValido(set2User, set2Rival) && (set2User + set2Rival > 0) || // Permitir 2 sets
                !esSetValido(set3User, set3Rival) && (set3User + set3Rival > 0)) { // Solo si jugaron 3 sets

                Toast.makeText(this, "Uno o más sets tienen puntuaciones no válidas", Toast.LENGTH_LONG).show();
                return;
            }

            //Victoria o Derrota
            int resultado = resultado(set1User, set1Rival, set2User, set2Rival, set3User, set3Rival);

            //Insertar partido en bd
            int usuarioId = dbhelper.obtenerIdUsuario(usuario);
            dbhelper.insertarPartido(usuarioId, fecha, pareja, rival1, rival2, set1User, set1Rival, set2User, set2Rival, set3User, set3Rival, resultado);

            //Eliminar partido de reservas
            dbhelper.eliminarReserva(usuario, club, fecha, hora, pista);

            //Confirmar regsitro y abrir historial de partidos
            Toast.makeText(this, "Resultado registrado correctamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ResultadosActivity.this, PartidosActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);

            finish();
        });
    }

    //Método que indica si la puntuación de un set es válida
    private boolean esSetValido(int juegosA, int juegosB) {
        int diff = Math.abs(juegosA - juegosB);
        int max = Math.max(juegosA, juegosB);

        // Caso empate a 6 -> solo válido si termina en 7–6 o 6–7
        if (juegosA == 7 && juegosB == 6 || juegosA == 6 && juegosB == 7) {
            return true;
        }

        // Caso normal: uno gana 6 o 7 con 2 de diferencia
        if ((juegosA == 6 || juegosA == 7 || juegosB == 6 || juegosB == 7) && diff >= 2 && max >= 6 && max <= 7) {
            return true;
        }

        // En cualquier otro caso, no válido
        return false;
    }

    // Método que indica el resultado del partido para el usuario
// Devuelve 1 si gana, 0 si pierde, -1 si empatan
    private int resultado(int set1User, int set1Rival, int set2User, int set2Rival, int set3User, int set3Rival) {
        int setsUsuario = 0;
        int setsRival = 0;

        // Comprobar y contar sets ganados
        if (esSetValido(set1User, set1Rival)) {
            if (set1User > set1Rival) setsUsuario++;
            else if (set1User < set1Rival) setsRival++;
        }

        if (esSetValido(set2User, set2Rival)) {
            if (set2User > set2Rival) setsUsuario++;
            else if (set2User < set2Rival) setsRival++;
        }

        if ((set3User + set3Rival > 0) && esSetValido(set3User, set3Rival)) {
            if (set3User > set3Rival) setsUsuario++;
            else if (set3User < set3Rival) setsRival++;
        }

        if (setsUsuario > setsRival) return 1;      // Gana usuario
        else if (setsUsuario < setsRival) return 0; // Pierde usuario
        else return -1;                             // Empate
    }



    //Método que convierte la puntuación de los sets en enteros
    private int parseScore(int editTextId) {
        EditText et = findViewById(editTextId);
        String text = et.getText().toString().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

}