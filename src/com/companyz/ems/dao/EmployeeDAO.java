package com.companyz.ems.dao;

import com.companyz.ems.domain.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final ConnectionManager cm;

    public EmployeeDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    // ========== CREATE NEW EMPLOYEE ==========

    public void createEmployee(Employee e) {
        String sql = """
            INSERT INTO employees
            (empid, fname, lname, dob, ssn, hire_date, base_salary,
             email, division, job_title, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getEmpid());
            ps.setString(2, e.getFname());
            ps.setString(3, e.getLname());
            ps.setDate(4, e.getDob() != null ? Date.valueOf(e.getDob()) : null);
            ps.setString(5, e.getSsn());
            ps.setDate(6, e.getHireDate() != null ? Date.valueOf(e.getHireDate()) : null);
            ps.setDouble(7, e.getSalary());
            ps.setString(8, e.getEmail());
            ps.setString(9, e.getDivision());
            ps.setString(10, e.getJobTitle());
            ps.setString(11, e.getStatus());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error creating employee", ex);
        }
    }

    // ========== FIND BY EMPID ==========

    public Employee findByEmpid(int empid) {
        String sql = """
            SELECT empid, fname, lname, dob, ssn, hire_date,
                   base_salary, email, division, job_title, status
            FROM employees
            WHERE empid = ?
            """;
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empid);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding employee by id", e);
        }
    }

    // ========== SEARCH ==========

    public List<Employee> search(String name, LocalDate dob, String ssn, Integer empid) {
        StringBuilder sb = new StringBuilder("""
            SELECT empid, fname, lname, dob, ssn, hire_date,
                   base_salary, email, division, job_title, status
            FROM employees
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            sb.append(" AND CONCAT(fname,' ',lname) LIKE ?");
            params.add("%" + name + "%");
        }
        if (dob != null) {
            sb.append(" AND dob = ?");
            params.add(Date.valueOf(dob));
        }
        if (ssn != null && !ssn.isBlank()) {
            sb.append(" AND ssn = ?");
            params.add(ssn);
        }
        if (empid != null) {
            sb.append(" AND empid = ?");
            params.add(empid);
        }

        List<Employee> list = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching employees", e);
        }
        return list;
    }

    // ========== UPDATE EMPLOYEE DETAILS ==========

    public void updateEmployee(Employee e) {
        String sql = """
            UPDATE employees
            SET email = ?, division = ?, job_title = ?, status = ?, base_salary = ?
            WHERE empid = ?
            """;
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getEmail());
            ps.setString(2, e.getDivision());
            ps.setString(3, e.getJobTitle());
            ps.setString(4, e.getStatus());
            ps.setDouble(5, e.getSalary());
            ps.setInt(6, e.getEmpid());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error updating employee", ex);
        }
    }

    // ========== UPDATE SALARY RANGE ==========

    public int updateSalaryRange(double min, double max, double percent) {
        String sql = """
            UPDATE employees
            SET base_salary = base_salary * (1 + ? / 100.0)
            WHERE base_salary >= ? AND base_salary < ?
            """;
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, percent);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating salary range", e);
        }
    }

    // ========== HIRED BETWEEN REPORT ==========

    public List<Employee> hiredBetween(LocalDate from, LocalDate to) {
        String sql = """
            SELECT empid, fname, lname, dob, ssn, hire_date,
                   base_salary, email, division, job_title, status
            FROM employees
            WHERE hire_date BETWEEN ? AND ?
            ORDER BY hire_date
            """;
        List<Employee> list = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving hired-between report", e);
        }
        return list;
    }

    // ========== ROW MAPPER ==========

    private Employee mapRow(ResultSet rs) throws SQLException {
        int empid = rs.getInt("empid");
        String fname = rs.getString("fname");
        String lname = rs.getString("lname");
        Date dobSql = rs.getDate("dob");
        LocalDate dob = dobSql != null ? dobSql.toLocalDate() : null;
        String ssn = rs.getString("ssn");
        Date hireSql = rs.getDate("hire_date");
        LocalDate hire = hireSql != null ? hireSql.toLocalDate() : null;
        double salary = rs.getDouble("base_salary");
        String email = rs.getString("email");
        String division = rs.getString("division");
        String jobTitle = rs.getString("job_title");
        String status = rs.getString("status");

        return new Employee(empid, fname, lname, dob, ssn,
                            hire, salary, email, division, jobTitle, status);
    }
}

    


