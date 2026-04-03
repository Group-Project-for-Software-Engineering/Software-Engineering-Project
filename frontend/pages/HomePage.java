/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: HomePage.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program controls the home page of the VCRTS system.
 */
package pages;

import classes.Client;
import classes.Job;
import classes.Owner;
import classes.User;
import classes.UserManager;
import classes.Vehicle;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
public class HomePage extends JPanel implements Refreshable {

    private JLabel name_of_view;
    private User user;
    private JPanel listPanel;

    // ---------------------------------------------------------------
    // constructor that sets the user and the user manager
    public HomePage(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {
        // user = person logged in
        // users = every person in the system
        this.user = user;

        setLayout(new BorderLayout());
        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        name_of_view = new JLabel("", SwingConstants.CENTER);
        name_of_view.setFont(new Font("Arial", Font.BOLD, 24));
        name_of_view.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // Make it scrollable
        JScrollPane scroll = new JScrollPane(listPanel);

        viewPanel.add(name_of_view);
        viewPanel.add(scroll);

        add(viewPanel, BorderLayout.CENTER);

        refresh();
    }

    // ---------------------------------------------------------------
    // refreshes home page => updating the # of vehicles a user has
    @Override
    public void refresh() {
        listPanel.removeAll();

        if (user.getUserType().equals("Owner")) {

            ArrayList<String> rejected = user.getRejectedList();
            ArrayList<String> accepted = user.getAcceptedList();
            for (String s : rejected) {
                JOptionPane.showMessageDialog(
                        null,
                        "One of your pending vehicles has been rejected. Vehicle: " + s,
                        "Vehicle Rejection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            for (String s : accepted) {
                JOptionPane.showMessageDialog(
                        null,
                        "One of your pending vehicles has been accepeted. Vehicle: " + s,
                        "Vehicle Acception",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            user.clearAcceptedList();
            user.clearRejectedList();

            name_of_view.setText("Owner view: Your vehicles");
            ArrayList<Vehicle> userVehicles = ((Owner) user).getVehicles();
            for (Vehicle v : userVehicles) {
                listPanel.add(vehicleCard(v));
                listPanel.add(Box.createVerticalStrut(10)); // This separates the boxes
            }

        } else {
            ArrayList<String> rejected = user.getRejectedList();
            ArrayList<String> accepted = user.getAcceptedList();
            for (String s : rejected) {
                JOptionPane.showMessageDialog(
                        null,
                        "One of your pending jobs has been rejected. Job: " + s,
                        "Job Rejection",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            for (String s : accepted) {
                JOptionPane.showMessageDialog(
                        null,
                        "One of your pending jobs has been accepted. Job: " + s,
                        "Job Acception",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            user.clearAcceptedList();
            user.clearRejectedList();

            name_of_view.setText("Client View: Your jobs");
            ArrayList<Job> userJobs = ((Client) user).getClientJobs();
            for (Job j : userJobs) {
                listPanel.add(jobCard(j));
                listPanel.add(Box.createVerticalStrut(10)); // This separates the boxes
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
    // ---------------------------

    // if user is an owner show all their vehicles
    private JPanel vehicleCard(Vehicle v) {
        JPanel vehicleCard = new JPanel();
        vehicleCard.setLayout(new BoxLayout(vehicleCard, BoxLayout.Y_AXIS));
        vehicleCard.add(new JLabel("Owner Vehicle Id: " + v.getVehicleOwnerId()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Vin Number: " + v.getNumber()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("License Plate: " + v.getLicensePlate()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Model: " + v.getModel()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Make: " + v.getMake()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Year: " + v.getYear()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Approximate Residency: " + v.approxTime()));
        // vehicleCard.add(Box.createVerticalStrut(20));
        // vehicleCard.add(new JLabel("Owner Id: " + v.getVehicleOwnerId()));
        vehicleCard.add(Box.createVerticalStrut(20));
        vehicleCard.add(new JLabel("Day Registered: " + v.getDayRegistered()));

        /*
         * 
         * JButton isParkedBtn = new JButton();
         * 
         * if(v.isAvailable()) { //vehicle is currently parked and must be sent to
         * leaving
         * vehicleCard.add(Box.createVerticalStrut(20));
         * isParkedBtn.setText("Vehicle is departing");
         * 
         * for (ActionListener al : isParkedBtn.getActionListeners()) {
         * isParkedBtn.removeActionListener(al);
         * }
         * isParkedBtn.addActionListener(e -> {
         * Admin.removeVehicle(v);
         * v.hasDeparted();
         * refresh();
         * });
         * 
         * }
         * else { //vehicle is not parked and is arriving
         * vehicleCard.add(Box.createVerticalStrut(20));
         * isParkedBtn.setText("Vehicle has arrived");
         * 
         * for (ActionListener al : isParkedBtn.getActionListeners()) {
         * isParkedBtn.removeActionListener(al);
         * }
         * 
         * isParkedBtn.addActionListener(e -> {
         * Admin.addVehicle(v);
         * v.isParked();
         * refresh();
         * });
         * }
         * 
         * vehicleCard.add(isParkedBtn);
         * 
         */
        vehicleCard.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        vehicleCard.setBackground(new Color(153, 204, 255));
        vehicleCard.setOpaque(true);

        return vehicleCard;
    }
    // -------------------------------

    // if user is a client show all their jobs
    private JPanel jobCard(Job j) {
        JPanel jobCard = new JPanel();
        jobCard.setLayout(new BoxLayout(jobCard, BoxLayout.Y_AXIS));
        jobCard.add(new JLabel("Job Id: " + j.getJobClientId()));
        jobCard.add(Box.createVerticalStrut(20));
        // jobCard.add(new JLabel("Client Id: " + ((Client)user).getClientId()));
        // jobCard.add(Box.createVerticalStrut(20));
        jobCard.add(new JLabel("Job Description: " + j.getJobDescription()));
        jobCard.add(Box.createVerticalStrut(20));
        jobCard.add(new JLabel("Deadline: " + j.getJobDeadline()));
        jobCard.add(Box.createVerticalStrut(20));
        jobCard.add(new JLabel("Duration: " + j.getApproximateJobDuration()));

        JLabel jobStatus = new JLabel();
        if (j.getJobStatus().equals("pending")) {
            jobStatus.setText("Status: Pending");
        }
        jobCard.add(Box.createVerticalStrut(20));
        jobCard.add(jobStatus);

        jobCard.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        jobCard.setBackground(new Color(153, 204, 255));
        jobCard.setOpaque(true);

        return jobCard;
    }
    // ------------------------

}
