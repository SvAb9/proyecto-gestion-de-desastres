package edu.universidad.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad Usuario con anotaciones JPA para persistencia
 * Compatible con JSON (Jackson) y JPA (Hibernate)
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Enumeración de roles del sistema
     */

    public enum Rol {
        ADMINISTRADOR("Administrador", "Acceso completo al sistema"),
        COORDINADOR("Coordinador", "Gestiona operaciones y recursos"),
        OPERADOR("Operador de Emergencia", "Monitorea zonas y coordina evacuaciones"),
        VISUALIZADOR("Visualizador", "Solo lectura, sin edición");

        private final String nombre;
        private final String descripcion;

        Rol(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
    }

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false)
    private boolean activo;

    /**
     * Constructor por defecto requerido por JPA y Jackson
     */
    public Usuario() {
        this.id = UUID.randomUUID().toString();
        this.activo = true;
    }

    /**
     * Constructor con parámetros
     */
    public Usuario(String nombre, String email, String password, Rol rol) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // ============================================
    // GETTERS Y SETTERS
    // ============================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // ============================================
    // MÉTODOS DE PERMISOS
    // ============================================

    /**
     * Verifica si el usuario es administrador
     */
    public boolean isAdministrador() {
        return rol == Rol.ADMINISTRADOR;
    }

    /**
     * Verifica si el usuario puede editar datos
     */
    public boolean puedeEditar() {
        return rol == Rol.ADMINISTRADOR || rol == Rol.COORDINADOR;
    }

    /**
     * Verifica si el usuario puede coordinar operaciones
     */
    public boolean puedeCoordinarOperaciones() {
        return rol == Rol.ADMINISTRADOR || rol == Rol.COORDINADOR || rol == Rol.OPERADOR;
    }

    /**
     * Verifica si el usuario solo tiene permisos de lectura
     */
    public boolean soloLectura() {
        return rol == Rol.VISUALIZADOR;
    }

    // ============================================
    // MÉTODOS DE OBJECT
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre + " (" + rol.getNombre() + ")";
    }

    /**
     * Método para debugging con información completa
     */
    public String toDetailedString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol.getNombre() +
                ", activo=" + activo +
                '}';
    }
}