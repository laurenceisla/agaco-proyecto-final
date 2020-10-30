package pe.edu.idat.proyectoagaco.model;

import java.time.LocalDate;

public class Venta {

    private int id;
    private String nombreCliente;
    private String apePaterno;
    private String apeMaterno;
    private String nombres;
    private String telefono;
    private String producto;
    private String direccion;
    private String distrito;
    private String fechaVenta;

    private Servicio servicio;

    public Venta() {
    }

    public Venta(int id, String nombreCliente, String apePaterno, String apeMaterno, String nombres, String telefono, String producto, String direccion, String distrito, String fechaVenta, Servicio servicio) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.apePaterno = apePaterno;
        this.apeMaterno = apeMaterno;
        this.nombres = nombres;
        this.telefono = telefono;
        this.producto = producto;
        this.direccion = direccion;
        this.distrito = distrito;
        this.fechaVenta = fechaVenta;
        this.servicio = servicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApePaterno() {
        return apePaterno;
    }

    public void setApePaterno(String apePaterno) {
        this.apePaterno = apePaterno;
    }

    public String getApeMaterno() {
        return apeMaterno;
    }

    public void setApeMaterno(String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
}

