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
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Make it scrollable
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        viewPanel.add(nameOfView);
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
            listPanel.add(createUserCard(u));
            listPanel.add(Box.createVerticalStrut(10)); // This separates the boxes
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ---------------------------------------------------------------
    // creates a card for each vehicle or job in the system
    private JPanel createUserCard(User u) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        if (u.getUserType().equals("Owner")) {
            for (Vehicle v : ((Owner) u).getVehicles()) {

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(new Color(153, 204, 255));
                card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

                card.add(new JLabel("Vehicle:"));
                card.add(new JLabel(
                        "[User Id: " + u.getUserId() +
                                " || Make: " + v.getMake() +
                                " || Model: " + v.getModel() +
                                " || VIN: " + v.getNumber() +
                                " || License Plate: " + v.getLicensePlate() +
                                " || Year: " + v.getYear() +
                                " || Approx. Parked Time: " + v.approxTime() +
                                " || Owner Vehicle Id: " + v.getVehicleOwnerId() +
                                " || Day Registered: " + v.getDayRegistered() + "]"));

                card.add(new JLabel("Name: " + u.getUsername()));
                card.add(new JLabel("Email: " + u.getEmail()));
                card.add(new JLabel("User Type: " + u.getUserType()));
                card.add(new JLabel("User Id: " + u.getUserId()));

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

                card.add(removeBtn);
                container.add(card);
                container.add(Box.createVerticalStrut(10));
            }
        }

        else if (u.getUserType().equals("Client")) {
            for (Job j : ((Client) u).getClientJobs()) {

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(new Color(153, 204, 255));
                card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

                card.add(new JLabel("Job:"));
                card.add(new JLabel(
                        "[User Id: " + u.getUserId() +
                                " || Job Id: " + j.getJobId() +
                                " || Client Id: " + j.getJobClientId() +
                                " || Duration: " + j.getApproximateJobDuration() +
                                " || Deadline: " + j.getJobDeadline() +
                                " || Description: " + j.getJobDescription() + "]"));

                card.add(new JLabel("Name: " + u.getUsername()));
                card.add(new JLabel("Email: " + u.getEmail()));
                card.add(new JLabel("User Type: " + u.getUserType()));
                card.add(new JLabel("User Id: " + u.getUserId()));

                JButton removeBtn = new JButton("Remove");
                removeBtn.setBackground(new Color(255, 51, 51));
                removeBtn.setPreferredSize(new Dimension(110, 36));
                removeBtn.setFont(new Font("Arial", Font.BOLD, 20));

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
                            stmt.setString(1, j.getJobId()); // VIN is the unique key
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

                card.add(removeBtn);
                container.add(card);
                container.add(Box.createVerticalStrut(10));
            }
        }

        return container;
    }

}
