package edu.universidad.modelo;

import edu.universidad.util.PersistenciaJSON;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Gestor local de recursos, equipos y rutas
 * Guarda todo en archivos JSON en la carpeta "datos/"
 * VERSIÓN MEJORADA con todas las operaciones CRUD
 */
public class GestorRecursosLocal {

    private static GestorRecursosLocal instance;

    private List<Recurso> recursos;
    private List<EquipoRescate> equipos;
    private List<Zona> zonas;
    private List<Ruta> rutas;

    private AtomicLong contadorRecursos = new AtomicLong(1);
    private AtomicLong contadorEquipos = new AtomicLong(1);
    private AtomicLong contadorZonas = new AtomicLong(1);
    private AtomicLong contadorRutas = new AtomicLong(1);

    private GestorRecursosLocal() {
        cargarDatos();
    }

    public static synchronized GestorRecursosLocal getInstance() {
        if (instance == null) {
            instance = new GestorRecursosLocal();
        }
        return instance;
    }

    private void cargarDatos() {
        System.out.println("📂 Cargando datos desde archivos JSON...");

        // Cargar o crear datos por defecto
        recursos = PersistenciaJSON.cargarLista("recursos.json", Recurso.class);
        if (recursos.isEmpty()) {
            crearRecursosPorDefecto();
        }

        equipos = PersistenciaJSON.cargarLista("equipos.json", EquipoRescate.class);
        if (equipos.isEmpty()) {
            crearEquiposPorDefecto();
        }

        zonas = PersistenciaJSON.cargarLista("zonas.json", Zona.class);
        if (zonas.isEmpty()) {
            crearZonasPorDefecto();
        }

        rutas = PersistenciaJSON.cargarLista("rutas.json", Ruta.class);
        if (rutas.isEmpty()) {
            crearRutasPorDefecto();
        }

        actualizarContadores();
        System.out.println("✓ Datos cargados: " + zonas.size() + " zonas, " + rutas.size() + " rutas");
    }

    private void actualizarContadores() {
        if (!recursos.isEmpty()) {
            contadorRecursos.set(recursos.stream()
                    .mapToLong(r -> r.getId() != null ? r.getId() : 0)
                    .max().orElse(0) + 1);
        }
        if (!equipos.isEmpty()) {
            contadorEquipos.set(equipos.stream()
                    .mapToLong(e -> e.getId() != null ? e.getId() : 0)
                    .max().orElse(0) + 1);
        }
        if (!zonas.isEmpty()) {
            contadorZonas.set(zonas.stream()
                    .mapToLong(z -> z.getId() != null ? z.getId() : 0)
                    .max().orElse(0) + 1);
        }
        if (!rutas.isEmpty()) {
            contadorRutas.set(rutas.stream()
                    .mapToLong(r -> r.getId() != null ? r.getId() : 0)
                    .max().orElse(0) + 1);
        }
    }

    // =============== RECURSOS ===============

    public List<Recurso> obtenerRecursos() {
        return new ArrayList<>(recursos);
    }

    public Recurso agregarRecurso(String nombre, int cantidad) {
        Recurso nuevo = new Recurso(nombre, cantidad);
        nuevo.setId(contadorRecursos.getAndIncrement());
        recursos.add(nuevo);
        guardarRecursos();
        System.out.println("✓ Recurso agregado: " + nombre);
        return nuevo;
    }

