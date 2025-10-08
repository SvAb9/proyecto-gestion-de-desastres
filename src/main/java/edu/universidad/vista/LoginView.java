package edu.universidad.vista;

import edu.universidad.repo.AdministradorRepository;
import edu.universidad.modelo.Grafo;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    public LoginView() {
        super("Login - DesaRecu");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420,240);
        setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        form.add(new JLabel("Usuario:"));
        form.add(user);
        form.add(Box.createRigidArea(new Dimension(0,8)));
        form.add(new JLabel("Contraseña:"));
        form.add(pass);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("\uD83D\uDD12  Iniciar sesión");
        loginBtn.addActionListener(e -> {
            String u = user.getText();
            String pss = new String(pass.getPassword());
            boolean ok = AdministradorRepository.getInstance().authenticate(u, pss);
            if (ok) {
                Grafo g = edu.universidad.util.DataLoader.cargarGrafoDesdeResource("/data/data.json");
                new VentanaPrincipal(g);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        actions.add(loginBtn);
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(form, BorderLayout.CENTER);
        p.add(actions, BorderLayout.SOUTH);
        add(p);
        setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); }
        catch (Exception ex) { /* ignore */ }
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}
