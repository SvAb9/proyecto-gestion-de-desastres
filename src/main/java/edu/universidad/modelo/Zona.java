package edu.universidad.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Zona compatible con persistencia JSON
 * (Sin anotaciones JPA para usar con Jackson)
 */
public class Zona {

    private Long id;
    private String nombre;
    private String estado; // "normal", "afectada", "evacuando", "evacuada"
    private int prioridad; // mayor = más necesidad (0-100)
    private List<EquipoRescate> equiposAsignados = new ArrayList<>();

    // Constructor vacío (necesario para Jackson)
    public Zona() {}

    // Constructor principal
    public Zona(String nombre, String estado, int prioridad) {
        this.nombre = nombre;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public List<EquipoRescate> getEquiposAsignados() {
        return equiposAsignados;
    }

    public void setEquiposAsignados(List<EquipoRescate> equiposAsignados) {
        this.equiposAsignados = equiposAsignados;
    }

    // Método para compatibilidad con código existente
    public int getNivelNecesidad() {
        return prioridad;
    }

    public void asignarEquipo(EquipoRescate equipo) {
        if (!equiposAsignados.contains(equipo)) {
            equiposAsignados.add(equipo);
        }
    }

    public void removerEquipo(EquipoRescate equipo) {
        equiposAsignados.remove(equipo);
    }

    @Override
    public String toString() {
        return String.format("Zona[id=%d, nombre='%s', estado='%s', prioridad=%d]",
                id, nombre, estado, prioridad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zona zona = (Zona) o;
        return id != null && id.equals(zona.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}