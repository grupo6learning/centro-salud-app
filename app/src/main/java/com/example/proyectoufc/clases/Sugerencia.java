package com.example.proyectoufc.clases;

import java.io.Serializable;

public class Sugerencia implements Serializable {

    private int id;
    private String paciente;
    private String sugerencia;

    public Sugerencia() {
    }

    public Sugerencia(int id, String paciente, String sugerencia) {
        this.id = id;
        this.paciente = paciente;
        this.sugerencia = sugerencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }
}
