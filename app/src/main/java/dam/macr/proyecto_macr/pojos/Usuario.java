package dam.macr.proyecto_macr.pojos;

public class Usuario {
    private int id;
    private String usuario;
    private String nombre;
    private String ciudad;
    private int nivel;
    private byte[] imagen;

    public Usuario(int id, String usuario, String nombre, String ciudad, int nivel, byte[] imagen) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.nivel = nivel;
        this.imagen = imagen;
    }

    // Getters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    public int getNivel() { return nivel; }
    public byte[] getImagen() { return imagen; }
}

