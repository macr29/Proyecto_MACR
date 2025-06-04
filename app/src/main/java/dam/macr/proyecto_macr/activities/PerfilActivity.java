package dam.macr.proyecto_macr.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.pojos.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private String usuario;
    private int nivel;
    private TextView tUsuarioNombre, tEliminarUsuario, textDescripcionNivel;
    private EditText editNombre, editCiudad;
    private ImageButton imagenPerfil;
    private Button btnGuardar;
    private ProgressBar progressNivel;

    private String imagenBase64 = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Cambia el color de la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        DBHelper dbHelper = new DBHelper(this);

        tUsuarioNombre = findViewById(R.id.t_usuario_nombre);
        editNombre = findViewById(R.id.editNombre);
        editCiudad = findViewById(R.id.editCiudad);
        imagenPerfil = findViewById(R.id.btnImagenPerfil);
        btnGuardar = findViewById(R.id.btnGuardar);
        tEliminarUsuario = findViewById(R.id.tvEliminarUsuario);
        progressNivel = findViewById(R.id.progressNivel);
        textDescripcionNivel = findViewById(R.id.textDescripcionNivel);

        usuario = getIntent().getStringExtra("usuario");
        nivel = dbHelper.obtenerNivelUsuario(usuario);

        // Muestra el nivel en la barra de progreso
        progressNivel.setProgress(nivel);
        textDescripcionNivel.setText(getDescripcionNivel(nivel));

        // Obtener datos del usuario
        Usuario usuarioData = dbHelper.obtenerUsuario(usuario);

        if (usuarioData != null) {
            tUsuarioNombre.setText(usuarioData.getUsuario());
            editNombre.setText(usuarioData.getNombre());
            editCiudad.setText(usuarioData.getCiudad());
        }

        if (usuarioData.getImagen() == null) {
            imagenPerfil.setImageResource(R.drawable.amigo);
        } else {
            byte[] decodedString = Base64.decode(usuarioData.getImagen(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Escalar la imagen a un tamaño adecuado para la vista
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(decodedByte, 200, 200, true);
            imagenPerfil.setImageBitmap(scaledBitmap);
        }

        // Registra el selector de imagen con Activity Result API
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            // Reducción de la imagen
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                            imagenPerfil.setImageBitmap(scaledBitmap);

                            // Convertir la imagen a Base64
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            imagenBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imagenPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Botón Guardar Cambios
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = editNombre.getText().toString().trim();
            String nuevaCiudad = editCiudad.getText().toString().trim();

            boolean esImagenPorDefecto = imagenPerfil.getDrawable().getConstantState()
                    .equals(ContextCompat.getDrawable(this, R.drawable.amigo).getConstantState());

            // Si es imagen personalizada, guarda el Base64, si no, null
            String imagen = esImagenPorDefecto ? null : imagenBase64;

            boolean exito = dbHelper.actualizarPerfil(usuario, imagen, nuevoNombre, nuevaCiudad);

            if (exito) {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                intent.putExtra("usuario", usuario);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón Eliminar cuenta
        tEliminarUsuario.setOnClickListener(v -> {
            new AlertDialog.Builder(PerfilActivity.this)
                    .setTitle("Eliminar usuario❗")
                    .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Los cambios no se podrán deshacer.")
                    .setPositiveButton("ELIMINAR", (dialog, which) -> {
                        boolean eliminado = dbHelper.eliminarUsuario(usuario);
                        if (eliminado) {
                            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
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
