package com.softnahu.modelo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Contrato implements Serializable {

    private int id;

    @SerializedName("precio")
    private double precio;

    @SerializedName("fechaInicio")
    private String fechaInicio;

    @SerializedName("fechaFin")
    private String fechaFin;

    @SerializedName("fechaTerminacion")
    private String fechaTerminacion;

    @SerializedName("inquilino")
    private Inquilino inquilino;

    @SerializedName("Id_Inquilino")
    private int idInquilino;

    @SerializedName("Id_Inmueble")
    private int idInmueble;

    @SerializedName("inmueble")
    private Inmueble inmueble;

    private List<Pago> pagos;
    // Constructor vacío
    public Contrato() {}



    // Constructor con parámetros
    public Contrato(int id, double precio, String fechaInicio, String fechaFin, String fechaTerminacion,
                    int idInquilino, Inquilino inquilino, int idInmueble, Inmueble inmueble) {
        this.id = id;
        this.precio = precio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaTerminacion = fechaTerminacion;
        this.idInquilino = idInquilino;
        this.inquilino = inquilino;
        this.idInmueble = idInmueble;
        this.inmueble = inmueble;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFechaInicio() {
        return fechaInicio;
        //return fechaInicio.substring(0, 10); // Extrae los primeros 10 caracteres (yyyy-MM-dd)
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
        //return fechaFin.substring(0, 10); // Extrae los primeros 10 caracteres (yyyy-MM-dd)
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(String fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
