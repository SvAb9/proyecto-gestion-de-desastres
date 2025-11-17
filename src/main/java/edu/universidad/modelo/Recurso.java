package edu.universidad.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "recursos")
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int cantidadDisponible;
    private int cantidadUsada;

    // Constructores
    public Recurso() {}

    public Recurso(String nombre, int disponible) {
        this.nombre = nombre;
        this.cantidadDisponible = disponible;
        this.cantidadUsada = 0;
    }

    // ============================================
    // Getters y Setters básicos
    // ============================================

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

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(int cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    // ============================================
    // Métodos adicionales requeridos por otras clases
    // ============================================

    /**
     * Obtiene el total de recursos (disponibles + usados)
     * Usado por: GeneradorReportes.generarInformeRecursos()
     */
    public int getTotal() {
        return cantidadDisponible + cantidadUsada;
    }

    /**
     * Alias de getCantidadDisponible() para compatibilidad
     * Usado por: GeneradorReportes.generarInformeRecursos()
     */
    public int getDisponible() {
        return cantidadDisponible;
    }

    /**
     * Alias de getCantidadUsada() para compatibilidad
     * Usado por: GeneradorReportes.generarInformeRecursos()
     */
    public int getUsado() {
        return cantidadUsada;
    }

    /**
     * Verifica si hay suficiente cantidad disponible
     * @param cantidad Cantidad a verificar
     * @return true si hay suficiente, false en caso contrario
     */
    public boolean hayDisponible(int cantidad) {
        return cantidadDisponible >= cantidad;
    }

    /**
     * Usa una cantidad de recursos (reduce disponibles, aumenta usados)
     * @param cantidad Cantidad a usar
     */
    public void usar(int cantidad) {
        if (cantidad <= cantidadDisponible) {
            cantidadDisponible -= cantidad;
            cantidadUsada += cantidad;
        }
    }

    /**
     * Libera una cantidad de recursos usados (aumenta disponibles, reduce usados)
     * @param cantidad Cantidad a liberar
     */
    public void liberar(int cantidad) {
        if (cantidad <= cantidadUsada) {
            cantidadUsada -= cantidad;
            cantidadDisponible += cantidad;
        }
    }

    // ============================================
    // toString para debugging
    // ============================================

    @Override
    public String toString() {
        return "Recurso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", disponible=" + cantidadDisponible +
                ", usado=" + cantidadUsada +
                ", total=" + getTotal() +
                '}';
    }
}