package dam.macr.proyecto_macr.pojos;

public class Club {
    private String nombre;
    private String ciudad;
    private int logoRecursoId;
    private int imagenRecursoId;

    public Club(String nombre, String ciudad, int logoRecursoId, int imagenRecursoId) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.logoRecursoId = logoRecursoId;
        this.imagenRecursoId = imagenRecursoId;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public int getLogoRecursoId() {
        return logoRecursoId;
    }

    public int getImagenRecursoId() {
        return imagenRecursoId;
    }
}
