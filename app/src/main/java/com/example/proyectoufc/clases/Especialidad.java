package com.example.proyectoufc.clases;

import java.io.Serializable;

public class Especialidad implements Serializable {
    private int id;
    private String especialidad;

    public Especialidad() {
    }

    public Especialidad(int id, String especialidad) {
        this.id = id;
        this.especialidad = especialidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
