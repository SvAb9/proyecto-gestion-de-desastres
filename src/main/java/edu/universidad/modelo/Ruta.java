package edu.universidad.modelo;

/**
 * Clase Ruta compatible con persistencia JSON
 * (Sin anotaciones JPA para usar con Jackson)
 */
public class Ruta {

    private Long id;
    private String origenZona;
    private String destinoZona;
    private double peso; // Distancia en km

    // Constructor vacío (necesario para Jackson)
    public Ruta() {}

    // Constructor principal
    public Ruta(String origenZona, String destinoZona, double peso) {
        this.origenZona = origenZona;
        this.destinoZona = destinoZona;
        this.peso = peso;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigenZona() {
        return origenZona;
    }

    public void setOrigenZona(String origenZona) {
        this.origenZona = origenZona;
    }

    public String getDestinoZona() {
        return destinoZona;
    }

    public void setDestinoZona(String destinoZona) {
        this.destinoZona = destinoZona;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return String.format("Ruta[id=%d, %s → %s, %.1f km]",
                id, origenZona, destinoZona, peso);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return id != null && id.equals(ruta.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}