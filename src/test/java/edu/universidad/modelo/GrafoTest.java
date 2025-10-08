package edu.universidad.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GrafoTest {

    @Test
    public void testDijkstraSimple() {
        Grafo g = new Grafo();
        g.agregarNodo(new Nodo("A"));
        g.agregarNodo(new Nodo("B"));
        g.agregarNodo(new Nodo("C"));
        g.agregarArista("A","B",5);
        g.agregarArista("B","C",3);
        g.agregarArista("A","C",10);

        List<Nodo> ruta = g.dijkstra("A", "C");
        assertNotNull(ruta);
        assertEquals("A", ruta.get(0).getId());
        assertEquals("C", ruta.get(ruta.size()-1).getId());
        assertEquals(3, ruta.size());
        assertEquals("B", ruta.get(1).getId());
    }
}
