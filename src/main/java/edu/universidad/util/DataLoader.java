package edu.universidad.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.universidad.modelo.Grafo;
import edu.universidad.modelo.Nodo;
import java.io.InputStream;

public class DataLoader {
    public static Grafo cargarGrafoDesdeResource(String resourcePath) {
        try {
            ObjectMapper m = new ObjectMapper();
            InputStream is = DataLoader.class.getResourceAsStream(resourcePath);
            if (is==null) return Grafo.createSample();
            JsonNode root = m.readTree(is);
            Grafo g = new Grafo();
            if (root.has("nodos")) {
                for (JsonNode n : root.get("nodos")) {
                    g.agregarNodo(new Nodo(n.get("id").asText()));
                }
            }
            if (root.has("aristas")) {
                for (JsonNode a : root.get("aristas")) {
                    g.agregarArista(a.get("origenId").asText(), a.get("destinoId").asText(), a.get("peso").asDouble());
                }
            }
            return g;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Grafo.createSample();
        }
    }
}
