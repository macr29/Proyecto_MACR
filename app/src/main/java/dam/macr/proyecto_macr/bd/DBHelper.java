package dam.macr.proyecto_macr.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dam.macr.proyecto_macr.R;
import dam.macr.proyecto_macr.pojos.Mensaje;
import dam.macr.proyecto_macr.pojos.Reserva;
import dam.macr.proyecto_macr.pojos.Usuario;

public class DBHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "bd_pingpadel.db";
    private static final int VERSION_BD = 1;

    private Context context;

    public DBHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Tabla usuarios
        db.execSQL("CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT UNIQUE," +
                "contrasena TEXT," +
                "nombre TEXT," +
                "ciudad TEXT," +
                "nivel INTEGER DEFAULT 1," +
                "imagen BLOB)");
        usuarioPredeterminado(db,"admin",".","Administrador","Petrer");
        usuarioPredeterminado(db,"castruiz29",".","Miguel Ángel","Petrer");

        //Tabla clubes
        db.execSQL("CREATE TABLE IF NOT EXISTS clubes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "ciudad TEXT NOT NULL, " +
                "logo INTEGER, " +
                "imagen INTEGER)");

        insertarClub(db, "Polideportivo San Jerónimo", "Petrer, Alicante", R.drawable.pp_logo1, R.drawable.pp_imagen1);
        insertarClub(db, "Pádel Lacy", "Elda, Alicante", R.drawable.pp_logo2, R.drawable.pp_imagen2);
        insertarClub(db, "Alonka Pádel", "Elda, Alicante", R.drawable.pp_logo3, R.drawable.pp_imagen3);
        insertarClub(db, "Centro Excursionista Eldense", "Elda, Alicante", R.drawable.pp_logo4, R.drawable.pp_imagen4);
        insertarClub(db, "Energy Pádel", "Elda, Alicante", R.drawable.pp_logo5, R.drawable.pp_imagen5);
        insertarClub(db, "Red Pádel", "Alicante", R.drawable.pp_logo6, R.drawable.pp_imagen6);
        insertarClub(db, "Area Padel Club", "Alicante", R.drawable.pp_logo7, R.drawable.pp_imagen7);

        //Tabla Reservas
        db.execSQL("CREATE TABLE IF NOT EXISTS reservas (" +
                "id_reserva INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "id_club INTEGER," +
                "pista INTEGER," +
                "hora TEXT," +
                "fecha TEXT," +
                "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)," +
                "FOREIGN KEY(id_club) REFERENCES clubes(id))");

        // Tabla partidos
        db.execSQL("CREATE TABLE IF NOT EXISTS partidos (" +
                "id_partido INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "pareja TEXT," +
                "rival1 TEXT," +
                "rival2 TEXT," +
                "resultado_p1s1 INTEGER," +
                "resultado_p2s1 INTEGER," +
                "resultado_p1s2 INTEGER," +
                "resultado_p2s2 INTEGER," +
                "resultado_p1s3 INTEGER," +
                "resultado_p2s3 INTEGER," +
                "resultado INTEGER," +
                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario))");

        // Tabla mensajes
        db.execSQL("CREATE TABLE mensajes (" +
                "id_mensaje INTEGER PRIMARY KEY AUTOINCREMENT," +
                "remitente TEXT," +
                "destinatario TEXT," +
                "fecha TEXT," +
                "asunto TEXT," +
                "mensaje TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    /*
    ***CREAR CUENTA***
    */
    public boolean insertarUsuario(String usuario, String contrasena, String nombre, String ciudad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        values.put("nombre", nombre);
        values.put("ciudad", ciudad);
        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }

    private void usuarioPredeterminado(SQLiteDatabase db, String usuario, String contrasena, String nombre, String ciudad) {
        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        values.put("nombre", nombre);
        values.put("ciudad", ciudad);
        db.insert("usuarios", null, values);
    }


    /*
     ***LOGIN***
     */

    // Método en DBHelper para verificar si el usuario existe
    public boolean usuarioExiste(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // Usuario encontrado
        } else {
            return false; // Usuario no encontrado
        }
    }

    // Método en DBHelper para verificar si la contraseña es correcta para el usuario
    public boolean verificarContrasena(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT contrasena FROM usuarios WHERE usuario = ?", new String[]{usuario});

        if (cursor != null && cursor.moveToFirst()) {
            String contrasenaGuardada = cursor.getString(cursor.getColumnIndex("contrasena"));
            cursor.close();
            return contrasena.equals(contrasenaGuardada); // Compara la contraseña
        } else {
            return false; // Usuario no encontrado o error
        }
    }

    // Devuelve la contraseña del usuario si existe
    public String obtenerContrasena(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT contrasena FROM usuarios WHERE usuario = ?", new String[]{usuario});

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("contrasena");
            if (index != -1) {
                String contrasena = cursor.getString(index);
                cursor.close();
                return contrasena;
            }
            cursor.close();
        }
        return null; // Usuario no encontrado o error
    }

    /*
     ***HOME***
     */

    public String obtenerNombre(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM usuarios WHERE usuario = ?", new String[]{usuario});

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("nombre");
            if (index != -1) {
                String nombre = cursor.getString(index);
                cursor.close();
                return nombre;
            }
            cursor.close();
        }
        return null; // Usuario no encontrado o error
    }

    public int obtenerIdUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_usuario FROM usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public String obtenerUsuarioPorId(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT usuario FROM usuarios WHERE id_usuario = ?",
                new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            cursor.close();
            return nombre;
        }
        cursor.close();
        return "Usuario";
    }


    /*
     ***CLUBES***
     */

    private void insertarClub(SQLiteDatabase db, String nombre, String ciudad, int logo, int imagen) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("ciudad", ciudad);
        values.put("logo", logo);
        values.put("imagen", imagen);
        db.insert("clubes", null, values);
    }

    public int obtenerIdClub(String nombreClub) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM clubes WHERE nombre = ?", new String[]{nombreClub});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }


    /*
     ***RESERVAS***
     */

    public boolean insertarReserva(int idUsuario, int idClub, int pista, String hora, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_club", idClub);
        values.put("pista", pista);
        values.put("hora", hora);
        values.put("fecha", fecha); // Añadido
        long resultado = db.insert("reservas", null, values);
        return resultado != -1;
    }

    public boolean estaReservada(int idClub, int pista, String hora, String fecha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reservas WHERE id_club = ? AND pista = ? AND hora = ? AND fecha = ?",
                new String[]{String.valueOf(idClub), String.valueOf(pista), hora, fecha});
        boolean reservada = cursor.getCount() > 0;
        cursor.close();
        return reservada;
    }

    public int obtenerIdUsuarioPorReserva(int idClub, int pista, String hora, String fecha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_usuario FROM reservas WHERE id_club = ? AND pista = ? AND hora = ? AND fecha = ?",
                new String[]{String.valueOf(idClub), String.valueOf(pista), hora, fecha});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public void eliminarReserva(String usuario, String club, String fecha, String hora, String pista) {
        SQLiteDatabase db = this.getWritableDatabase();
        int idUsuario = obtenerIdUsuario(usuario);
        int idClub = obtenerIdClub(club);
        String numPista = pista.replaceAll("\\D+", "");
        db.delete("reservas", "id_usuario = ? AND id_club = ? AND fecha = ? AND hora = ? AND pista = ?",
                new String[]{String.valueOf(idUsuario), String.valueOf(idClub), fecha, hora, numPista});
    }

    public List<Reserva> obtenerReservasUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query = "SELECT r.fecha, r.hora, r.pista, c.nombre, c.logo " +
                "FROM reservas r " +
                "JOIN clubes c ON r.id_club = c.id " +
                "WHERE r.id_usuario = ? AND r.fecha >= ? " +
                "ORDER BY r.fecha ASC, r.hora ASC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario), fechaHoy});

        if (cursor.moveToFirst()) {
            do {
                String fecha = cursor.getString(0);
                String hora = cursor.getString(1);
                String pista = "Pista " + cursor.getInt(2);
                String nombreClub = cursor.getString(3);
                String nombreLogo = cursor.getString(4);
                int logoResId = context.getResources().getIdentifier(nombreLogo, "drawable", context.getPackageName());

                Reserva reserva = new Reserva(logoResId, nombreClub, pista, fecha, hora);
                lista.add(reserva);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lista;
    }

     /*
     ***USUARIOS***
     */

    public List<Usuario> obtenerTodosLosUsuarios(int idUsuarioActual) {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM usuarios WHERE id_usuario != ? AND id_usuario != 1";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idUsuarioActual)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String ciudad = cursor.getString(cursor.getColumnIndexOrThrow("ciudad"));
                int nivel = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
                byte[] imagenBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"));

                Usuario u = new Usuario(id, usuario, nombre, ciudad, nivel, imagenBytes);
                listaUsuarios.add(u);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listaUsuarios;
    }

    public List<Usuario> FiltroUsuarios(String texto, int nivel, int idUsuarioActual) {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM usuarios WHERE id_usuario != ? AND id_usuario != 1";
        List<String> args = new ArrayList<>();
        args.add(String.valueOf(idUsuarioActual));

        if (!texto.isEmpty()) {
            sql += " AND (usuario LIKE ? OR nombre LIKE ?)";
            args.add(texto + "%");
            args.add(texto + "%");
        }

        if (nivel > 0) {
            sql += " AND nivel = ?";
            args.add(String.valueOf(nivel));
        }

        Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                String usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String ciudad = cursor.getString(cursor.getColumnIndexOrThrow("ciudad"));
                int nivelUser = cursor.getInt(cursor.getColumnIndexOrThrow("nivel"));
                byte[] imagenBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"));

                Usuario u = new Usuario(id, usuario, nombre, ciudad, nivelUser, imagenBytes);
                listaUsuarios.add(u);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaUsuarios;
    }



    public Usuario obtenerUsuario(String usuario) {
        String query = "SELECT id_usuario, usuario, nombre, ciudad, nivel, imagen FROM usuarios WHERE usuario = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{usuario});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id_usuario"));
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String ciudad = cursor.getString(cursor.getColumnIndex("ciudad"));
            int nivel = cursor.getInt(cursor.getColumnIndex("nivel"));
            byte[] imagen = cursor.getBlob(cursor.getColumnIndex("imagen"));
            cursor.close();

            // Se devuelve el objeto Usuario con todos los datos obtenidos
            return new Usuario(id, usuario, nombre, ciudad, nivel, imagen);
        }
        return null; // Si no se encuentra el usuario
    }

    public boolean actualizarPerfil(String usuario, String imagen, String nombre, String ciudad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("ciudad", ciudad);
        valores.put("imagen", imagen);  // Puede ser null si corresponde

        int filas = db.update("usuarios", valores, "usuario = ?", new String[]{usuario});
        return filas > 0;
    }

    public boolean eliminarUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasEliminadas = db.delete("usuarios", "usuario = ?", new String[]{usuario});
        return filasEliminadas > 0;
    }

    public int obtenerNivelUsuario(String nombreUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nivel = 1;

        Cursor cursor = db.rawQuery("SELECT nivel FROM usuarios WHERE usuario = ?", new String[]{nombreUsuario});
        if (cursor.moveToFirst()) {
            nivel = cursor.getInt(0);
        }
        cursor.close();
        return nivel;
    }

    public void actualizarNivelUsuario(String nombreUsuario, int nuevoNivel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nivel", nuevoNivel);
        db.update("usuarios", values, "usuario = ?", new String[]{nombreUsuario});
    }

    /*
     ***PARTIDOS***
     */

    public boolean insertarPartido(int idUsuario, String fecha, String pareja, String rival1, String rival2,
                                   Integer p1s1, Integer p2s1,
                                   Integer p1s2, Integer p2s2,
                                   Integer p1s3, Integer p2s3,
                                   Integer result) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id_usuario", idUsuario);
        values.put("fecha", fecha);
        values.put("pareja", pareja);
        values.put("rival1", rival1);
        values.put("rival2", rival2);
        values.put("resultado_p1s1", p1s1);
        values.put("resultado_p2s1", p2s1);
        values.put("resultado_p1s2", p1s2);
        values.put("resultado_p2s2", p2s2);
        values.put("resultado_p1s3", p1s3);
        values.put("resultado_p2s3", p2s3);
        values.put("resultado", result);

        long resultado = db.insert("partidos", null, values);
        return resultado != -1;
    }

    /*
     ***MENSAJES***
     */

    public boolean insertarMensaje(String remitente, String destinatario, String fecha, String asunto, String mensajeTexto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("remitente", remitente);
        values.put("destinatario", destinatario);
        values.put("fecha", fecha);
        values.put("asunto", asunto);
        values.put("mensaje", mensajeTexto);

        long resultado = db.insert("mensajes", null, values);
        return resultado != -1; // Devuelve true si se insertó correctamente
    }

    public List<Mensaje> obtenerMensajesUsuario(String usuario) {
        List<Mensaje> mensajes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mensajes WHERE destinatario = ? OR remitente = ? ORDER BY fecha DESC",
                new String[]{usuario, usuario});

        if (cursor.moveToFirst()) {
            do {
                Mensaje m = new Mensaje(
                        cursor.getString(1), // remitente
                        cursor.getString(2), //destinatario
                        cursor.getString(3), // fecha
                        cursor.getString(4), // asunto
                        cursor.getString(5)  // mensaje
                );
                mensajes.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mensajes;
    }

    public void eliminarMensaje(String remitente, String destinatario, String fecha, String asunto) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("mensajes",
                "remitente = ? AND destinatario = ? AND fecha = ? AND asunto = ?",
                new String[]{remitente, destinatario, fecha, asunto});

        db.close();
    }

}

