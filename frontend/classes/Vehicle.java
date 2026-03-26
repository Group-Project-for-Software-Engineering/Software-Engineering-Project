/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: Vehicle.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: February 2026
 * The Vehicle class represents a registered vehicle in the VCRTS system. It stores vehicle identification information including VIN number,
 * make, model, and license plate. The class validates that all required vehicle information is provided during object creation.
 */
package classes;
//Focus on the backend and make necessary classes to store information for the user and their vehicles (Sebastian)

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;

// ---------------------------------------------------------------
// vehicle class
//The system shall receive and verify the vehicle's number, license plate, make, and model.
public class Vehicle {
    private final String VIN_NUMBER;
    private String make;
    private String model;
    private String licensePlate;

    private String vehicleOwnerId;
    
    private String year;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Duration duration;
    private double approxResidencyTime; // in hrs
    private String dayRegistered;

    private String userId;

    private boolean isParked = false; //is the vehicle able to be used. Default value is no.

    // constructor
    // ---------------------------------------------------------------
    // add user as parameter in constructor

    public Vehicle(String VIN_NUMBER, String make, String model, String licensePlate, String year, String arrive, String depart, String userId, String id) {
        this.VIN_NUMBER = VIN_NUMBER;
        this.make = make;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.userId = userId;
        this.vehicleOwnerId = id;

        LocalDateTime arrival = LocalDateTime.parse(arrive, formatter);
        LocalDateTime departure = LocalDateTime.parse(depart, formatter);
        duration = Duration.between(arrival, departure);
        int approxHrs = (int) duration.toHours();
        this.approxResidencyTime = approxHrs;
        dayRegistered = arrive.substring(0, 10);


        // make sure vehicle has all the necessary information
        if (VIN_NUMBER.equals("") || make == null || model == null || licensePlate == null 
        || year == null || approxResidencyTime == 0) {
            throw new IllegalArgumentException("Vehicle information is incomplete");
        }
    }

    // ---------------------------------------------------------------
    //overloading from reading from vehicle file
    public Vehicle(String VIN_NUMBER, String make, String model, String licensePlate, String year, double approxTime, String dayRegistered
        , String userId, String vehicleOwnerId) {
        this.VIN_NUMBER = VIN_NUMBER;
        this.make = make;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.approxResidencyTime = approxTime;
        this.dayRegistered = dayRegistered;
        this.userId = userId;
        this.vehicleOwnerId = vehicleOwnerId;
    }

    // implementing the getters in order to access the private variables

    // ---------------------------------------------------------------
    // returns the VIN number of the vehicle
    public String getNumber() {
        return VIN_NUMBER;
    }

    // ---------------------------------------------------------------
    // returns the make of the vehicle
    public String getMake() {
        return make;
    }

    // ---------------------------------------------------------------
    // returns the model of the vehicle
    public String getModel() {
        return model;
    }

    // ---------------------------------------------------------------
    // returns the license plate of the vehicle
    public String getLicensePlate() {
        return licensePlate;
    }

    //----------------------------
    //returns the year 
    public String getYear() {
        return year;
    }

    //-------------------------
    //return the residency time
    public double approxTime() {
        return approxResidencyTime;
    }

    // ---------------------------------------------------------------
    //returns the day the vehicle is registered for
    public String getDayRegistered() {
        return dayRegistered;

    }

    // ---------------------------------------------------------------
    // returns the owner ID of the vehicle
    public String getOwnerId() {
        return userId;
    }

    // ---------------------------------------------------------------
    // returns true if the vehicle is available, false otherwise
    public boolean isAvailable() {
        return isParked;
    }

    // ---------------------------------------------------------------
    // sets the vehicle to parked
    public void isParked() {
        isParked = true;
        Admin.addVehicle(this);
    }

    // ---------------------------------------------------------------
    // sets the vehicle to departed
    public void hasDeparted() {
        isParked = false;
        Admin.removeVehicle(this);
    }

    // ---------------------------------------------------------------
    // returns the owner ID of the vehicle
    public String getVehicleOwnerId() {
        return vehicleOwnerId;
    }

    public String toString() {
        return "Vehicle make" + this.getMake() + " || Model: " + this.getModel() + " || VIN: " + this.getNumber() 
                + " || License Plate: " + this.getLicensePlate() + " || Year: " + this.getYear() + " || Approximate parked time: " + this.approxTime()
                + " || Owner Vehicle Id: " + this.getVehicleOwnerId() 
                +" || Day Registered " + this.getDayRegistered() + "|| User Id: " + this.getVehicleOwnerId();
    }

}
