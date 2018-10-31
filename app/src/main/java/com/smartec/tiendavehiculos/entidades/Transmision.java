package com.smartec.tiendavehiculos.entidades;

public class Transmision {
    private int idTransmision;
    private String descripcionTransmision;

    public Transmision() {
    }

    public Transmision(int idTransmision, String descripcionTransmision) {
        this.idTransmision = idTransmision;
        this.descripcionTransmision = descripcionTransmision;
    }

    public int getIdTransmision() {
        return idTransmision;
    }

    public void setIdTransmision(int idTransmision) {
        this.idTransmision = idTransmision;
    }

    public String getDescripcionTransmision() {
        return descripcionTransmision;
    }

    public void setDescripcionTransmision(String descripcionTransmision) {
        this.descripcionTransmision = descripcionTransmision;
    }

    @Override
    public String toString() {
        return descripcionTransmision;
    }
}
