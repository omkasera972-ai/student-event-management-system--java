import services.StudentService;
import services.EventService;
import services.RegistrationService;
import services.FacultyService;
import services.ReportService;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.InputMismatchException;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static StudentService studentService = new StudentService();
    private static EventService eventService = new EventService();
    private static RegistrationService registrationService = new RegistrationService();
    private static FacultyService facultyService = new FacultyService();
    private static ReportService reportService = new ReportService();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n============================================================");
            System.out.println("                         SCNEMS");
            System.out.println("          Smart Campus Network Event Management System");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("1. Student Management");
            System.out.println("2. Event Management");
            System.out.println("3. Registration Management");
            System.out.println("4. Faculty Management");
            System.out.println("5. Reports");
            System.out.println("6. Exit");
            System.out.println();
            System.out.println("============================================================");
            System.out.print("Enter Choice: ");

            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // clear buffer
                continue;
            }

            switch (choice) {
                case 1: studentMenu(); break;
                case 2: eventMenu(); break;
                case 3: registrationMenu(); break;
                case 4: facultyMenu(); break;
                case 5: reportsMenu(); break;
                case 6:
                    System.out.println("SCNEMS Closed.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    private static void studentMenu() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. Show Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter Choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            
            if (choice == 5) return;
            
            switch (choice) {
                case 1:
                    System.out.println("\n==================================================");
                    System.out.println("                ADD NEW STUDENT");
                    System.out.println("==================================================");
                    System.out.println();
                    System.out.print("Name       : "); String addName = sc.nextLine();
                    System.out.print("Branch     : "); String addBranch = sc.nextLine();
                    System.out.print("Email      : "); String addEmail = sc.nextLine();
                    System.out.println();
                    String generatedStudentId = studentService.addStudent(addName, addBranch, addEmail);
                    if (generatedStudentId != null) {
                        System.out.println("Generated Student ID : " + generatedStudentId);
                        System.out.println("Student added successfully.");
                    }
                    System.out.println("==================================================");
                    break;
                case 2:
                    List<models.Student> students = studentService.getAllStudents();
                    System.out.println("\n============================================================");
                    System.out.println("                    ALL STUDENTS");
                    System.out.println("============================================================");
                    System.out.println();
                    System.out.printf("%-15s %-18s %-12s %s\n", "Student ID", "Name", "Branch", "Email");
                    System.out.println("------------------------------------------------------------");
                    for (models.Student st : students) {
                        System.out.printf("%-15s %-18s %-12s %s\n", st.getId(), st.getName(), st.getBranch(), st.getEmail());
                    }
                    System.out.println();
                    System.out.println("============================================================");
                    System.out.println("Total Students : " + students.size());
                    System.out.println("============================================================");
                    break;
                case 3:
                    System.out.print("Enter Student ID: ");
                    String updateId = sc.nextLine();
                    models.Student currStudent = studentService.getStudentById(updateId);
                    if (currStudent == null) {
                        System.out.println("Error: Student ID Not Found!");
                        break;
                    }
                    System.out.println("\nCurrent Student Details:");
                    System.out.println("Student ID : " + currStudent.getId());
                    System.out.println("Name       : " + currStudent.getName());
                    System.out.println("Branch     : " + currStudent.getBranch());
                    System.out.println("Email      : " + currStudent.getEmail());
                    System.out.println();
                    System.out.print("Enter New Name   : "); String newName = sc.nextLine();
                    System.out.print("Enter New Branch : "); String newBranch = sc.nextLine();
                    System.out.print("Enter New Email  : "); String newEmail = sc.nextLine();

                    if (newName.trim().isEmpty()) newName = currStudent.getName();
                    if (newBranch.trim().isEmpty()) newBranch = currStudent.getBranch();
                    if (newEmail.trim().isEmpty()) newEmail = currStudent.getEmail();

                    studentService.updateStudent(updateId, newName, newBranch, newEmail);
                    break;
                case 4:
                    System.out.print("Enter Student ID: ");
                    String deleteId = sc.nextLine();
                    models.Student delStudent = studentService.getStudentById(deleteId);
                    if (delStudent == null) {
                        System.out.println("Error: Student ID Not Found!");
                        break;
                    }
                    System.out.println();
                    System.out.print("Are you sure you want to delete this student? (Y/N): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        studentService.deleteStudent(deleteId);
                    }
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    private static void eventMenu() {
        while (true) {
            System.out.println("\n--- Event Management ---");
            System.out.println("1. Add Event");
            System.out.println("2. Show Events");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter Choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            
            if (choice == 5) return;
            
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Event Name: "); String eventName = sc.nextLine();
                        System.out.print("Venue: "); String venue = sc.nextLine();
                        System.out.print("Total Seats: "); int totalSeats = sc.nextInt();
                        System.out.print("Available Seats: "); int availableSeats = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Event Date (YYYY-MM-DD): "); String eventDate = sc.nextLine();
                        System.out.print("Start Time (HH:MM AM/PM): "); String startTime = sc.nextLine();
                        System.out.print("End Time (HH:MM AM/PM): "); String endTime = sc.nextLine();
                        String generatedEventId = eventService.addEvent(eventName, venue, totalSeats, availableSeats, eventDate, startTime, endTime);
                        if (generatedEventId != null) {
                            System.out.println("Generated Event ID : " + generatedEventId);
                        }
                        break;
                    case 2:
                        eventService.showEvents();
                        break;
                    case 3:
                        System.out.print("Event ID: "); String updateEventId = sc.nextLine();
                        System.out.print("New Event Name: "); String newEventName = sc.nextLine();
                        System.out.print("New Venue: "); String newVenue = sc.nextLine();
                        System.out.print("New Total Seats: "); int newTotalSeats = sc.nextInt();
                        System.out.print("New Available Seats: "); int newAvailableSeats = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New Event Date (YYYY-MM-DD): "); String newEventDate = sc.nextLine();
                        System.out.print("New Start Time (HH:MM AM/PM): "); String newStartTime = sc.nextLine();
                        System.out.print("New End Time (HH:MM AM/PM): "); String newEndTime = sc.nextLine();
                        eventService.updateEvent(updateEventId, newEventName, newVenue, newTotalSeats, newAvailableSeats, newEventDate, newStartTime, newEndTime);
                        break;
                    case 4:
                        System.out.print("Event ID: "); String deleteEventId = sc.nextLine();
                        models.Event eventToDelete = eventService.getEventById(deleteEventId);
                        if (eventToDelete == null) {
                            System.out.println("Error: Event ID Not Found!");
                        } else {
                            System.out.println("Event ID   : " + eventToDelete.getEventId());
                            System.out.println("Event Name : " + eventToDelete.getEventName());
                            System.out.print("Are you sure you want to delete this event? (Y/N): ");
                            String confirm = sc.nextLine();
                            if (confirm.equalsIgnoreCase("Y")) {
                                eventService.deleteEvent(deleteEventId, eventToDelete.getEventName());
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for numeric field.");
                sc.nextLine();
            }
        }
    }

    private static void printExistingRegistrationsFlat() {
        java.util.List<dao.RegistrationDAO.RegistrationDetail> list = registrationService.getAllRegistrationsFlat();
        System.out.println("\n============================================================");
        System.out.println("                EXISTING REGISTRATIONS");
        System.out.println("============================================================");
        System.out.printf("%-10s %-15s %-18s %-13s %s\n", "Reg ID", "Student ID", "Student Name", "Event ID", "Event Name");
        System.out.println("----------------------------------------------------------------------");
        for (dao.RegistrationDAO.RegistrationDetail rd : list) {
            System.out.printf("%-10s %-15s %-18s %-13s %s\n", rd.regId, rd.studentId, rd.studentName, rd.eventId, rd.eventName);
        }
        System.out.println("============================================================");
        System.out.println();
    }

    private static void registrationMenu() {
        while (true) {
            System.out.println("\n--- Registration Management ---");
            System.out.println("1. Add Registration");
            System.out.println("2. Show Registrations");
            System.out.println("3. Update Registration");
            System.out.println("4. Delete Registration");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter Choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            
            if (choice == 5) return;
            
            try {
                switch (choice) {
                                        case 1:
                        List<String[]> events = eventService.getAvailableEventsForRegistration();
                        System.out.println("\n========================================================================================================");
                        System.out.println("              AVAILABLE EVENTS");
                        System.out.println("========================================================================================================");
                        System.out.println();
                        System.out.printf("%-15s %-23s %-14s %-15s %-12s %-25s\n", "Event ID", "Event Name", "Venue", "Available Seats", "Date", "Time");
                        System.out.println("--------------------------------------------------------------------------------------------------------");
                        for (String[] ev : events) {
                            System.out.printf("%-15s %-23s %-14s %-15s %-12s %-25s\n", ev[0], ev[1], ev[2], ev[3], ev[4], ev[5]);
                        }
                        System.out.println("========================================================================================================");
                        System.out.println();
                        System.out.print("Enter Student ID: "); String regStudentId = sc.nextLine();
                        System.out.print("Enter Event ID: "); String regEventId = sc.nextLine();
                        try {
                            String[] details = registrationService.addRegistration(regStudentId, regEventId);
                            String generatedRegId = details[0];
                            String studentName = details[1];
                            String eventName = details[2];
                            String eventDateStr = details[3];
                            String eventTimeStr = details[4];
                            
                            System.out.println("\n============================================================");
                            System.out.println("             REGISTRATION SUCCESSFUL");
                            System.out.println("============================================================");
                            System.out.println("Generated Registration ID : " + generatedRegId);

                            java.time.LocalDateTime now = java.time.LocalDateTime.now();
                            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");

                            System.out.println("==================================================");
                            System.out.println("           REGISTRATION SUCCESSFUL");
                            System.out.println("==================================================");
                            System.out.println();
                            System.out.println("Student ID        : " + regStudentId);
                            System.out.println("Student Name      : " + studentName);
                            System.out.println();
                            System.out.println("Event ID          : " + regEventId);
                            System.out.println("Event Name        : " + eventName);
                            System.out.println("Event Date        : " + eventDateStr);
                            System.out.println("Event Time        : " + eventTimeStr);
                            System.out.println();
                            System.out.println("Registration Date : " + now.format(dateFormatter));
                            System.out.println("Registration Time : " + now.format(timeFormatter));
                            System.out.println();
                            System.out.println("Status            : Registered Successfully");
                            System.out.println("==================================================");
                        } catch (exceptions.EntityNotFoundException | exceptions.DuplicateEntityException e) {
                            System.out.println(e.getMessage());
                        } catch (exceptions.NoSeatsAvailableException e) {
                            System.out.println("No seats available for this event.");
                        } catch (java.sql.SQLException e) {
                            System.out.println("Database error during registration: " + e.getMessage());
                        }
                        break;
                    case 2:
                        java.util.List<dao.RegistrationDAO.EventRegistrationGroup> groups = registrationService.getGroupedRegistrations();
                        System.out.println();
                        System.out.println("================================================================================================================================");
                        System.out.println("                                      ALL EVENT REGISTRATIONS");
                        System.out.println("================================================================================================================================");
                        int totalEvents = groups.size();
                        int totalRegistrations = 0;
                        System.out.printf("%-10s %-21s %-8s %-11s %-20s %-8s %-13s %-18s %s\n", "Event ID", "Event Name", "Venue", "Date", "Time", "Reg ID", "Student ID", "Student Name", "Branch");
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                        for (dao.RegistrationDAO.EventRegistrationGroup g : groups) {
                            String formattedDate = g.eventDate;
                            if (formattedDate != null && formattedDate.length() > 0) {
                                String[] parts = formattedDate.split("-");
                                if(parts.length == 3) {
                                    formattedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                                }
                            }
                            
                            if (g.students.isEmpty()) {
                                System.out.printf("%-10s %-21s %-8s %-11s %-20s %-8s %-13s %-18s %s\n", g.eventId, g.eventName.length() > 20 ? g.eventName.substring(0, 18) + ".." : g.eventName, g.venue.length() > 7 ? g.venue.substring(0, 6) + "." : g.venue, formattedDate, g.eventTime, "-", "-", "No Registration", "-");
                            } else {
                                for (dao.RegistrationDAO.StudentRegistration sr : g.students) {
                                    System.out.printf("%-10s %-21s %-8s %-11s %-20s %-8s %-13s %-18s %s\n", g.eventId, g.eventName.length() > 20 ? g.eventName.substring(0, 18) + ".." : g.eventName, g.venue.length() > 7 ? g.venue.substring(0, 6) + "." : g.venue, formattedDate, g.eventTime, sr.regId, sr.studentId, sr.studentName.length() > 17 ? sr.studentName.substring(0, 15) + ".." : sr.studentName, sr.branch);
                                    totalRegistrations++;
                                }
                            }
                        }
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("Total Events        : " + totalEvents);
                        System.out.println("Total Registrations : " + totalRegistrations);
                        System.out.println("================================================================================================================================");
                        break;
                    case 3:
                        printExistingRegistrationsFlat();
                        System.out.print("Enter Registration ID to Update: ");
                        System.out.print("Enter Registration ID: "); String updateRegId = sc.nextLine();
                        dao.RegistrationDAO.RegistrationDetail currReg = registrationService.getRegistrationDetails(updateRegId);
                        if (currReg == null) {
                            System.out.println("Error: Registration ID not found.");
                            break;
                        }
                        
                        System.out.println("\nCurrent Registration:");
                        System.out.println("Student ID : " + currReg.studentId);
                        System.out.println("Student Name : " + currReg.studentName);
                        System.out.println("Event ID : " + currReg.eventId);
                        System.out.println("Event Name : " + currReg.eventName);
                        System.out.println();
                        
                        System.out.println("Available Events:");
                        System.out.println("------------------------------------------------------------");
                        System.out.printf("%-15s %-23s %s\n", "Event ID", "Event Name", "Venue");
                        System.out.println("------------------------------------------------------------");
                        List<String[]> availEvents = eventService.getAvailableEventsForRegistration();
                        for (String[] ev : availEvents) {
                            System.out.printf("%-15s %-23s %s\n", ev[0], ev[1], ev[2]);
                        }
                        System.out.println("------------------------------------------------------------");
                        System.out.println();
                        
                        System.out.print("Enter New Event ID: ");
                        String newEventId = sc.nextLine();
                        
                        try {
                            String[] res = registrationService.updateRegistration(updateRegId, newEventId);
                            if (res == null) {
                                System.out.println("No changes made. The student is already registered for this event.");
                            } else {
                                System.out.println("\n============================================================");
                                System.out.println("          REGISTRATION UPDATED SUCCESSFULLY");
                                System.out.println("============================================================");
                                System.out.println("Registration ID : " + updateRegId);
                                System.out.println("Student         : " + currReg.studentName);
                                System.out.println("Old Event       : " + res[0]);
                                System.out.println("New Event       : " + res[1]);
                                System.out.println("============================================================");
                            }
                        } catch (exceptions.EntityNotFoundException | exceptions.DuplicateEntityException | exceptions.NoSeatsAvailableException e) {
                            System.out.println("Error: " + e.getMessage());
                        } catch (java.sql.SQLException e) {
                            System.out.println("Database error during update.");
                        }
                        break;
                    case 4:
                        printExistingRegistrationsFlat();
                        System.out.print("Enter Registration ID to Delete: ");
                        System.out.print("Enter Registration ID: "); String deleteRegId = sc.nextLine();
                        
                        dao.RegistrationDAO.RegistrationDetail delReg = registrationService.getRegistrationDetails(deleteRegId);
                        if (delReg == null) {
                            System.out.println("Error: Registration ID not found.");
                            break;
                        }
                        
                        System.out.println("\nRegistration Details:");
                        System.out.println("Student : " + delReg.studentName);
                        System.out.println("Event   : " + delReg.eventName);
                        System.out.println();
                        
                        System.out.print("Are you sure you want to delete this registration? (Y/N): ");
                        String confirm = sc.nextLine();
                        if (confirm.equalsIgnoreCase("Y")) {
                            try {
                                registrationService.deleteRegistration(deleteRegId);
                                System.out.println("\n============================================================");
                                System.out.println("          REGISTRATION DELETED SUCCESSFULLY");
                                System.out.println("============================================================");
                                System.out.println("Registration ID : " + deleteRegId);
                                System.out.println("Student         : " + delReg.studentName);
                                System.out.println("Event            : " + delReg.eventName);
                                System.out.println("Available Seats  : Updated");
                                System.out.println("============================================================");
                            } catch (exceptions.EntityNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            } catch (java.sql.SQLException e) {
                                System.out.println("Database error during deletion.");
                            }
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for numeric field.");
                sc.nextLine();
            }
        }
    }

    private static void printFacultyTable(String title) {
        java.util.List<String[]> facultyList = facultyService.getAllFaculty();
        System.out.println("\n============================================================");
        int spaces = (60 - title.length()) / 2;
        for (int i = 0; i < spaces; i++) System.out.print(" ");
        System.out.println(title);
        System.out.println("============================================================");
        System.out.println();
        System.out.printf("%-15s %-21s %s\n", "Faculty ID", "Faculty Name", "Department");
        System.out.println("------------------------------------------------------------");
        for (String[] f : facultyList) {
            System.out.printf("%-15s %-21s %s\n", f[0], f[1], f[2]);
        }
        System.out.println("------------------------------------------------------------");
        if (title.equals("ALL FACULTY")) {
            System.out.println("Total Faculty : " + facultyList.size());
            System.out.println("============================================================");
        }
        System.out.println();
    }

    private static void facultyMenu() {
        while (true) {
            System.out.println("\n--- Faculty Management ---");
            System.out.println("1. Add Faculty");
            System.out.println("2. Show Faculty");
            System.out.println("3. Update Faculty");
            System.out.println("4. Delete Faculty");
            System.out.println("5. Assign Faculty to Event");
            System.out.println("6. Show Event-wise Faculty");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter Choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                sc.nextLine();
                continue;
            }
            
            if (choice == 7) return;
            
            switch (choice) {
                case 1:
                    System.out.print("Name: "); String name = sc.nextLine();
                    System.out.print("Department: "); String dept = sc.nextLine();
                    System.out.print("Email: "); String email = sc.nextLine();
                    try {
                        String generatedFacultyId = facultyService.addFaculty(name, dept, email);
                        if (generatedFacultyId != null) {
                            System.out.println("\n============================================================");
                            System.out.println("              FACULTY ADDED SUCCESSFULLY");
                            System.out.println("============================================================");
                            System.out.println("Generated Faculty ID : " + generatedFacultyId);
                            System.out.println("Name        : " + name);
                            System.out.println("Department  : " + dept);
                            System.out.println("============================================================");
                        }
                    } catch (java.sql.SQLException e) {
                        if (e.getErrorCode() == 1062) {
                            System.out.println("Error: Faculty ID already exists.");
                        } else {
                            System.out.println("Database error: Could not add faculty.");
                        }
                    }
                    break;
                case 2:
                    printFacultyTable("ALL FACULTY");
                    break;
                case 3:
                    printFacultyTable("EXISTING FACULTY");
                    System.out.print("Enter Faculty ID to Update: "); String updateId = sc.nextLine();
                    String[] currFac = facultyService.getFacultyById(updateId);
                    if (currFac == null) {
                        System.out.println("Error: Faculty ID not found.");
                        break;
                    }
                    System.out.println("\n============================================================");
                    System.out.println("              CURRENT FACULTY DETAILS");
                    System.out.println("============================================================");
                    System.out.println("Faculty ID  : " + currFac[0]);
                    System.out.println("Name        : " + currFac[1]);
                    System.out.println("Department  : " + currFac[2]);
                    System.out.println("============================================================");
                    System.out.println();
                    
                    System.out.print("Enter New Name: "); String newName = sc.nextLine();
                    System.out.print("Enter New Department: "); String newDept = sc.nextLine();
                    System.out.print("Enter New Email: "); String newEmail = sc.nextLine();
                    
                    try {
                        facultyService.updateFaculty(updateId, newName, newDept, newEmail);
                        System.out.println("\n============================================================");
                        System.out.println("             FACULTY UPDATED SUCCESSFULLY");
                        System.out.println("============================================================");
                        System.out.println("Faculty ID  : " + updateId);
                        System.out.println("Name        : " + newName);
                        System.out.println("Department  : " + newDept);
                        System.out.println("============================================================");
                    } catch (java.sql.SQLException e) {
                        System.out.println("Database error during update.");
                    }
                    break;
                case 4:
                    printFacultyTable("EXISTING FACULTY");
                    System.out.print("Enter Faculty ID to Delete: "); String deleteId = sc.nextLine();
                    String[] delFac = facultyService.getFacultyById(deleteId);
                    if (delFac == null) {
                        System.out.println("Error: Faculty ID not found.");
                        break;
                    }
                    System.out.println("\n============================================================");
                    System.out.println("              FACULTY TO BE DELETED");
                    System.out.println("============================================================");
                    System.out.println("Faculty ID  : " + delFac[0]);
                    System.out.println("Name        : " + delFac[1]);
                    System.out.println("Department  : " + delFac[2]);
                    System.out.println("============================================================");
                    System.out.println();
                    
                    System.out.print("Are you sure you want to delete this faculty? (Y/N): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        try {
                            facultyService.deleteFaculty(deleteId);
                            System.out.println("\n============================================================");
                            System.out.println("             FACULTY DELETED SUCCESSFULLY");
                            System.out.println("============================================================");
                            System.out.println("Faculty ID  : " + delFac[0]);
                            System.out.println("Name        : " + delFac[1]);
                            System.out.println("Department  : " + delFac[2]);
                            System.out.println("============================================================");
                        } catch (java.sql.SQLException e) {
                            System.out.println("Database error during deletion.");
                        }
                    }
                    break;
                case 5:
                    System.out.println("\n============================================================");
                    System.out.println("                 ASSIGN FACULTY TO EVENT");
                    System.out.println("============================================================");
                    System.out.println();
                    System.out.println("Which Event do you want to assign a Faculty to?");
                    
                    System.out.println("\n============================================================");
                    System.out.println("                    AVAILABLE EVENTS");
                    System.out.println("============================================================");
                    System.out.println();
                    System.out.printf("%-15s %-23s %s\n", "Event ID", "Event Name", "Venue");
                    System.out.println("------------------------------------------------------------");
                    List<String[]> availEventsFac = eventService.getAvailableEventsForRegistration();
                    for (String[] ev : availEventsFac) {
                        System.out.printf("%-15s %-23s %s\n", ev[0], ev[1], ev[2]);
                    }
                    System.out.println("------------------------------------------------------------");
                    System.out.println();
                    
                    System.out.print("Enter Event ID: ");
                    String assignEventId = sc.nextLine();
                    
                    System.out.println("\nWhich Faculty do you want to assign to this Event?");
                    printFacultyTable("AVAILABLE FACULTY");
                    
                    System.out.print("Enter Faculty ID: ");
                    String assignFacId = sc.nextLine();
                    
                    try {
                        String[] assignment = facultyService.assignFacultyToEvent(assignFacId, assignEventId);
                        System.out.println("\n============================================================");
                        System.out.println("             FACULTY ASSIGNED SUCCESSFULLY");
                        System.out.println("============================================================");
                        System.out.println("Event ID      : " + assignEventId);
                        System.out.println("Event Name    : " + assignment[0]);
                        System.out.println("Faculty ID    : " + assignFacId);
                        System.out.println("Faculty Name  : " + assignment[1]);
                        System.out.println("Department    : " + assignment[2]);
                        System.out.println("============================================================");
                    } catch (exceptions.EntityNotFoundException | exceptions.DuplicateEntityException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (java.sql.SQLException e) {
                        System.out.println("Database error during assignment.");
                    }
                    break;
                case 6:
                    java.util.List<String[]> assignedList = facultyService.getEventWiseFaculty();
                    System.out.println("\n============================================================");
                    System.out.println("                 EVENT-WISE FACULTY");
                    System.out.println("============================================================");
                    System.out.println();
                    System.out.printf("%-15s %-23s %-15s %s\n", "Event ID", "Event Name", "Faculty ID", "Faculty Name");
                    System.out.println("----------------------------------------------------------------------");
                    for (String[] row : assignedList) {
                        System.out.printf("%-15s %-23s %-15s %s\n", row[0], row[1], row[2], row[3]);
                    }
                    System.out.println("----------------------------------------------------------------------");
                    System.out.println("Total Assigned Events : " + assignedList.size());
                    System.out.println("============================================================");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
    private static void reportsMenu() {
        while (true) {
            System.out.println("\n============================================================");
            System.out.println("                       REPORTS");
            System.out.println("============================================================");
            System.out.println("1. Overall Summary");
            System.out.println("2. Student Report");
            System.out.println("3. Event Report");
            System.out.println("4. Registration Report");
            System.out.println("5. Seat Availability Report");
            System.out.println("6. Faculty Assignment Report");
            System.out.println("7. Most Registered Event");
            System.out.println("8. Complete Report");
            System.out.println("9. Back to Main Menu");
            System.out.println("============================================================");
            System.out.print("Enter Choice: ");

            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            if (choice == 9) return;

            switch (choice) {
                case 1: showOverallSummary(true); break;
                case 2: showStudentReport(true); break;
                case 3: showEventReport(true); break;
                case 4: showRegistrationReport(true); break;
                case 5: showSeatAvailabilityReport(true); break;
                case 6: showFacultyAssignmentReport(true); break;
                case 7: showMostRegisteredEvent(true); break;
                case 8: showCompleteReport(); break;
                default: System.out.println("Invalid Choice!");
            }
        }
    }

    private static void showOverallSummary(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("1. OVERALL SUMMARY");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                    OVERALL SUMMARY");
        System.out.println("============================================================");
        Map<String, Integer> summary = reportService.getOverallSummary();
        if (summary == null || summary.isEmpty()) {
            System.out.println("No data available.");
            return;
        }
        System.out.println();
        System.out.println("Total Students          : " + summary.get("Total Students"));
        System.out.println("Total Events            : " + summary.get("Total Events"));
        System.out.println("Total Registrations     : " + summary.get("Total Registrations"));
        System.out.println("Total Faculty           : " + summary.get("Total Faculty"));
        System.out.println("Assigned Faculty        : " + summary.get("Assigned Faculty"));
        System.out.println("Unassigned Events       : " + summary.get("Unassigned Events"));
        System.out.println("Total Available Seats   : " + summary.get("Total Available Seats"));
        System.out.println("Total Occupied Seats    : " + summary.get("Total Occupied Seats"));
        System.out.println("============================================================");
    }

    private static void showStudentReport(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("2. STUDENT REPORT");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                    STUDENT REPORT");
        System.out.println("============================================================");
        List<String[]> list = reportService.getStudentReport();
        if (list == null || list.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println();
        System.out.printf("%-15s %-21s %-12s %s\n", "Student ID", "Student Name", "Branch", "Email");
        System.out.println("------------------------------------------------------------");
        for (String[] row : list) {
            System.out.printf("%-15s %-21s %-12s %s\n", row[0], row[1], row[2], row[3]);
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("Total Students : " + list.size());
        System.out.println("============================================================");
    }

    private static void showEventReport(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("3. EVENT REPORT");
            System.out.println("============================================================");
        }
        System.out.println("\n==========================================================================================================================");
        System.out.println("                                      EVENT REPORT");
        System.out.println("==========================================================================================================================");
        List<String[]> list = reportService.getEventReport();
        if (list == null || list.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        System.out.println();
        System.out.printf("%-15s %-23s %-11s %-12s %-25s %-11s %s\n", "Event ID", "Event Name", "Venue", "Date", "Time", "Total Seats", "Available");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        for (String[] row : list) {
            System.out.printf("%-15s %-23s %-11s %-12s %-25s %-11s %s\n", row[0], row[1], row[2], row[5], row[6], row[3], row[4]);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Total Events : " + list.size());
        System.out.println("==========================================================================================================================");
    }

    private static void showRegistrationReport(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("4. REGISTRATION REPORT");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                  REGISTRATION REPORT");
        System.out.println("============================================================");
        List<String[]> list = reportService.getRegistrationReport();
        if (list == null || list.isEmpty()) {
            System.out.println("No registrations found.");
            System.out.println("============================================================");
            return;
        }
        System.out.println();
        System.out.printf("%-10s %-15s %-18s %-13s %s\n", "Reg ID", "Student ID", "Student Name", "Event ID", "Event Name");
        System.out.println("----------------------------------------------------------------------");
        for (String[] row : list) {
            System.out.printf("%-10s %-15s %-18s %-13s %s\n", row[0], row[1], row[2], row[3], row[4]);
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Total Registrations : " + list.size());
        System.out.println("============================================================");
    }

    private static void showSeatAvailabilityReport(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("5. SEAT AVAILABILITY REPORT");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                  SEAT AVAILABILITY");
        System.out.println("============================================================");
        List<String[]> list = reportService.getSeatAvailabilityReport();
        if (list == null || list.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        System.out.println();
        System.out.printf("%-15s %-23s %-8s %-9s %s\n", "Event ID", "Event Name", "Total", "Booked", "Available");
        System.out.println("---------------------------------------------------------------------");
        int totalSeats = 0;
        int totalBooked = 0;
        int totalAvailable = 0;
        for (String[] row : list) {
            System.out.printf("%-15s %-23s %-8s %-9s %s\n", row[0], row[1], row[2], row[3], row[4]);
            totalSeats += Integer.parseInt(row[2]);
            totalBooked += Integer.parseInt(row[3]);
            totalAvailable += Integer.parseInt(row[4]);
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.printf("%-39s %-8d %-9d %d\n", "TOTAL", totalSeats, totalBooked, totalAvailable);
        System.out.println("============================================================");
    }

    private static void showFacultyAssignmentReport(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("6. FACULTY ASSIGNMENT REPORT");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                 FACULTY ASSIGNMENT REPORT");
        System.out.println("============================================================");
        List<String[]> list = reportService.getFacultyAssignmentReport();
        if (list == null || list.isEmpty()) {
            System.out.println("No faculty assignments found.");
            System.out.println("============================================================");
            return;
        }
        System.out.println();
        System.out.printf("%-15s %-23s %-15s %s\n", "Event ID", "Event Name", "Faculty ID", "Faculty Name");
        System.out.println("--------------------------------------------------------------------");
        int assigned = 0;
        int unassigned = 0;
        for (String[] row : list) {
            System.out.printf("%-15s %-23s %-15s %s\n", row[0], row[1], row[2], row[3]);
            if (row[2].equals("--")) {
                unassigned++;
            } else {
                assigned++;
            }
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println();
        System.out.println("Assigned Events   : " + assigned);
        System.out.println("Unassigned Events : " + unassigned);
        System.out.println("============================================================");
    }

    private static void showMostRegisteredEvent(boolean showHeader) {
        if (showHeader) {
            System.out.println("\n============================================================");
            System.out.println("7. MOST REGISTERED EVENT");
            System.out.println("============================================================");
        }
        System.out.println("\n============================================================");
        System.out.println("                 MOST REGISTERED EVENT");
        System.out.println("============================================================");
        List<String[]> list = reportService.getMostRegisteredEvents();
        if (list == null || list.isEmpty()) {
            System.out.println("No registrations found.");
            System.out.println("============================================================");
            return;
        }
        for (String[] row : list) {
            System.out.println();
            System.out.println("Event ID        : " + row[0]);
            System.out.println("Event Name      : " + row[1]);
            System.out.println("Venue           : " + row[2]);
            System.out.println("Total Seats     : " + row[3]);
            System.out.println("Registered      : " + row[4]);
            System.out.println("Available Seats : " + row[5]);
        }
        System.out.println("============================================================");
    }

    private static void showCompleteReport() {
        System.out.println("\n============================================================");
        System.out.println("8. COMPLETE REPORT");
        System.out.println("============================================================");
        System.out.println("\n============================================================");
        System.out.println("                    COMPLETE SCNEMS REPORT");
        System.out.println("============================================================");

        System.out.println("\n[OVERALL SUMMARY]");
        showOverallSummary(false);

        System.out.println("\n[STUDENT REPORT]");
        showStudentReport(false);

        System.out.println("\n[EVENT REPORT]");
        showEventReport(false);

        System.out.println("\n[REGISTRATION REPORT]");
        showRegistrationReport(false);

        System.out.println("\n[SEAT AVAILABILITY]");
        showSeatAvailabilityReport(false);

        System.out.println("\n[FACULTY ASSIGNMENT]");
        showFacultyAssignmentReport(false);

        System.out.println("\n[MOST REGISTERED EVENT]");
        showMostRegisteredEvent(false);

        System.out.println("\n============================================================");
        System.out.println("              END OF SCNEMS COMPLETE REPORT");
        System.out.println("============================================================");
    }
}