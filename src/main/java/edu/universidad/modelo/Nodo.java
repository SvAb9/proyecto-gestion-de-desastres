package edu.universidad.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Nodo {
    private String id;
    private Map<String,Double> vecinos = new LinkedHashMap<>();
    public Nodo() {}
    public Nodo(String id) { this.id = id; }
    public String getId() { return id; }
    public Map<String,Double> getVecinos() { return vecinos; }
    public void addVecino(String id, double peso) { vecinos.put(id,peso); }
}
