/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: Admin.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, and Sebastian Villavicencio)
 * Date: February 2026
 * The Admin class represents an administrator in the VCRTS system who manages the system and approves or rejects jobs and vehicles.
 */
package classes;

import java.util.ArrayList;

public class Admin extends User {
    // private String adminId;
    // private static int increment = 0;
    // private static Map<User, ArrayList<Vehicle>> pendingVehicles= new HashMap<>(); //stores vehicles yet to be approved
    // private static Map<User, ArrayList<Job>> pendingJobs = new HashMap<>(); //stores jobs yet to be approved

    private static ArrayList<Vehicle> availableVehicles = new ArrayList<>();
    private static ArrayList<Job> jobs = new ArrayList<>();
    //the jobs being stored are ones that have been approved by the controller with the pending form.

    //--------------------------------------------
    // constructor for admin class => uses constructor of User class
    // sets username and password
    public Admin(String username, String password) {
        this(username, password, "");
    }

    //--------------------------------------------
    // overloaded constructor 
    // email is not a required field for admin
    public Admin(String username, String password, String email) {
        super(username, password, email, "Admin");
        // increment++;
        // adminId = Integer.toString(increment);

    }

    //--------------------------------------------
    // overloaded constructor 
    // tracks userId field for admin
    public Admin(String username, String password, String email, String userId) {
        super(username, password, userId, email, "Admin");
    }

    //--------------------------------------------
    // @return the list of vehicles that can be assigned jobs
    public ArrayList<Vehicle> getAvailableVehicles() {
        return availableVehicles;
    }

    //--------------------------------------------
    // adds to the list of current usable vehicles
    public static void addVehicle(Vehicle v) {
        if (!availableVehicles.contains(v)) {
            availableVehicles.add(v);
        }
        System.out.println(availableVehicles);
    }

    //--------------------------------------------
    // removes a vehicle from the list of current available vehicles
    public static void removeVehicle(Vehicle v) {
        availableVehicles.remove(v);
        System.out.println(availableVehicles);
    }

    //--------------------------------------------
    // adds a job to the list of jobs
    public static void addJob(Job j) {
        jobs.add(j);
        j.setStatusPending();
    }

    //--------------------------------------------
    // @return the list of jobs
    public static ArrayList<Job> getJobs() {
        return jobs;
    }

    public static void removeJob(Job j) {
        jobs.remove(j);
    }

    //--------------------------------------------
    /**
     * caculates completion time for a list of jobs <br>
     * FIFO (First In, First Out) structure <br>
     * Jobs are added into the arraylist in the order they are submitted
     */
    public static ArrayList<Double> computeCompletionTimes() { // returns arraylist of completed times for all jobs in the order they were submitted
        ArrayList<Double> completionTimes = new ArrayList<>();
        double totalTime = 0;

        // iterates through all jobs 
        for (Job j : jobs) {
            // adds the current job's duration to the total time
            totalTime += j.getApproximateJobDuration();
            // stores the total time for a specific job
            completionTimes.add(totalTime);
        }
        // returns the arraylist of all completion times for all jobs 
        return completionTimes;
    }
    //--------------------

}