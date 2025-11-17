package edu.universidad.modelo;

import edu.universidad.estructura.ColaPrioridad;
import java.util.*;

/**
 * Grafo dirigido mejorado que usa ColaPrioridad propia
 */
public class Grafo {
    private Map<String, Nodo> nodos = new LinkedHashMap<>();
    private List<Arista> aristas = new ArrayList<>();

    public void agregarNodo(Nodo n) {
        nodos.put(n.getId(), n);
    }

    public void agregarArista(String origen, String destino, double peso) {
        Nodo o = nodos.get(origen), d = nodos.get(destino);
        if (o == null || d == null) return;
        aristas.add(new Arista(origen, destino, peso));
        o.addVecino(destino, peso);
    }

    public Collection<Nodo> getNodos() {
        return nodos.values();
    }

    public List<Arista> getAristas() {
        return aristas;
    }

    public Nodo getNodo(String id) {
        return nodos.get(id);
    }

    /**
     * Algoritmo de Dijkstra usando ColaPrioridad PROPIA
     * Calcula la ruta más corta entre dos nodos
     */
    public List<Nodo> dijkstra(String origenId, String destinoId) {
        if (!nodos.containsKey(origenId) || !nodos.containsKey(destinoId)) {
            return Collections.emptyList();
        }

        // Estructuras de datos
        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> previos = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        // Inicializar distancias
        for (String id : nodos.keySet()) {
            distancias.put(id, Double.POSITIVE_INFINITY);
            previos.put(id, null);
        }
        distancias.put(origenId, 0.0);

        // Cola de prioridad propia (menor distancia = mayor prioridad)
        ColaPrioridad<NodoDistancia> colaPrioridad = new ColaPrioridad<>();
        colaPrioridad.insertar(new NodoDistancia(origenId, 0.0), 0);

        while (!colaPrioridad.estaVacia()) {
            NodoDistancia actual = colaPrioridad.extraer();
            String actualId = actual.nodoId;

            // Si ya visitamos este nodo, continuar
            if (visitados.contains(actualId)) {
                continue;
            }

            visitados.add(actualId);

            // Si llegamos al destino, podemos terminar
            if (actualId.equals(destinoId)) {
                break;
            }

            Nodo nodoActual = nodos.get(actualId);
            double distanciaActual = distancias.get(actualId);

            // Explorar vecinos
            for (Map.Entry<String, Double> vecino : nodoActual.getVecinos().entrySet()) {
                String vecinoId = vecino.getKey();
                double pesoArista = vecino.getValue();

                if (visitados.contains(vecinoId)) {
                    continue;
                }

                double nuevaDistancia = distanciaActual + pesoArista;

                if (nuevaDistancia < distancias.get(vecinoId)) {
                    distancias.put(vecinoId, nuevaDistancia);
                    previos.put(vecinoId, actualId);

                    // Insertar en cola con prioridad basada en distancia
                    // Multiplicamos por 100 y convertimos a int para la prioridad
                    int prioridad = (int) (nuevaDistancia * 100);
                    colaPrioridad.insertar(new NodoDistancia(vecinoId, nuevaDistancia), prioridad);
                }
            }
        }

        // Reconstruir camino
        return reconstruirCamino(previos, origenId, destinoId);
    }

    /**
     * Reconstruye el camino desde origen hasta destino
     */
    private List<Nodo> reconstruirCamino(Map<String, String> previos, String origen, String destino) {
        LinkedList<Nodo> camino = new LinkedList<>();
        String actual = destino;

        // Si no hay camino al destino
        if (previos.get(actual) == null && !actual.equals(origen)) {
            return camino;
        }

        // Reconstruir desde destino hacia origen
        while (actual != null) {
            camino.addFirst(nodos.get(actual));
            actual = previos.get(actual);
        }

        return camino;
    }

