package edu.universidad.test;

import edu.universidad.estructura.*;
import edu.universidad.modelo.*;
import edu.universidad.servicio.GestorEvacuaciones;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Pruebas unitarias del sistema
 * Cubre las estructuras de datos propias y funcionalidades principales
 */
public class PruebasUnitarias {

    // ==============================
    // PRUEBAS: COLA DE PRIORIDAD
    // ==============================

    @Test
    @DisplayName("Test 1: Cola de Prioridad - Inserción y Extracción")
    public void testColaPrioridadInsercionExtraccion() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.insertar("Tarea C", 30);
        cola.insertar("Tarea A", 10);
        cola.insertar("Tarea B", 20);

        assertEquals("Tarea A", cola.extraer(), "Debe extraer elemento de mayor prioridad");
        assertEquals("Tarea B", cola.extraer());
        assertEquals("Tarea C", cola.extraer());
        assertTrue(cola.estaVacia(), "Cola debe estar vacía después de extraer todos");
    }

    @Test
    @DisplayName("Test 2: Cola de Prioridad - Peek sin extraer")
    public void testColaPrioridadPeek() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.insertar("Bajo", 50);
        cola.insertar("Alto", 10);
        cola.insertar("Medio", 30);

        assertEquals("Alto", cola.peek(), "Peek debe retornar el de mayor prioridad");
        assertEquals(3, cola.tamanio(), "Peek no debe reducir tamaño");
        assertEquals("Alto", cola.extraer());
    }

    @Test
    @DisplayName("Test 3: Cola de Prioridad - Orden con mismo dato")
    public void testColaPrioridadOrden() {
        ColaPrioridad<Integer> cola = new ColaPrioridad<>();

        for (int i = 10; i >= 1; i--) {
            cola.insertar(i, i);
        }

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, cola.extraer(), "Debe extraer en orden de prioridad");
        }
    }

    // ==============================
    // PRUEBAS: ÁRBOL DE DISTRIBUCIÓN
    // ==============================

    @Test
    @DisplayName("Test 4: Árbol de Distribución - Construcción básica")
    public void testArbolDistribucionConstruccion() {
        ArbolDistribucion<String> arbol = new ArbolDistribucion<>("Centro", "raiz");

        arbol.agregarNodo("raiz", "Zona Norte", "norte");
        arbol.agregarNodo("raiz", "Zona Sur", "sur");
        arbol.agregarNodo("norte", "Sector A", "norte_a");

        assertNotNull(arbol.buscarNodo("norte"), "Debe encontrar nodo agregado");
        assertNotNull(arbol.buscarNodo("norte_a"));
        assertEquals(2, arbol.altura(), "Altura debe ser 2");
    }

    @Test
    @DisplayName("Test 5: Árbol de Distribución - Distribución de recursos")
    public void testArbolDistribucionRecursos() {
        ArbolDistribucion<String> arbol = new ArbolDistribucion<>("Centro", "raiz");

        arbol.agregarNodo("raiz", "Zona A", "a");
        arbol.agregarNodo("raiz", "Zona B", "b");
        arbol.agregarNodo("raiz", "Zona C", "c");

        arbol.distribuirRecursos(300);

        // Verificar que se distribuyó
        assertTrue(arbol.getRaiz().getCantidadAsignada() == 300);
        assertTrue(arbol.calcularTotalHojas() <= 300, "Total en hojas no debe exceder");

        // Verificar que todas las hojas tienen recursos
        List<ArbolDistribucion.NodoArbol<String>> hojas = arbol.obtenerHojas();
        for (var hoja : hojas) {
            assertTrue(hoja.getCantidadAsignada() > 0, "Cada hoja debe tener recursos");
        }
    }

    // ==============================
    // PRUEBAS: GRAFO Y DIJKSTRA
    // ==============================

    @Test
    @DisplayName("Test 6: Grafo - Dijkstra encuentra camino más corto")
    public void testGrafoDijkstra() {
        Grafo grafo = new Grafo();
        grafo.agregarNodo(new Nodo("A"));
        grafo.agregarNodo(new Nodo("B"));
        grafo.agregarNodo(new Nodo("C"));
        grafo.agregarNodo(new Nodo("D"));

        grafo.agregarArista("A", "B", 4);
        grafo.agregarArista("A", "C", 2);
        grafo.agregarArista("B", "D", 1);
        grafo.agregarArista("C", "D", 5);

        List<Nodo> camino = grafo.dijkstra("A", "D");

        assertFalse(camino.isEmpty(), "Debe encontrar un camino");
        assertEquals("A", camino.get(0).getId(), "Debe comenzar en A");
        assertEquals("D", camino.get(camino.size() - 1).getId(), "Debe terminar en D");

        // El camino más corto es A -> B -> D (peso 5)
        // vs A -> C -> D (peso 7)
        assertEquals(3, camino.size(), "Camino óptimo debe tener 3 nodos");
    }

    @Test
    @DisplayName("Test 7: Grafo - Sin camino entre nodos desconectados")
    public void testGrafoSinCamino() {
        Grafo grafo = new Grafo();
        grafo.agregarNodo(new Nodo("A"));
        grafo.agregarNodo(new Nodo("B"));
        grafo.agregarNodo(new Nodo("C"));

        grafo.agregarArista("A", "B", 1);
        // C está desconectado

        List<Nodo> camino = grafo.dijkstra("A", "C");

        assertTrue(camino.isEmpty(), "No debe encontrar camino entre nodos desconectados");
    }

    // ==============================
    // PRUEBAS: GESTOR DE EVACUACIONES
    // ==============================

    @Test
    @DisplayName("Test 8: Gestor Evacuaciones - Priorización correcta")
    public void testGestorEvacuacionesPrioridad() {
        GestorEvacuaciones gestor = new GestorEvacuaciones();

        Zona zonaBaja = new Zona("Zona Baja", "normal", 30);
        zonaBaja.setId(1L);
        Zona zonaAlta = new Zona("Zona Alta", "crítica", 90);
        zonaAlta.setId(2L);
        Zona zonaMedia = new Zona("Zona Media", "afectada", 60);
        zonaMedia.setId(3L);

        gestor.programarEvacuacion(zonaBaja, 100);
        gestor.programarEvacuacion(zonaAlta, 200);
        gestor.programarEvacuacion(zonaMedia, 150);

        // Debe extraer primero la de mayor prioridad
        GestorEvacuaciones.Evacuacion primera = gestor.verSiguienteEvacuacion();
        assertNotNull(primera);
        assertEquals(90, primera.getZona().getPrioridad(),
                "Primera evacuación debe ser la de mayor prioridad");
    }

    @Test
    @DisplayName("Test 9: Gestor Evacuaciones - Actualización de progreso")
    public void testGestorEvacuacionesProgreso() {
        GestorEvacuaciones gestor = new GestorEvacuaciones();

        Zona zona = new Zona("Test", "afectada", 80);
        zona.setId(1L);
        GestorEvacuaciones.Evacuacion ev = gestor.programarEvacuacion(zona, 100);

        EquipoRescate equipo = new EquipoRescate("Equipo 1", "Responsable");
        equipo.setId(1L);

        gestor.iniciarSiguienteEvacuacion(equipo);
        gestor.actualizarProgreso(ev.getId(), 50);

        assertEquals(50.0, ev.getProgreso(), 0.01, "Progreso debe ser 50%");

        gestor.actualizarProgreso(ev.getId(), 100);
        assertTrue(ev.estaCompletada(), "Evacuación debe estar completada");
    }

    // ==============================
    // PRUEBAS: MODELOS
    // ==============================

    @Test
    @DisplayName("Test 10: Recurso - Uso y liberación")
    public void testRecursoUsoLiberacion() {
        Recurso recurso = new Recurso("Agua", 100);
        recurso.setId(1L);

        assertTrue(recurso.hayDisponible(50), "Debe haber 50 disponibles");
        recurso.usar(30);

        assertEquals(70, recurso.getDisponible(), "Deben quedar 70 disponibles");
        assertEquals(30, recurso.getUsado(), "Deben estar 30 en uso");

        recurso.liberar(10);

        assertEquals(80, recurso.getDisponible(), "Deben quedar 80 disponibles tras liberar");
        assertEquals(20, recurso.getUsado(), "Deben quedar 20 en uso");
    }

    // ==============================
    // EJECUCIÓN DE TODAS LAS PRUEBAS
    // ==============================

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   EJECUTANDO PRUEBAS UNITARIAS             ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        PruebasUnitarias pruebas = new PruebasUnitarias();
        int pasadas = 0;
        int fallidas = 0;

        try {
            System.out.print("Test 1: Cola Prioridad Inserción/Extracción... ");
            pruebas.testColaPrioridadInsercionExtraccion();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 2: Cola Prioridad Peek... ");
            pruebas.testColaPrioridadPeek();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 3: Cola Prioridad Orden... ");
            pruebas.testColaPrioridadOrden();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 4: Árbol Construcción... ");
            pruebas.testArbolDistribucionConstruccion();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 5: Árbol Distribución Recursos... ");
            pruebas.testArbolDistribucionRecursos();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 6: Grafo Dijkstra... ");
            pruebas.testGrafoDijkstra();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 7: Grafo Sin Camino... ");
            pruebas.testGrafoSinCamino();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 8: Gestor Evacuaciones Prioridad... ");
            pruebas.testGestorEvacuacionesPrioridad();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 9: Gestor Evacuaciones Progreso... ");
            pruebas.testGestorEvacuacionesProgreso();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        try {
            System.out.print("Test 10: Recurso Uso/Liberación... ");
            pruebas.testRecursoUsoLiberacion();
            System.out.println("✅ PASADO");
            pasadas++;
        } catch (Exception e) {
            System.out.println("❌ FALLIDO: " + e.getMessage());
            fallidas++;
        }

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println(String.format("║  RESULTADOS: %d PASADAS | %d FALLIDAS      ║", pasadas, fallidas));
        System.out.println("╚════════════════════════════════════════════╝");
    }
}