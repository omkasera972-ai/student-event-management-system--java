package models;

import java.util.LinkedList;

public class Student extends Person {

    private String branch;
    private LinkedList<Event> registeredEvents;

    public Student(String name, String id, String email, String branch) {
        super(name, id, email);
        this.branch = branch;
        this.registeredEvents = new LinkedList<>();
    }

    @Override
    public void displayDetails() {
        System.out.println("Student ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Email      : " + email);
        System.out.println("Branch     : " + branch);
    }

    public String getBranch() {
        return branch;
    }

    public LinkedList<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}