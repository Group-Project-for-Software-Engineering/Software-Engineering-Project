/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: SubmitJobPage.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program builds / controls the Submit Job page of the VCRTS system.
* It allows users to enter job information and submit the job for review. Submitted jobs are then added to the pending job registry for approval by an admin.
*/

package pages;

import classes.Admin;
import classes.Job;
import classes.PlaceHolderTextField;
import classes.User;
import classes.UserManager;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import classes.Client;


// ---------------------------------------------------------------
// Submit Job Page
public class SubmitJobPage extends JPanel implements Refreshable {

    // Date Format
    private static final DateTimeFormatter DEADLINE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Look up vehicles by JobID.
    private final Map<String, Job> JOB_BY_JOBID = new LinkedHashMap<>();
    private final JTextArea STATUS_AREA = new JTextArea(6, 50);

    private final User user;

    private JTextField jobDescription;
    private JTextField jobDuration;
    private JTextField jobDeadline;

    private JTextField clientIdField;

    // ---------------------------------------------------------------
       // ---------------------------------------------------------------
    // constructor: sets user + user manager + registry
    public SubmitJobPage(JPanel cards, User user, Map<String, Refreshable> registry, UserManager users) {
        setLayout(new BorderLayout());
        this.user = user;

        // NavBar
        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        JPanel splitPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(50, 75, 155));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel leftTitle = new JLabel("Submit Job");
        leftTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftTitle.setForeground(Color.WHITE);
        leftTitle.setFont(new Font("Arial", Font.PLAIN, 50));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(leftTitle);
        leftPanel.add(Box.createVerticalGlue());

        JPanel jobForm = new JPanel();
        jobForm.setBackground(new Color(242, 245, 249));
        jobForm.setLayout(new BoxLayout(jobForm, BoxLayout.Y_AXIS)); // center everything vertically

        JLabel jobTitle = new JLabel("Enter Job information");
        jobTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jobTitle.setForeground(new Color(65, 105, 255));
        jobTitle.setFont(new Font("Arial", Font.PLAIN, 36));

        //JLabel clientId = new JLabel("Your Client Id: " + ((Client)user).getClientId());
       // clientId.setAlignmentX(Component.CENTER_ALIGNMENT);
        //clientId.setForeground(new Color(65, 105, 255));
       // clientId.setFont(new Font("Arial", Font.PLAIN, 36));

        clientIdField = new PlaceHolderTextField("Enter client id for this job:", 36); // adds more graphics to regular textfield
        clientIdField.setMaximumSize(clientIdField.getPreferredSize());
        clientIdField.setAlignmentX(Component.CENTER_ALIGNMENT);

        jobDescription = new PlaceHolderTextField("Job Description                    (Enter Job Description)", 36); // adds more graphics to regular textfield
        jobDescription.setMaximumSize(jobDescription.getPreferredSize());
        jobDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        jobDuration = new PlaceHolderTextField("Approximate Job Duration   (in terms of hours)", 36); // adds more graphics to regular textfield
        jobDuration.setMaximumSize(jobDuration.getPreferredSize());
        jobDuration.setAlignmentX(Component.CENTER_ALIGNMENT);

        jobDeadline = new PlaceHolderTextField("Deadline                               (yyyy-mm-dd hh:mm:ss)", 36); // adds more graphics to regular textfield
        jobDeadline.setMaximumSize(jobDeadline.getPreferredSize());
        jobDeadline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setBackground(new Color(77, 163, 255));
        submitBtn.setForeground(Color.DARK_GRAY);

        
        //jobForm.add(clientId);
        jobForm.add(Box.createVerticalGlue());
        jobForm.add(jobTitle);
        jobForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        jobForm.add(clientIdField);
        jobForm.add(createFormatLabel("Enter client id for this job"));
        //jobForm.add(createFormatLabel("Your Client Id: " + ((Client)user).getClientId()));
        jobForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        jobForm.add(jobDescription);
        jobForm.add(createFormatLabel("Enter Job Description"));
        jobForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        jobForm.add(jobDuration);
        jobForm.add(createFormatLabel("In terms of hours"));
        jobForm.add(Box.createVerticalStrut(20)); // creates padding between elements
        jobForm.add(jobDeadline);
        jobForm.add(createFormatLabel("Format: yyyy-mm-dd hh:mm:ss"));
        jobForm.add(Box.createVerticalStrut(30)); // creates padding between elements
        jobForm.add(submitBtn);
        jobForm.add(Box.createVerticalGlue());

        splitPanel.add(leftPanel);
        splitPanel.add(jobForm);
        add(splitPanel, BorderLayout.CENTER);

        submitBtn.addActionListener(e -> {
            String idText = jobDescription.getText().trim();
            String durationText = jobDuration.getText().trim();
            String deadlineText = jobDeadline.getText().trim();
            String id = clientIdField.getText().trim();

            if(idText.isEmpty() || durationText.isEmpty() || deadlineText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
                return;
            }

            double duration; 
            try { 
                duration = Double.parseDouble(durationText); 
                if (duration <= 0) { 
                    JOptionPane.showMessageDialog(this, "Duration must be a positive number."); 
                    return; 
                } 
            } 
            catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Duration must be an integer or decimal."); 
                return; 
            }

            LocalDateTime deadline; 
            try { 
                deadline = LocalDateTime.parse(deadlineText, DEADLINE_FORMAT); 
            } 
            catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Deadline must be in the format: yyyy-mm-dd hh:mm:ss\nExample: 2024-03-09 17:45:00"); 
                return; 
            }

            //make new vehicle from form information
            Job j = new Job(idText, duration, deadline, user.getUserId(), id);
            
            //add user and job to pending so admin can look at it 
            UserManager.updateJobFile(j);
            Admin.addJob(j);
            ((Client)user).addJob(j);
            JOptionPane.showMessageDialog(this, "Submitted job application.");

            refresh();
        });
    }

    // ---------------------------------------------------------------
    // refreshes submit job page
    @Override
    public void refresh() {
        jobDeadline.setText("");
        jobDuration.setText("");
        jobDescription.setText("");
        clientIdField.setText("");
    }

    private JLabel createFormatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.ITALIC, 12));
        label.setForeground(Color.GRAY); // Makes it look like helper text
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

}



