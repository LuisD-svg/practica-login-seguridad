package com.mycompany.base_de_datos_lavanderia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioGridFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private String rolUsuario;

    // Paleta de colores minimalista oscura
    private final Color COLOR_FONDO = new Color(11, 15, 23);
    private final Color COLOR_CARD = new Color(24, 34, 52);
    private final Color COLOR_TEXTO = new Color(248, 250, 252);
    private final Color COLOR_TEXTO_MUTED = new Color(148, 163, 184);
    private final Color COLOR_PRIMARIO = new Color(37, 99, 235);
    private final Color COLOR_FILA_PAR = new Color(15, 23, 42);
    private final Color COLOR_FILA_IMPAR = new Color(24, 34, 52);

    // Clave simétrica de desencriptado AES (la misma que usaste en MySQL)
    private static final String CLAVE_AES = "MiClaveSecretaAES2026";

    public UsuarioGridFrame(String rol) {
        this.rolUsuario = rol;

        setTitle("User Listing Report - Rol: " + rol);
        setSize(1150, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Encabezado superior
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setOpaque(false);

        JLabel lblTitulo = new JLabel("User Listing Report");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO);

        JLabel lblSubtitulo = new JLabel("Visualizando datos con permisos de: " + rol);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COLOR_TEXTO_MUTED);

        JPanel panelTextos = new JPanel(new GridLayout(2, 1));
        panelTextos.setOpaque(false);
        panelTextos.add(lblTitulo);
        panelTextos.add(lblSubtitulo);

        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.setBackground(COLOR_PRIMARIO);
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelEncabezado.add(panelTextos, BorderLayout.WEST);
        panelEncabezado.add(btnRefrescar, BorderLayout.EAST);

        // Definición de las 15 columnas del GRID
        String[] columnas = {
            "ID", "Usuario", "Rol", "Nombre", "Apellido", "Email",
            "Teléfono", "F. Nac.", "Dirección", "Estado", "Puesto",
            "CURP (AES)", "NSS (AES)", "Salario (AES)", "Registro"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla es de solo lectura
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setForeground(COLOR_TEXTO);
        tabla.setBackground(COLOR_FILA_PAR);
        tabla.setGridColor(new Color(40, 53, 76));
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Estilo del encabezado de la tabla
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_CARD);
        header.setForeground(COLOR_TEXTO);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));

        // Alternancia de colores en las filas (efecto cebra)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FILA_PAR : COLOR_FILA_IMPAR);
                    c.setForeground(COLOR_TEXTO);
                } else {
                    c.setBackground(COLOR_PRIMARIO);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 53, 76), 1));

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        setContentPane(panelPrincipal);

        // Carga inicial
        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        boolean esAdmin = "ADMINISTRADOR".equalsIgnoreCase(this.rolUsuario);
        String sql;

        // Si es Administrador: desencripta con AES y muestra todos
        // Si es Empleado: solo ve filas de empleados y campos enmascarados con asteriscos
        if (esAdmin) {
            sql = "SELECT id_usuario, username, rol, first_name, last_name, email, telefono, " +
                  "fecha_nacimiento, direccion, user_status, puesto, " +
                  "CAST(AES_DECRYPT(curp_cifrado, ?) AS CHAR) AS curp, " +
                  "CAST(AES_DECRYPT(nss_cifrado, ?) AS CHAR) AS nss, " +
                  "CAST(AES_DECRYPT(salario_cifrado, ?) AS CHAR) AS salario, " +
                  "created_date " +
                  "FROM usuarios";
        } else {
            sql = "SELECT id_usuario, username, rol, first_name, last_name, email, telefono, " +
                  "fecha_nacimiento, direccion, user_status, puesto, " +
                  "'********' AS curp, '********' AS nss, '********' AS salario, " +
                  "created_date " +
                  "FROM usuarios WHERE rol = 'EMPLEADO'";
        }

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return;

            if (esAdmin) {
                ps.setString(1, CLAVE_AES);
                ps.setString(2, CLAVE_AES);
                ps.setString(3, CLAVE_AES);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("rol"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("fecha_nacimiento"),
                        rs.getString("direccion"),
                        rs.getString("user_status"),
                        rs.getString("puesto"),
                        rs.getString("curp"),
                        rs.getString("nss"),
                        rs.getString("salario"),
                        rs.getString("created_date")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar los usuarios: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}