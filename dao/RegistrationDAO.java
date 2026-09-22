package dao;
import exceptions.EntityNotFoundException;
import exceptions.NoSeatsAvailableException;
import exceptions.DuplicateEntityException;
import exceptions.ValidationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO {

    public String[] registerStudentForEvent(String regId, String studentId, String eventId) throws SQLException, EntityNotFoundException, NoSeatsAvailableException, DuplicateEntityException {
        Connection con = null;
        PreparedStatement checkStudent = null;
        PreparedStatement checkEvent = null;
        PreparedStatement insertReg = null;
        PreparedStatement updateSeats = null;
        ResultSet rsStudent = null;
        ResultSet rsEvent = null;
        
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // Start transaction

            // 1. Validate student
            checkStudent = con.prepareStatement("SELECT * FROM students WHERE student_id = ?");
            checkStudent.setString(1, studentId);
            rsStudent = checkStudent.executeQuery();
            if (!rsStudent.next()) {
                throw new EntityNotFoundException("Student not found.");
            }
            String studentName = rsStudent.getString("name");

            // 2. Validate event and 3. check available seats
            checkEvent = con.prepareStatement("SELECT * FROM events WHERE event_id = ? FOR UPDATE");
            checkEvent.setString(1, eventId);
            rsEvent = checkEvent.executeQuery();
            if (!rsEvent.next()) {
                throw new EntityNotFoundException("Event not found.");
            }
            String eventName = rsEvent.getString("event_name");
            int availableSeats = rsEvent.getInt("available_seats");
            
            String dateStr = rsEvent.getDate("event_date") != null ? rsEvent.getDate("event_date").toString() : "N/A";
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                dateStr = parts[2] + "-" + parts[1] + "-" + parts[0];
            }
            
            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
            java.sql.Time st = rsEvent.getTime("start_time");
            java.sql.Time et = rsEvent.getTime("end_time");
            String timeStr = "N/A";
            if (st != null && et != null) {
                timeStr = st.toLocalTime().format(timeFormatter) + " - " + et.toLocalTime().format(timeFormatter);
            }
            
            // Check Duplicate Registration
            PreparedStatement checkDup = con.prepareStatement("SELECT * FROM registrations WHERE student_id = ? AND event_id = ?");
            checkDup.setString(1, studentId);
            checkDup.setString(2, eventId);
            ResultSet rsDup = checkDup.executeQuery();
            if (rsDup.next()) {
                rsDup.close();
                checkDup.close();
                throw new DuplicateEntityException("Registration already exists.\nStudent: " + studentName + "\nEvent: " + eventName);
            }
            rsDup.close();
            checkDup.close();

            if (availableSeats <= 0) {
                throw new NoSeatsAvailableException("No seats available for event " + eventId);
            }

            // 4. Insert registration
            insertReg = con.prepareStatement("INSERT INTO registrations (reg_id, student_id, event_id) VALUES (?, ?, ?)");
            insertReg.setString(1, regId);
            insertReg.setString(2, studentId);
            insertReg.setString(3, eventId);
            insertReg.executeUpdate();

            // 5. Decrease available seats
            updateSeats = con.prepareStatement("UPDATE events SET available_seats = available_seats - 1 WHERE event_id = ?");
            updateSeats.setString(1, eventId);
            updateSeats.executeUpdate();

            // 6. Commit
            con.commit();
            
            return new String[]{studentName, eventName, dateStr, timeStr};
        } catch (SQLException | EntityNotFoundException | NoSeatsAvailableException | DuplicateEntityException e) {
            if (con != null) {
                try {
                    con.rollback(); // 7. Rollback everything on failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            // Use try-with-resources semantics for cleanup
            if (rsStudent != null) try { rsStudent.close(); } catch(SQLException e) {}
            if (rsEvent != null) try { rsEvent.close(); } catch(SQLException e) {}
            if (checkStudent != null) try { checkStudent.close(); } catch(SQLException e) {}
            if (checkEvent != null) try { checkEvent.close(); } catch(SQLException e) {}
            if (insertReg != null) try { insertReg.close(); } catch(SQLException e) {}
            if (updateSeats != null) try { updateSeats.close(); } catch(SQLException e) {}
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch(SQLException e) {}
            }
        }
    }

        public static class EventRegistrationGroup {
        public String eventId;
        public String eventName;
        public String venue;
        public String eventDate;
        public String eventTime;
        public int totalSeats;
        public int availableSeats;
        public java.util.List<StudentRegistration> students = new java.util.ArrayList<>();
    }

    public static class StudentRegistration {
        public String regId;
        public String studentId;
        public String studentName;
        public String branch;
        public String regTime;
    }

    public java.util.List<EventRegistrationGroup> getGroupedRegistrations() throws SQLException {
        java.util.List<EventRegistrationGroup> groups = new java.util.ArrayList<>();
        String eventSql = "SELECT * FROM events";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(eventSql);
             ResultSet rs = ps.executeQuery()) {
             
             while(rs.next()) {
                 EventRegistrationGroup group = new EventRegistrationGroup();
                 group.eventId = rs.getString("event_id");
                 group.eventName = rs.getString("event_name");
                 group.venue = rs.getString("venue");
                 
                 java.sql.Date d = rs.getDate("event_date");
                 group.eventDate = (d != null) ? d.toString() : "";
                 
                 java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
                 java.sql.Time st = rs.getTime("start_time");
                 java.sql.Time et = rs.getTime("end_time");
                 if (st != null && et != null) {
                     group.eventTime = st.toLocalTime().format(timeFormatter) + " - " + et.toLocalTime().format(timeFormatter);
                 } else {
                     group.eventTime = "N/A";
                 }
                 
                 group.totalSeats = rs.getInt("total_seats");
                 group.availableSeats = rs.getInt("available_seats");
                 
                 String regSql = "SELECT r.reg_id, s.student_id, s.name, s.branch, r.reg_time FROM registrations r JOIN students s ON r.student_id = s.student_id WHERE r.event_id = ? ORDER BY r.reg_id";
                 try (PreparedStatement psReg = con.prepareStatement(regSql)) {
                     psReg.setString(1, group.eventId);
                     try (ResultSet rsReg = psReg.executeQuery()) {
                         while (rsReg.next()) {
                             StudentRegistration sr = new StudentRegistration();
                             sr.regId = rsReg.getString("reg_id");
                             sr.studentId = rsReg.getString("student_id");
                             sr.studentName = rsReg.getString("name");
                             sr.branch = rsReg.getString("branch");
                             
                             java.sql.Timestamp ts = rsReg.getTimestamp("reg_time");
                             if (ts != null) {
                                 sr.regTime = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm a").format(ts);
                             } else {
                                 sr.regTime = "N/A";
                             }
                             
                             group.students.add(sr);
                         }
                     }
                 }
                 groups.add(group);
             }
        }
        return groups;
    }

    public static class RegistrationDetail {
        public String regId;
        public String studentId;
        public String studentName;
        public String eventId;
        public String eventName;
    }

    public java.util.List<RegistrationDetail> getAllRegistrationsFlat() throws SQLException {
        java.util.List<RegistrationDetail> list = new java.util.ArrayList<>();
        String sql = "SELECT r.reg_id, s.student_id, s.name AS student_name, e.event_id, e.event_name " +
                     "FROM registrations r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "JOIN events e ON r.event_id = e.event_id " +
                     "ORDER BY r.reg_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                 RegistrationDetail rd = new RegistrationDetail();
                 rd.regId = rs.getString("reg_id");
                 rd.studentId = rs.getString("student_id");
                 rd.studentName = rs.getString("student_name");
                 rd.eventId = rs.getString("event_id");
                 rd.eventName = rs.getString("event_name");
                 list.add(rd);
             }
        }
        return list;
    }

    public RegistrationDetail getRegistrationDetails(String regId) throws SQLException {
        String sql = "SELECT r.reg_id, s.student_id, s.name AS student_name, e.event_id, e.event_name " +
                     "FROM registrations r " +
                     "JOIN students s ON r.student_id = s.student_id " +
                     "JOIN events e ON r.event_id = e.event_id " +
                     "WHERE r.reg_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             ps.setString(1, regId);
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     RegistrationDetail rd = new RegistrationDetail();
                     rd.regId = rs.getString("reg_id");
                     rd.studentId = rs.getString("student_id");
                     rd.studentName = rs.getString("student_name");
                     rd.eventId = rs.getString("event_id");
                     rd.eventName = rs.getString("event_name");
                     return rd;
                 }
             }
        }
        return null;
    }

    public String[] updateRegistration(String regId, String newEventId) throws SQLException, EntityNotFoundException, NoSeatsAvailableException, DuplicateEntityException {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            
            String selReg = "SELECT * FROM registrations WHERE reg_id = ? FOR UPDATE";
            String studentId = null;
            String oldEventId = null;
            try (PreparedStatement psSel = con.prepareStatement(selReg)) {
                psSel.setString(1, regId);
                try (ResultSet rsSel = psSel.executeQuery()) {
                    if (!rsSel.next()) {
                        throw new EntityNotFoundException("Registration ID not found.");
                    }
                    studentId = rsSel.getString("student_id");
                    oldEventId = rsSel.getString("event_id");
                }
            }
            
            if (oldEventId.equals(newEventId)) {
                return null;
            }
            
            String selOldEvent = "SELECT event_name FROM events WHERE event_id = ? FOR UPDATE";
            String oldEventName = null;
            try (PreparedStatement psOld = con.prepareStatement(selOldEvent)) {
                psOld.setString(1, oldEventId);
                try (ResultSet rsOld = psOld.executeQuery()) {
                    if (rsOld.next()) oldEventName = rsOld.getString("event_name");
                }
            }
            
            String selNewEvent = "SELECT event_name, available_seats FROM events WHERE event_id = ? FOR UPDATE";
            String newEventName = null;
            int availableSeats = 0;
            try (PreparedStatement psNew = con.prepareStatement(selNewEvent)) {
                psNew.setString(1, newEventId);
                try (ResultSet rsNew = psNew.executeQuery()) {
                    if (!rsNew.next()) {
                        throw new EntityNotFoundException("New Event ID not found.");
                    }
                    newEventName = rsNew.getString("event_name");
                    availableSeats = rsNew.getInt("available_seats");
                }
            }
            
            if (availableSeats <= 0) {
                throw new NoSeatsAvailableException("No seats available for event " + newEventId);
            }
            
            String checkDup = "SELECT * FROM registrations WHERE student_id = ? AND event_id = ?";
            try (PreparedStatement psDup = con.prepareStatement(checkDup)) {
                psDup.setString(1, studentId);
                psDup.setString(2, newEventId);
                try (ResultSet rsDup = psDup.executeQuery()) {
                    if (rsDup.next()) {
                        throw new DuplicateEntityException("Duplicate registration.");
                    }
                }
            }
            
            try (PreparedStatement psUpdOld = con.prepareStatement("UPDATE events SET available_seats = available_seats + 1 WHERE event_id = ?")) {
                psUpdOld.setString(1, oldEventId);
                psUpdOld.executeUpdate();
            }
            
            try (PreparedStatement psUpdNew = con.prepareStatement("UPDATE events SET available_seats = available_seats - 1 WHERE event_id = ?")) {
                psUpdNew.setString(1, newEventId);
                psUpdNew.executeUpdate();
            }
            
            try (PreparedStatement psUpdReg = con.prepareStatement("UPDATE registrations SET event_id = ? WHERE reg_id = ?")) {
                psUpdReg.setString(1, newEventId);
                psUpdReg.setString(2, regId);
                psUpdReg.executeUpdate();
            }
            
            con.commit();
            return new String[]{oldEventName, newEventName};
            
        } catch (SQLException | EntityNotFoundException | NoSeatsAvailableException | DuplicateEntityException e) {
            if (con != null) {
                try { con.rollback(); } catch(SQLException ex) {}
            }
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch(SQLException ex) {}
            }
        }
    }

    public void deleteRegistration(String regId) throws SQLException, EntityNotFoundException {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            
            String selReg = "SELECT * FROM registrations WHERE reg_id = ? FOR UPDATE";
            String eventId = null;
            try (PreparedStatement psSel = con.prepareStatement(selReg)) {
                psSel.setString(1, regId);
                try (ResultSet rsSel = psSel.executeQuery()) {
                    if (!rsSel.next()) {
                        throw new EntityNotFoundException("Registration ID not found.");
                    }
                    eventId = rsSel.getString("event_id");
                }
            }
            
            try (PreparedStatement psDel = con.prepareStatement("DELETE FROM registrations WHERE reg_id = ?")) {
                psDel.setString(1, regId);
                psDel.executeUpdate();
            }
            
            try (PreparedStatement psUpdOld = con.prepareStatement("UPDATE events SET available_seats = available_seats + 1 WHERE event_id = ?")) {
                psUpdOld.setString(1, eventId);
                psUpdOld.executeUpdate();
            }
            
            con.commit();
        } catch (SQLException | EntityNotFoundException e) {
            if (con != null) {
                try { con.rollback(); } catch(SQLException ex) {}
            }
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch(SQLException ex) {}
            }
        }
    }
}
