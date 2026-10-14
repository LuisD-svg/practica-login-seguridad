package com.mycompany.base_de_datos_lavanderia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Lavanderia extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnVerPass;
    private boolean passVisible = false;

    // Paleta de colores Dark Minimalista
    private final Color COLOR_FONDO_TOTAL = new Color(11, 15, 23);       // Negro suave / Night
    private final Color COLOR_CARD = new Color(24, 34, 52);             // Azul grisáceo oscuro elegante
    private final Color COLOR_BORDE_CARD = new Color(40, 53, 76);       // Borde tenue de tarjeta
    private final Color COLOR_TEXTO_BLANCO = new Color(248, 250, 252);  // Blanco limpio
    private final Color COLOR_TEXTO_MUTED = new Color(148, 163, 184);   // Gris azulado
    private final Color COLOR_INPUT_BG = new Color(15, 23, 38);         // Fondo campos oscuro
    private final Color COLOR_INPUT_BORDE = new Color(51, 65, 85);      // Borde inputs
    private final Color COLOR_PRIMARIO = new Color(37, 99, 235);        // Azul Royal moderno
    private final Color COLOR_HOVER = new Color(29, 78, 216);           // Azul hover

    public Lavanderia() {
        setTitle("Sistema - Iniciar Sesión");
        setSize(440, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo exterior negro profundo
        JPanel fondoExterior = new JPanel(new GridBagLayout());
        fondoExterior.setBackground(COLOR_FONDO_TOTAL);

        // Tarjeta central azul/gris oscuro con esquinas redondeadas
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Relleno de la tarjeta
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Borde elegante
                g2.setColor(COLOR_BORDE_CARD);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(35, 35, 35, 35));
        cardPanel.setPreferredSize(new Dimension(360, 440));

        // Título
        JLabel lblTitle = new JLabel("Iniciar Sesión");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXTO_BLANCO);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel lblSub = new JLabel("Ingresa tus credenciales para acceder");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(COLOR_TEXTO_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Etiqueta Usuario
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(COLOR_TEXTO_BLANCO);

        // Campo Usuario
        txtUser = crearCampoTexto("Ingresa tu usuario");

        // Etiqueta Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(COLOR_TEXTO_BLANCO);

        // Contenedor para el Password + Botón de Ojo
        JPanel panelPassword = new JPanel(new BorderLayout(5, 0));
        panelPassword.setOpaque(false);
        panelPassword.setMaximumSize(new Dimension(290, 40));
        panelPassword.setPreferredSize(new Dimension(290, 40));

        txtPass = crearCampoPassword("••••••••");

        // Botón toggle de visibilidad
        btnVerPass = new JButton("👁");
        btnVerPass.setPreferredSize(new Dimension(42, 40));
        btnVerPass.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        btnVerPass.setForeground(COLOR_TEXTO_MUTED);
        btnVerPass.setBackground(COLOR_INPUT_BG);
        btnVerPass.setFocusPainted(false);
        btnVerPass.setBorder(BorderFactory.createLineBorder(COLOR_INPUT_BORDE, 1, true));
        btnVerPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerPass.setToolTipText("Mostrar / Ocultar contraseña");

        btnVerPass.addActionListener(e -> alternarVisibilidadPass());

        panelPassword.add(txtPass, BorderLayout.CENTER);
        panelPassword.add(btnVerPass, BorderLayout.EAST);

        // Botón Iniciar Sesión con animación hover
        btnLogin = new JButton("Sign In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(COLOR_HOVER.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_HOVER);
                } else {
                    g2.setColor(COLOR_PRIMARIO);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setMaximumSize(new Dimension(290, 42));
        btnLogin.setPreferredSize(new Dimension(290, 42));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addActionListener(e -> ejecutarLogin());

        // Ensamblado en tarjeta
        cardPanel.add(lblTitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        cardPanel.add(lblSub);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 26)));

        cardPanel.add(crearContenedorIzquierda(lblUser));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        cardPanel.add(txtUser);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        cardPanel.add(crearContenedorIzquierda(lblPass));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        cardPanel.add(panelPassword);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 26)));

        cardPanel.add(btnLogin);

        fondoExterior.add(cardPanel);
        setContentPane(fondoExterior);
    }

    private void alternarVisibilidadPass() {
        String passActual = new String(txtPass.getPassword());
        if (passActual.equals("••••••••")) return;

        passVisible = !passVisible;
        if (passVisible) {
            txtPass.setEchoChar((char) 0);
            btnVerPass.setText("🙈");
            btnVerPass.setForeground(COLOR_PRIMARIO);
        } else {
            txtPass.setEchoChar('•');
            btnVerPass.setText("👁");
            btnVerPass.setForeground(COLOR_TEXTO_MUTED);
        }
    }

    private JPanel crearContenedorIzquierda(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(290, 20));
        p.add(comp);
        return p;
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField(placeholder);
        campo.setMaximumSize(new Dimension(290, 40));
        campo.setPreferredSize(new Dimension(290, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(COLOR_INPUT_BG);
        campo.setForeground(COLOR_TEXTO_MUTED);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_INPUT_BORDE, 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO_BLANCO);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(COLOR_TEXTO_MUTED);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_INPUT_BORDE, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return campo;
    }

    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField(placeholder);
        campo.setEchoChar((char) 0);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBackground(COLOR_INPUT_BG);
        campo.setForeground(COLOR_TEXTO_MUTED);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_INPUT_BORDE, 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String pass = new String(campo.getPassword());
                if (pass.equals(placeholder)) {
                    campo.setText("");
                    if (!passVisible) {
                        campo.setEchoChar('•');
                    }
                    campo.setForeground(COLOR_TEXTO_BLANCO);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                String pass = new String(campo.getPassword());
                if (pass.trim().isEmpty()) {
                    campo.setEchoChar((char) 0);
                    campo.setText(placeholder);
                    campo.setForeground(COLOR_TEXTO_MUTED);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_INPUT_BORDE, 1, true),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return campo;
    }

   private void ejecutarLogin() {
        String usuario = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (usuario.isEmpty() || usuario.equals("Ingresa tu usuario") || 
            pass.isEmpty() || pass.equals("••••••••")) {
            JOptionPane.showMessageDialog(this, "Por favor completa ambos campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consulta usando 'first_name' que es el nombre real en la tabla
        String sql = "SELECT id_usuario, username, rol, first_name " +
                     "FROM usuarios " +
                     "WHERE username = ? AND password_hash = SHA2(?, 256)";

        try (java.sql.Connection con = ConexionDB.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return; // Si la conexión a Azure falló, ya mostró error

            ps.setString(1, usuario);
            ps.setString(2, pass);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                // Se evalúa UNA SOLA VEZ
                if (rs.next()) {
                    String nombre = rs.getString("first_name");
                    String rol = rs.getString("rol");

                    JOptionPane.showMessageDialog(this, 
                        "¡Bienvenido, " + nombre + "!\nRol asignado: " + rol, 
                        "Acceso Concedido", 
                        JOptionPane.INFORMATION_MESSAGE);

                    // Abre la ventana del GRID con los permisos correspondientes (Admin o Empleado)
                    new UsuarioGridFrame(rol).setVisible(true);

                    // Cierra la ventana de login
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Usuario o contraseña incorrectos.", 
                        "Acceso Denegado", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la consulta: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Lavanderia().setVisible(true));
    }
}