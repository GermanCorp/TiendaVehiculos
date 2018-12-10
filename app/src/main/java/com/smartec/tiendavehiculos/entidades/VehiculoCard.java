package com.smartec.tiendavehiculos.entidades;

import android.graphics.Bitmap;

import java.text.DecimalFormat;

public class VehiculoCard {
    private int id;
    private String descripcionMarca;
    private String precioVenta;
    private Bitmap imagen;
    private String rutaImagen;

    public VehiculoCard() {
    }

    public VehiculoCard(String descripcionMarca, String precioVenta, String rutaImagen) {
        this.descripcionMarca = descripcionMarca;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
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
}
