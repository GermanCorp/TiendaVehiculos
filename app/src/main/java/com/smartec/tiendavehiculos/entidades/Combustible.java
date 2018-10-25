package com.smartec.tiendavehiculos.entidades;

public class Combustible {
    private int idCombustible;
    private String descripcionCombustible;

    public Combustible() {
    }

    public Combustible(int idCombustible, String descripcionCombustible) {
        this.idCombustible = idCombustible;
        this.descripcionCombustible = descripcionCombustible;
    }

    public int getIdCombustible() {
        return idCombustible;
    }

    public void setIdCombustible(int idCombustible) {
        this.idCombustible = idCombustible;
    }

    public String getDescripcionCombustible() {
        return descripcionCombustible;
    }

    public void setDescripcionCombustible(String descripcionCombustible) {
        this.descripcionCombustible = descripcionCombustible;
    }

    @Override
    public String toString() {
        return descripcionCombustible;
    }
}
