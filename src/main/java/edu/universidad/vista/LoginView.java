package edu.universidad.vista;

import edu.universidad.modelo.Grafo;
import edu.universidad.modelo.Usuario;
import edu.universidad.repository.UsuarioRepository;
import edu.universidad.util.PersistenciaJSON;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * Vista de Login FINAL
 * ✅ Muestra ubicación de archivos JSON
 * ✅ Opción de reset de datos
 * ✅ Validaciones completas
 */
public class LoginView extends JFrame {

    private static final Color COLOR_PRIMARY = new Color(67, 97, 238);
    private static final Color COLOR_BACKGROUND = new Color(249, 250, 251);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_TEXT = new Color(17, 24, 39);
    private static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128);
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129);
    private static final Color COLOR_DANGER = new Color(239, 68, 68);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginView() {
        super("DesaRecu - Iniciar Sesión");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BACKGROUND);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(COLOR_BACKGROUND);

        JPanel loginCard = createLoginCard();
        centerPanel.add(loginCard);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));
        card.setPreferredSize(new Dimension(440, 600));

        // Logo y título
        JLabel logo = new JLabel("DesaRecu");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logo.setForeground(COLOR_PRIMARY);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sistema de Gestión de Desastres");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(COLOR_TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(40));

        // Título del formulario
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(30));

        // Campo Email
        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(COLOR_TEXT);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(new CompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblEmail);
        card.add(Box.createVerticalStrut(8));
        card.add(txtEmail);
        card.add(Box.createVerticalStrut(20));

        // Campo Contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setForeground(COLOR_TEXT);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(new CompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblPassword);
        card.add(Box.createVerticalStrut(8));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(30));

        // Botón de login
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(20));

        // Línea separadora
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(229, 231, 235));

        card.add(separator);
        card.add(Box.createVerticalStrut(20));

        // Botón de registro
        JButton btnRegistro = new JButton("Crear Nueva Cuenta");
        btnRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRegistro.setForeground(COLOR_PRIMARY);
        btnRegistro.setBackground(COLOR_CARD);
        btnRegistro.setBorder(new CompoundBorder(
                new LineBorder(COLOR_PRIMARY, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistro.addActionListener(e -> mostrarRegistro());

        card.add(btnRegistro);
        card.add(Box.createVerticalStrut(20));

        // Usuarios de prueba
        JLabel lblInfo = new JLabel("<html><center><b>Usuarios de prueba:</b><br>" +
                "admin@desarecu.com / admin<br>" +
                "coord@desarecu.com / coord123<br>" +
                "operador@desarecu.com / oper123</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(COLOR_TEXT_LIGHT);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblInfo);

        return card;
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos", "Campos Vacíos");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            mostrarError("Por favor ingrese un email válido", "Email Inválido");
            txtEmail.requestFocus();
            return;
        }

        Usuario usuario = UsuarioRepository.getInstance().autenticar(email, password);

        if (usuario != null) {
            System.out.println("Login exitoso: " + usuario.getNombre());
            Grafo g = edu.universidad.util.DataLoader.cargarGrafoDesdeResource("/data/data.json");
            new VentanaPrincipal(g, usuario);
            dispose();
        } else {
            mostrarError("Email o contraseña incorrectos\nVerifique sus credenciales", "Credenciales Inválidas");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private void mostrarRegistro() {
        JDialog dialog = new JDialog(this, "Registrar Nuevo Usuario", true);
        dialog.setSize(480, 650);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(COLOR_CARD);

        JLabel titulo = new JLabel("Crear Nueva Cuenta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(30));

        JTextField txtNombre = createTextField();
        JTextField txtEmailReg = createTextField();
        JPasswordField txtPassReg = createPasswordField();
        JPasswordField txtPassConfirm = createPasswordField();

        JComboBox<Usuario.Rol> cboRol = new JComboBox<>(Usuario.Rol.values());
        cboRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        cboRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboRol.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Usuario.Rol) {
                    setText(((Usuario.Rol) value).getNombre());
                }
                return this;
            }
        });

        addFormField(panel, "Nombre completo:", txtNombre);
        addFormField(panel, "Email:", txtEmailReg);
        addFormField(panel, "Contraseña:", txtPassReg);
        addFormField(panel, "Confirmar Contraseña:", txtPassConfirm);
        addFormField(panel, "Rol:", cboRol);

        panel.add(Box.createVerticalStrut(20));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton btnGuardar = new JButton("Registrar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(COLOR_SUCCESS);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(130, 40));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(COLOR_TEXT);
        btnCancelar.setBackground(new Color(229, 231, 235));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(130, 40));

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmailReg.getText().trim();
            String pass = new String(txtPassReg.getPassword());
            String passConfirm = new String(txtPassConfirm.getPassword());
            Usuario.Rol rol = (Usuario.Rol) cboRol.getSelectedItem();

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.length() < 3) {
                JOptionPane.showMessageDialog(dialog, "El nombre debe tener al menos 3 caracteres",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                JOptionPane.showMessageDialog(dialog, "Ingrese un email válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pass.length() < 4) {
                JOptionPane.showMessageDialog(dialog, "La contraseña debe tener al menos 4 caracteres",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pass.equals(passConfirm)) {
                JOptionPane.showMessageDialog(dialog, "Las contraseñas no coinciden",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario(nombre, email, pass, rol);

            if (UsuarioRepository.getInstance().registrar(nuevoUsuario)) {
                JOptionPane.showMessageDialog(dialog,
                        "Usuario registrado exitosamente\nYa puede iniciar sesión",
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                txtEmail.setText(email);
                txtPassword.requestFocus();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "El email ya está registrado",
                        "Email Duplicado",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btnGuardar);
        buttonsPanel.add(btnCancelar);

        panel.add(buttonsPanel);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
                new LineBorder(new Color(209, 213, 219), 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(COLOR_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        // ✅ MOSTRAR UBICACIÓN DE ARCHIVOS JSON
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("📂 UBICACIÓN DE ARCHIVOS JSON:");
        System.out.println("   " + new File("datos").getAbsolutePath());
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("ℹ️  Para borrar datos antiguos:");
        System.out.println("   1. Ve a la carpeta de arriba");
        System.out.println("   2. Elimina todos los archivos .json");
        System.out.println("   3. Reinicia el programa");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> new LoginView());
    }
}