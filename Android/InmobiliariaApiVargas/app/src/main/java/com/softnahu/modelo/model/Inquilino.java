package com.softnahu.modelo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Inquilino implements Serializable {
    private int id;

    @SerializedName("dni")
    private int dni;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("telefono")
    private int telefono;

    @SerializedName("direccion")
    private String direccion;



    // Constructor vacío
    public Inquilino() {}

    // Constructor con parámetros
    public Inquilino(int id, int dni, String apellido, String nombre, String direccion, int telefono) {
        this.id = id;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;

    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Inquilino{" +
                "id=" + id +
                ", dni=" + dni +
                ", apellido='" + apellido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono=" + telefono +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
