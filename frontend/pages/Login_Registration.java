/* Project: Vehicular Cloud Real Time System (VCRTS)
* Class: Login_Registration.java
* Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
* Date: February 2026
* This program builds and controls the main Swing GUI for VCRTS, including login, registration, and home navigation using CardLayout. 
* It connects to UserManager to support user registration and login.
 */
package pages;

import classes.PlaceHolderPasswordField;
import classes.PlaceHolderTextField;
import classes.User;
import classes.UserManager;
import classes.VCServer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
// class that controls the login and registration page
public class Login_Registration {

    // once a person logs in, this holds all information for that user
    public static User currentUser;

    // ---------------------------------------------------------------
    // main method that generates the GUI for the client + owners
    public static void main(String[] args) {
        VCServer server = new VCServer();
        Thread serverThread = new Thread(server);
        serverThread.start();

        VCServer.startPendingListener();

        // create UI when invoked
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    // ---------------------------------------------------------------
    // method that creates the GUI for the client + owners
    private static void createAndShowGUI() {

        // management tool for keeping track of who creates accounts.
        UserManager userManager = new UserManager();
        userManager.loadVehiclesFromFile(); // this tells the program to load the vehicles from the file for the current user
        userManager.loadJobsFromFile(); // this tells the program to load the jobs from the file for the current user
        //userManager.loadPendingRequests(); //this tells the program to load pending requests for an admin to look at 
        //UserManager.loadCompletedTransactions(); //gets correct job id even for rejected jobs

        JFrame frame = new JFrame("VCRTS App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // CardLayout drives screen switching between Login/Home/Register.
        JPanel cards = new JPanel(new CardLayout());

        // Creation of different screens
        JPanel aboutPage = new JPanel(new BorderLayout());

        JPanel loginPage = new JPanel(new BorderLayout());

        JPanel registerPage = new JPanel(new BorderLayout());

        cards.add(aboutPage, "about");
        cards.add(loginPage, "login");
        cards.add(registerPage, "register");
        // --------------------------------------------

        //About page 
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBackground(new Color(50, 75, 155));
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS)); // center everything vertically
        aboutPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
        aboutPanel.setPreferredSize(new Dimension(500, 800));

        JLabel aboutTitle = new JLabel("VCRTS");
        aboutTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutTitle.setForeground(Color.white);
        aboutTitle.setFont(new Font("Arial", Font.PLAIN, 40));

        String text = "The purpose of the Vehicular Cloud Real-Time System (VCRTS) is to utilize the unused computing power of parked vehicles by pooling their computational resources and renting them out to consumers who require computing services. \n\nVehicles parked in a dedicated parking lot serve as possible computing nodes. \n\nVCRTS manages job execution, resource allocation, real-time monitoring, and reporting to ensure everything is running smoothly. \n\nThe VCRTS attempts to optimize resources by pulling computational power from unused vehicles. \n\nThis helps maximize computing power and minimize load on other systems. \n\nAn Owner registers vehicles for use and a Client can rent a vehicle and/or can submit a job.";
        JTextArea aboutInfo = new JTextArea(text);
        aboutInfo.setLineWrap(true);
        aboutInfo.setWrapStyleWord(true);
        aboutInfo.setEditable(false);
        aboutInfo.setOpaque(false);
        aboutInfo.setBackground(new Color(0, 0, 0, 0));
        aboutInfo.setBorder(null);
        aboutInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutInfo.setForeground(Color.white);
        aboutInfo.setFont(new Font("Arial", Font.PLAIN, 20));
        aboutInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutInfo.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
        aboutInfo.setOpaque(false);
        aboutInfo.setForeground(Color.WHITE);
        aboutPanel.add(Box.createVerticalGlue());
        aboutPanel.add(aboutTitle);
        aboutPanel.add(Box.createVerticalStrut(100));
        aboutPanel.add(aboutInfo);
        aboutPanel.add(Box.createVerticalGlue());

        JPanel startPanel = new JPanel();
        startPanel.setBackground(new Color(242, 245, 249));
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.setPreferredSize(new Dimension(600, 800));

        JLabel startLabel = new JLabel("Get Started");
        startLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startLabel.setForeground(new Color(65, 105, 255));
        startLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        JButton toLoginButton = new JButton("Login");
        toLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toLoginButton.setFont(new Font("Arial", Font.PLAIN, 30));
        toLoginButton.setBackground(new Color(77, 163, 255));
        toLoginButton.setForeground(Color.DARK_GRAY);

        JButton toRegisterButton = new JButton("Create an Account");
        toRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toRegisterButton.setFont(new Font("Arial", Font.PLAIN, 30));
        toRegisterButton.setBackground(new Color(77, 163, 255));
        toRegisterButton.setForeground(Color.DARK_GRAY);

        //register button swithces pages
        toRegisterButton.addActionListener(e -> {
            CardLayout c = (CardLayout) cards.getLayout();
            c.show(cards, "register");
        });

        aboutPanel.add(Box.createVerticalGlue());
        aboutPanel.add(aboutTitle);
        aboutPanel.add(Box.createVerticalStrut(30)); // creates padding between elements
        aboutPanel.add(aboutInfo);
        aboutPanel.add(Box.createVerticalGlue());

        startPanel.add(Box.createVerticalGlue());
        startPanel.add(startLabel);
        startPanel.add(Box.createVerticalStrut(10));
        startPanel.add(toLoginButton);
        startPanel.add(Box.createVerticalStrut(30));
        startPanel.add(toRegisterButton);
        startPanel.add(Box.createVerticalGlue());

        aboutPanel.setOpaque(true);

        aboutPage.add(aboutPanel, BorderLayout.WEST);
        aboutPage.add(startPanel, BorderLayout.CENTER);
        // -------------------------------------------

        // Login page
        JPanel loginSidePanel = new JPanel();
        loginSidePanel.setBackground(new Color(50, 75, 155));
        loginSidePanel.setLayout(new BoxLayout(loginSidePanel, BoxLayout.Y_AXIS)); // center everything vertically
        loginSidePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
        loginSidePanel.setPreferredSize(new Dimension(500, 800));

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(242, 245, 249));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.setPreferredSize(new Dimension(600, 800));

