package dam.macr.proyecto_macr.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.bd.DBHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText t_user, t_password;
    private Button b_login, b_signup;
    private TextView et_password;
    private ImageView b_salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Cambiar el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Referencias a los elementos del layout
        t_user = findViewById(R.id.t_user);
        t_password = findViewById(R.id.t_password);
        b_login = findViewById(R.id.b_login);
        et_password = findViewById(R.id.et_password);
        b_salir = findViewById(R.id.b_salir);
        b_signup = findViewById(R.id.b_signup);

        //Carga los datos de la pantalla de registro
        Intent intent = getIntent();
        String usuario = intent.getStringExtra("usuario");
        String contrasena = intent.getStringExtra("contrasena");

        if (usuario != null) {
            t_user.setText(usuario);
        }
        if (contrasena != null) {
            t_password.setText(contrasena);
        }

        // Evento al pulsar el botón de login
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = t_user.getText().toString().trim();
                String contrasena = t_password.getText().toString().trim();

                DBHelper dbHelper = new DBHelper(LoginActivity.this);

                // Verificar si el usuario existe
                if (dbHelper.usuarioExiste(usuario)) {
                    // Verificar si la contraseña es correcta
                    if (dbHelper.verificarContrasena(usuario, contrasena)) {
                        // Usuario y contraseña correctos
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Evento al pulsar el botón de registrarse
        b_signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
               startActivity(intent);
           }
        });


        // Evento al pulsar "¿Has olvidado la contraseña?"
        et_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = t_user.getText().toString().trim();

                if (usuario.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Introduce tu nombre de usuario primero.", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper dbHelper = new DBHelper(LoginActivity.this);
                    String contrasena = dbHelper.obtenerContrasena(usuario);

                    if (contrasena != null) {
                        Toast.makeText(LoginActivity.this, "Tu contraseña es: " + contrasena, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Evento al pulsar icono de salida
        b_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoSalida();
            }
        });
    }


    // Método para mostrar el diálogo de salida
    private void mostrarDialogoSalida() {
        // Evento al pulsar el icono de salida
        b_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un AlertDialog para confirmar si desea salir
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("¿Estás seguro de que deseas salir de la aplicación?")
                        .setCancelable(false) // Desactivar el toque fuera para que no se cierre accidentalmente
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cerrarAplicacion(); // Cierra todas las actividades y termina la aplicación
                            }
                        })
                        .setNegativeButton("No", null) // No hace nada si selecciona "No"
                        .show();
            }
        });
    }

    // Método para cerrar la aplicación "autóctonamente"
    private void cerrarAplicacion() {
        System.exit(0); // Fuerza la salida de la aplicación
    }
}
