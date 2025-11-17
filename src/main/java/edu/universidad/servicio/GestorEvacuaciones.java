package edu.universidad.servicio;

import edu.universidad.estructura.ColaPrioridad;
import edu.universidad.modelo.*;
import edu.universidad.util.PersistenciaJSON;
import java.util.*;

/**
 * Gestor de evacuaciones CON PERSISTENCIA FUNCIONAL
 */
public class GestorEvacuaciones {

    private static final String ARCHIVO_EVACUACIONES = "evacuaciones.json";

    private ColaPrioridad<Evacuacion> colaEvacuaciones;
    private List<Evacuacion> evacuacionesCompletadas;
    private List<Evacuacion> evacuacionesEnProceso;
    private Map<Long, Integer> zonasEvacuadas;

    /**
     * Clase Evacuacion - INCLUYE TODA LA INFO DE LA ZONA
     */
    public static class Evacuacion {
        private Long id;

        // Información de la zona (para mostrar sin necesitar el objeto)
        private String zonaNombre;
        private Long zonaId;
        private String zonaEstado;
        private int zonaPrioridad;

        // Datos de evacuación
        private int personasAEvacuar;
        private int personasEvacuadas;
        private String estado;
        private String equipoNombre;
        private Long equipoId;

        // Fechas
        private Date fechaCreacion;
        private Date fechaInicio;
        private Date fechaCompletado;
        private String rutaEvacuacion;

        // Constructor vacío para Jackson
        public Evacuacion() {}

        // Constructor desde Zona
        public Evacuacion(Zona zona, int personas) {
            this.id = System.currentTimeMillis() + (long)(Math.random() * 1000);
            this.zonaNombre = zona.getNombre();
            this.zonaId = zona.getId();
            this.zonaEstado = zona.getEstado();
            this.zonaPrioridad = zona.getPrioridad();
            this.personasAEvacuar = personas;
            this.personasEvacuadas = 0;
            this.estado = "pendiente";
            this.fechaCreacion = new Date();
        }

        // ===== GETTERS Y SETTERS =====

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getZonaNombre() { return zonaNombre; }
        public void setZonaNombre(String zonaNombre) { this.zonaNombre = zonaNombre; }

        public Long getZonaId() { return zonaId; }
        public void setZonaId(Long zonaId) { this.zonaId = zonaId; }

        public String getZonaEstado() { return zonaEstado; }
        public void setZonaEstado(String zonaEstado) { this.zonaEstado = zonaEstado; }

        public int getZonaPrioridad() { return zonaPrioridad; }
        public void setZonaPrioridad(int zonaPrioridad) { this.zonaPrioridad = zonaPrioridad; }

        public int getPersonasAEvacuar() { return personasAEvacuar; }
        public void setPersonasAEvacuar(int personasAEvacuar) { this.personasAEvacuar = personasAEvacuar; }

        public int getPersonasEvacuadas() { return personasEvacuadas; }
        public void setPersonasEvacuadas(int n) { this.personasEvacuadas = n; }

        public String getEstado() { return estado; }
        public void setEstado(String e) { this.estado = e; }

        public String getEquipoNombre() { return equipoNombre; }
        public void setEquipoNombre(String equipoNombre) { this.equipoNombre = equipoNombre; }

        public Long getEquipoId() { return equipoId; }
        public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }

        public Date getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

        public Date getFechaInicio() { return fechaInicio; }
        public void setFechaInicio(Date f) { this.fechaInicio = f; }

        public Date getFechaCompletado() { return fechaCompletado; }
        public void setFechaCompletado(Date f) { this.fechaCompletado = f; }

        public String getRutaEvacuacion() { return rutaEvacuacion; }
        public void setRutaEvacuacion(String r) { this.rutaEvacuacion = r; }

        // ===== MÉTODOS AUXILIARES =====

        public int getPrioridad() {
            return 100 - zonaPrioridad;
        }

        public double getProgreso() {
            if (personasAEvacuar == 0) return 0;
            return (personasEvacuadas * 100.0) / personasAEvacuar;
        }

        public boolean estaCompletada() {
            return personasEvacuadas >= personasAEvacuar;
        }

