/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: NavBar.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program controls the navigation bar at the top of the screen.
*/
package pages;

import classes.User;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
// class that controls the navigation bar at the top of the screen across pages
public class NavBar extends JPanel {

    private final Map<String, Refreshable> refreshables;
    private JButton submitJobBtn;
    private JButton offerVehicleBtn; 
    private JButton homeBtn;
    //private JButton pendingBtn;

    // ---------------------------------------------------------------
    // constructor
    public NavBar(JPanel cards, User user, Map<String, Refreshable> refreshables) {
        this.refreshables = refreshables;

        setBackground(new Color(70, 80, 120));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(1000, 60));

        JLabel title = new JLabel("VCRTS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        String displayName = "User";
            if (user != null) {
                displayName = user.getUsername();
            }
        JLabel welcomeLabel = new JLabel("  Welcome, " + displayName + "!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));


        homeBtn = new JButton("Home");

        //declaration of user specific buttons

        //if(user.getUserType().equals("Admin")) {
            //pendingBtn = new JButton("Pending");
        //}
        JButton scheduleBtn = new JButton("Schedule");

        if (user.getUserType().equals("Owner")) {
            offerVehicleBtn = new JButton("Offer Vehicle");
        }

        if (user.getUserType().equals("Client")) {
            submitJobBtn = new JButton("Submit Job");
        }

        JButton settingsBtn = new JButton("Settings");
        JButton logoutBtn = new JButton("Log Out");

        //--------------------------------------------

        //event listeners for buttons. Some buttons are restricted based on user

        if (user.getUserType().equals("Admin")) {
            homeBtn.addActionListener(e -> {
                refreshables.get("adminHome").refresh();
                ((CardLayout) cards.getLayout()).show(cards, "adminHome");
            });
        }
        else {
             homeBtn.addActionListener(e -> {
                refreshables.get("home").refresh();
                ((CardLayout) cards.getLayout()).show(cards, "home");
            });
        }


        scheduleBtn.addActionListener(e -> {
            refreshables.get("schedule").refresh();
            ((CardLayout) cards.getLayout()).show(cards, "schedule");
        });

        if (user.getUserType().equals("Owner")) {
            offerVehicleBtn.addActionListener(e -> {
                refreshables.get("offerVehicle").refresh();
                ((CardLayout) cards.getLayout()).show(cards, "offerVehicle");
            });
        }


        if (user.getUserType().equals("Client")) {
            submitJobBtn.addActionListener(e -> {
                refreshables.get("submitJob").refresh();
                ((CardLayout) cards.getLayout()).show(cards, "submitJob");
            });
        }

        /* 
        if (user.getUserType().equals("Admin")) {
            pendingBtn.addActionListener(e -> {
                refreshables.get("pending").refresh();
                ((CardLayout) cards.getLayout()).show(cards, "pending");
            });
        }
        */


        settingsBtn.addActionListener(e -> {
            refreshables.get("settings").refresh();
            ((CardLayout) cards.getLayout()).show(cards, "settings");
        });

        logoutBtn.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, "about"));

        //--------------------------------------

        add(Box.createHorizontalStrut(20));
        add(title);
        add(Box.createHorizontalStrut(15));
        add(welcomeLabel);  
        add(Box.createHorizontalGlue());
        add(homeBtn);
        add(Box.createHorizontalStrut(10));

        // TODO: SCHEDULE BUTTON IS COMMENTED OUT FOR NOW, AS SCHEDULE PAGE IS NOT IMPLEMENTED. UNCOMMENT WHEN SCHEDULE PAGE IS READY
        // add(scheduleBtn);
        add(Box.createHorizontalStrut(10));

        /* 

         if (user.getUserType().equals("Admin")) {
            add(pendingBtn);
            add(Box.createHorizontalStrut(10));
        }
        */
        
        
        if (user.getUserType().equals("Owner")) {
            add(offerVehicleBtn);
            add(Box.createHorizontalStrut(10));
        }
        

        if (user.getUserType().equals("Client")) {
            add(submitJobBtn);
            add(Box.createHorizontalStrut(10));
        }

        // TODO: COMMENTED FOR NOW, AS SETTINGS PAGE IS NOT IMPLEMENTED. UNCOMMENT WHEN SETTINGS PAGE IS READY
        // add(settingsBtn);
        add(Box.createHorizontalStrut(10));
        add(logoutBtn);
        add(Box.createHorizontalStrut(30));
    }
    //-----------------------------

    //adds pointer to a given navbar page
    public void register(String name, Refreshable page) {
        refreshables.put(name, page);
    }
    //--------------------------
}
