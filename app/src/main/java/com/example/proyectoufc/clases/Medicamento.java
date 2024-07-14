package com.example.proyectoufc.clases;

import java.io.Serializable;

public class Medicamento implements Serializable {
    private int id;
    private String nombre_medicamento;
    private String forma_farmaceutica;
    private String concentracion;
    private double precio;

    public Medicamento() {
    }

    public Medicamento(int id, String nombre_medicamento, String forma_farmaceutica, String concentracion, double precio) {
        this.id = id;
        this.nombre_medicamento = nombre_medicamento;
        this.forma_farmaceutica = forma_farmaceutica;
        this.concentracion = concentracion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_medicamento() {
        return nombre_medicamento;
    }

    public void setNombre_medicamento(String nombre_medicamento) {
        this.nombre_medicamento = nombre_medicamento;
    }

    public String getForma_farmaceutica() {
        return forma_farmaceutica;
    }

    public void setForma_farmaceutica(String forma_farmaceutica) {
        this.forma_farmaceutica = forma_farmaceutica;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
