/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

/**
 *
 * @author guilherme
 */
public class DbExeption extends RuntimeException{
    public DbExeption(String msg) {
        super(msg);
    }
}
