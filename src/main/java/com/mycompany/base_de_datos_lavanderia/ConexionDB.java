package com.mycompany.base_de_datos_lavanderia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {
    // Datos de tu servidor en Azure
    private static final String HOST = "Mysql@divinidad.mysql.database.azure.com"; 
    private static final String PUERTO = "3306";
    private static final String DATABASE = "practica_seguridad"; 
    private static final String USER = "Mysql@dedos"; 
    private static final String PASSWORD = "Root1234";

    public static Connection getConexion() {
        Connection con = null;
        try {
            // Azure requiere conexión segura SSL habilitada
            String url = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + DATABASE 
                       + "?useSSL=true&requireSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
            con = DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con Azure:\n" + e.getMessage(), 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }
}