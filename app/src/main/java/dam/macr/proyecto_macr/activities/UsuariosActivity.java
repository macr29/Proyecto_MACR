package dam.macr.proyecto_macr.activities;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.macr.proyecto_macr.adapters.UsuarioAdapter;
import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Usuario;

public class UsuariosActivity extends AppCompatActivity {

   private EditText editBuscar;
   private ImageView icono_lupa;
   private String usuario;
   private int idUsuario;
   private DBHelper dbHelper;
   private UsuarioAdapter adapter;
   private Spinner spinnerNivel;
   private final String[] niveles = {"Cualquiera", "Principiante", "Amateur", "Avanzado", "Experto", "Máster"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        editBuscar = findViewById(R.id.editBuscar);
        icono_lupa = findViewById(R.id.icono_lupa);
        spinnerNivel = findViewById(R.id.spinnerNivel);

        // Crear el adaptador del Spinner con los niveles
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, niveles);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivel.setAdapter(adapterSpinner);


        usuario = getIntent().getStringExtra("usuario");

        //Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        RecyclerView recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        recyclerUsuarios.setLayoutManager(new GridLayoutManager(this, 2)); // Grid de 2 columnas

        dbHelper = new DBHelper(this);
        idUsuario = dbHelper.obtenerIdUsuario(usuario);// ID del usuario logueado

        List<Usuario> listaUsuarios = dbHelper.obtenerTodosLosUsuarios(idUsuario);

        adapter = new UsuarioAdapter(this, listaUsuarios, usuario);
        recyclerUsuarios.setAdapter(adapter);

        //Filtrar usuarios con lupa
        icono_lupa.setOnClickListener(v -> filtrarUsuarios());

        //Filtrar usaurios por spinner
        spinnerNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarUsuarios(); // Llama a tu método para actualizar la lista
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filtrarUsuarios() {
        String textoBusqueda = editBuscar.getText().toString().trim();
        String nivelSeleccionado = spinnerNivel.getSelectedItem().toString();
        TextView textoVacio = findViewById(R.id.textoVacio);
        int nivel = 0;

        // Si ha seleccionado un nivel específico, traducimos el nombre a número
        switch (nivelSeleccionado) {
            case "Principiante": nivel = 1; break;
            case "Amateur": nivel = 2; break;
            case "Avanzado": nivel = 3; break;
            case "Experto": nivel = 4; break;
            case "Máster": nivel = 5; break;
            default: nivel = 0; break; // "Todos"
        }

        List<Usuario> listaFiltrada;
        if (textoBusqueda.isEmpty() && nivel == 0) {
            listaFiltrada = dbHelper.obtenerTodosLosUsuarios(idUsuario);
        } else {
            listaFiltrada = dbHelper.FiltroUsuarios(textoBusqueda, nivel, idUsuario);
        }

        adapter.actualizarLista(listaFiltrada);

        //Texto No se han encontrado usuarios
        if (listaFiltrada.isEmpty()) {
            textoVacio.setVisibility(View.VISIBLE);
        } else {
            textoVacio.setVisibility(View.GONE);
        }

    }
}
