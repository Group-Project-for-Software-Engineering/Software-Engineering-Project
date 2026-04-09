/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: OfferVehiclePage.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program controls the offer vehicle page of the VCRTS system. This page allows
* a user to: offer, remove, edit, and view their vehicles.
 */
package pages;

import java.sql.*;
import classes.Owner;
import classes.PlaceHolderTextField;
import classes.User;
import classes.UserManager;
import classes.Vehicle;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
// class that controls the offer vehicle page
public class OfferVehiclePage extends JPanel implements Refreshable {

    // Date Format
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Look up vehicles by VIN.
    private final Map<String, Vehicle> VEHICLES_BY_VIN = new LinkedHashMap<>();

    private final JTextArea STATUS_AREA = new JTextArea(6, 50);

    private JTextField vehicleVin;
    private JTextField vehicleMake;
    private JTextField vehicleModel;
    private JTextField vehiclePlate;

    private JTextField vehicleYear;
    private JTextField vehicleArrival;
    private JTextField vehicleDeparture;

    private JTextField ownerIdField;

    // ---------------------------------------------------------------
    // constructor: sets user + user manager + registry
    public OfferVehiclePage(JPanel cards, User user, Map<String, Refreshable> registry, UserManager users) {
        setLayout(new BorderLayout());

        // NavBar
        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        JPanel splitPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(27, 94, 32));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel leftTitle = new JLabel("Offer Vehicle");
        leftTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftTitle.setForeground(Color.WHITE);
        leftTitle.setFont(new Font("Arial", Font.PLAIN, 50));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftTitle);
        leftPanel.add(Box.createVerticalGlue());

        JPanel vehicleForm = new JPanel();
        vehicleForm.setBackground(new Color(242, 245, 249));
        vehicleForm.setLayout(new BoxLayout(vehicleForm, BoxLayout.Y_AXIS)); // center everything vertically

        JLabel vehicleLabel = new JLabel("Enter vehicle information");
        vehicleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vehicleLabel.setForeground(new Color(65, 105, 255));
        vehicleLabel.setFont(new Font("Arial", Font.PLAIN, 36));

        // JLabel ownerId = new JLabel("Your Owner Id: " + ((Owner)user).getOwnerId());
        // ownerId.setAlignmentX(Component.CENTER_ALIGNMENT);
        // ownerId.setForeground(new Color(65, 105, 255));
        // ownerId.setFont(new Font("Arial", Font.PLAIN, 36));

        // adds more graphics to regular textfield
        ownerIdField = new PlaceHolderTextField("Enter an owner id for this vehicle", 36);

        ownerIdField.setMaximumSize(ownerIdField.getPreferredSize());
        ownerIdField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // adds more graphics to regular textfield
        vehicleVin = new PlaceHolderTextField("Vin                               (XXXXXXXXXXXXXXXXX)", 36);

        vehicleVin.setMaximumSize(vehicleVin.getPreferredSize());
        vehicleVin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // adds more graphics to regular textfield
        vehicleMake = new PlaceHolderTextField("Make                           (Letters and/or Numbers)", 36);

        vehicleMake.setMaximumSize(vehicleMake.getPreferredSize());
        vehicleMake.setAlignmentX(Component.CENTER_ALIGNMENT);

        // adds more graphics to regular textfield
        vehicleModel = new PlaceHolderTextField("Model                           (Letters and/or Numbers)", 36);

        vehicleModel.setMaximumSize(vehicleModel.getPreferredSize());
        vehicleModel.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehiclePlate = new PlaceHolderTextField("License Plate               (Letters and/or Numbers)", 36);
        vehiclePlate.setMaximumSize(vehiclePlate.getPreferredSize());
        vehiclePlate.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleYear = new PlaceHolderTextField("Year                             (yyyy)", 36);
        vehicleYear.setMaximumSize(vehiclePlate.getPreferredSize());
        vehicleYear.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleArrival = new PlaceHolderTextField("Expected arrival          (yyyy-mm-dd hh:mm:ss)", 36);
        vehicleArrival.setMaximumSize(vehiclePlate.getPreferredSize());
        vehicleArrival.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleDeparture = new PlaceHolderTextField("Expected departure    (yyyy-mm-dd hh:mm:ss)", 36);
        vehicleDeparture.setMaximumSize(vehiclePlate.getPreferredSize());
        vehicleDeparture.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setBackground(new Color(77, 163, 255));
        submitBtn.setForeground(Color.DARK_GRAY);

        // vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between
        // elements
        vehicleForm.add(Box.createVerticalGlue());
        vehicleForm.add(vehicleLabel);
        // vehicleForm.add(ownerId);
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(ownerIdField);
        vehicleForm.add(createFormatLabel("Enter an owner id for this vehicle"));
        // vehicleForm.add(createFormatLabel("Your Owner Id: " +
        // ((Owner)user).getOwnerId()));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        // vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between
        // elements
        vehicleForm.add(vehicleVin);
        vehicleForm.add(createFormatLabel("Must be a 17-character alphanumeric string"));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehicleMake);
        vehicleForm.add(createFormatLabel("Letters and/or Numbers. E.g. Ford, Honda"));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehicleModel);
        vehicleForm.add(createFormatLabel("Letters and/or Numbers. E.g. Mustang, Civic"));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehiclePlate);
        vehicleForm.add(createFormatLabel("Letters and/or Numbers."));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehicleYear);
        vehicleForm.add(createFormatLabel("Must be 4 digit number "));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehicleArrival);
        vehicleForm.add(createFormatLabel("Format: yyyy-mm-dd hh:mm:ss"));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(vehicleDeparture);
        vehicleForm.add(createFormatLabel("Format: yyyy-mm-dd hh:mm:ss"));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements

        vehicleForm.add(submitBtn);
        vehicleForm.add(Box.createVerticalGlue());

        splitPanel.add(leftPanel);
        splitPanel.add(vehicleForm);
        add(splitPanel, BorderLayout.CENTER);

        submitBtn.addActionListener(e -> {
            String VIN_NUMBER = vehicleVin.getText().trim();
            String make = vehicleMake.getText().trim();
            String model = vehicleModel.getText().trim();
            String licensePlate = vehiclePlate.getText().trim();
            String year = vehicleYear.getText().trim();
            String arrivalText = vehicleArrival.getText().trim();
            String departureText = vehicleDeparture.getText().trim();
            String id = ownerIdField.getText().trim();

            if (VIN_NUMBER.isEmpty() || make.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || year.isEmpty()
                    || arrivalText.isEmpty() || departureText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                return;
            }

            if (VIN_NUMBER.length() != 17) {
                JOptionPane.showMessageDialog(this, "Vin number must be 17 digits");
                return;
            }

            try {
                if (year.length() != 4) {
                    JOptionPane.showMessageDialog(this, "Year must be a 4 digit number");
                    return;
                }
                @SuppressWarnings("unused")
                int yearNum = Integer.parseInt(year);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be an integer number");
                return;
            }

            LocalDateTime arrival;
            try {
                arrival = LocalDateTime.parse(arrivalText, TS_FORMAT);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Expected arrival must be in the format: yyyy-mm-dd hh:mm:ss\nExample: 2024-03-09 17:45:00");
                return;
            }

            LocalDateTime departure;
            try {
                departure = LocalDateTime.parse(departureText, TS_FORMAT);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Expected departure must be in the format: yyyy-mm-dd hh:mm:ss\nExample: 2024-03-09 17:45:00");
                return;
            }

            int duration = (int) Duration.between(arrival, departure).toHours();
            if (duration < 1) {
                JOptionPane.showMessageDialog(this,
                        "Time between expected arrival and departure must be more than 1 hour");
                return;
            }

            // make new vehicle from form information
            Vehicle v = new Vehicle(VIN_NUMBER, make, model, licensePlate, year, arrivalText, departureText,
                    user.getUserId(), id);
            JOptionPane.showMessageDialog(this, "Submitted vehicle application.");
            refresh();

            new Thread(() -> {
                try {
                    Socket socket = new Socket("localhost", 9806);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    out.writeUTF(v.toString());

                    // String ack = in.readUTF();
                    String decision = in.readUTF();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Admin decision: " + decision);

                        if (decision.equals("ACCEPT")) {// if admin accepted it, show message, add it to the jobs file
                            
                            UserManager.updateVehiclesFile(v); //to comment out later 

                            /* New stuff for sql
                            try {
                                // declares a connection to your database
                                Connection conn = DriverManager.getConnection(Login_Registration.url,
                                        Login_Registration.username, Login_Registration.password);

                                Statement statement = conn.createStatement();

                                // creates an insert query
                                String sql = "INSERT INTO vehicles"
                                        + "(user_id, vin, model, make, plate, year, approx_time, day_registered, user_owner_id, timestamp)"
                                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement ps = conn.prepareStatement(sql);

                                ps.setInt(1, v.getOwnerId());
                                ps.setString(2, v.getNumber());
                                ps.setString(3, v.getModel());
                                ps.setString(4, v.getMake());
                                ps.setString(5, v.getLicensePlate());
                                ps.setInt(6, v.getYear());
                                ps.setDouble(7, v.g());
                                ps.setDate(8, java.sql.Date.valueOf(v.getDayRegistered()));
                                ps.setString(9, v.getUserOwnerId());
                                ps.setTimestamp(10, java.sql.Timestamp.valueOf(v.getCreatedAt()));

                                ps.executeUpdate();
                                // establishes the connection session
                                // executes the query
                                int row = statement.executeUpdate(sql);
                                // the return value is the indication of success or failure of the query
                                // execution
                                if (row > 0)
                                    System.out.println("Data was inserted!");

                                conn.close();

                            } catch (SQLException s) {
                                s.printStackTrace();
                            }
                            */

                            ((Owner) user).addVehicle(v);
                            user.addAccepted(v.toString());

                        } else { // don't add it to file; show message
                            user.addRejected(v.toString());
                        }
                    });

                    socket.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

        });

    }

    private JLabel createFormatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.ITALIC, 12));
        label.setForeground(Color.GRAY); // Makes it look like helper text
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // ---------------------------------------------------------------
    // refreshes offer vehicle page
    @Override
    public void refresh() {
        vehicleVin.setText("");
        vehicleMake.setText("");
        vehicleModel.setText("");
        vehiclePlate.setText("");
        vehicleArrival.setText("");
        vehicleYear.setText("");
        vehicleDeparture.setText("");
        ownerIdField.setText("");
    }
}
