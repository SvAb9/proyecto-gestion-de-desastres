package edu.universidad.vista;

import edu.universidad.modelo.Grafo;
import edu.universidad.modelo.Nodo;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel de mapa DINÁMICO que se actualiza con el grafo
 * - Se recalculan posiciones automáticamente
 * - Distribución circular de nodos
 * - Visualización de rutas resaltadas
 * - Pesos de aristas visibles
 * - Flechas direccionales
 */
public class MapPanel extends JPanel {

    private Grafo grafo;
    private List<Nodo> highlightedPath;
    private Map<String, Point> coords = new HashMap<>();

    // Constantes de diseño
    private static final int ANCHO_PANEL = 950;
    private static final int ALTO_PANEL = 500;
    private static final int MARGEN = 80;
    private static final int RADIO_NODO = 25;

    // Colores
    private static final Color COLOR_BACKGROUND = new Color(249, 250, 251);
    private static final Color COLOR_ARISTA = new Color(209, 213, 219);
    private static final Color COLOR_RUTA_RESALTADA = new Color(239, 68, 68);
    private static final Color COLOR_NODO_NORMAL = new Color(67, 97, 238);
    private static final Color COLOR_NODO_RESALTADO = new Color(239, 68, 68);
    private static final Color COLOR_TEXTO_PESO = new Color(107, 114, 128);
    private static final Color COLOR_ETIQUETA = new Color(55, 65, 81);

