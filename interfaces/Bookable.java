package interfaces;

import models.Student;
import exceptions.NoSeatsAvailableException;

public interface Bookable {

    void bookSeat(Student s) throws NoSeatsAvailableException;

    void cancelSeat(Student s);
}