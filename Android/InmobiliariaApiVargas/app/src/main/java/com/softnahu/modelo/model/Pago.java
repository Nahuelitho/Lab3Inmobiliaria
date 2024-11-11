package com.softnahu.modelo.model;

import java.io.Serializable;



import java.util.Date;

public class Pago implements Serializable {
    private int id;
    private int numeroPago;
    private int idContrato;
    private String fechaPago;
    private Double importe;
    private String detalle;

    public Pago() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(int numeroPago) {
        this.numeroPago = numeroPago;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", numeroPago=" + numeroPago +
                ", idContrato=" + idContrato +
                ", fechaPago='" + fechaPago + '\'' +
                ", importe=" + importe +
                ", detalle='" + detalle + '\'' +
                '}';
    }
}

