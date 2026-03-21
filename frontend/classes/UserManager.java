/*  Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: UserManager.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: February 2026
 * This program manages user registration and login for the VCRTS system by storing user accounts in memory and persisting them to a tab-separated
 * text file (UserInfo/users.txt). Usernames are normalized (trimmed and lowercased) to support consistent lookups.
 */
package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// ---------------------------------------------------------------
/**
 * Stores users in memory and persists them to a tab-separated file.
 * Usernames are normalized for lookup (trimmed, lowercase).
 */

public class UserManager {

    private Map<String, User> users = new HashMap<>(); //username => User
    private final Path USERS_FILE_PATH;
    private static final Path VEHICLES_FILE_PATH = Paths.get("VehicleInfo", "vehicles.txt"); // Currently hardcoded, change if necessary
    private static final Path JOBS_FILE_PATH = Paths.get("JobInfo", "jobs.txt"); // Currently hardcoded, change if necessary

    private static final Path PENDING_TRANSACTIONS_PATH = Paths.get("Transactions", "pending_transactions.txt");
    private static final Path COMPLETED_TRANSACTIONS_PATH = Paths.get("Transactions", "completed_transactions.txt");

    // ---------------------------------------------------------------

    // Uses the default users file path under UserInfo/users.txt. 
    // calls other constructor
    public UserManager() {
        this(Paths.get("UserInfo", "users.txt"));
    }
    // ---------------------------------------------------------------

    // Loads users from the provided file path. 
    public UserManager(Path USERS_FILE_PATH) {
        this.USERS_FILE_PATH = USERS_FILE_PATH;
        loadUsersFromFile();
    }
    
    // ---------------------------------------------------------------

    /** 
     * @param username: this is the users usersname when they make an account
     * @param password: this is the users password when they make an account
     * @param email: this is the users email when they make an account
     * @param Username undergoes normalization 
     * @see User is put into the system 
     * @return boolean: force person to register again or put them to login screen
     */

    public boolean register(String username, String password, String email, String userType) {
        User newUser;
        String normalizedUsername = normalizeUsername(username);
       
        if(users.containsKey(normalizedUsername)) {
            return false;
        }

        if(userType.equals("Owner")) {
            newUser = new Owner(username, password, email);
            addUserToFile(username, password, newUser.getUserId(), email, userType);
        }
        else if(userType.equals("Admin")) {
            newUser = new Admin(username, password, email);
            addUserToFile(username, password, newUser.getUserId(), email, userType);
        }
        else {
            newUser = new Client(username, password, email);
            addUserToFile(username, password, newUser.getUserId(), email, userType);
        }



        // Create and store the user once
        //User newUser = new User (username, password, "", userType);
        
        users.put(normalizedUsername, newUser);
        // Save to file once

        return true;
    }   

    // Validates credentials against the in-memory store. 
    //------------------------------

    /** 
     * @param username: this is the users username from the login screen
     * @param password: this is the users password from the login screen
     * @param Username undergoes normalization 
     * @see User information is looked up in hashmap from UserManager
     * @return boolean: allows user into application if username and password are valid
     */
    public boolean login(String username, String password) {
        String normalizedUsername = normalizeUsername(username);
        User user = users.get(normalizedUsername); //gets user data from username
        if(user == null) {
            return false;
        }
        return user.getPassword().equals(password); //checks that password is correct
    }
    //------------------------------

    /** 
     * @param username: this is the users username
     * @param Username undergoes normalization 
     * @see User information is looked up in hashmap from UserManager
     * @return boolean: User information from User class
     */
    public User getUser(String username) {
        String normalizedUsername = normalizeUsername(username);
        return users.get(normalizedUsername); //get user object from hashamp
    }
    //------------------------------

    /** 
     * Read all user informaton from users.txt
     * Put all their information in the hashmap. Keeps registrations after application closed
     */

    /* 
    users.txt structure: username|password|userId|email|userType
    */
    private void loadUsersFromFile() {
        if (!Files.exists(USERS_FILE_PATH)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(USERS_FILE_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                // Split using "|" as the delimiter
                String[] parts = trimmed.split("\\|");

                String username = parts[0].trim();
                String password = parts[1].trim();
                String email = parts[3].trim();
                String type = parts[4].trim();

                String normalizedUsername = normalizeUsername(username);

                //If the person you just read from the file is not in the map of current users, add them
                if (!username.isEmpty() && !users.containsKey(normalizedUsername)) {
                    if(type.equals("Owner")) {
                        users.put(normalizedUsername, new Owner(username, password, email));
                    }
                    else if(type.equals("Admin")) {
                        users.put(normalizedUsername, new Admin(username, password, email));
                    }
                    else {
                        users.put(normalizedUsername, new Client(username, password, email));
                    }
                }
            }

        } catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }
    //-----------------


    /** 
     * @param username: from the user when they register
     * @param password: from the user when they register
     * @param email: from the user when they register
     * Put their information into the txt file
     */
    /* 
    users.txt structure: username|password|userId|email|userType
    */

