package dam.macr.proyecto_macr.activities;

import dam.macr.proyecto_macr.bd.DBHelper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.widget.ImageView;
import android.widget.Toast;
import dam.macr.proyecto_macr.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText t_nombre, t_user, t_ciudad, t_password, t_password2;
    private Button b_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Crear instancia del helper
        DBHelper dbHelper = new DBHelper(SignUpActivity.this);

        t_nombre = findViewById(R.id.T_nombre);
        t_user = findViewById(R.id.T_user);
        t_ciudad = findViewById(R.id.T_ciudad);
        t_password = findViewById(R.id.T_password);
        t_password2 = findViewById(R.id.T_password2);
        b_signup = findViewById(R.id.B_signup);

        // Evento al pulsar el botón de login
        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = t_nombre.getText().toString().trim();
                String usuario = t_user.getText().toString().trim();
                String ciudad = t_ciudad.getText().toString().trim();
                String pass = t_password.getText().toString().trim();
                String pass2 = t_password2.getText().toString().trim();

                //Comprobación de que los capos importantes se rellenan
                if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty() || pass2.isEmpty())
                    Toast.makeText(SignUpActivity.this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
                //Comprobación de que ambas contraseñas son iguales
                else if (!pass.equals(pass2))
                    Toast.makeText(SignUpActivity.this, "Ambas contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
                else if (pass.length() < 8)
                    Toast.makeText(SignUpActivity.this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
                else if (!pass.matches(".*\\d.*"))
                    Toast.makeText(SignUpActivity.this, "La contraseña debe contener al menos 1 número", Toast.LENGTH_SHORT).show();
                else
                {
                    //Guarda datos en bd
                    boolean insertado = dbHelper.insertarUsuario(usuario, pass, nombre, ciudad);

                    if (insertado) {
                        Toast.makeText(SignUpActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

                        // Pasa al login
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra("usuario", usuario);
                        intent.putExtra("contrasena", pass);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: El usuario ya existe o no se pudo guardar.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}