package com.example.proyectoufc.clases;

import java.io.Serializable;
import java.util.Date;

public class Citas implements Serializable {

    private int id;
    private String paciente,correo,doctor, especialidad;
    private Date fecha;

    public Citas() {
    }

    public Citas(int id, String paciente, String correo, String doctor, String especialidad, Date fecha) {
        this.id = id;
        this.paciente = paciente;
        this.correo = correo;
        this.doctor = doctor;
        this.especialidad = especialidad;
        this.fecha = fecha;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}


