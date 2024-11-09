package com.softnahu.modelo.model;

public class CrearInmuebleRespuesta {
    private String message;
    private String inmuebleId;

    // Constructor, getters y setters
    public CrearInmuebleRespuesta(String message, String inmuebleId) {
        this.message = message;
        this.inmuebleId = inmuebleId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInmuebleId() {
        return inmuebleId;
    }

    public void setInmuebleId(String inmuebleId) {
        this.inmuebleId = inmuebleId;
    }

}