        @Override
        public String toString() {
            return String.format("%s - %d/%d personas (%.1f%%) - %s",
                    zonaNombre, personasEvacuadas, personasAEvacuar,
                    getProgreso(), estado);
        }
    }

    // ===== CONSTRUCTOR =====

    public GestorEvacuaciones() {
        this.colaEvacuaciones = new ColaPrioridad<>();
        this.evacuacionesCompletadas = new ArrayList<>();
        this.evacuacionesEnProceso = new ArrayList<>();
        this.zonasEvacuadas = new HashMap<>();

        cargarEvacuaciones();
    }

    // ===== PERSISTENCIA =====

    private void cargarEvacuaciones() {
        System.out.println("📂 Cargando evacuaciones desde JSON...");

        List<Evacuacion> todas = PersistenciaJSON.cargarLista(ARCHIVO_EVACUACIONES, Evacuacion.class);

        for (Evacuacion ev : todas) {
            switch (ev.getEstado()) {
                case "pendiente":
                    colaEvacuaciones.insertar(ev, ev.getPrioridad());
                    break;
                case "en_proceso":
                    evacuacionesEnProceso.add(ev);
                    break;
                case "completada":
                    evacuacionesCompletadas.add(ev);
                    break;
            }
        }

        System.out.println("✅ Evacuaciones cargadas: " + todas.size());
    }

    private void guardarEvacuaciones() {
        List<Evacuacion> todas = new ArrayList<>();
        todas.addAll(colaEvacuaciones.obtenerTodos());
        todas.addAll(evacuacionesEnProceso);
        todas.addAll(evacuacionesCompletadas);

        PersistenciaJSON.guardar(ARCHIVO_EVACUACIONES, todas);
    }

    // ===== OPERACIONES =====

    public Evacuacion programarEvacuacion(Zona zona, int personas) {
        Evacuacion evacuacion = new Evacuacion(zona, personas);
        colaEvacuaciones.insertar(evacuacion, evacuacion.getPrioridad());
        guardarEvacuaciones();
        System.out.println("✅ Evacuación programada: " + evacuacion);
        return evacuacion;
    }

    public Evacuacion iniciarSiguienteEvacuacion(EquipoRescate equipo) {
        if (colaEvacuaciones.estaVacia()) {
            System.out.println("ℹ️ No hay evacuaciones pendientes");
            return null;
        }

        Evacuacion evacuacion = colaEvacuaciones.extraer();
        evacuacion.setEstado("en_proceso");
        evacuacion.setFechaInicio(new Date());

        if (equipo != null) {
            evacuacion.setEquipoNombre(equipo.getNombre());
            evacuacion.setEquipoId(equipo.getId());
            equipo.setDisponible(false);
        }

        evacuacionesEnProceso.add(evacuacion);
        guardarEvacuaciones();

        System.out.println("🚀 Evacuación iniciada: " + evacuacion);
        return evacuacion;
    }

    public boolean actualizarProgreso(Long evacuacionId, int personasEvacuadas) {
        for (Evacuacion ev : evacuacionesEnProceso) {
            if (ev.getId().equals(evacuacionId)) {
                ev.setPersonasEvacuadas(personasEvacuadas);

                if (ev.estaCompletada()) {
                    completarEvacuacion(ev);
                }

                guardarEvacuaciones();
                return true;
            }
        }
        return false;
    }

    private void completarEvacuacion(Evacuacion evacuacion) {
        evacuacion.setEstado("completada");
        evacuacion.setFechaCompletado(new Date());
        evacuacionesEnProceso.remove(evacuacion);
        evacuacionesCompletadas.add(evacuacion);

        Long zonaId = evacuacion.getZonaId();
        zonasEvacuadas.put(zonaId,
                zonasEvacuadas.getOrDefault(zonaId, 0) + evacuacion.getPersonasEvacuadas());

        guardarEvacuaciones();
        System.out.println("✅ Evacuación completada: " + evacuacion);
    }

    // ===== CONSULTAS =====

    public Evacuacion verSiguienteEvacuacion() {
        if (colaEvacuaciones.estaVacia()) {
            return null;
        }
        return colaEvacuaciones.peek();
    }

    public List<Evacuacion> getEvacuacionesPendientes() {
        return colaEvacuaciones.obtenerTodos();
    }

    public List<Evacuacion> getEvacuacionesEnProceso() {
        return new ArrayList<>(evacuacionesEnProceso);
    }

    public List<Evacuacion> getEvacuacionesCompletadas() {
        return new ArrayList<>(evacuacionesCompletadas);
    }

    public List<Evacuacion> getTodasEvacuaciones() {
        List<Evacuacion> todas = new ArrayList<>();
        todas.addAll(colaEvacuaciones.obtenerTodos());
        todas.addAll(evacuacionesEnProceso);
        todas.addAll(evacuacionesCompletadas);
        return todas;
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        int totalPendientes = colaEvacuaciones.tamanio();
        int totalEnProceso = evacuacionesEnProceso.size();
        int totalCompletadas = evacuacionesCompletadas.size();

        int personasTotales = 0;
        int personasEvacuadas = 0;

        for (Evacuacion ev : getTodasEvacuaciones()) {
            personasTotales += ev.getPersonasAEvacuar();
            personasEvacuadas += ev.getPersonasEvacuadas();
        }

        double progresoGeneral = personasTotales > 0 ?
                (personasEvacuadas * 100.0) / personasTotales : 0;

        stats.put("pendientes", totalPendientes);
        stats.put("enProceso", totalEnProceso);
        stats.put("completadas", totalCompletadas);
        stats.put("personasTotales", personasTotales);
        stats.put("personasEvacuadas", personasEvacuadas);
        stats.put("progresoGeneral", progresoGeneral);

        return stats;
    }

    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("╔═══════════════════════════════════════╗\n");
        reporte.append("║     REPORTE DE EVACUACIONES           ║\n");
        reporte.append("╚═══════════════════════════════════════╝\n\n");

        Map<String, Object> stats = obtenerEstadisticas();

        reporte.append("📊 ESTADÍSTICAS GENERALES:\n");
        reporte.append(String.format("   • Pendientes: %d\n", stats.get("pendientes")));
        reporte.append(String.format("   • En Proceso: %d\n", stats.get("enProceso")));
        reporte.append(String.format("   • Completadas: %d\n", stats.get("completadas")));
        reporte.append(String.format("   • Personas Total: %d\n", stats.get("personasTotales")));
        reporte.append(String.format("   • Personas Evacuadas: %d\n", stats.get("personasEvacuadas")));
        reporte.append(String.format("   • Progreso: %.1f%%\n\n", stats.get("progresoGeneral")));

        return reporte.toString();
    }

    public void limpiarTodo() {
        colaEvacuaciones.limpiar();
        evacuacionesEnProceso.clear();
        evacuacionesCompletadas.clear();
        zonasEvacuadas.clear();
        guardarEvacuaciones();
        System.out.println("✅ Todas las evacuaciones han sido limpiadas");
    }
}