package edu.universidad.estructura;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol de distribución para organizar la asignación de recursos
 * Estructura jerárquica: Raíz (Centro de Distribución) -> Zonas -> Subzonas
 *
 * Ejemplo de uso:
 * Centro Principal
 *   ├── Zona Norte (300 recursos)
 *   │   ├── Sector A (150 recursos)
 *   │   └── Sector B (150 recursos)
 *   └── Zona Sur (200 recursos)
 *       └── Sector C (200 recursos)
 */
public class ArbolDistribucion<T> {

    private NodoArbol<T> raiz;

    /**
     * Clase interna: Nodo del árbol
     */
    public static class NodoArbol<T> {
        private T dato;
        private String id;
        private int cantidadAsignada;
        private NodoArbol<T> padre;
        private List<NodoArbol<T>> hijos;

        public NodoArbol(T dato, String id) {
            this.dato = dato;
            this.id = id;
            this.cantidadAsignada = 0;
            this.hijos = new ArrayList<>();
            this.padre = null;
        }

        public void agregarHijo(NodoArbol<T> hijo) {
            hijo.padre = this;
            this.hijos.add(hijo);
        }

        public void eliminarHijo(NodoArbol<T> hijo) {
            hijo.padre = null;
            this.hijos.remove(hijo);
        }

        // Getters y Setters
        public T getDato() { return dato; }
        public void setDato(T dato) { this.dato = dato; }
        public String getId() { return id; }
        public int getCantidadAsignada() { return cantidadAsignada; }
        public void setCantidadAsignada(int cantidad) { this.cantidadAsignada = cantidad; }
        public void incrementarAsignacion(int cantidad) { this.cantidadAsignada += cantidad; }
        public NodoArbol<T> getPadre() { return padre; }
        public List<NodoArbol<T>> getHijos() { return hijos; }
        public boolean esHoja() { return hijos.isEmpty(); }
        public boolean esRaiz() { return padre == null; }

        @Override
        public String toString() {
            return id + " (" + cantidadAsignada + " unidades)";
        }
    }

    /**
     * Constructor: Crea el árbol con un nodo raíz
     */
    public ArbolDistribucion(T raizDato, String raizId) {
        this.raiz = new NodoArbol<>(raizDato, raizId);
    }

    /**
     * Obtiene el nodo raíz
     */
    public NodoArbol<T> getRaiz() {
        return raiz;
    }

    /**
     * Busca un nodo por su ID
     * @param id Identificador del nodo
     * @return Nodo encontrado o null
     */
    public NodoArbol<T> buscarNodo(String id) {
        return buscarNodoRecursivo(raiz, id);
    }

    private NodoArbol<T> buscarNodoRecursivo(NodoArbol<T> nodo, String id) {
        if (nodo == null) return null;
        if (nodo.getId().equals(id)) return nodo;

        for (NodoArbol<T> hijo : nodo.getHijos()) {
            NodoArbol<T> resultado = buscarNodoRecursivo(hijo, id);
            if (resultado != null) return resultado;
        }

        return null;
    }

    /**
     * Agrega un hijo a un nodo específico
     * @param idPadre ID del nodo padre
     * @param dato Dato del nuevo nodo
     * @param idHijo ID del nuevo nodo
     * @return true si se agregó exitosamente
     */
    public boolean agregarNodo(String idPadre, T dato, String idHijo) {
        NodoArbol<T> padre = buscarNodo(idPadre);
        if (padre == null) return false;

        NodoArbol<T> nuevoNodo = new NodoArbol<>(dato, idHijo);
        padre.agregarHijo(nuevoNodo);
        return true;
    }

    /**
     * Distribuye recursos desde la raíz hacia las hojas
     * Algoritmo: distribuye proporcionalmente según número de hijos
     * @param cantidadTotal Cantidad total a distribuir
     */
    public void distribuirRecursos(int cantidadTotal) {
        if (raiz == null || raiz.getHijos().isEmpty()) return;

        // Limpiar asignaciones previas
        limpiarAsignaciones(raiz);

        // Distribuir desde la raíz
        raiz.setCantidadAsignada(cantidadTotal);
        distribuirRecursivo(raiz);
    }

    private void distribuirRecursivo(NodoArbol<T> nodo) {
        if (nodo.esHoja()) return;

        int cantidadDisponible = nodo.getCantidadAsignada();
        List<NodoArbol<T>> hijos = nodo.getHijos();
        int numHijos = hijos.size();

        if (numHijos == 0) return;

        // Distribución equitativa
        int porHijo = cantidadDisponible / numHijos;
        int resto = cantidadDisponible % numHijos;

        for (int i = 0; i < hijos.size(); i++) {
            NodoArbol<T> hijo = hijos.get(i);
            int asignacion = porHijo + (i < resto ? 1 : 0);
            hijo.setCantidadAsignada(asignacion);
            distribuirRecursivo(hijo);
        }
    }

