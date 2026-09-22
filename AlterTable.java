import dao.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class AlterTable {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {
            
            try {
                stmt.execute("ALTER TABLE events ADD COLUMN start_time TIME");
                System.out.println("start_time column added.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1060) {
                    System.out.println("start_time column already exists.");
                } else {
                    throw e;
                }
            }

            try {
                stmt.execute("ALTER TABLE events ADD COLUMN end_time TIME");
                System.out.println("end_time column added.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1060) {
                    System.out.println("end_time column already exists.");
                } else {
                    throw e;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