    public boolean actualizarRecurso(Long id, String nuevoNombre, int nuevaCantidad) {
        for (Recurso r : recursos) {
            if (r.getId().equals(id)) {
                r.setNombre(nuevoNombre);
                r.setCantidadDisponible(nuevaCantidad);
                guardarRecursos();
                System.out.println("✓ Recurso actualizado: " + nuevoNombre);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarRecurso(Long id) {
        boolean eliminado = recursos.removeIf(r -> r.getId().equals(id));
        if (eliminado) {
            guardarRecursos();
            System.out.println("✓ Recurso eliminado: " + id);
        }
        return eliminado;
    }

    private void guardarRecursos() {
        PersistenciaJSON.guardar("recursos.json", recursos);
    }

    private void crearRecursosPorDefecto() {
        recursos = new ArrayList<>();
        recursos.add(new Recurso("Agua Potable", 500));
        recursos.add(new Recurso("Kits Médicos", 100));
        recursos.add(new Recurso("Mantas Térmicas", 300));
        recursos.add(new Recurso("Alimentos No Perecederos", 800));
        recursos.add(new Recurso("Medicamentos Básicos", 150));
        recursos.add(new Recurso("Tiendas de Campaña", 50));

        for (int i = 0; i < recursos.size(); i++) {
            recursos.get(i).setId((long) (i + 1));
        }
        guardarRecursos();
    }

    // =============== EQUIPOS ===============

    public List<EquipoRescate> obtenerEquipos() {
        return new ArrayList<>(equipos);
    }

    public EquipoRescate agregarEquipo(String nombre, String responsable, int miembros) {
        EquipoRescate nuevo = new EquipoRescate(nombre, responsable);
        nuevo.setId(contadorEquipos.getAndIncrement());
        equipos.add(nuevo);
        guardarEquipos();
        System.out.println("✓ Equipo agregado: " + nombre);
        return nuevo;
    }

    public boolean actualizarEquipo(Long id, String nuevoNombre, String nuevoResponsable) {
        for (EquipoRescate e : equipos) {
            if (e.getId().equals(id)) {
                e.setNombre(nuevoNombre);
                e.setResponsable(nuevoResponsable);
                guardarEquipos();
                System.out.println("✓ Equipo actualizado: " + nuevoNombre);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarEquipo(Long id) {
        boolean eliminado = equipos.removeIf(e -> e.getId().equals(id));
        if (eliminado) {
            guardarEquipos();
            System.out.println("✓ Equipo eliminado: " + id);
        }
        return eliminado;
    }

    private void guardarEquipos() {
        PersistenciaJSON.guardar("equipos.json", equipos);
    }

    private void crearEquiposPorDefecto() {
        equipos = new ArrayList<>();
        equipos.add(new EquipoRescate("Equipo Médico Alpha", "Dr. García"));
        equipos.add(new EquipoRescate("Equipo Bomberos Beta", "Cap. Rodríguez"));
        equipos.add(new EquipoRescate("Equipo Cruz Roja Gamma", "Enf. Martínez"));
        equipos.add(new EquipoRescate("Equipo Defensa Civil Delta", "Coord. López"));
        equipos.add(new EquipoRescate("Equipo Paramédicos Epsilon", "Dr. Sánchez"));

        for (int i = 0; i < equipos.size(); i++) {
            equipos.get(i).setId((long) (i + 1));
        }
        guardarEquipos();
    }

    // =============== ZONAS ===============

    public List<Zona> obtenerZonas() {
        return new ArrayList<>(zonas);
    }

    public Zona agregarZona(String nombre, String estado, int prioridad) {
        Zona nueva = new Zona(nombre, estado, prioridad);
        nueva.setId(contadorZonas.getAndIncrement());
        zonas.add(nueva);
        guardarZonas();
        System.out.println("✓ Zona agregada y guardada: " + nombre);
        return nueva;
    }

    public boolean actualizarZona(Long id, String nuevoNombre, String nuevoEstado, int nuevaPrioridad) {
        for (Zona z : zonas) {
            if (z.getId().equals(id)) {
                z.setNombre(nuevoNombre);
                z.setEstado(nuevoEstado);
                z.setPrioridad(nuevaPrioridad);
                guardarZonas();
                System.out.println("✓ Zona actualizada y guardada: " + nuevoNombre);
                return true;
            }
        }
        return false;
    }

    /**
     * NUEVO: Elimina una zona por ID
     */
    public boolean eliminarZona(Long id) {
        boolean eliminado = zonas.removeIf(z -> z.getId().equals(id));
        if (eliminado) {
            guardarZonas();
            System.out.println("✓ Zona eliminada y guardada: " + id);
        }
        return eliminado;
    }

    /**
     * NUEVO: Busca una zona por nombre
     */
    public Zona buscarZonaPorNombre(String nombre) {
        for (Zona z : zonas) {
            if (z.getNombre().equals(nombre)) {
                return z;
            }
        }
        return null;
    }

    private void guardarZonas() {
        PersistenciaJSON.guardar("zonas.json", zonas);
    }

    private void crearZonasPorDefecto() {
        zonas = new ArrayList<>();
        zonas.add(new Zona("Zona Norte", "afectada", 90));
        zonas.add(new Zona("Zona Centro", "evacuando", 70));
        zonas.add(new Zona("Zona Sur", "normal", 30));
        zonas.add(new Zona("Zona Este", "afectada", 85));

        for (int i = 0; i < zonas.size(); i++) {
            zonas.get(i).setId((long) (i + 1));
        }
        guardarZonas();
    }

    // =============== RUTAS ===============

    public List<Ruta> obtenerRutas() {
        return new ArrayList<>(rutas);
    }

    public Ruta agregarRuta(String origen, String destino, double peso) {
        Ruta nueva = new Ruta(origen, destino, peso);
        nueva.setId(contadorRutas.getAndIncrement());
        rutas.add(nueva);
        guardarRutas();
        System.out.println("✓ Ruta agregada y guardada: " + origen + " → " + destino);
        return nueva;
    }

    /**
     * Elimina una ruta por ID (String)
     */
    public boolean eliminarRuta(String rutaId) {
        try {
            Long id = Long.parseLong(rutaId);
            return eliminarRuta(id);
        } catch (NumberFormatException e) {
            System.err.println("❌ ID de ruta inválido: " + rutaId);
            return false;
        }
    }

    /**
     * Elimina una ruta por ID (Long)
     */
    public boolean eliminarRuta(Long id) {
        boolean eliminado = rutas.removeIf(r -> r.getId().equals(id));
        if (eliminado) {
            guardarRutas();
            System.out.println("✓ Ruta eliminada y guardada: " + id);
        }
        return eliminado;
    }

    /**
     * NUEVO: Obtiene rutas asociadas a una zona
     */
    public List<Ruta> obtenerRutasPorZona(String nombreZona) {
        List<Ruta> rutasAsociadas = new ArrayList<>();
        for (Ruta r : rutas) {
            if (r.getOrigenZona().equals(nombreZona) ||
                    r.getDestinoZona().equals(nombreZona)) {
                rutasAsociadas.add(r);
            }
        }
        return rutasAsociadas;
    }

    private void guardarRutas() {
        PersistenciaJSON.guardar("rutas.json", rutas);
    }

    private void crearRutasPorDefecto() {
        rutas = new ArrayList<>();
        rutas.add(new Ruta("Zona Norte", "Refugio Central", 12.5));
        rutas.add(new Ruta("Zona Centro", "Refugio Central", 8.2));
        rutas.add(new Ruta("Zona Sur", "Refugio Central", 15.7));
        rutas.add(new Ruta("Zona Este", "Refugio Central", 6.8));
        rutas.add(new Ruta("Zona Norte", "Zona Centro", 10.0));
        rutas.add(new Ruta("Zona Centro", "Zona Sur", 7.5));

        for (int i = 0; i < rutas.size(); i++) {
            rutas.get(i).setId((long) (i + 1));
        }
        guardarRutas();
    }

    // =============== UTILIDADES ===============

    /**
     * Recarga todos los datos desde los archivos JSON
     */
    public void recargarDatos() {
        cargarDatos();
        System.out.println("✓ Datos recargados desde archivos JSON");
    }

    /**
     * Exporta estadísticas del sistema
     */
    public String obtenerEstadisticas() {
        return String.format(
                "=== Sistema de Gestión de Desastres ===\n" +
                        "Zonas: %d\n" +
                        "Rutas: %d\n" +
                        "Equipos: %d\n" +
                        "Recursos: %d\n" +
                        "========================================",
                zonas.size(),
                rutas.size(),
                equipos.size(),
                recursos.size()
        );
    }

    /**
     * Limpia todos los datos (útil para testing)
     */
    public void limpiarTodosDatos() {
        recursos.clear();
        equipos.clear();
        zonas.clear();
        rutas.clear();

        guardarRecursos();
        guardarEquipos();
        guardarZonas();
        guardarRutas();

        System.out.println("✓ Todos los datos han sido limpiados");
    }

    /**
     * Guarda todos los datos manualmente (por si acaso)
     */
    public void guardarTodo() {
        guardarRecursos();
        guardarEquipos();
        guardarZonas();
        guardarRutas();
        System.out.println("✓ Todos los datos guardados");
    }
}