package pe.edu.idat.proyectoagaco.model;

public class Servicio {

    private int id;
    private String tipoServicio;
    private Integer idEspecialista;
    private String nombreEspecialista;
    private String fecha;
    private String estado;

    public Servicio() {
    }

    public Servicio(int id, String tipoServicio, Integer idEspecialista, String nombreEspecialista, String fecha, String estado) {
        this.id = id;
        this.tipoServicio = tipoServicio;
        this.idEspecialista = idEspecialista;
        this.nombreEspecialista = nombreEspecialista;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public String getNombreEspecialista() {
        return nombreEspecialista;
    }

    public void setNombreEspecialista(String nombreEspecialista) {
        this.nombreEspecialista = nombreEspecialista;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
