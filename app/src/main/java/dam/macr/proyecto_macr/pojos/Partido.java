package dam.macr.proyecto_macr.pojos;

public class Partido {
    private String fecha;
    private String pareja;
    private String rival1;
    private String rival2;
    private int resultadoP1S1, resultadoP2S1;
    private int resultadoP1S2, resultadoP2S2;
    private int resultadoP1S3, resultadoP2S3;
    private int resultado;
    private String nombreUsuario;

    // Constructor, getters y setters
    public Partido(String fecha, String pareja, String rival1, String rival2,
                   int resultadoP1S1, int resultadoP2S1,
                   int resultadoP1S2, int resultadoP2S2,
                   int resultadoP1S3, int resultadoP2S3,
                   int resultado, String nombreUsuario) {
        this.fecha = fecha;
        this.pareja = pareja;
        this.rival1 = rival1;
        this.rival2 = rival2;
        this.resultadoP1S1 = resultadoP1S1;
        this.resultadoP2S1 = resultadoP2S1;
        this.resultadoP1S2 = resultadoP1S2;
        this.resultadoP2S2 = resultadoP2S2;
        this.resultadoP1S3 = resultadoP1S3;
        this.resultadoP2S3 = resultadoP2S3;
        this.resultado = resultado;
        this.nombreUsuario = nombreUsuario;
    }

    // Getters...

    public String getFecha() { return fecha; }
    public String getPareja() { return pareja; }
    public String getRival1() { return rival1; }
    public String getRival2() { return rival2; }
    public int getResultadoP1S1() { return resultadoP1S1; }
    public int getResultadoP2S1() { return resultadoP2S1; }
    public int getResultadoP1S2() { return resultadoP1S2; }
    public int getResultadoP2S2() { return resultadoP2S2; }
    public int getResultadoP1S3() { return resultadoP1S3; }
    public int getResultadoP2S3() { return resultadoP2S3; }
    public int getResultado() { return resultado; }
    public String getNombreUsuario() { return nombreUsuario; }
}
