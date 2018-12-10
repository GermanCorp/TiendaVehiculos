package com.smartec.tiendavehiculos.entidades;

public class Usuario {
    private static Integer id;
    private static String nombres;
    private static String apellidos;
    private static String direccion;
    private static String nombreUsuario;
    private static String celular;
    private static String telefono;
    private static String contrasenia;
    private static String email;
    private static String fotoPerfil;

    public static Integer getId() {
        return id;
    }

    public static void setId(Integer id) {
        Usuario.id = id;
    }

    public static String getNombres() {
        return nombres;
    }

    public static void setNombres(String nombres) {
        Usuario.nombres = nombres;
    }

    public static String getApellidos() {
        return apellidos;
    }

    public static void setApellidos(String apellidos) {
        Usuario.apellidos = apellidos;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        Usuario.direccion = direccion;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        Usuario.nombreUsuario = nombreUsuario;
    }

    public static String getCelular() {
        return celular;
    }

    public static void setCelular(String celular) {
        Usuario.celular = celular;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static void setTelefono(String telefono) {
        Usuario.telefono = telefono;
    }

    public static String getContrasenia() {
        return contrasenia;
    }

    public static void setContrasenia(String contrasenia) {
        Usuario.contrasenia = contrasenia;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Usuario.email = email;
    }

    public static String getFotoPerfil() {
        return fotoPerfil;
    }

    public static void setFotoPerfil(String fotoPerfil) {
        Usuario.fotoPerfil = fotoPerfil;
    }
}