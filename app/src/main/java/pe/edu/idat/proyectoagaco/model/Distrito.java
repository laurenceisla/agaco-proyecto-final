package pe.edu.idat.proyectoagaco.model;

public class Distrito {

    private Integer id;
    private String nombre;

    public Distrito(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
