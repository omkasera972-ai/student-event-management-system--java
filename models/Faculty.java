package models;

public class Faculty extends Person {

    private String department;

    public Faculty(String name, String id, String email, String department) {
        super(name, id, email);
        this.department = department;
    }

    @Override
    public void displayDetails() {
        System.out.println("Faculty ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Email      : " + email);
        System.out.println("Department : " + department);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}