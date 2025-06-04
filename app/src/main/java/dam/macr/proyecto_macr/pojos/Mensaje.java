package dam.macr.proyecto_macr.pojos;

public class Mensaje {
    private String remitente;
    private String destinatario;
    private String fecha;
    private String asunto;
    private String mensaje;

    public Mensaje(String remitente, String destinatario, String fecha, String asunto, String mensaje) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.fecha = fecha;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String getRemitente() { return remitente; }
    public String getDestinatario() { return destinatario; }
    public String getFecha() { return fecha; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }
}

