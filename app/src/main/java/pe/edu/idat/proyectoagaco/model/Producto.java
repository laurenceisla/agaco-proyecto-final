package pe.edu.idat.proyectoagaco.model;

public class Producto {

    private Integer id;
    private String nombre;

    public Producto(Integer id, String nombre) {
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
