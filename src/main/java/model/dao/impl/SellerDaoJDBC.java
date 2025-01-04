package model.dao.impl;

import db.DB;
import db.DbExeption;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        String sql = "SELECT seller.*, deparment.Name ad DepName" +
                "FROM seller INNER JOIN department" +
                "ON seller.DepartmentId = deparment.Id" +
                "WHERE seller.Id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Department department = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, department);

                return seller;
            }

            return null;
        } catch (SQLException e) {
            throw new DbExeption(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDateOfBirth(rs.getDate("BirthDate").toLocalDate());
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        String sql = "SELECT seller.*, deparment.Name ad DepName" +
                "FROM seller INNER JOIN department" +
                "ON seller.DepartmentId = deparment.Id" +
                "WHERE Department.Id = ?" +
                "ORDER BY Name";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, department.getId());
            rs = stmt.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                sellers.add(seller);
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbExeption(e.getMessage());
        }
        finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }
}
