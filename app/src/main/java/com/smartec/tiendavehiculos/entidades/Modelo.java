package com.smartec.tiendavehiculos.entidades;

public class Modelo {
    private int marca;
    private int modelo;
    private String descripcionModelo;

    public Modelo() {
    }

    public Modelo(int marca, int modelo, String descripcionModelo) {
        this.marca = marca;
        this.modelo = modelo;
        this.descripcionModelo = descripcionModelo;
    }

    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public String getDescripcionModelo() {
        return descripcionModelo;
    }

    public void setDescripcionModelo(String descripcionModelo) {
        this.descripcionModelo = descripcionModelo;
    }

    @Override
    public String toString() {
        return descripcionModelo;
    }
}
