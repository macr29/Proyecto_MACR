package dam.macr.proyecto_macr.pojos;

public class Reserva {
    private int logoResId; // ID del recurso de la imagen
    private String nombreClub;
    private String pista;
    private String fecha;
    private String hora;

    public Reserva(int logoResId, String nombreClub, String pista, String fecha, String hora) {
        this.logoResId = logoResId;
        this.nombreClub = nombreClub;
        this.pista = pista;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters
    public int getLogoResId() { return logoResId; }
    public String getNombreClub() { return nombreClub; }
    public String getPista() { return pista; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
}
