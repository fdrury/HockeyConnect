package ca.hockeyconnect.hockeyconnect;

import java.io.Serializable;

public class Player implements Serializable {
    private String firstName;
    private String lastName;
    private int ID;

    public Player(String firstName, String lastName, int ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getID() {
        return ID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
