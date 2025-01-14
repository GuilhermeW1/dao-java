package application;

import db.DB;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Department obj = new Department(1, "Books");
        Seller seller = new Seller(21, "Bob", "bob@gmail.com", LocalDate.of(2000, 07, 24), 3000.0, obj);
        System.out.println(seller);
    }
}