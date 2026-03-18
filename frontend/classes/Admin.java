/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: Admin.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, and Sebastian Villavicencio)
 * Date: February 2026
 * The Admin class represents an administrator in the VCRTS system who manages the system and approves or rejects jobs and vehicles.
 */
package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Admin extends User {
    private String adminId;
    private static int increment = 0;
    private static Map<User, ArrayList<Vehicle>> pendingVehicles= new HashMap<>(); //stores vehicles yet to be approved
    private static Map<User, ArrayList<Job>> pendingJobs = new HashMap<>(); //stores jobs yet to be approved

    private static ArrayList<Vehicle> availableVehicles = new ArrayList<>();
    private static ArrayList<Vehicle> jobs = new ArrayList<>();
    //--------------------------------------------

    public Admin(String username, String password) {
        this(username, password, "");
    }
    //--------------------------

    public Admin(String username, String password, String email) {
        super(username, password, email, "Admin");
        increment++;
        adminId = Integer.toString(increment);
        
    }
    //------------------------------

    public static Map<User, ArrayList<Vehicle>> getPendingVehicles() {
        return pendingVehicles;
    }
    //------------------------

    public static Map<User, ArrayList<Job>> getPendingJobs() {
        return pendingJobs;
    }
    //-----------------------

    public String getAdminId() {
        return adminId;
    }
    //--------------------------

    public ArrayList<Vehicle> getAvailableVehicles() { //returns the list of vehicles that can be assigned jobs
        return availableVehicles;
    }

    public static void addVehicle(Vehicle v) { //adds to the list of current usable vehicles
        if(!availableVehicles.contains(v)) {
            availableVehicles.add(v);
        }
        System.out.println(availableVehicles);
    }

    public static void removeVehicle(Vehicle v) {
        availableVehicles.remove(v);
        System.out.println(availableVehicles);
    }

    /* 
    This method adds pending vehicles to the hashmap
    If it's the first time we are seeing the request, it also adds it to the pending requests file
    */

    public static void addPendingVehicle(User u, Vehicle v, boolean addToPendingFile) {
        if(pendingVehicles.containsKey(u)) {
            pendingVehicles.get(u).add(v);
        }
        else {
            ArrayList<Vehicle> list = new ArrayList<>(); 
            list.add(v); 
            pendingVehicles.put(u, list);
        }
        if(addToPendingFile) {//chekcs if this is the first time we are seeing this request
            UserManager.updatePendingFile(u, v);
        }
        

    }
    //------------------------------
    
    /* 
    If admin allows vehicle than add it to that user
    Update the vehicle file 
    And move the request from pending to completed  
    */
    public static void allowVehicle(User u, Vehicle v) { 
        for(int i = 0; i < pendingVehicles.get(u).size(); i++) {
            if(pendingVehicles.get(u).get(i).getNumber().equals(v.getNumber())) {
                ((Owner) u).addVehicle(pendingVehicles.get(u).get(i));
                pendingVehicles.get(u).remove(i);
                
                UserManager.updateVehiclesFile(u);

                try { 
                    UserManager.transactionUpdate(v.getNumber(), true, u.getUsername(), "Owner"); 
                } catch (IOException ex) { 

                }

                break;
            }
        }
        
    }
    //-----------------------

    /* 
    If admin rejects a vehicle than remove it from pending list
    And move the request from pending to completed and set status to rejected 
    */
    public static void rejectVehicle(User u, Vehicle v) { 
        for(int i = 0; i < pendingVehicles.get(u).size(); i++) {
            if(pendingVehicles.get(u).get(i).getNumber().equals(v.getNumber())) {
                pendingVehicles.get(u).remove(i);
                
                //add call to move it from pending to reserved as well
                try { 
                    UserManager.transactionUpdate(v.getNumber(), false, u.getUsername(), "Owner"); 
                } catch (IOException ex) { 

                }

                break;
            }
        }
        
    }
    //----------------------------------

    /* 
    This method adds pending jobs to the hashmap
    If it's the first time we are seeing the request, it also adds it to the pending requests file
    */
    public static void addPendingJob(User u, Job j, boolean addToPendingFile) {
        if(pendingJobs.containsKey(u)) {
            pendingJobs.get(u).add(j);
        }
        else {
            ArrayList<Job> list = new ArrayList<>(); 
            list.add(j); 
            pendingJobs.put(u, list);
        }

        if(addToPendingFile) {
            UserManager.updatePendingFile(u, j);
        }
        

    }
    //----------------------------------

    /* 
    If admin allows a job than add it to that user
    Update the job file 
    And move the request from pending to completed  
    */
    public static void allowJob(User u, Job j) { 
        for(int i = 0; i < pendingJobs.get(u).size(); i++) {
            if (pendingJobs.get(u).get(i).getJobId().equals(j.getJobId())) {
                ((Client) u).addJob(pendingJobs.get(u).get(i));
                pendingJobs.get(u).remove(i);

                UserManager.updateJobFile(u);
                j.setStatusPending();

                try { 
                    UserManager.transactionUpdate(j.getJobId(), true, u.getUsername(), "Client"); 
                } catch (IOException ex) { 

                }

                break;
            }
        }
        
    }
    //--------------------------------------

    /* 
    If admin rejects a job than remove it from pending list
    And move the request from pending to completed and set status to rejected 
    */
    public static void rejectJob(User u, Job j) { 
        for(int i = 0; i < pendingJobs.get(u).size(); i++) {
            if (pendingJobs.get(u).get(i).getJobId().equals(j.getJobId())) {
                pendingJobs.get(u).remove(i);

                try { 
                    UserManager.transactionUpdate(j.getJobId(), false, u.getUsername(), "Client"); 
                } catch (IOException ex) { 

                }
                
                break;
            }
        }
        
    }

    // caculates completion time for a list of jobs
    // FIFO (First In, First Out) structure
    // Jobs are added into the arraylist in the order they are submitted 
    public static ArrayList<Double> computeCompletionTimes(ArrayList<Job> jobs) { // returns arraylist of completed times for all jobs in the order they were submitted
        ArrayList<Double> completionTimes = new ArrayList<>();
        double totalTime = 0;

        // iterates through all jobs 
        for(Job j : jobs) {
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
