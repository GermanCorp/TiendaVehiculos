package com.smartec.tiendavehiculos.entidades;

public class Tipo {

    private int idTipo;
    private String descripcionTipo;

    public Tipo(){
    }

    public Tipo(int idTipo, String descripcionTipo) {
        this.idTipo = idTipo;
        this.descripcionTipo = descripcionTipo;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }

    @Override
    public String toString() {
        return descripcionTipo;
    }
}
