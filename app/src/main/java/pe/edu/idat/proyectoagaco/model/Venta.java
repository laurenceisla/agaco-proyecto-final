package pe.edu.idat.proyectoagaco.model;

import java.time.LocalDate;

public class Venta {

    private int id;
    private String nombreCliente;
    private String producto;
    private String direccion;
    private String fechaVenta;

    public Venta(int id, String nombreCliente, String producto, String direccion, String fechaVenta) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.producto = producto;
        this.direccion = direccion;
        this.fechaVenta = fechaVenta;
    }

    public int getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getProducto() {
        return producto;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }
}