    private void addUserToFile(String username, String password, String userId, String email, String userType) {
        try {
            Path parent = USERS_FILE_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(
                    USERS_FILE_PATH,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                writer.write(username + "|" + password + "|" + userId + "|" + email + "|" + userType);
                writer.newLine();
            }
        } catch (IOException e) {
            // If the write fails, keep the user in memory for this session.
        }
    }
    //------------------------------

    /** 
     * Read all user informaton from vehicles.txt
     * Put all their information in the ArrayList. Keeps registrations after application closed
     */
    /* 
     vehicle txt format: userId|vin|model|make|plate|year|approxTime|day registered|userOwnerId
    */
    public void loadVehiclesFromFile() {
        if (!Files.exists(VEHICLES_FILE_PATH)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(VEHICLES_FILE_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                // Split using "|" as the delimiter
                String[] parts = trimmed.split("\\|");
              
                String userId = parts[0].trim();
                //String ownerId = parts[1].trim();
                String vinNumber = parts[1].trim();
                String make = parts[2].trim();
                String model = parts[3].trim();
                String licensePlate = parts[4].trim();
                String year = parts[5].trim();
                String approxTime = parts[6].trim();
                String dayRegistered = parts[7].trim();
                String vehicleOwnerId = parts[8].trim();
               
                
                //User u = users.get(normalizedUsername);
                
                // Add the vehicle to the corresponding owner
                for (User user : users.values()) {
                    if (user instanceof Owner && user.getUserId().equals(userId)) {
                        Vehicle vehicle = new Vehicle(vinNumber, make, model, licensePlate, year, Double.parseDouble(approxTime),
                         dayRegistered, user.getUserId(), vehicleOwnerId);
                        ((Owner) user).addVehicle(vehicle);
                    }
                }
            }
        } catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    /* 
     vehicle txt format: userId|vin|model|make|plate|year|approxTime|day registered|userOwnerId
    */
   //Add vehicle information to vehicle file
    public static void updateVehiclesFile(User u) {
        if (!Files.exists(VEHICLES_FILE_PATH)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                    VEHICLES_FILE_PATH,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) 
            {
                Vehicle newVehicle = ((Owner) u).getVehicles().get(((Owner) u).getVehicles().size()-1);
                writer.write(u.getUserId() + "|" + newVehicle.getNumber() + "|" + newVehicle.getModel() + "|" + newVehicle.getMake() + 
                 "|" + newVehicle.getLicensePlate() + "|" + newVehicle.getYear() + "|" + newVehicle.approxTime() + "|" + newVehicle.getDayRegistered() + "|" + newVehicle.getVehicleOwnerId());
                writer.newLine();
        }
        
        catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    /* 
    jobs.txt format: userId|description|hrs|deadline|jobId|userJobId
    */
   //add a job to the job file
    public static void updateJobFile(User u) {
        if (!Files.exists(JOBS_FILE_PATH)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                    JOBS_FILE_PATH,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) 
            {
                Job newJob = ((Client) u).getClientJobs().get(((Client) u).getClientJobs().size()-1);
                writer.write(u.getUserId() + "|" + newJob.getJobDescription() + "|" + newJob.getApproximateJobDuration() + "|"
                 + newJob.getJobDeadline() + "|" + newJob.getJobId() + "|" + newJob.getJobClientId());
                writer.newLine();
        }
        
        catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }


    /** 
     * Read all user informaton from jobs.txt
     * Put all their information in the ArrayList. Keeps registrations after application closed
     */
     /* 
    jobs.txt format: userId|description|hrs|deadline|jobId|userJobId
    */
    public void loadJobsFromFile() {
        if (!Files.exists(JOBS_FILE_PATH)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(JOBS_FILE_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                // Split using "|" as the delimiter
                String[] parts = trimmed.split("\\|");
              
                //will have to change if more fields are added to the jobs class
                String userId = parts[0].trim();
                String jobDescription = parts[1].trim();
                String approximateJobDuration = parts[2].trim();
                String jobDeadline = parts[3].trim();
                String id = parts[4].trim();

                String jobClientId = parts[5].trim();
                
                // Add the job to the corresponding client
                for (User user : users.values()) {
                    if (user instanceof Client && user.getUserId().equals(userId)) {
                        Job job = new Job(jobDescription, approximateJobDuration, LocalDateTime.parse(jobDeadline), 
                        id, user.getUserId(), jobClientId);
                        job.setStatusPending();
                        Admin.addJob(job);
                        ((Client) user).addJob(job);
                    }
                }
            }
        } catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    /* 
    pending transaction file structure: userId|userType|vin|model|make|plate|year|approxTime|day registered|vehicleOwnerId|timestamp
    */
   //add a new pending transaction to the file. This one is for vehicles

    public static void updatePendingFile(User u,Vehicle v) {
        if (!Files.exists(PENDING_TRANSACTIONS_PATH)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                    PENDING_TRANSACTIONS_PATH,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) 
            {
                Instant timestamp = Instant.now();
                writer.write(u.getUserId() + "|" + u.getUserType() + "|" + v.getNumber() + "|" + v.getModel() + "|" + v.getMake() + 
                "|" + v.getLicensePlate() + "|" + v.getYear() + "|" + v.approxTime() + "|" + v.getDayRegistered() + "|" + v.getVehicleOwnerId() + "|" + timestamp.toString());
                writer.newLine();
        }
        
        catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    
    /* 
    pending transaction file structure: userId|userType|description|hrs|deadline|jobId|userJobId|timestamp
    */
   //update pending transaction to file. This one is for jobs
    public static void updatePendingFile(User u, Job j) {
        if (!Files.exists(PENDING_TRANSACTIONS_PATH)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                    PENDING_TRANSACTIONS_PATH,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) 
            {
                Instant timestamp = Instant.now();
                writer.write(u.getUserId() + "|" + u.getUserType() + "|" + j.getJobDescription() + "|" + 
                j.getApproximateJobDuration() + "|" + j.getJobDeadline() + "|" + j.getJobId() + "|" + j.getJobClientId() + "|" + timestamp.toString());
                writer.newLine();
        }
        
        catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    
    /* 
    pending transaction file structure: 
        userId|userType|description|hrs|deadline|jobId|jobClientId|timestamp
        or 
        userId|userType|vin|model|make|plate|year|approxTime|day registered|vehicleOwnerId|timestamp
    */
   //On application load, add all pending requests back into the system
    public void loadPendingRequests() {
        if (!Files.exists(PENDING_TRANSACTIONS_PATH)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(PENDING_TRANSACTIONS_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                // Split using "|" as the delimiter
                String[] parts = trimmed.split("\\|");
              
                String userId = parts[0].trim();
                String userType = parts[1].trim();

            
                if(userType.equals("Owner")) {
                    String vin = parts[2].trim();
                    String model = parts[3].trim();
                    String make = parts[4].trim();
                    String plate = parts[5].trim();
                    String year = parts[6].trim();
                    String approxTime = parts[7].trim();
                    String dayRegistered = parts[8].trim();
                    String vehicleOwnerId = parts[9].trim();
                    for (User user : users.values()) {
                        if (user.getUserId().equals(userId)) {
                            User u = user;
                            Vehicle v = new Vehicle(vin, make, model, plate, year, Double.parseDouble(approxTime), dayRegistered, u.getUserId(), vehicleOwnerId);
                            Admin.addPendingVehicle(u, v, false);
                            break;
                        }
                    }
                }
                else {
                    String desc = parts[2].trim();
                    String hrs = parts[3].trim();
                    String deadline = parts[4].trim();
                    String jobdId = parts[5].trim();
                    String jobClientId = parts[6].trim();
                    for (User user : users.values()) {
                        if (user.getUserId().equals(userId)) {
                            User u = user;
                            Job j = new Job(desc, hrs, LocalDateTime.parse(deadline), jobdId, u.getUserId(), jobClientId);
                            Admin.addPendingJob(u, j, false);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }

    //pending transaction => completed 
    //remove old pending transaction and move it to completed file
    public static void transactionUpdate(String uniqueForType, boolean accepted, String username, String type) throws IOException {
        Path tempFile = Files.createTempFile("pending_transactions", ".txt");
        try (
            BufferedReader pendingRead  = Files.newBufferedReader(PENDING_TRANSACTIONS_PATH);
            BufferedWriter pendingRewrite = Files.newBufferedWriter(tempFile);
            BufferedWriter completedWrite = Files.newBufferedWriter(COMPLETED_TRANSACTIONS_PATH, StandardOpenOption.APPEND);
        ) {
            String line;
            while ((line = pendingRead.readLine()) != null) {
                String[] parts = line.split("\\|");
                String userId = parts[0];
                String userType = parts[1];
                String unique;
                if(type.equals("Owner")) {
                    unique = parts[3].trim();
                }
                else {
                    unique = parts[6].trim();
                }

                if (unique.equals(uniqueForType)) {
                    // Write the removed line to the new file
                    if(accepted) {
                        completedWrite.write(line + "|accepted");
                    }
                    else {
                        completedWrite.write(line + "|rejected");
                    }
                    completedWrite.newLine();
                } 
                else {
                    // Keep the line in the original file
                    pendingRewrite.write(line);
                    pendingRewrite.newLine();
                }
            }
        }
        // Replace original file with the filtered temp file
        Files.move(tempFile, PENDING_TRANSACTIONS_PATH, StandardCopyOption.REPLACE_EXISTING);
    }


    //Make Username no longer case sensitive and trim space.
    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }
        return username.trim().toLowerCase();
    }
    //------------------------------

    public Map<String, User> getAllUsers() {
        return users;
    }

    public static void loadCompletedTransactions() {
        if (!Files.exists(COMPLETED_TRANSACTIONS_PATH)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(COMPLETED_TRANSACTIONS_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                // Split using "|" as the delimiter
                String[] parts = trimmed.split("\\|");
                String type = parts[1];
                String action = parts[7];
                if(type.equals("Client") && action.equals("rejected")) {
                    Job.increaseJobIdCount();
                }
            }
        } catch (IOException e) {
            // If the file is unreadable, continue with an empty in-memory list.
        }
    }
    
}
