package com.smartec.tiendavehiculos.entidades;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.text.DecimalFormat;

public class VehiculoCard {
    private int id;
    private String descripcionMarca;
    private String descripcionModelo;
    private String anio;
    private String color;
    private String cilindraje;
    private String precioVenta;
    private Bitmap imagen;
    private String rutaImagen;
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String celular;
    private String telefono;
    private String email;
    private String direccion;
    private String fotoPerfil;

    public VehiculoCard() {
    }

    public VehiculoCard(int id, String descripcionMarca, String descripcionModelo,
                        String anio, String color, String cilindraje, String precioVenta, Bitmap imagen,
                        String rutaImagen, int idUsuario, String nombres, String apellidos, String celular,
                        String telefono, String email, String direccion, String fotoPerfil) {
        this.id = id;
        this.descripcionMarca = descripcionMarca;
        this.descripcionModelo = descripcionModelo;
        this.anio = anio;
        this.color = color;
        this.cilindraje = cilindraje;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
        this.rutaImagen = rutaImagen;
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fotoPerfil = fotoPerfil;
    }

    public String formatearNumero(double numero) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(numero);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcionMarca() {
        return descripcionMarca;
    }

    public void setDescripcionMarca(String descripcionMarca) {
        this.descripcionMarca = descripcionMarca;
    }

    public String getPrecioVenta() {
        return "L."+ formatearNumero(Double.parseDouble(precioVenta));
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getDescripcionModelo() {
        return descripcionModelo;
    }

    public void setDescripcionModelo(String descripcionModelo) {
        this.descripcionModelo = descripcionModelo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCilindraje() {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(Double.parseDouble(cilindraje)) + " c.c.";
    }

    public void setCilindraje(String cilindraje) {
        this.cilindraje = cilindraje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