    /**
     * Distribuye recursos con prioridades personalizadas
     * @param cantidadTotal Cantidad total
     * @param prioridades Mapa de ID -> prioridad (mayor = más recursos)
     */
    public void distribuirConPrioridades(int cantidadTotal, java.util.Map<String, Integer> prioridades) {
        limpiarAsignaciones(raiz);
        raiz.setCantidadAsignada(cantidadTotal);
        distribuirConPrioridadesRecursivo(raiz, prioridades);
    }

    private void distribuirConPrioridadesRecursivo(NodoArbol<T> nodo, java.util.Map<String, Integer> prioridades) {
        if (nodo.esHoja()) return;

        List<NodoArbol<T>> hijos = nodo.getHijos();
        if (hijos.isEmpty()) return;

        // Calcular suma de prioridades
        int sumaPrioridades = 0;
        for (NodoArbol<T> hijo : hijos) {
            sumaPrioridades += prioridades.getOrDefault(hijo.getId(), 1);
        }

        // ORDENAR HIJOS POR PRIORIDAD DESCENDENTE (MAYOR A MENOR)
        List<NodoArbol<T>> hijosOrdenados = new ArrayList<>(hijos);
        hijosOrdenados.sort((a, b) -> {
            int prioA = prioridades.getOrDefault(a.getId(), 1);
            int prioB = prioridades.getOrDefault(b.getId(), 1);
            return Integer.compare(prioB, prioA); // Descendente: mayor prioridad primero
        });

        // Distribuir proporcionalmente
        int cantidadDisponible = nodo.getCantidadAsignada();
        int asignado = 0;

        for (int i = 0; i < hijosOrdenados.size(); i++) {
            NodoArbol<T> hijo = hijosOrdenados.get(i);
            int prioridad = prioridades.getOrDefault(hijo.getId(), 1);

            int asignacion;
            if (i == hijosOrdenados.size() - 1) {
                // Último hijo recibe el resto (evita pérdida por redondeo)
                asignacion = cantidadDisponible - asignado;
            } else {
                // Distribución proporcional con redondeo
                asignacion = (int) Math.round((double) cantidadDisponible * prioridad / sumaPrioridades);
                asignado += asignacion;
            }

            hijo.setCantidadAsignada(asignacion);
            distribuirConPrioridadesRecursivo(hijo, prioridades);
        }
    }

    private void limpiarAsignaciones(NodoArbol<T> nodo) {
        if (nodo == null) return;
        nodo.setCantidadAsignada(0);
        for (NodoArbol<T> hijo : nodo.getHijos()) {
            limpiarAsignaciones(hijo);
        }
    }

    /**
     * Calcula el total de recursos asignados en las hojas
     */
    public int calcularTotalHojas() {
        return calcularTotalHojasRecursivo(raiz);
    }

    private int calcularTotalHojasRecursivo(NodoArbol<T> nodo) {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return nodo.getCantidadAsignada();

        int total = 0;
        for (NodoArbol<T> hijo : nodo.getHijos()) {
            total += calcularTotalHojasRecursivo(hijo);
        }
        return total;
    }

    /**
     * Retorna todas las hojas (nodos finales de distribución)
     */
    public List<NodoArbol<T>> obtenerHojas() {
        List<NodoArbol<T>> hojas = new ArrayList<>();
        obtenerHojasRecursivo(raiz, hojas);
        return hojas;
    }

    private void obtenerHojasRecursivo(NodoArbol<T> nodo, List<NodoArbol<T>> hojas) {
        if (nodo == null) return;

        if (nodo.esHoja()) {
            hojas.add(nodo);
        } else {
            for (NodoArbol<T> hijo : nodo.getHijos()) {
                obtenerHojasRecursivo(hijo, hojas);
            }
        }
    }

    /**
     * Retorna la altura del árbol
     */
    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(NodoArbol<T> nodo) {
        if (nodo == null || nodo.esHoja()) return 0;

        int maxAltura = 0;
        for (NodoArbol<T> hijo : nodo.getHijos()) {
            maxAltura = Math.max(maxAltura, alturaRecursiva(hijo));
        }

        return 1 + maxAltura;
    }

    /**
     * Cuenta el número total de nodos
     */
    public int contarNodos() {
        return contarNodosRecursivo(raiz);
    }

    private int contarNodosRecursivo(NodoArbol<T> nodo) {
        if (nodo == null) return 0;

        int count = 1;
        for (NodoArbol<T> hijo : nodo.getHijos()) {
            count += contarNodosRecursivo(hijo);
        }
        return count;
    }

