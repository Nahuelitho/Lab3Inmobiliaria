package com.softnahu.modelo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Inmueble implements Serializable {
        private int id;
        private boolean disponible;

        @SerializedName("direccion")
        private String direccion;

        @SerializedName("ambientes")
        private int ambientes;

        @SerializedName("tipo")
        private String tipo;

        @SerializedName("uso")
        private String uso;

        @SerializedName("precio")
        private int precio;

        @SerializedName("foto")
        private String foto;

        @SerializedName("id_Propietario")
        private int idPropietario;

        @SerializedName("contratos")
        private List<Contrato> contratos;

        @SerializedName("propietario")
        private Propietario propietario;



    public Inmueble(int id, String direccion, int ambientes, String tipo, String uso, int precio, boolean disponible, String foto, int idPropietario, Propietario propietario) {
        this.id = id;
        this.direccion = direccion;
        this.ambientes = ambientes;
        this.tipo = tipo;
        this.uso = uso;
        this.precio = precio;
        this.disponible = disponible;
        this.foto = foto;
        this.idPropietario = idPropietario;
        this.propietario = propietario;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "Id=" + id +
                ", direccion='" + direccion + '\'' +
                ", ambientes=" + ambientes +
                ", tipo='" + tipo + '\'' +
                ", uso='" + uso + '\'' +
                ", precio=" + precio +
                ", disponible=" + disponible +
                ", foto='" + foto + '\'' +
                ", idPropietario=" + idPropietario +
                ", propietario=" + propietario +
                '}';
    }

    public List<Contrato> getContratos() {
        return contratos;
    }
}