    /**
     * Búsqueda en anchura (BFS) para encontrar camino
     */
    public List<Nodo> bfs(String origenId, String destinoId) {
        if (!nodos.containsKey(origenId) || !nodos.containsKey(destinoId)) {
            return Collections.emptyList();
        }

        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, String> previos = new HashMap<>();

        cola.add(origenId);
        visitados.add(origenId);
        previos.put(origenId, null);

        while (!cola.isEmpty()) {
            String actual = cola.poll();

            if (actual.equals(destinoId)) {
                return reconstruirCamino(previos, origenId, destinoId);
            }

            Nodo nodoActual = nodos.get(actual);
            for (String vecino : nodoActual.getVecinos().keySet()) {
                if (!visitados.contains(vecino)) {
                    cola.add(vecino);
                    visitados.add(vecino);
                    previos.put(vecino, actual);
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Calcula todos los caminos posibles entre dos nodos
     */
    public List<List<Nodo>> encontrarTodosCaminos(String origen, String destino, int maxProfundidad) {
        List<List<Nodo>> caminos = new ArrayList<>();
        List<Nodo> caminoActual = new ArrayList<>();
        Set<String> visitados = new HashSet<>();

        encontrarCaminosRecursivo(origen, destino, visitados, caminoActual, caminos, 0, maxProfundidad);

        return caminos;
    }

    private void encontrarCaminosRecursivo(String actual, String destino,
                                           Set<String> visitados,
                                           List<Nodo> caminoActual,
                                           List<List<Nodo>> caminos,
                                           int profundidad,
                                           int maxProfundidad) {

        if (profundidad > maxProfundidad) return;

        visitados.add(actual);
        caminoActual.add(nodos.get(actual));

        if (actual.equals(destino)) {
            caminos.add(new ArrayList<>(caminoActual));
        } else {
            Nodo nodoActual = nodos.get(actual);
            for (String vecino : nodoActual.getVecinos().keySet()) {
                if (!visitados.contains(vecino)) {
                    encontrarCaminosRecursivo(vecino, destino, visitados,
                            caminoActual, caminos,
                            profundidad + 1, maxProfundidad);
                }
            }
        }

        caminoActual.remove(caminoActual.size() - 1);
        visitados.remove(actual);
    }

    /**
     * Verifica si el grafo está conectado
     */
    public boolean esConectado() {
        if (nodos.isEmpty()) return true;

        String primerNodo = nodos.keySet().iterator().next();
        Set<String> alcanzables = new HashSet<>();
        dfsAlcanzables(primerNodo, alcanzables);

        return alcanzables.size() == nodos.size();
    }

    private void dfsAlcanzables(String nodo, Set<String> alcanzables) {
        alcanzables.add(nodo);
        Nodo actual = nodos.get(nodo);

        for (String vecino : actual.getVecinos().keySet()) {
            if (!alcanzables.contains(vecino)) {
                dfsAlcanzables(vecino, alcanzables);
            }
        }
    }

    /**
     * Obtiene estadísticas del grafo
     */
    public String obtenerEstadisticas() {
        int numNodos = nodos.size();
        int numAristas = aristas.size();
        double densidad = numNodos > 1 ?
                (double) numAristas / (numNodos * (numNodos - 1)) : 0;

        return String.format(
                "Nodos: %d | Aristas: %d | Densidad: %.2f | Conectado: %s",
                numNodos, numAristas, densidad, esConectado() ? "Sí" : "No"
        );
    }

    /**
     * Clase auxiliar para Dijkstra
     */
    private static class NodoDistancia {
        String nodoId;
        double distancia;

        NodoDistancia(String nodoId, double distancia) {
            this.nodoId = nodoId;
            this.distancia = distancia;
        }

        @Override
        public String toString() {
            return nodoId + ":" + String.format("%.1f", distancia);
        }
    }

    /**
     * Crea un grafo de muestra
     */
    public static Grafo createSample() {
        Grafo g = new Grafo();
        g.agregarNodo(new Nodo("A"));
        g.agregarNodo(new Nodo("B"));
        g.agregarNodo(new Nodo("C"));
        g.agregarNodo(new Nodo("D"));
        g.agregarArista("A", "B", 5);
        g.agregarArista("B", "C", 3);
        g.agregarArista("A", "C", 10);
        g.agregarArista("C", "D", 2);
        return g;
    }
}