        JLabel loginLabel = new JLabel("Sign In");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setForeground(new Color(65, 105, 255));
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        JTextField usernameTextField = new PlaceHolderTextField("Username", 20);
        usernameTextField.setMaximumSize(usernameTextField.getPreferredSize());
        usernameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hides password on login page and add a button to show password when pressed
        JPasswordField passwordTextField = new PlaceHolderPasswordField("Password", 20);
        passwordTextField.setMaximumSize(passwordTextField.getPreferredSize());
        passwordTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        char loginEchoChar = passwordTextField.getEchoChar();
        JButton showLoginPasswordButton = new JButton("Show Password");
        showLoginPasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showLoginPasswordButton.setMargin(new Insets(2, 8, 2, 8));
        showLoginPasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                passwordTextField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                passwordTextField.setEchoChar(loginEchoChar);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                passwordTextField.setEchoChar(loginEchoChar);
            }

        });

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 30));
        loginButton.setBackground(new Color(77, 163, 255));
        loginButton.setForeground(Color.DARK_GRAY);

        // Register button
        JButton registerButton = new JButton("Create an Account");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFont(new Font("Arial", Font.PLAIN, 30));

        // Validate credentials before switching to Home.
        loginButton.addActionListener(e -> {
            String user = usernameTextField.getText().trim();
            String userPassword = new String(passwordTextField.getPassword());

            if (userManager.login(user, userPassword)) {
                currentUser = userManager.getUser(user); // this tells the program the person who is logged in

                // based on kind of user, navbar is different
                if (currentUser.getUserType().equals("Owner")) {//owner navbar
                    CardLayout cl = (CardLayout) cards.getLayout();

                    Map<String, Refreshable> registry = new HashMap<>();
                    HomePage home = new HomePage(cards, currentUser, userManager, registry);
                    SchedulePage schedule = new SchedulePage(cards, currentUser, registry);
                    OfferVehiclePage offer = new OfferVehiclePage(cards, currentUser, registry, userManager);
                    Settings settings = new Settings(cards, currentUser, registry);

                    // These cards can now be refreshed when looked up in the hashmap
                    registry.put("home", home);
                    registry.put("schedule", schedule);
                    registry.put("offerVehicle", offer);
                    registry.put("settings", settings);

                    cards.add(home, "home");
                    cards.add(schedule, "schedule");
                    cards.add(offer, "offerVehicle");
                    cards.add(settings, "settings");
                    cl.show(cards, "home");
                } 
                else if (currentUser.getUserType().equals("Admin")) {//admin navbar
                    CardLayout cl = (CardLayout) cards.getLayout();

                    Map<String, Refreshable> registry = new HashMap<>();
                    AdminHome home = new AdminHome(cards, currentUser, userManager, registry);
                    AdminPending pending = new AdminPending(cards, currentUser, userManager, registry);
                    SchedulePage schedule = new SchedulePage(cards, currentUser, registry);
                    Settings settings = new Settings(cards, currentUser, registry);

                    // These cards can now be refreshed when looked up in the hashmap
                    registry.put("adminHome", home);
                    registry.put("pending", pending);
                    registry.put("schedule", schedule);
                    registry.put("settings", settings);

                    cards.add(home, "adminHome");
                    cards.add(pending, "pending");
                    cards.add(schedule, "schedule");
                    cards.add(settings, "settings");
                    cl.show(cards, "adminHome");
                } 
                else { // Client navbar
                    CardLayout cl = (CardLayout) cards.getLayout();

                    Map<String, Refreshable> registry = new HashMap<>();
                    HomePage home = new HomePage(cards, currentUser, userManager, registry);
                    SchedulePage schedule = new SchedulePage(cards, currentUser, registry);
                    SubmitJobPage submit = new SubmitJobPage(cards, currentUser, registry, userManager);
                    Settings settings = new Settings(cards, currentUser, registry);

                    // These cards can now be refreshed when looked up in the hashmap
                    registry.put("home", home);
                    registry.put("schedule", schedule);
                    registry.put("submitJob", submit);
                    registry.put("settings", settings);

                    cards.add(home, "home");
                    cards.add(schedule, "schedule");
                    cards.add(submit, "submitJob");
                    cards.add(settings, "settings");
                    cl.show(cards, "home");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        // Go to register screen.
        registerButton.addActionListener(e -> {
            meetAndSwitch(cards, loginPage, loginSidePanel, loginPanel, "register");
        });

        // Title on sidebar
        JLabel title_label = new JLabel("VCRTS");
        title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        title_label.setForeground(Color.WHITE);
        title_label.setFont(new Font("Arial", Font.PLAIN, 50));

        // Add all of the elements
        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(loginLabel);
        loginPanel.add(Box.createVerticalStrut(30)); // creates padding between elements
        loginPanel.add(usernameTextField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(passwordTextField);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(showLoginPasswordButton);
        loginPanel.add(Box.createVerticalStrut(30));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(Box.createVerticalGlue());

        loginSidePanel.add(Box.createVerticalGlue());
        loginSidePanel.add(title_label);
        loginSidePanel.add(Box.createVerticalStrut(30));
        loginSidePanel.add(registerButton);
        loginSidePanel.add(Box.createVerticalGlue());

        loginPage.add(loginPanel, BorderLayout.CENTER);
        loginPage.add(loginSidePanel, BorderLayout.WEST);

        loginPanel.setOpaque(true); // allows changing color
        loginSidePanel.setOpaque(true);

        // Set up the toLoginButton listener now that text fields are created
        toLoginButton.addActionListener(e -> {
            CardLayout c = (CardLayout) cards.getLayout();
            c.show(cards, "login");
            // Clear login fields when returning to login page
            usernameTextField.setText("");
            passwordTextField.setText("");
        });

        // --------------------------------------------
        // Register screen
        JPanel registerSidePanel = new JPanel();
        registerSidePanel.setBackground(new Color(50, 75, 155));
        registerSidePanel.setLayout(new BoxLayout(registerSidePanel, BoxLayout.Y_AXIS));
        registerSidePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerSidePanel.setPreferredSize(new Dimension(500, 800));

        JLabel registerTitleLabel = new JLabel("VCRTS");
        registerTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerTitleLabel.setForeground(Color.WHITE);
        registerTitleLabel.setFont(new Font("Arial", Font.PLAIN, 50));

        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(new Color(242, 245, 249));
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerPanel.setPreferredSize(new Dimension(600, 800));

        JLabel registerHeader = new JLabel("Register");
        registerHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerHeader.setForeground(new Color(65, 105, 255));
        registerHeader.setFont(new Font("Arial", Font.PLAIN, 40));

        JTextField regUsernameField = new PlaceHolderTextField("Username    (Letters and/or Numbers)", 20);
        regUsernameField.setMaximumSize(regUsernameField.getPreferredSize());
        regUsernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField regEmailField = new PlaceHolderTextField("Email            (username@domain.com)", 20);
        regEmailField.setMaximumSize(regEmailField.getPreferredSize());
        regEmailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField regPasswordField = new PlaceHolderPasswordField("Password", 20);
        regPasswordField.setMaximumSize(regPasswordField.getPreferredSize());
        regPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField regConfirmPasswordField = new PlaceHolderPasswordField("Confirm Password", 20);
        regConfirmPasswordField.setMaximumSize(regConfirmPasswordField.getPreferredSize());
        regConfirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton showRegistrationPasswordButton = new JButton("Show Password");
        showRegistrationPasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showRegistrationPasswordButton.setMargin(new Insets(2, 8, 2, 8));
        showRegistrationPasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                regPasswordField.setEchoChar((char) 0);
                regConfirmPasswordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                regPasswordField.setEchoChar(loginEchoChar);
                regConfirmPasswordField.setEchoChar(loginEchoChar);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                regPasswordField.setEchoChar(loginEchoChar);
                regConfirmPasswordField.setEchoChar(loginEchoChar);
            }

        });

        String[] userTypes = {"Client", "Owner"};
        // Create the JComboBox (the dropdown)
        JComboBox<String> comboBox = new JComboBox<>(userTypes);
        Dimension comboBoxSize = new Dimension(140, 28);
        comboBox.setPreferredSize(comboBoxSize);
        comboBox.setMaximumSize(comboBoxSize);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        DefaultListCellRenderer centeredRenderer = new DefaultListCellRenderer();
        centeredRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        comboBox.setRenderer(centeredRenderer);
        comboBox.setSelectedIndex(-1);

        JLabel registerAsLabel = new JLabel("Register As:");
        registerAsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel registerAsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        registerAsRow.setLayout(new BoxLayout(registerAsRow, BoxLayout.Y_AXIS));
        registerAsRow.setOpaque(false);
        registerAsRow.add(registerAsLabel);
        registerAsRow.add(comboBox);
        registerAsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerAsRow.add(registerAsLabel);
        registerAsRow.add(Box.createVerticalStrut(8));
        registerAsRow.add(comboBox);

        JButton submitRegisterButton = new JButton("Create Account");
        submitRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitRegisterButton.setFont(new Font("Arial", Font.PLAIN, 30));
        submitRegisterButton.setBackground(new Color(77, 163, 255));
        submitRegisterButton.setForeground(Color.DARK_GRAY);

        JButton backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 24));

        // Create a new account in memory and return to Login.
        submitRegisterButton.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String email = regEmailField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String confirmPassword = new String(regConfirmPasswordField.getPassword());

            String userType = (String) comboBox.getSelectedItem();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }

            if (userType == null || userType.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Please select a user type.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.");
                regPasswordField.setText("");
                regConfirmPasswordField.setText("");
                return;
            }

            if (userManager.register(username, password, email, userType)) { // creates a User with username and passwords and
                // puts them into system
                JOptionPane.showMessageDialog(frame, "User " + username + " created!");
                regUsernameField.setText("");
                regEmailField.setText("");
                regPasswordField.setText("");
                regConfirmPasswordField.setText("");
                comboBox.setSelectedIndex(-1);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "login");
                return;
            } else { // checks if someone with repeat username
                JOptionPane.showMessageDialog(frame, "Username already exists. Try again");
                regUsernameField.setText("");
                regEmailField.setText("");
                regPasswordField.setText("");
                regConfirmPasswordField.setText("");
                comboBox.setSelectedIndex(-1);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "register");
                return;
            }
        });

        // Return without changes. Textfield reset when user clicks back to login.
        backToLoginButton.addActionListener(e -> {
            regUsernameField.setText("");
            regEmailField.setText("");
            regPasswordField.setText("");
            regConfirmPasswordField.setText("");
            comboBox.setSelectedIndex(-1);
            meetAndSwitch(cards, registerPage, registerPanel, registerSidePanel, "login");
        });

        registerPanel.add(Box.createVerticalGlue());
        registerPanel.add(registerHeader);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(regUsernameField);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(regEmailField);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(regPasswordField);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(regConfirmPasswordField);
        registerPanel.add(Box.createVerticalStrut(20));
        registerPanel.add(showRegistrationPasswordButton);
        registerPanel.add(Box.createVerticalStrut(20));

        registerPanel.add(registerAsRow);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(Box.createVerticalGlue());

        registerPanel.add(submitRegisterButton);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(Box.createVerticalGlue());

        registerSidePanel.setOpaque(true);

        registerSidePanel.add(Box.createVerticalGlue()); // pushes content down
        registerSidePanel.add(registerTitleLabel);
        registerSidePanel.add(Box.createVerticalStrut(30));
        registerSidePanel.add(backToLoginButton);
        registerSidePanel.add(Box.createVerticalGlue()); // pushes content up

        registerPage.add(registerPanel, BorderLayout.CENTER);
        registerPage.add(registerSidePanel, BorderLayout.EAST);
        registerPanel.setOpaque(true);

        frame.add(cards);
        frame.setVisible(true);

    }

    // animations from login => register and register => login
    public static void meetAndSwitch(JPanel cards, JPanel currentCard, JPanel leftPanel, JPanel rightPanel,
            String nextCardName) {
        int halfWidth = currentCard.getWidth() / 2;

        // Temporarily disable layout on the CURRENT card
        currentCard.setLayout(null);

        // Starting positions
        leftPanel.setLocation(0, 0);
        rightPanel.setLocation(halfWidth, 0);

        Timer timer = new Timer(5, null);
        timer.addActionListener(e -> {
            // Move left panel right
            leftPanel.setLocation(leftPanel.getX() + 10, 0);

            // Move right panel left
            rightPanel.setLocation(rightPanel.getX() - 10, 0);

            currentCard.repaint();

            // Stop when they meet in the middle
            if (rightPanel.getX() <= halfWidth / 2) {
                timer.stop();

                currentCard.removeAll();
                currentCard.setLayout(new BorderLayout());

                // Put the panels back where they originally belonged
                // Login page uses WEST + CENTER
                // Register page uses CENTER + EAST
                if (nextCardName.equals("register")) {
                    currentCard.add(leftPanel, BorderLayout.WEST);
                    currentCard.add(rightPanel, BorderLayout.CENTER);
                } else {
                    currentCard.add(leftPanel, BorderLayout.CENTER);
                    currentCard.add(rightPanel, BorderLayout.EAST);
                }
                currentCard.revalidate();
                currentCard.repaint();

                // Switch to the next card
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, nextCardName);
            }
        });
        timer.start();
    }
    // ------------------------------

}
