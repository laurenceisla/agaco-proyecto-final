package pe.edu.idat.proyectoagaco.model;

import java.time.LocalDate;
import java.util.List;

public class Venta {

    private int id;
    private String nombreCliente;
    private String tipoDocumentoIdentidad;
    private String nroDocumentoIdentidad;
    private String apePaterno;
    private String apeMaterno;
    private String nombres;
    private String telefono;
    private String producto;
    private String direccion;
    private String distrito;
    private String fechaVenta;
    private boolean solicitaEntrega;
    private boolean solicitaArmado;

    private List<Servicio> servicios;

    public Venta() {
    }

    public Venta(int id, String nombreCliente, String tipoDocumentoIdentidad, String nroDocumentoIdentidad, String apePaterno, String apeMaterno, String nombres, String telefono, String producto, String direccion, String distrito, String fechaVenta, boolean solicitaEntrega, boolean solicitaArmado, List<Servicio> servicios) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
        this.nroDocumentoIdentidad = nroDocumentoIdentidad;
        this.apePaterno = apePaterno;
        this.apeMaterno = apeMaterno;
        this.nombres = nombres;
        this.telefono = telefono;
        this.producto = producto;
        this.direccion = direccion;
        this.distrito = distrito;
        this.fechaVenta = fechaVenta;
        this.solicitaEntrega = solicitaEntrega;
        this.solicitaArmado = solicitaArmado;
        this.servicios = servicios;
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

    public String getTipoDocumentoIdentidad() {
        return tipoDocumentoIdentidad;
    }

    public void setTipoDocumentoIdentidad(String tipoDocumentoIdentidad) {
        this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
    }

    public String getNroDocumentoIdentidad() {
        return nroDocumentoIdentidad;
    }

    public void setNroDocumentoIdentidad(String nroDocumentoIdentidad) {
        this.nroDocumentoIdentidad = nroDocumentoIdentidad;
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

    public boolean isSolicitaEntrega() {
        return solicitaEntrega;
    }

    public void setSolicitaEntrega(boolean solicitaEntrega) {
        this.solicitaEntrega = solicitaEntrega;
    }

    public boolean isSolicitaArmado() {
        return solicitaArmado;
    }

    public void setSolicitaArmado(boolean solicitaArmado) {
        this.solicitaArmado = solicitaArmado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicio(List<Servicio> servicios) {
        this.servicios = servicios;
    }
}

