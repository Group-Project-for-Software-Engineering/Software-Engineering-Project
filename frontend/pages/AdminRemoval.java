/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: AdminRemoval.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: April 2026
 * This class implements the admin view for removing jobs and/or vehicles.
 */
package pages;

import classes.Admin;
import classes.Client;
import classes.DatabaseConfig;
import classes.Job;
import classes.Owner;
import classes.User;
import classes.UserManager;
import classes.VCServer;
import classes.Vehicle;

import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AdminRemoval extends JPanel implements Refreshable {

    // private User user;
    private UserManager users;
    private JPanel listPanel;
    private JLabel nameOfView;
    

    // ---------------------------------------------------------------
    public AdminRemoval(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {
        // user = person logged in
        // users = every person in the system
        // this.user = user;
        this.users = users;

        setLayout(new BorderLayout());
        add(new NavBar(cards, user, registry), BorderLayout.NORTH); // create navbar

        nameOfView = new JLabel("", SwingConstants.CENTER);

        nameOfView.setFont(new Font("Arial", Font.BOLD, 24));
        nameOfView.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        // Panel that will hold all user entries
        listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        listPanel.setPreferredSize(new Dimension(0, 0));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(1500, 600));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        viewPanel.add(Box.createVerticalStrut(15));
        viewPanel.add(nameOfView);
        viewPanel.add(Box.createVerticalStrut(15));
        viewPanel.add(scroll);

        add(viewPanel, BorderLayout.CENTER);

        refresh();
    }

    // ---------------------------------------------------------------
    @Override
    public void refresh() {
        listPanel.removeAll();
        // clear old content
        nameOfView.setText("Admin Removal: All User Vehicles and Jobs");
        
        for (User u : users.getAllUsers().values()) {
            createUserCard(u);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ---------------------------------------------------------------
    // creates a card for each vehicle or job in the system
    private void createUserCard(User u) {

        if (u.getUserType().equals("Owner")) {
            for (Vehicle v : ((Owner) u).getVehicles()) {

                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(340, 280));
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 3),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                card.setBackground(new Color(153, 204, 255));
                card.setOpaque(true);
                card.setAlignmentX(Component.CENTER_ALIGNMENT);

                String formatted = "<html>"
                        + "Vehicle:<br>"
                        + "User Id: " + u.getUserId() + "<br>"
                        + "Make: " + v.getMake() + "<br>"
                        + "Model: " + v.getModel() + "<br>"
                        + "VIN: " + v.getNumber() + "<br>"
                        + "License Plate: " + v.getLicensePlate() + "<br>"
                        + "Year: " + v.getYear() + "<br>"
                        + "Approx. Parked Time: " + v.approxTime() + "<br>"
                        + "Owner Vehicle Id: " + v.getVehicleOwnerId() + "<br>"
                        + "Day Registered: " + v.getDayRegistered()
                        + "</html>";

                JLabel label = new JLabel(formatted);
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                label.setHorizontalAlignment(SwingConstants.LEFT);

                card.add(Box.createVerticalStrut(4));
                label.setMaximumSize(new Dimension(320, 100));
                card.add(label);

                JButton removeBtn = new JButton("Remove");
                removeBtn.setBackground(new Color(255, 51, 51));
                removeBtn.setPreferredSize(new Dimension(110, 36));
                removeBtn.setFont(new Font("Arial", Font.BOLD, 20));

                // Remove logic: remove from file, database, and user list
                removeBtn.addActionListener(e -> {

                    ((Owner) u).removeVehicle(v); // remove vehicle from the users list
                    UserManager.removeVehicleFromFile(v.getNumber()); // remove vehicle from file

                    // Remove from SQL database
                    try (Connection conn = DriverManager.getConnection(
                            DatabaseConfig.getURL(),
                            DatabaseConfig.getUsername(),
                            DatabaseConfig.getPassword())) {

                        String sql = "DELETE FROM vehicles WHERE vin = ?";

                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, v.getNumber()); // VIN is the unique key
                            int rows = stmt.executeUpdate();

                            if (rows > 0) {
                                System.out.println("Vehicle removed from SQL database.");
                            } else {
                                System.out.println("No matching vehicle found in SQL.");
                            }
                        }

                    } catch (SQLException s) {
                        s.printStackTrace();
                    }

                    refresh();
                });

                JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                buttonRow.setOpaque(false);
                buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                buttonRow.add(removeBtn);

                card.add(Box.createVerticalStrut(6));
                card.add(buttonRow);
                card.add(Box.createVerticalStrut(3));
                listPanel.add(card);
                
            }
        }

        else if (u.getUserType().equals("Client")) {
            for (Job j : ((Client) u).getClientJobs()) {

                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(340, 280));
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 3),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                card.setBackground(new Color(153, 204, 255));
                card.setOpaque(true);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                String formatted = "<html>"
                        + "Job:<br>"
                        + "User Id: " + u.getUserId() + "<br>"
                        + "Job Id: " + j.getJobId() + "<br>"
                        + "Client Id: " + j.getJobClientId() + "<br>"
                        + "Duration: " + j.getApproximateJobDuration() + "<br>"
                        + "Deadline: " + j.getJobDeadline() + "<br>"
                        + "Description: " + j.getJobDescription()
                        + "</html>";

                JLabel label = new JLabel(formatted);
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                label.setHorizontalAlignment(SwingConstants.LEFT);

                card.add(Box.createVerticalStrut(4));
                label.setMaximumSize(new Dimension(320, 100));
                card.add(label);

                JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                buttonRow.setOpaque(false);
                buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton removeBtn = new JButton("Remove");
                removeBtn.setBackground(new Color(255, 51, 51));
                removeBtn.setPreferredSize(new Dimension(110, 36));
                removeBtn.setFont(new Font("Arial", Font.BOLD, 20));
                buttonRow.add(removeBtn);
                card.add(Box.createVerticalStrut(8));
                card.add(buttonRow);
                listPanel.add(card);
                
                // Remove logic: remove from file, database, and user list
                removeBtn.addActionListener(e -> {
                    ((Client) u).removeJob(j); // remove job from the users list
                    UserManager.removeJobFromFile(j.getJobId()); // remove job from the file
                    // Remove from SQL database
                    try (Connection conn = DriverManager.getConnection(
                            DatabaseConfig.getURL(),
                            DatabaseConfig.getUsername(),
                            DatabaseConfig.getPassword())) {

                        String sql = "DELETE FROM jobs WHERE job_id = ?";

                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, j.getJobId()); // JobID is the unique key
                            int rows = stmt.executeUpdate();

                            if (rows > 0) {
                                System.out.println("Job removed from SQL database.");
                            } else {
                                System.out.println("No matching job found in SQL.");
                            }
                        }

                    } catch (SQLException s) {
                        s.printStackTrace();
                    }

                    refresh();
                });
            }
        }
    }
}
