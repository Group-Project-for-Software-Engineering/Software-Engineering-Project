/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: AdminHome.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: March 2026
 * This class implements the admin home view.
 */
package pages;

import classes.Admin;
import classes.Client;
import classes.Job;
import classes.Owner;
import classes.User;
import classes.UserManager;
import classes.Vehicle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class AdminHome extends JPanel implements Refreshable {

    private User user;
    private UserManager users;
    private JPanel listPanel;
    private JLabel nameOfView;
    private JButton calculateCompletionTimesBtn;

    public AdminHome(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {
        // user = person logged in
        // users = every person in the system
        this.user = user;
        this.users = users;

        setLayout(new BorderLayout());
        add(new NavBar(cards, user, registry), BorderLayout.NORTH); //create navbar
        
        nameOfView = new JLabel("", SwingConstants.CENTER);

        calculateCompletionTimesBtn = new JButton("Calculate Completion Times");
        calculateCompletionTimesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateCompletionTimesBtn.addActionListener(e -> showCompletionTimes());

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
        viewPanel.add(calculateCompletionTimesBtn);
        viewPanel.add(scroll);

        add(viewPanel, BorderLayout.CENTER);
        
        refresh();
    }
    //----------------------------

    @Override 
    public void refresh() { 
        listPanel.removeAll(); 
        // clear old content 
        nameOfView.setText("Admin view: All Users");
        
        for (User u : users.getAllUsers().values()) { 
            listPanel.add(createUserCard(u));
            listPanel.add(Box.createVerticalStrut(10)); //This separates the boxes
        } 

        listPanel.revalidate(); 
        listPanel.repaint(); 
    }
    //----------------------------

    //creates a card for each user for the admin to see. The content show is based on the type of user
    private JPanel createUserCard (User u) { 
        JPanel userCard = new JPanel(); 
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.Y_AXIS));
        //userCard.setLayout(new GridLayout(0, 1)); 
        userCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        userCard.add(new JLabel("Name: " + u.getUsername())); 
        userCard.add(Box.createVerticalStrut(5));
        userCard.add(new JLabel("Email: " + u.getEmail())); 
        userCard.add(Box.createVerticalStrut(5));
        userCard.add(new JLabel("User Type: " + u.getUserType())); 
        userCard.add(Box.createVerticalStrut(5));
        userCard.add(new JLabel("User Id: " + u.getUserId())); 
        userCard.add(Box.createVerticalStrut(5));
        userCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 

        if(u.getUserType().equals("Owner")) {
            //userCard.add(new JLabel("Owner Id: " + ((Owner)u).getOwnerId()));
            userCard.add(new JLabel("Vehicles:"));
            for(Vehicle v: ((Owner)u).getVehicles()) {
                userCard.add(new JLabel("[Make: " + v.getMake() + " || Model: " + v.getModel() + " || VIN: " + v.getNumber() 
                + " || License Plate: " + v.getLicensePlate() + " || Year: " + v.getYear() + " || Approximate parked time: " + v.approxTime()
                + " || Owner Vehicle Id: " + v.getVehicleOwnerId() 
                +" || Day Registered " + v.getDayRegistered() + "]"));
            }
        }
        else if (u.getUserType().equals("Client")) {
            // userCard.add(new JLabel("Client Id: " + ((Client)u).getClientId()));
            userCard.add(new JLabel("Jobs:"));
            for(Job j: ((Client)u).getClientJobs()) {
                userCard.add(new JLabel("[Job Id: " + j.getJobId() +" || Job Client Id: " + j.getJobClientId() + " || Job duration: " + j.getApproximateJobDuration() + 
                " || Deadline: " + j.getJobDeadline() + " || Description: " + j.getJobDescription() + "]"));
            }
        }
        else {
            // userCard.add(new JLabel("Admin Id: " + ((Admin)u).getAdminId()));
        }

        userCard.setBackground(new Color(153, 204, 255));
        userCard.setOpaque(true);

        return userCard; 
    }


    // helper method to show the completion times for all jobs
    // computes the completion times using the FIFO (First In, First Out) structure
    // jobs are added into the arraylist in the order they are submitted
    private void showCompletionTimes() {
        // stores all of the jobs that are submitted by the client that is approved by controller
        ArrayList<Job> allJobs = new ArrayList<>();
        allJobs = Admin.getJobs();

        /* 
        // iterates through all of the users and gets their jobs if they are a client
        for (User u : users.getAllUsers().values()) {
            if ("Client".equals(u.getUserType())) {
                allJobs.addAll(((Client) u).getClientJobs());
            }
        }
         */
        // if there are no jobs, show a message stating this 
        if (allJobs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No jobs available.");
            return;
        }

        // computes the completion times for all jobs
        // references admin.java computerCompletionTimes() method 
        ArrayList<Double> completionTimes = Admin.computeCompletionTimes();

        // stores the completion times in a stringbuilder
        StringBuilder result = new StringBuilder("Completion Times for all jobs: \n\n");

        // iterates through all of the jobs and adds the completion times to the stringbuilder
        for (int i = 0; i < allJobs.size(); i++) {
            Job j = allJobs.get(i);
            int jobHours = (int) j.getApproximateJobDuration();
            long jobMinutes = Math.round((j.getApproximateJobDuration() - jobHours) * 60);
            int jobCompletionHours = completionTimes.get(i).intValue();
            long jobCompletionMinutes = Math.round((completionTimes.get(i) - jobCompletionHours) * 60);
            result.append("Job ID: ").append(j.getJobId())
                    .append("| Client Job ID: ").append(j.getJobClientId())
                    .append(" | Duration: ");
            if (jobHours != 0) {
                result.append(jobHours)
                .append(" hours ");
            }
            if (jobMinutes != 0) {
                result.append(jobMinutes)
                .append(" minutes");
            }
            result.append(" | Completion Time: ");
            if (jobCompletionHours != 0) {
                result.append(jobCompletionHours)
                .append(" hours ");
            }
            if (jobCompletionMinutes != 0) {
                result.append(jobCompletionMinutes)
                .append(" minutes");
            }
            result.append(" \n\n");
        }
        // shows the completion times in a popup window
        JTextArea completionTextArea = new JTextArea(result.toString());
        completionTextArea.setEditable(false); // disables typing
        JOptionPane.showMessageDialog(this, new JScrollPane(completionTextArea),
                "Completion Times", JOptionPane.INFORMATION_MESSAGE);
    }
    //-----------------------------------------
}
