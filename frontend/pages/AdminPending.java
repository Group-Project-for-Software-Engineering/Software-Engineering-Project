/* Project: Vehicular Cloud Real Time System (VCRTS)
 * Class: AdminPending.java
 * Authors: Group 2 (Justin Cracchiolo, Lauren Rodriguez, David Choi, Tristan Huertas, Ivan Lin, Anthony Vallejo, Sebastian Villavicencio)
 * Date: March 2026
 * This class implements the admin view for managing pending job requests.
 */
package pages;

import classes.Request;
import classes.User;
import classes.UserManager;
import classes.VCServer;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

// ---------------------------------------------------------------
public class AdminPending extends JPanel implements Refreshable {

    private JPanel listPanel;

    // ---------------------------------------------------------------
    public AdminPending(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {

        setLayout(new BorderLayout());

        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        JLabel title = new JLabel("Pending Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        // FlowLayout used to display the cards in a row
        // allowing for cards to automatically move to the next row when previous row is filled
        listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        //listPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        //listPanel.setLayout(new GridLayout(0,2,10,10));
        JScrollPane scroll = new JScrollPane(listPanel);
        // disables horizontal scrollbar
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // allows vertical scrollbar to be shown when needed
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // prevents each panel from stretching too wide 
        // allows cards to wrap to new rows when needed
        listPanel.setPreferredSize(new Dimension(0, 0));
        // Load all requests already moved into adminVisible
        
        SwingUtilities.invokeLater(() -> {
            synchronized (VCServer.adminVisible) {
                if (!VCServer.adminVisible.isEmpty()) {
                   for(Request r: VCServer.adminVisible) {
                         JOptionPane.showMessageDialog(
                            this,
                            "You have a new pending request.",
                            "Pending request",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        viewPanel.add(title);
        viewPanel.add(scroll);

        add(viewPanel, BorderLayout.CENTER);

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ---------------------------------------------------------------
    @Override
    public void refresh() {
        listPanel.removeAll();

        synchronized (VCServer.adminVisible) {
            for (Request req : VCServer.adminVisible) {
                JPanel card = createPendingCard(req);
                listPanel.add(card);
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // ---------------------------------------------------------------
    private JPanel createPendingCard(Request req) {

        JPanel card = new JPanel();
        // sets a consistent size for cards to be set to
        card.setPreferredSize(new Dimension(340, 280));
        // sets the layout of the card to be a vertical box layout
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setBackground(new Color(153, 204, 255));
        card.setOpaque(true);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        String formatted = "<html>" + req.request.replace("||", "<br>") + "</html>"; // changing the format of the
                                                                                     // .toString()
        JLabel label = new JLabel(formatted);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(Box.createVerticalStrut(4));
        label.setMaximumSize(new Dimension(320, 100));
        card.add(label);

        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBackground(new Color(153, 255, 153));
        acceptBtn.setPreferredSize(new Dimension(110, 36));
        acceptBtn.setFont(new Font("Arial", Font.BOLD, 20));

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(255, 51, 51));
        rejectBtn.setPreferredSize(new Dimension(110, 36));
        rejectBtn.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonRow.setOpaque(false);

        // Accept logic
        acceptBtn.addActionListener(e -> {
            synchronized (VCServer.adminVisible) {
                VCServer.adminVisible.remove(req);
            }
            synchronized (req) {
                req.decision = "ACCEPT";
                req.notify();
            }
            refresh();
        });

        // Reject logic
        rejectBtn.addActionListener(e -> {
            synchronized (VCServer.adminVisible) {
                VCServer.adminVisible.remove(req);
            }
            synchronized (req) {
                req.decision = "REJECT";
                req.notify();
            }
            refresh();
        });

        buttonRow.add(acceptBtn);
        buttonRow.add(Box.createHorizontalStrut(12));
        buttonRow.add(rejectBtn);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(Box.createVerticalStrut(6));
        card.add(buttonRow);
        card.add(Box.createVerticalStrut(3));

        return card;
    }
}
