// Java
package edu.universidad.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos_rescate")
public class EquipoRescate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String responsable;
    private boolean disponible;

    public EquipoRescate() {}
    public EquipoRescate(String nombre, String responsable){
        this.nombre = nombre; this.responsable = responsable; this.disponible = true;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId(){return id;}
    public String getNombre(){return nombre;}
    public void setNombre(String n){this.nombre = n;}
    public String getResponsable(){return responsable;}
    public void setResponsable(String r){this.responsable = r;}
    public boolean isDisponible(){return disponible;}
    public void setDisponible(boolean d){this.disponible = d;}
}
