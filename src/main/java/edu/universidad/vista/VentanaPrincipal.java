package edu.universidad.vista;

import edu.universidad.modelo.Grafo;
import edu.universidad.modelo.Nodo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private Grafo grafo;
    private MapPanel mapPanel;
    private DefaultTableModel recursosModel;
    private JPanel centerContainer;

    public VentanaPrincipal(Grafo grafo) {
        super("DesaRecu - Panel de Administración");
        this.grafo = grafo;
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // top
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245,246,250));
        top.setBorder(new EmptyBorder(8,12,8,12));
        JLabel title = new JLabel("DesaRecu - Panel de Administración");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        top.add(title, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        // sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(12,12,12,12));
        String[] items = {"🏠 Panel Principal","📦 Gestión de Recursos","🧭 Planificación de Rutas","🚨 Estado de Emergencia","📊 Estadísticas","🗺️ Mapa Interactivo"};
        for (String it : items) {
            JButton b = new JButton(it);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.addActionListener(e -> onSidebar(it));
            sidebar.add(b);
            sidebar.add(Box.createRigidArea(new Dimension(0,8)));
        }
        add(sidebar, BorderLayout.WEST);

        // center container will swap panels
        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBorder(new EmptyBorder(12,12,12,12));
        add(centerContainer, BorderLayout.CENTER);

        // default -> main dashboard
        centerContainer.add(createDashboard(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createDashboard() {
        JPanel container = new JPanel(new BorderLayout());
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setPreferredSize(new Dimension(360, getHeight()));
        left.add(createCard("Resumen de Disponibilidad"));
        left.add(Box.createRigidArea(new Dimension(0,12)));
        left.add(createCard("Gestión de Zona Afectada"));

        JPanel right = new JPanel(new BorderLayout());
        mapPanel = new MapPanel(grafo);
        right.add(mapPanel, BorderLayout.CENTER);

        recursosModel = new DefaultTableModel(new Object[]{"Cantidad","Tipo","Ubicación"},0);
        JTable tabla = new JTable(recursosModel);
        right.add(new JScrollPane(tabla), BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(360);
        container.add(split, BorderLayout.CENTER);

        // bottom zone cards area
        JPanel zonesPanel = new JPanel(new GridLayout(2,3,12,12));
        zonesPanel.setBorder(new EmptyBorder(12,12,12,12));
        zonesPanel.add(createZoneCard("Zona Alfa", "Población: 5000", "Recursos Necesarios: 100", Color.RED));
        zonesPanel.add(createZoneCard("Zona Beta", "Población: 3000", "Recursos Necesarios: 60", Color.YELLOW));
        zonesPanel.add(createZoneCard("Zona Gamma", "Población: 1000", "Recursos Necesarios: 20", Color.GREEN));
        zonesPanel.add(createZoneCard("Zona Delta", "Población: 7000", "Recursos Necesarios: 150", Color.RED));
        zonesPanel.add(createZoneCard("Zona Epsilon", "Población: 2500", "Recursos Necesarios: 45", Color.YELLOW));
        zonesPanel.add(createZoneCard("Zona Zeta", "Población: 800", "Recursos Necesarios: 15", Color.GREEN));
        container.add(zonesPanel, BorderLayout.SOUTH);

        // sample data
        recursosModel.addRow(new Object[]{150,"Agua Potable","Almacén Central A"});
        recursosModel.addRow(new Object[]{35,"Kits Médicos","Clínica Móvil 1"});
        recursosModel.addRow(new Object[]{200,"Mantas Térmicas","Centro Logístico B"});

        return container;
    }

    private JPanel createCard(String title) {
        JPanel c = new JPanel(new BorderLayout());
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220),1,true),
            new EmptyBorder(10,10,10,10)
        ));
        c.setBackground(Color.WHITE);
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD,14f));
        c.add(t, BorderLayout.NORTH);
        JTextArea body = new JTextArea("Contenido...");
        body.setEditable(false);
        body.setOpaque(false);
        c.add(body, BorderLayout.CENTER);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        return c;
    }

    private JPanel createZoneCard(String title, String p1, String p2, Color sideColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200),1,true),
                new EmptyBorder(6,6,6,6)
        ));
        JPanel left = new JPanel();
        left.setBackground(sideColor);
        left.setPreferredSize(new Dimension(10,80));
        left.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        card.add(left, BorderLayout.WEST);
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(8,8,8,8));
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD,13f));
        center.add(t);
        center.add(new JLabel(p1));
        center.add(new JLabel(p2));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(Color.WHITE);
        JButton evac = new JButton("Iniciar Evacuación");
        evac.addActionListener(e -> JOptionPane.showMessageDialog(this, "Evacuación iniciada en " + title));
        JButton alert = new JButton("Enviar Alerta");
        alert.addActionListener(e -> JOptionPane.showMessageDialog(this, "Alerta enviada a " + title));
        actions.add(evac);
        actions.add(alert);
        center.add(actions);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private void onSidebar(String item) {
        // swap panels in centerContainer according to selection
        switch (item) {
            case "🏠 Panel Principal":
                centerContainer.removeAll();
                centerContainer.add(createDashboard(), BorderLayout.CENTER);
                break;
            case "📦 Gestión de Recursos":
                centerContainer.removeAll();
                centerContainer.add(createRecursosPanel(), BorderLayout.CENTER);
                break;
            case "🧭 Planificación de Rutas":
                centerContainer.removeAll();
                centerContainer.add(createRutasPanel(), BorderLayout.CENTER);
                break;
            case "🚨 Estado de Emergencia":
                centerContainer.removeAll();
                centerContainer.add(createEstadoPanel(), BorderLayout.CENTER);
                break;
            case "📊 Estadísticas":
                centerContainer.removeAll();
                centerContainer.add(createStatsPanel(), BorderLayout.CENTER);
                break;
            case "🗺️ Mapa Interactivo":
                centerContainer.removeAll();
                centerContainer.add(mapPanel, BorderLayout.CENTER);
                break;
        }
        centerContainer.revalidate();
        centerContainer.repaint();
    }

    private JPanel createRecursosPanel() {
        JPanel p = new JPanel(new BorderLayout());
        recursosModel = new DefaultTableModel(new Object[]{"Cantidad","Tipo","Ubicación"},0);
        JTable tabla = new JTable(recursosModel);
        recursosModel.addRow(new Object[]{150,"Agua Potable","Almacén Central A"});
        recursosModel.addRow(new Object[]{35,"Kits Médicos","Clínica Móvil 1"});
        recursosModel.addRow(new Object[]{200,"Mantas Térmicas","Centro Logístico B"});
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    private JPanel createRutasPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> origen = new JComboBox<>();
        JComboBox<String> destino = new JComboBox<>();
        for (Nodo n : grafo.getNodos()) { origen.addItem(n.getId()); destino.addItem(n.getId()); }
        JButton buscar = new JButton("Buscar Ruta Más Corta");
        buscar.addActionListener(e -> {
            String o = (String) origen.getSelectedItem();
            String d = (String) destino.getSelectedItem();
            java.util.List<Nodo> ruta = grafo.dijkstra(o,d);
            mapPanel.setHighlightedPath(ruta);
        });
        controls.add(new JLabel("Origen:")); controls.add(origen);
        controls.add(new JLabel("Destino:")); controls.add(destino);
        controls.add(buscar);
        p.add(controls, BorderLayout.NORTH);
        p.add(mapPanel, BorderLayout.CENTER);
        return p;
    }

    private JPanel createEstadoPanel() {
        JPanel grid = new JPanel(new GridLayout(3,2,12,12));
        grid.setBorder(new EmptyBorder(12,12,12,12));
        grid.add(createZoneCard("Zona Alfa", "Población: 5000", "Recursos Necesarios: 100", Color.RED));
        grid.add(createZoneCard("Zona Beta", "Población: 3000", "Recursos Necesarios: 60", Color.YELLOW));
        grid.add(createZoneCard("Zona Gamma", "Población: 1000", "Recursos Necesarios: 20", Color.GREEN));
        grid.add(createZoneCard("Zona Delta", "Población: 7000", "Recursos Necesarios: 150", Color.RED));
        grid.add(createZoneCard("Zona Epsilon", "Población: 2500", "Recursos Necesarios: 45", Color.YELLOW));
        grid.add(createZoneCard("Zona Zeta", "Población: 800", "Recursos Necesarios: 15", Color.GREEN));
        JScrollPane sp = new JScrollPane(grid);
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel createStatsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Estadísticas (próximamente)");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }
}
