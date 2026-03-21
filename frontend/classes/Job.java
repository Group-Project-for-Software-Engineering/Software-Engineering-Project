/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: Job.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: February 2026
 * The Job class represents a Job submitted into the VCRTS system. It stores job information including job ID,
 * estimated job duration, and job deadline. The class validates that all required job information is provided during object creation.
 */

package classes;

// ---------------------------------------------------------------
// allows to see the job deadline and approximate job duration of the client
import java.time.LocalDateTime;
// ---------------------------------------------------------------
// job class
//The system shall receive and verify the vehicle's jobId, approximateJobDuration, and jobDeadline.

public class Job {
    private String JOB_ID;
    private static int increment = 0;
    private String description;
    private double approximateJobDuration; // in minutes or hours
    private LocalDateTime jobDeadline;

    private String userId;
    private String jobStatus; //this can be pending, in progress, completed, or approved

    private String jobClientId;

    // ---------------------------------------------------------------
    // constructor
    public Job(String description, double approximateJobDuration, LocalDateTime jobDeadline, String userId, String jobClientId) {
        increment++;
        this.JOB_ID = Integer.toString(increment);
        this.approximateJobDuration = approximateJobDuration;
        this.jobDeadline = jobDeadline;
        this.description = description;
        
        this.userId = userId;
        this.jobClientId = jobClientId;

        // make sure job has all the necessary information
        if (JOB_ID.equals("") || approximateJobDuration <= 0 || jobDeadline == null) {
            throw new IllegalArgumentException("Job information is incomplete");
        }
    }

    public Job(String description, String hrs,LocalDateTime jobDeadline, String id, String userId, String jobClientId) {
        increment++;
        this.JOB_ID = id;
        this.approximateJobDuration = Double.parseDouble(hrs);
        this.jobDeadline = jobDeadline;
        this.description = description;
        this.userId = userId;
        this.jobClientId = jobClientId;

        // make sure job has all the necessary information
        if (JOB_ID.equals("") || approximateJobDuration <= 0 || jobDeadline == null) {
            throw new IllegalArgumentException("Job information is incomplete");
        }
    }

    // implementing the getters and setters in order to access the private variables

    // ---------------------------------------------------------------
    // This method returns the job ID for a specific job.
    public String getJobId() {
        return JOB_ID;
    }

    // ---------------------------------------------------------------
    // This method returns the approximate job duration for a specific job.
    public double getApproximateJobDuration() {
        return approximateJobDuration;
    }

    // ---------------------------------------------------------------
    // This method returns the deadline for a specific job.
    public LocalDateTime getJobDeadline() {
        return jobDeadline;
    }

    // ---------------------------------------------------------------
    // This method updates the approximate job duration for a specific job.
    public void setApproximateJobDuration(int approximateJobDuration) {
        this.approximateJobDuration = approximateJobDuration;
    }

    // ---------------------------------------------------------------
    // This method updates the deadline for a specific job.
    public void setJobDeadline(LocalDateTime jobDeadline) {
        this.jobDeadline = jobDeadline;
    }
    //--------------------------------------

    //this method returns the job description

    public String getJobDescription() {
        return description;
    }
    //--------------------------

    public static void increaseJobIdCount() {
        increment++;
    }

    public String getClientId() {
        return userId;
    }

    public String getJobStatus() {
        return jobStatus;
    }
    public void setStatusPending() {
        jobStatus = "pending";
    }

    public String getJobClientId() {
        return jobClientId;
    }

}
