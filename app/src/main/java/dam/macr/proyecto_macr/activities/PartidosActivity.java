package dam.macr.proyecto_macr.activities;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.adapters.PartidoAdapter;
import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.pojos.Partido;
import dam.macr.proyecto_macr.pojos.Usuario;

public class PartidosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidoAdapter adapter;
    private List<Partido> listaPartidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_partidos);

        // Cambia el color de la barra de arriba
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        DBHelper dbHelper = new DBHelper(this);
        String nombre;

        // Obtener el usuario desde el intent
        String usuario = getIntent().getStringExtra("usuario");
        int idUsuario = dbHelper.obtenerIdUsuario(usuario);

        // Inicializar la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Crear la lista de partidos
        listaPartidos = new ArrayList<>();

        // Consultar la base de datos
        Cursor cursor = db.rawQuery("SELECT * FROM partidos WHERE id_usuario = ? ORDER BY fecha DESC ",
                new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            do {
                Partido partido = new Partido(
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                        cursor.getString(cursor.getColumnIndexOrThrow("pareja")),
                        cursor.getString(cursor.getColumnIndexOrThrow("rival1")),
                        cursor.getString(cursor.getColumnIndexOrThrow("rival2")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p1s1")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p2s1")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p1s2")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p2s2")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p1s3")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado_p2s3")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("resultado")),
                        nombre = dbHelper.obtenerNombre(usuario)
                );
                listaPartidos.add(partido);
            } while (cursor.moveToNext());

            TextView textoSinPartidos = findViewById(R.id.textSinPartidos);
            TextView titulo = findViewById(R.id.titulo);
            RecyclerView recyclerView = findViewById(R.id.recyclerPartidos);

            if (listaPartidos.isEmpty()) {
                textoSinPartidos.setVisibility(View.VISIBLE);
                titulo.setVisibility(View.GONE);
            } else {
                textoSinPartidos.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                PartidoAdapter adapter = new PartidoAdapter(this, listaPartidos);
                recyclerView.setAdapter(adapter);
            }

        }

        cursor.close();
        db.close();

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerPartidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PartidoAdapter(this, listaPartidos);
        recyclerView.setAdapter(adapter);
    }
}
