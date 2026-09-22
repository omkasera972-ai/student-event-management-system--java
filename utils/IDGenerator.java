package utils;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDGenerator {

    public static String generateNextId(String entityType, String prefix) {
        String generatedId = null;
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Read next_number with row lock
            String selectSql = "SELECT next_number FROM id_sequences WHERE entity_type = ? FOR UPDATE";
            selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, entityType);
            rs = selectStmt.executeQuery();

            if (rs.next()) {
                int nextNumber = rs.getInt("next_number");

                // Generate ID
                if (nextNumber < 100) {
                    generatedId = String.format("%s-%02d", prefix, nextNumber);
                } else {
                    generatedId = String.format("%s-%d", prefix, nextNumber);
                }

                // Increment next_number
                String updateSql = "UPDATE id_sequences SET next_number = next_number + 1 WHERE entity_type = ?";
                updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, entityType);
                updateStmt.executeUpdate();

                conn.commit(); // Commit transaction
            } else {
                conn.rollback();
                throw new RuntimeException("Entity type " + entityType + " not found in id_sequences table.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to generate ID for " + entityType, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (selectStmt != null) selectStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return generatedId;
    }
}
