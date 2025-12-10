package com.companyz.ems.dao;

import com.companyz.ems.domain.PayStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    private final ConnectionManager cm;

    public PayrollDAO(ConnectionManager cm) {
        this.cm = cm;
    }

    // 6a: full-time employee pay history sorted by most recent pay date
    public List<PayStatement> payHistoryForEmployee(int empid) {
        String sql = """
            SELECT pay_id, empid, pay_date, gross_pay, deductions, net_pay
            FROM payroll
            WHERE empid = ?
            ORDER BY pay_date DESC
            """;
        List<PayStatement> list = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new PayStatement(
                            rs.getInt("pay_id"),
                            rs.getInt("empid"),
                            rs.getDate("pay_date").toLocalDate(),
                            rs.getDouble("gross_pay"),
                            rs.getDouble("deductions"),
                            rs.getDouble("net_pay")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pay history", e);
        }
        return list;
    }

    
    // 6b: total pay for month by job title
public List<String> totalPayByJobTitleForMonth(int year, int month) {
    String sql = """
        SELECT e.job_title,
               SUM(p.net_pay) AS total_net
        FROM payroll p
        JOIN employees e ON p.empid = e.empid
        WHERE YEAR(p.pay_date) = ? AND MONTH(p.pay_date) = ?
        GROUP BY e.job_title
        ORDER BY total_net DESC
        """;

    List<String> rows = new ArrayList<>();

    try (Connection conn = cm.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, year);
        ps.setInt(2, month);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String jobTitle = rs.getString("job_title");
                double total = rs.getDouble("total_net");
                // simple text row, e.g. "Software Engineer|128450.0"
                rows.add(jobTitle + "|" + total);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error fetching total pay by job title", e);
    }

    return rows;
}

    

    // 6c: total pay for month by division
    public List<String> totalPayByDivisionForMonth(int year, int month) {
        String sql = """
            SELECT e.division,
                   SUM(p.net_pay) AS total_net
            FROM payroll p
            JOIN employees e ON p.empid = e.empid
            WHERE YEAR(p.pay_date) = ? AND MONTH(p.pay_date) = ?
            GROUP BY e.division
            ORDER BY total_net DESC
            """;
        List<String> rows = new ArrayList<>();
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String division = rs.getString("division");
                    double total = rs.getDouble("total_net");
                    rows.add(division + " : " + total);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching total pay by division", e);
        }
        return rows;
    }
}


