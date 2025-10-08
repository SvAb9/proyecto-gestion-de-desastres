package edu.universidad.vista;

import edu.universidad.modelo.Grafo;
import edu.universidad.modelo.Nodo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private Grafo grafo;
    private List<Nodo> highlightedPath;
    private Map<String, Point> coords = new HashMap<>();

    public MapPanel(Grafo grafo) {
        this.grafo = grafo;

        // 📍 Posiciones personalizadas de los nodos
        coords.put("A", new Point(100, 200));
        coords.put("B", new Point(250, 120));
        coords.put("C", new Point(400, 250));
        coords.put("D", new Point(550, 150));
        coords.put("E", new Point(700, 220));
        coords.put("F", new Point(850, 100)); // opcional si agregas más nodos

        // Si hay nodos sin coordenadas predefinidas, se ubican automáticamente
        int defaultX = 80, defaultY = 350;
        for (Nodo n : grafo.getNodos()) {
            coords.putIfAbsent(n.getId(), new Point(defaultX, defaultY));
            defaultX += 140;
        }

        setPreferredSize(new Dimension(760, 420));
        setBackground(Color.WHITE);
    }

    public void setHighlightedPath(List<Nodo> path) {
        this.highlightedPath = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2f));

        // 🔹 Dibujar aristas en gris
        g.setColor(new Color(200, 200, 200));
        for (var a : grafo.getAristas()) {
            Point p1 = coords.get(a.getOrigenId());
            Point p2 = coords.get(a.getDestinoId());
            if (p1 != null && p2 != null) {
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // 🔴 Dibujar ruta resaltada
        if (highlightedPath != null && highlightedPath.size() > 1) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(3f));
            for (int i = 0; i < highlightedPath.size() - 1; i++) {
                Point p1 = coords.get(highlightedPath.get(i).getId());
                Point p2 = coords.get(highlightedPath.get(i + 1).getId());
                if (p1 != null && p2 != null) g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // 🔵 Dibujar nodos
        for (Nodo n : grafo.getNodos()) {
            Point p = coords.get(n.getId());
            if (p != null) {
                g.setColor(new Color(66, 133, 244)); // azul
                g.fillOval(p.x - 12, p.y - 12, 24, 24);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString(n.getId(), p.x - 5, p.y + 5);
            }
        }
    }
}
