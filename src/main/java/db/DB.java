/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class DB {
    private static Connection conn = null;
    
   
    private static final String USER = null;
    private static final String PASSWORD = null;
    private static final String DB_NAME = null;

    private static Properties loadConfiguration() {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.txt"))) {
            Properties prop = new Properties();
            prop.load(br);
            return prop;
        } catch (IOException e) {
            e.getMessage();
            throw new DbExeption("Error loading database configuration");
        }
    }

    public static Connection getConnection() {
        if (conn != null) return conn;
        try {
            Properties prop = loadConfiguration();
            String url = prop.getProperty("dburl");
            conn = DriverManager.getConnection(url, prop);
        } catch (SQLException ex) {
            throw new DbExeption(ex.getMessage());
        }
       
       return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbExeption(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new DbExeption(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbExeption(e.getMessage());
            }
        }
    }
}
