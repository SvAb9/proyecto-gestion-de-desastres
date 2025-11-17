package edu.universidad.estructura;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementación propia de Cola de Prioridad usando Min-Heap
 * Usada para gestionar evacuaciones y distribución de recursos según urgencia
 */
public class ColaPrioridad<T> {

    private List<Nodo<T>> heap;
    private Comparator<T> comparador;

    private static class Nodo<T> {
        T elemento;
        int prioridad;

        Nodo(T elemento, int prioridad) {
            this.elemento = elemento;
            this.prioridad = prioridad;
        }
    }

    public ColaPrioridad() {
        this.heap = new ArrayList<>();
        this.comparador = null;
    }

    public ColaPrioridad(Comparator<T> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador;
    }

    /**
     * Inserta un elemento con su prioridad (menor número = mayor prioridad)
     */
    public void insertar(T elemento, int prioridad) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento, prioridad);
        heap.add(nuevoNodo);
        heapifyUp(heap.size() - 1);
    }

    /**
     * Extrae y retorna el elemento de mayor prioridad
     */
    public T extraer() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }

        T elemento = heap.get(0).elemento;

        if (heap.size() == 1) {
            heap.remove(0);
            return elemento;
        }

        // Mover el último al inicio y hacer heapify down
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        heapifyDown(0);

        return elemento;
    }

    /**
     * Retorna el elemento de mayor prioridad sin extraerlo
     */
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return heap.get(0).elemento;
    }

    /**
     * Retorna la prioridad del elemento más prioritario
     */
    public int peekPrioridad() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return heap.get(0).prioridad;
    }

    /**
     * Verifica si la cola está vacía
     */
    public boolean estaVacia() {
        return heap.isEmpty();
    }

    /**
     * Retorna el tamaño de la cola
     */
    public int tamanio() {
        return heap.size();
    }

    /**
     * Limpia todos los elementos de la cola
     */
    public void limpiar() {
        heap.clear();
    }

    /**
     * Reorganiza el heap hacia arriba (después de inserción)
     */
    private void heapifyUp(int indice) {
        while (indice > 0) {
            int padre = (indice - 1) / 2;

            if (heap.get(indice).prioridad < heap.get(padre).prioridad) {
                intercambiar(indice, padre);
                indice = padre;
            } else {
                break;
            }
        }
    }

    /**
     * Reorganiza el heap hacia abajo (después de extracción)
     */
    private void heapifyDown(int indice) {
        int size = heap.size();

        while (indice < size) {
            int izq = 2 * indice + 1;
            int der = 2 * indice + 2;
            int menor = indice;

            if (izq < size && heap.get(izq).prioridad < heap.get(menor).prioridad) {
                menor = izq;
            }

            if (der < size && heap.get(der).prioridad < heap.get(menor).prioridad) {
                menor = der;
            }

            if (menor != indice) {
                intercambiar(indice, menor);
                indice = menor;
            } else {
                break;
            }
        }
    }

    /**
     * Intercambia dos elementos en el heap
     */
    private void intercambiar(int i, int j) {
        Nodo<T> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Retorna una lista con todos los elementos (sin orden específico)
     */
    public List<T> obtenerTodos() {
        List<T> elementos = new ArrayList<>();
        for (Nodo<T> nodo : heap) {
            elementos.add(nodo.elemento);
        }
        return elementos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ColaPrioridad[");
        for (int i = 0; i < heap.size(); i++) {
            Nodo<T> nodo = heap.get(i);
            sb.append("(").append(nodo.elemento).append(":").append(nodo.prioridad).append(")");
            if (i < heap.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}