package edu.universidad.modelo;

import java.util.*;

public class Grafo {
    private Map<String,Nodo> nodos = new LinkedHashMap<>();
    private List<Arista> aristas = new ArrayList<>();

    public void agregarNodo(Nodo n) { nodos.put(n.getId(), n); }
    public void agregarArista(String origen, String destino, double peso) {
        Nodo o = nodos.get(origen), d = nodos.get(destino);
        if (o==null || d==null) return;
        aristas.add(new Arista(origen,destino,peso));
        o.addVecino(destino,peso);
    }
    public Collection<Nodo> getNodos() { return nodos.values(); }
    public List<Arista> getAristas() { return aristas; }
    public Nodo getNodo(String id) { return nodos.get(id); }

    public List<Nodo> dijkstra(String origenId, String destinoId) {
        if (!nodos.containsKey(origenId) || !nodos.containsKey(destinoId)) return Collections.emptyList();
        Map<String,Double> dist = new HashMap<>();
        Map<String,String> prev = new HashMap<>();
        for (String id : nodos.keySet()) { dist.put(id, Double.POSITIVE_INFINITY); prev.put(id,null); }
        dist.put(origenId, 0.0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(origenId);
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(destinoId)) break;
            Nodo nu = nodos.get(u);
            for (var e : nu.getVecinos().entrySet()) {
                String v = e.getKey(); double w = e.getValue();
                double alt = dist.get(u) + w;
                if (alt < dist.get(v)) { dist.put(v, alt); prev.put(v, u); pq.remove(v); pq.add(v); }
            }
        }
        LinkedList<Nodo> path = new LinkedList<>();
        String u = destinoId;
        if (prev.get(u)==null && !u.equals(origenId)) {
            if (u.equals(origenId)) path.add(nodos.get(u));
            return path;
        }
        while (u!=null) { path.addFirst(nodos.get(u)); u = prev.get(u); }
        return path;
    }

    public static Grafo createSample() {
        Grafo g = new Grafo();
        g.agregarNodo(new Nodo("A"));
        g.agregarNodo(new Nodo("B"));
        g.agregarNodo(new Nodo("C"));
        g.agregarNodo(new Nodo("D"));
        g.agregarArista("A","B",5);
        g.agregarArista("B","C",3);
        g.agregarArista("A","C",10);
        g.agregarArista("C","D",2);
        return g;
    }
}
