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
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, seller.getName());
            stmt.setString(2, seller.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(seller.getDateOfBirth()));
            stmt.setDouble(4, seller.getBaseSalary());
            stmt.setInt(5, seller.getDepartment().getId());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbExeption("Unexpected error, no rows affected");
            }

        } catch (SQLException e) {
            throw new DbExeption(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("UPDATE seller" +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?" +
                            "WHERE Id = ?");
            stmt.setString(1, seller.getName());
            stmt.setString(2, seller.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(seller.getDateOfBirth()));
            stmt.setDouble(4, seller.getBaseSalary());
            stmt.setInt(5, seller.getDepartment().getId());
            stmt.setInt(6, seller.getId());

           stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbExeption(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM seller" +
                            "WHERE Id = ?");

            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbExeption(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
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
        String sql = "SELECT seller.*, deparment.Name ad DepName" +
                "FROM seller INNER JOIN department" +
                "ON seller.DepartmentId = deparment.Id" +
                "ORDER BY Name";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
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
