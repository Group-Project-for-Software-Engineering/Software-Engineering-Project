/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: User.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: February 2026
 * The User class represents a general system user in the VCRTS system. It stores authentication-related information including username,
 * password, and email. This class acts as a base class for more specific user types such as Owner and Client.
 */
package classes;

import java.util.ArrayList;

// ---------------------------------------------------------------
//this class represents a user of the system, with a username and password. It is used by UserManager to store user data in memory and persist it to a file. The Owner class extends User to add additional fields for car owners.
public class User {

    private static int increment = 1000;
    private String userId;
    private String username;
    private String password;
    private String email;

    private String userType;

    private ArrayList<String> rejected = new ArrayList<>();
    private ArrayList<String> accepted = new ArrayList<>();

    // ---------------------------------------------------------------
    // constructor that sets the username, password, email, and user type
    public User(String username, String password, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        increment++;
        this.userId = Integer.toString(increment);

        this.userType = userType;

    }

    //this overloaded constructor is for reading users from the text file. They already have assigned user ids
    public User(String username, String password, String userId, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        increment++;
        this.userType = userType;
        this.userId = userId;
    }

    // ---------------------------------------------------------------
    // returns the username of the user
    public String getUsername() {
        return username;
    }

    // ---------------------------------------------------------------
    // returns the password of the user
    public String getPassword() {
        return password;
    }

    // ---------------------------------------------------------------
    // returns the email of the user
    public String getEmail() {
        return email;
    }

    // ---------------------------------------------------------------
    // returns the user ID of the user
    public String getUserId() {
        return userId;
    }

    // ---------------------------------------------------------------
    // returns the user type of the user
    public String getUserType() {
        return userType;
    }

    // ---------------------------------------------------------------
    // returns the list of rejected vehicles/jobs
    public ArrayList<String> getRejectedList() {
        return rejected;
    }

    // ---------------------------------------------------------------
    // returns the list of accepted vehicles/jobs
    public ArrayList<String> getAcceptedList() {
        return accepted;
    }

    // ---------------------------------------------------------------
    // clears the list of rejected vehicles/jobs
    public void clearRejectedList() {
        rejected.clear();
    }

    // ---------------------------------------------------------------
    // clears the list of accepted vehicles/jobs
    public void clearAcceptedList() {
        accepted.clear();
    }

    // ---------------------------------------------------------------
    // adds an accepted job/vehicle to the list
    public void addAccepted(String s) {
        accepted.add(s);
    }

    // ---------------------------------------------------------------
    // adds a rejected job/vehicle to the list
    public void addRejected(String s) {
        rejected.add(s);
    }
}