    /**
     * Recorrido en preorden
     */
    public List<NodoArbol<T>> recorridoPreorden() {
        List<NodoArbol<T>> resultado = new ArrayList<>();
        preordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void preordenRecursivo(NodoArbol<T> nodo, List<NodoArbol<T>> resultado) {
        if (nodo == null) return;

        resultado.add(nodo);
        for (NodoArbol<T> hijo : nodo.getHijos()) {
            preordenRecursivo(hijo, resultado);
        }
    }

    /**
     * Recorrido en postorden
     */
    public List<NodoArbol<T>> recorridoPostorden() {
        List<NodoArbol<T>> resultado = new ArrayList<>();
        postordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void postordenRecursivo(NodoArbol<T> nodo, List<NodoArbol<T>> resultado) {
        if (nodo == null) return;

        for (NodoArbol<T> hijo : nodo.getHijos()) {
            postordenRecursivo(hijo, resultado);
        }
        resultado.add(nodo);
    }

    /**
     * Recorrido por niveles (BFS)
     */
    public List<List<NodoArbol<T>>> recorridoPorNiveles() {
        List<List<NodoArbol<T>>> niveles = new ArrayList<>();
        if (raiz == null) return niveles;

        List<NodoArbol<T>> nivelActual = new ArrayList<>();
        nivelActual.add(raiz);

        while (!nivelActual.isEmpty()) {
            niveles.add(new ArrayList<>(nivelActual));
            List<NodoArbol<T>> siguienteNivel = new ArrayList<>();

            for (NodoArbol<T> nodo : nivelActual) {
                siguienteNivel.addAll(nodo.getHijos());
            }

            nivelActual = siguienteNivel;
        }

        return niveles;
    }

    /**
     * Encuentra el camino desde la raíz hasta un nodo
     */
    public List<NodoArbol<T>> encontrarCamino(String idDestino) {
        List<NodoArbol<T>> camino = new ArrayList<>();
        if (encontrarCaminoRecursivo(raiz, idDestino, camino)) {
            return camino;
        }
        return new ArrayList<>();
    }

    private boolean encontrarCaminoRecursivo(NodoArbol<T> nodo, String idDestino, List<NodoArbol<T>> camino) {
        if (nodo == null) return false;

        camino.add(nodo);

        if (nodo.getId().equals(idDestino)) {
            return true;
        }

        for (NodoArbol<T> hijo : nodo.getHijos()) {
            if (encontrarCaminoRecursivo(hijo, idDestino, camino)) {
                return true;
            }
        }

        camino.remove(camino.size() - 1);
        return false;
    }

    /**
     * Representación visual del árbol en formato ASCII
     */
    public String visualizar() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════╗\n");
        sb.append("║     ÁRBOL DE DISTRIBUCIÓN              ║\n");
        sb.append("╚════════════════════════════════════════╝\n\n");
        visualizarRecursivo(raiz, "", true, sb);
        sb.append("\n");
        return sb.toString();
    }

    private void visualizarRecursivo(NodoArbol<T> nodo, String prefijo, boolean esUltimo, StringBuilder sb) {
        if (nodo == null) return;

        sb.append(prefijo);
        sb.append(esUltimo ? "└── " : "├── ");
        sb.append(nodo.toString());
        sb.append("\n");

        List<NodoArbol<T>> hijos = nodo.getHijos();
        for (int i = 0; i < hijos.size(); i++) {
            boolean ultimo = (i == hijos.size() - 1);
            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");
            visualizarRecursivo(hijos.get(i), nuevoPrefijo, ultimo, sb);
        }
    }

    /**
     * Genera un reporte de distribución
     */
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("\n═══════════════════════════════════════════\n");
        reporte.append("   REPORTE DE DISTRIBUCIÓN DE RECURSOS\n");
        reporte.append("═══════════════════════════════════════════\n\n");

        reporte.append(String.format("📊 Total de nodos: %d\n", contarNodos()));
        reporte.append(String.format("📊 Altura del árbol: %d\n", altura()));
        reporte.append(String.format("📊 Recursos en raíz: %d\n", raiz.getCantidadAsignada()));
        reporte.append(String.format("📊 Recursos en hojas: %d\n\n", calcularTotalHojas()));

        reporte.append("📍 DISTRIBUCIÓN POR NIVELES:\n");
        List<List<NodoArbol<T>>> niveles = recorridoPorNiveles();
        for (int i = 0; i < niveles.size(); i++) {
            reporte.append(String.format("\nNivel %d:\n", i));
            for (NodoArbol<T> nodo : niveles.get(i)) {
                reporte.append(String.format("  • %s\n", nodo));
            }
        }

        reporte.append("\n📍 NODOS FINALES (HOJAS):\n");
        for (NodoArbol<T> hoja : obtenerHojas()) {
            reporte.append(String.format("  • %s\n", hoja));
        }

        reporte.append("\n═══════════════════════════════════════════\n");
        return reporte.toString();
    }

    @Override
    public String toString() {
        return visualizar();
    }

    /**
     * Método main para pruebas
     */
    public static void main(String[] args) {
        // Ejemplo de uso
        ArbolDistribucion<String> arbol = new ArbolDistribucion<>("Centro Principal", "centro");

        // Agregar zonas
        arbol.agregarNodo("centro", "Zona Norte", "norte");
        arbol.agregarNodo("centro", "Zona Sur", "sur");
        arbol.agregarNodo("centro", "Zona Este", "este");

        // Agregar sectores a Zona Norte
        arbol.agregarNodo("norte", "Sector A", "norte_a");
        arbol.agregarNodo("norte", "Sector B", "norte_b");

        // Agregar sectores a Zona Sur
        arbol.agregarNodo("sur", "Sector C", "sur_c");

        // Distribuir 1000 recursos
        arbol.distribuirRecursos(1000);

        // Visualizar
        System.out.println(arbol.visualizar());
        System.out.println(arbol.generarReporte());
    }
}