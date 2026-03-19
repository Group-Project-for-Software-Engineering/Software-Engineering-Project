/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: OfferVehiclePage.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program controls the offer vehicle page of the VCRTS system. This page allows
* a user to: offer, remove, edit, and view their vehicles.
*/

package pages;

import classes.Admin;
import classes.Owner;
import classes.PlaceHolderTextField;
import classes.User;
import classes.UserManager;
import classes.Vehicle;
import java.awt.*;
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
        leftPanel.setBackground(new Color(50, 75, 155));
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

        //JLabel ownerId = new JLabel("Your Owner Id: " + ((Owner)user).getOwnerId());
        //ownerId.setAlignmentX(Component.CENTER_ALIGNMENT);
       // ownerId.setForeground(new Color(65, 105, 255));
       // ownerId.setFont(new Font("Arial", Font.PLAIN, 36));

        
        ownerIdField= new PlaceHolderTextField("Enter an owner id for this vehicle", 36); // adds more graphics to regular textfield
        ownerIdField.setMaximumSize(ownerIdField.getPreferredSize());
        ownerIdField.setAlignmentX(Component.CENTER_ALIGNMENT);


        vehicleVin = new PlaceHolderTextField("Vin                               (XXXXXXXXXXXXXXXXX)", 36); // adds more graphics to regular textfield
        vehicleVin.setMaximumSize(vehicleVin.getPreferredSize());
        vehicleVin.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleMake = new PlaceHolderTextField("Make                           (Letters and/or Numbers)", 36); // adds more graphics to regular textfield
        vehicleMake.setMaximumSize(vehicleMake.getPreferredSize());
        vehicleMake.setAlignmentX(Component.CENTER_ALIGNMENT);

        vehicleModel = new PlaceHolderTextField("Model                           (Letters and/or Numbers)", 36); // adds more graphics to regular textfield
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

        
        
        //vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(Box.createVerticalGlue());
        vehicleForm.add(vehicleLabel);
        //vehicleForm.add(ownerId);
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        vehicleForm.add(ownerIdField);
        //vehicleForm.add(createFormatLabel("Your Owner Id: " + ((Owner)user).getOwnerId()));
        vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        //vehicleForm.add(Box.createVerticalStrut(20)); // creates padding between elements
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
        vehicleForm.add(createFormatLabel("Must be 4 digit number"));
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


            if(VIN_NUMBER.isEmpty() || make.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || year.isEmpty() 
             || arrivalText.isEmpty() || departureText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                return;
            }

            //make new vehicle from form information
            Vehicle v = new Vehicle(VIN_NUMBER, make, model, licensePlate, year, arrivalText, departureText, user.getUserId(), id);
            
            //add the vehicle and user to pending for admin to look at
            Admin.addPendingVehicle(user, v, true);

            JOptionPane.showMessageDialog(this, "Succesfully sent a vehicle request.");
            refresh();
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
