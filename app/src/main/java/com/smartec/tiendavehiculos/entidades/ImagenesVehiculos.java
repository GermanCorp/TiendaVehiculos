package com.smartec.tiendavehiculos.entidades;

public class ImagenesVehiculos {
    int idImagen;
    int fkVehiculo;
    String imagen;

    public ImagenesVehiculos(int idImagen, int fkVehiculo, String imagen) {
        this.idImagen = idImagen;
        this.fkVehiculo = fkVehiculo;
        this.imagen = imagen;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public int getFkVehiculo() {
        return fkVehiculo;
    }

    public void setFkVehiculo(int fkVehiculo) {
        this.fkVehiculo = fkVehiculo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
