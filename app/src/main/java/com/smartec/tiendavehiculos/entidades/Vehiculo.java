package com.smartec.tiendavehiculos.entidades;

public class Vehiculo {
    private Integer id;
    private String marca;
    private String modelo;
    private String color;
    private int anio;
    private int cilindraje;
    private String transmision;
    private String combustible;
    private String tipo;
    private Double precioVenta;
    private String imagen;

    public Vehiculo(Integer id) {
    }

    public Vehiculo(Integer id, String marca, String modelo, String color, int anio, int cilindraje, String transmision, String combustible, String tipo, Double precioVenta, String imagen) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.anio = anio;
        this.cilindraje = cilindraje;
        this.transmision = transmision;
        this.combustible = combustible;
        this.tipo = tipo;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
