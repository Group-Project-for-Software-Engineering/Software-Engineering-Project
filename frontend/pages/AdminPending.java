package pages;

import classes.Admin;
import classes.Job;
import classes.User;
import classes.UserManager;
import classes.Vehicle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class AdminPending extends JPanel implements Refreshable{

    private User user;
    private UserManager users;
    private JPanel listPanel;

    public AdminPending(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {
       // user = person logged in
       // users = every person in the system
       this.user = user;
       this.users = users;

       setLayout(new BorderLayout());
       add(new NavBar(cards, user, registry), BorderLayout.NORTH); //add navbar

       JLabel title = new JLabel("Pending Forms", SwingConstants.CENTER); 
       
       add(Box.createVerticalStrut(10));
       title.setFont(new Font("Arial", Font.BOLD, 26)); 
       add(title); 
       
       // Panel that will hold all user entries 
       listPanel = new JPanel(); 
       listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); 
       
       // Make it scrollable 
       JScrollPane scroll = new JScrollPane(listPanel); 
       add(scroll, BorderLayout.CENTER); 
       
       refresh();
    }
    //---------------------------------------
    
    @Override
    public void refresh() {
        listPanel.removeAll(); 
        // clear old content 
        Map<User, ArrayList<Vehicle>> pendingVehicles= Admin.getPendingVehicles();
        Map<User, ArrayList<Job>> pendingJobs = Admin.getPendingJobs();
        //gets pending requests 

        for (Map.Entry<User, ArrayList<Vehicle>> entry : pendingVehicles.entrySet()) {
            ArrayList<Vehicle> vehicles = entry.getValue();
            User u = entry.getKey();  // get the User object
            
            for (Vehicle v : vehicles) {
                listPanel.add(createPendingCard(u, v)); 
                listPanel.add(Box.createVerticalStrut(10)); //This separates the boxes
            }
        }

        for (Map.Entry<User, ArrayList<Job>> entry : pendingJobs.entrySet()) {
            //String userId = entry.getKey();
            ArrayList<Job> jobs = entry.getValue();
            User u = entry.getKey();
            
            for (Job j : jobs) {
                listPanel.add(createPendingCard(u, j));
                listPanel.add(Box.createVerticalStrut(10)); //This separates the boxes
            }
        }

        listPanel.revalidate(); 
        listPanel.repaint(); 
    }
    //-------------------------------------

    //this created a UI for pending requests for vehicles
    private JPanel createPendingCard(User u, Vehicle v) {
        JPanel pendingCard = new JPanel(); 
        pendingCard.setLayout(new GridLayout(0, 1)); 
        pendingCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        pendingCard.add(new JLabel("Name: " + u.getUsername())); 
        pendingCard.add(new JLabel("User Type: " + u.getUserType())); 
        pendingCard.add(new JLabel("User Id: " + u.getUserId()));
        // pendingCard.add(new JLabel("Owner Id: " + ((Owner)u).getOwnerId()));
        pendingCard.add(new JLabel("Vin Number: " + v.getNumber())); 
        pendingCard.add(new JLabel("Licenese Plate: " + v.getLicensePlate())); 
        pendingCard.add(new JLabel("Model: " + v.getModel()));
        pendingCard.add(new JLabel("Make: " + v.getMake()));
        pendingCard.add(new JLabel("Year: " + v.getYear())); 
        pendingCard.add(new JLabel("Approximate Residency: " + v.approxTime()));
        pendingCard.add(new JLabel("Day Registered: " + v.getDayRegistered()));
       
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBackground(new Color(153, 255, 153));
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(255, 51, 51));

        pendingCard.add(acceptBtn);
        pendingCard.add(rejectBtn);
        
        acceptBtn.addActionListener(e -> {
            Admin.allowVehicle(u, v);
            JOptionPane.showMessageDialog(this, "Vehicle accepted.");
            refresh();
        });
    
        rejectBtn.addActionListener(e -> {
            Admin.rejectVehicle(u, v);
            JOptionPane.showMessageDialog(this, "Vehicle rejected.");
            refresh();

        });
      
        pendingCard.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        
        pendingCard.setBackground(new Color(153, 204, 255));
        pendingCard.setOpaque(true);

        return pendingCard;
    }
    //------------------------------------

    //this creates a pending request UI for jobs

    private JPanel createPendingCard(User u, Job j) {
        JPanel pendingCard = new JPanel(); 
        pendingCard.setLayout(new GridLayout(0, 1)); 
        pendingCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        pendingCard.add(new JLabel("Name: " + u.getUsername())); 
        pendingCard.add(new JLabel("User Type: " + u.getUserType())); 
        pendingCard.add(new JLabel("User Id: " + u.getUserId()));
        // pendingCard.add(new JLabel("Client Id: " + ((Client)u).getClientId()));
        pendingCard.add(new JLabel("Job Id: " + j.getJobId()));
        pendingCard.add(new JLabel("Job Description: " + j.getJobDescription())); 
        pendingCard.add(new JLabel("Deadline: " + j.getJobDeadline()));
        pendingCard.add(new JLabel("Duration: " + j.getApproximateJobDuration()));
       
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBackground(new Color(153, 255, 153));
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(255, 51, 51));
        pendingCard.add(acceptBtn);
        pendingCard.add(rejectBtn);
        
        acceptBtn.addActionListener(e -> {
            Admin.allowJob(u, j);
            JOptionPane.showMessageDialog(this, "Job accepted.");
            refresh();
        });
    
        rejectBtn.addActionListener(e -> {
            Admin.rejectJob(u, j);
            JOptionPane.showMessageDialog(this, "Job rejected.");
            refresh();

        });
      
        pendingCard.setBorder(BorderFactory.createLineBorder(Color.black, 3)); 

        pendingCard.setBackground(new Color(153, 204, 255));
        pendingCard.setOpaque(true);

        return pendingCard;
        
    }
    //-------------------------

}
