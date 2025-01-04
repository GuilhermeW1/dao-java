/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class DB {
    private static Connection conn = null;
    
   
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String DB_NAME = "javaJDBC";
    
    public static Connection getConnection() {
        if (conn != null) return conn;
        
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+DB_NAME, USER, PASSWORD);
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
