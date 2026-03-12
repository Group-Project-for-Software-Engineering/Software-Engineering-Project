package pages;

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import classes.User;
import classes.UserManager;
import java.awt.BorderLayout;
import classes.Admin;
import classes.Client;
import classes.Job;
import classes.Owner;
import classes.Vehicle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.*;

public class AdminAssignJob extends JPanel implements Refreshable{

    private User user;
    private UserManager users;
    private JPanel listPanel;
    private JLabel name_of_view;

    public AdminAssignJob(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {
        // user = person logged in
        // users = every person in the system
        this.user = user;
        this.users = users;

        setLayout(new BorderLayout());
        add(new NavBar(cards, user, registry), BorderLayout.NORTH); //create navbar
        
        name_of_view = new JLabel("", SwingConstants.CENTER);
        name_of_view.setFont(new Font("Arial", Font.BOLD, 24));
        name_of_view.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        
        // Panel that will hold all user entries 
        listPanel = new JPanel(); 
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); 
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Make it scrollable 
        JScrollPane scroll = new JScrollPane(listPanel); 
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        viewPanel.add(name_of_view);
        viewPanel.add(scroll);

        add(viewPanel, BorderLayout.CENTER);
        
        refresh();
    }
    //----------------------------
    @Override 
    public void refresh() { 
        listPanel.removeAll(); 
        // clear old content 
        name_of_view.setText("Assign Jobs to Vehicles");
        
        for (User u : users.getAllUsers().values()) { 
            //listPanel.add(createUserCard(u));
            listPanel.add(Box.createVerticalStrut(10)); //This separates the boxes
        } 

        listPanel.revalidate(); 
        listPanel.repaint(); 
    }
    //----------------------------
}

