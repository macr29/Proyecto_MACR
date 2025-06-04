package dam.macr.proyecto_macr.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

import dam.macr.proyecto_macr.bd.DBHelper;
import dam.macr.proyecto_macr.R;

public class ReservaActivity extends AppCompatActivity {

    private String usuario;
    private String clubName;
    private DBHelper dbHelper = new DBHelper(this);
    private TextView textoFecha;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private TableLayout tablaReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reserva);

        usuario = getIntent().getStringExtra("usuario");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul));
        }

        // Bloquear orientación a Vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Intent intent = getIntent();
        clubName = intent.getStringExtra("club_name");
        String clubCity = intent.getStringExtra("club_city");
        int clubLogo = intent.getIntExtra("club_logo", -1);

        TextView clubNameTextView = findViewById(R.id.nombre_club);
        TextView clubCityTextView = findViewById(R.id.ciudad_club);
        ImageView clubLogoImageView = findViewById(R.id.logo_club);
        clubNameTextView.setText(clubName);
        clubCityTextView.setText(clubCity);
        clubLogoImageView.setImageResource(clubLogo);

        Button btnSeleccionarFecha = findViewById(R.id.btn_seleccionar_fecha);
        textoFecha = findViewById(R.id.texto_fecha);
        tablaReservas = findViewById(R.id.tabla_reservas);

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fechaActual = sdf.format(calendar.getTime());
        textoFecha.setText("Fecha: " + fechaActual);

        actualizarTablaConFecha(fechaActual);

        btnSeleccionarFecha.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ReservaActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        calendar.set(year1, month1, dayOfMonth);
                        String fechaSeleccionada = sdf.format(calendar.getTime());
                        textoFecha.setText("Fecha: " + fechaSeleccionada);
                        actualizarTablaConFecha(fechaSeleccionada);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    private void actualizarTablaConFecha(String fechaSeleccionada) {
        int idUsuario = dbHelper.obtenerIdUsuario(usuario);
        int idClub = dbHelper.obtenerIdClub(clubName);

        // Obtener la fecha actual (solo parte de la fecha)
        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);

        // Convertir la fecha seleccionada a Calendar
        Calendar fechaSeleccionadaCal = Calendar.getInstance();
        try {
            fechaSeleccionadaCal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaSeleccionada));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        boolean esPasada = fechaSeleccionadaCal.before(hoy);

        for (int i = 0; i < tablaReservas.getChildCount(); i++) {
            TableRow row = (TableRow) tablaReservas.getChildAt(i);
            int rowIndex = tablaReservas.indexOfChild(row);
            if (rowIndex < 1 || rowIndex > 4) continue;

            for (int j = 0; j < row.getChildCount(); j++) {
                TextView cell = (TextView) row.getChildAt(j);
                int pista = j + 1;
                String horario;
                switch (rowIndex) {
                    case 1: horario = "08:00 - 09:30"; break;
                    case 2: horario = "09:30 - 11:00"; break;
                    case 3: horario = "11:00 - 12:30"; break;
                    case 4: horario = "12:30 - 14:00"; break;
                    default: horario = "Desconocido"; break;
                }

                String finalHorario = horario;

                if (esPasada) {
                    // Mostrar "NO DISPONIBLE" para fechas anteriores
                    cell.setBackgroundColor(ContextCompat.getColor(this, R.color.azul_claro)); // Asegúrate de definirlo en colors.xml
                    cell.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                    cell.setText("NO DISPONIBLE");
                    cell.setOnClickListener(null); // Desactiva click
                } else {
                    if (dbHelper.estaReservada(idClub, pista, horario, fechaSeleccionada)) {
                        int idUsuarioReserva = dbHelper.obtenerIdUsuarioPorReserva(idClub, pista, horario, fechaSeleccionada);
                        String nombreUsuarioReserva = dbHelper.obtenerUsuarioPorId(idUsuarioReserva);
                        cell.setBackgroundColor(ContextCompat.getColor(this, R.color.rojo));
                        cell.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                        cell.setText("RESERVADO\n" + nombreUsuarioReserva);
                    } else {
                        cell.setBackgroundColor(ContextCompat.getColor(this, R.color.azul_claro));
                        cell.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                        cell.setText(horario);
                    }

                    // Click solo si la fecha es válida
                    cell.setOnClickListener(v -> {
                        if (dbHelper.estaReservada(idClub, pista, finalHorario, fechaSeleccionada)) {
                            Toast.makeText(getApplicationContext(), "Esta pista ya está reservada en ese horario.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        new AlertDialog.Builder(v.getContext())
                                .setMessage("¿Quieres reservar la Pista " + pista + " en el horario de " + finalHorario + "?")
                                .setCancelable(true)
                                .setPositiveButton("Sí", (dialog, id) -> {
                                    dbHelper.insertarReserva(idUsuario, idClub, pista, finalHorario, fechaSeleccionada);
                                    cell.setBackgroundColor(ContextCompat.getColor(this, R.color.rojo));
                                    cell.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                                    cell.setText("RESERVADO\n" + usuario);
                                    Toast.makeText(getApplicationContext(), "Reserva realizada para Pista " + pista + " en el horario de " + finalHorario, Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", null)
                                .show();
                    });
                }
            }
        }
    }

}
