package com.smartec.tiendavehiculos.entidades;

public class Marca {
    private Integer id;
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public Marca() {
    }

    public Marca(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
