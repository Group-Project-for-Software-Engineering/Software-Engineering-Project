/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: Settings.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program controls the settings page of the VCRTS system.
 */
package pages;

import classes.User;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
// Settings Page
public class Settings extends JPanel implements Refreshable {

    // ---------------------------------------------------------------
    // constructor that sets the cards, user, and registry
    public Settings(JPanel cards, User user, Map<String, Refreshable> registry) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Settings Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        add(label, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(new NavBar(cards, user, registry), BorderLayout.NORTH);
        add(label, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------------
    // refreshes settings page
    @Override
    public void refresh() {
    }
}
