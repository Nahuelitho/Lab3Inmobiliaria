package com.softnahu.modelo.model;

public class Propietario {
    private int id;
    String dni;
    String apellido;
    String nombre;
    String telefono;
    String mail;
    String password;
    String avatar;
    public Propietario(){

    }
    public Propietario(int id, String dni, String apellido, String nombre, String telefono, String mail, String password, String avatar) {
        this.id = id;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.mail = mail;
        this.password = password;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