    public MapPanel(Grafo grafo) {
        this.grafo = grafo;
        recalcularPosiciones();
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 2));
    }

    /**
     * Actualiza el grafo y recalcula posiciones
     * Este método se llama cuando se agregan/eliminan rutas o zonas
     */
    public void actualizarGrafo(Grafo nuevoGrafo) {
        this.grafo = nuevoGrafo;
        recalcularPosiciones();
        repaint();
        System.out.println("Mapa actualizado: " + grafo.getNodos().size() + " nodos");
    }

    /**
     * Recalcula posiciones de nodos automáticamente
     * Usa distribución circular para mejor visualización
     */
    private void recalcularPosiciones() {
        coords.clear();

        if (grafo == null || grafo.getNodos().isEmpty()) {
            return;
        }

        List<Nodo> nodos = new java.util.ArrayList<>(grafo.getNodos());
        int cantidadNodos = nodos.size();

        if (cantidadNodos == 0) return;

        // Centro del panel
        int centroX = ANCHO_PANEL / 2;
        int centroY = ALTO_PANEL / 2;

        // Radio del círculo (ajustado según el tamaño del panel)
        int radio = Math.min(centroX, centroY) - MARGEN;

        // Distribución circular
        for (int i = 0; i < cantidadNodos; i++) {
            // Ángulo para cada nodo (comienza desde arriba: -90°)
            double angulo = 2 * Math.PI * i / cantidadNodos - Math.PI / 2;

            int x = centroX + (int) (radio * Math.cos(angulo));
            int y = centroY + (int) (radio * Math.sin(angulo));

            coords.put(nodos.get(i).getId(), new Point(x, y));
        }
    }

    /**
     * Establece el camino a resaltar en el mapa
     */
    public void setHighlightedPath(List<Nodo> path) {
        this.highlightedPath = path;
        repaint();
    }

    /**
     * Limpia el camino resaltado
     */
    public void limpiarRutaResaltada() {
        this.highlightedPath = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        // Activar antialiasing para mejor calidad visual
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (grafo == null || grafo.getNodos().isEmpty()) {
            dibujarMensajeVacio(g);
            return;
        }

        // Orden de dibujo (de atrás hacia adelante):
        // 1. Aristas normales
        dibujarAristas(g);

        // 2. Ruta resaltada (encima de aristas normales)
        dibujarRutaResaltada(g);

        // 3. Nodos (encima de todo)
        dibujarNodos(g);
    }

    /**
     * Dibuja mensaje cuando no hay datos
     */
    private void dibujarMensajeVacio(Graphics2D g) {
        g.setColor(new Color(156, 163, 175));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        String mensaje = "No hay rutas disponibles. Agregue rutas para visualizar el mapa.";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y = getHeight() / 2;

        g.drawString(mensaje, x, y);
    }

    /**
     * Dibuja todas las aristas en color gris claro
     */
    private void dibujarAristas(Graphics2D g) {
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(COLOR_ARISTA);

        for (var arista : grafo.getAristas()) {
            Point p1 = coords.get(arista.getOrigenId());
            Point p2 = coords.get(arista.getDestinoId());

            if (p1 != null && p2 != null) {
                // Dibujar línea
                g.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Dibujar peso de la arista
                dibujarPesoArista(g, p1, p2, arista.getPeso());
            }
        }
    }

    /**
     * Dibuja el peso de una arista en el punto medio
     */
    private void dibujarPesoArista(Graphics2D g, Point p1, Point p2, double peso) {
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;

        // Fondo blanco para el texto
        g.setColor(Color.WHITE);
        g.fillRoundRect(midX - 20, midY - 12, 40, 24, 12, 12);

        // Borde del fondo
        g.setColor(COLOR_ARISTA);
        g.drawRoundRect(midX - 20, midY - 12, 40, 24, 12, 12);

        // Texto del peso
        g.setColor(COLOR_TEXTO_PESO);
        g.setFont(new Font("Segoe UI", Font.BOLD, 11));
        String pesoStr = String.format("%.1f", peso);
        FontMetrics fm = g.getFontMetrics();
        int textX = midX - fm.stringWidth(pesoStr) / 2;
        int textY = midY + fm.getAscent() / 2;
        g.drawString(pesoStr, textX, textY);
    }

    /**
     * Dibuja la ruta resaltada en rojo
     */
    private void dibujarRutaResaltada(Graphics2D g) {
        if (highlightedPath != null && highlightedPath.size() > 1) {
            g.setStroke(new BasicStroke(4f));
            g.setColor(COLOR_RUTA_RESALTADA);

            for (int i = 0; i < highlightedPath.size() - 1; i++) {
                Point p1 = coords.get(highlightedPath.get(i).getId());
                Point p2 = coords.get(highlightedPath.get(i + 1).getId());

                if (p1 != null && p2 != null) {
                    // Dibujar línea
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // Dibujar flecha direccional
                    dibujarFlecha(g, p1, p2);
                }
            }
        }
    }

    /**
     * Dibuja una flecha direccional en el punto medio de la línea
     */
    private void dibujarFlecha(Graphics2D g, Point p1, Point p2) {
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        int arrowSize = 12;

        // Punto medio de la línea
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;

        // Calcular puntos del triángulo
        int x1 = midX - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = midY - (int) (arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = midX - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = midY - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        // Dibujar triángulo (flecha)
        g.fillPolygon(new int[]{midX, x1, x2}, new int[]{midY, y1, y2}, 3);
    }

    /**
     * Dibuja todos los nodos
     */
    private void dibujarNodos(Graphics2D g) {
        for (Nodo nodo : grafo.getNodos()) {
            Point p = coords.get(nodo.getId());
            if (p == null) continue;

            boolean esRutaResaltada = highlightedPath != null && highlightedPath.contains(nodo);

            // 1. Sombra del nodo
            g.setColor(new Color(0, 0, 0, 30));
            g.fillOval(p.x - RADIO_NODO + 2, p.y - RADIO_NODO + 2,
                    RADIO_NODO * 2, RADIO_NODO * 2);

            // 2. Círculo del nodo
            if (esRutaResaltada) {
                g.setColor(COLOR_NODO_RESALTADO);
            } else {
                g.setColor(COLOR_NODO_NORMAL);
            }
            g.fillOval(p.x - RADIO_NODO, p.y - RADIO_NODO,
                    RADIO_NODO * 2, RADIO_NODO * 2);

            // 3. Borde del nodo
            g.setStroke(new BasicStroke(3f));
            g.setColor(Color.WHITE);
            g.drawOval(p.x - RADIO_NODO, p.y - RADIO_NODO,
                    RADIO_NODO * 2, RADIO_NODO * 2);

            // 4. Texto del nodo (ID)
            g.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            int textX = p.x - fm.stringWidth(nodo.getId()) / 2;
            int textY = p.y + fm.getAscent() / 2;
            g.drawString(nodo.getId(), textX, textY);

            // 5. Etiqueta debajo del nodo
            dibujarEtiquetaNodo(g, p, nodo.getId(), fm);
        }
    }

    /**
     * Dibuja la etiqueta debajo del nodo
     */
    private void dibujarEtiquetaNodo(Graphics2D g, Point p, String label, FontMetrics fm) {
        g.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        int labelX = p.x - fm.stringWidth(label) / 2;
        int labelY = p.y + RADIO_NODO + 18;

        // Fondo para la etiqueta
        g.setColor(new Color(255, 255, 255, 220));
        g.fillRoundRect(labelX - 5, labelY - 12,
                fm.stringWidth(label) + 10, 16, 8, 8);

        // Texto de la etiqueta
        g.setColor(COLOR_ETIQUETA);
        g.drawString(label, labelX, labelY);
    }

    /**
     * Obtiene el grafo actual
     */
    public Grafo getGrafo() {
        return grafo;
    }

    /**
     * Verifica si hay una ruta resaltada
     */
    public boolean tieneRutaResaltada() {
        return highlightedPath != null && !highlightedPath.isEmpty();
    }

    /**
     * Obtiene el número de nodos visibles
     */
    public int getNodosCount() {
        return grafo != null ? grafo.getNodos().size() : 0;
    }

    /**
     * Obtiene el número de aristas visibles
     */
    public int getAristasCount() {
        return grafo != null ? grafo.getAristas().size() : 0;
    }
}