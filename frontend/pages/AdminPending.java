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

public class AdminPending extends JPanel implements Refreshable {

    private JPanel listPanel;

    public AdminPending(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {

        setLayout(new BorderLayout());

        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        JLabel title = new JLabel("Pending Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add(title, BorderLayout.SOUTH);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(listPanel);
        // add(scroll, BorderLayout.CENTER);

        // Load all requests already moved into adminVisible
        SwingUtilities.invokeLater(() -> {
            synchronized (VCServer.adminVisible) {
                if (!VCServer.adminVisible.isEmpty()) {
                    for(Request r: VCServer.adminVisible) {
                         JOptionPane.showMessageDialog(
                            this,
                            "You have pending requests.",
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

    @Override
    public void refresh() {
        listPanel.removeAll();

        synchronized (VCServer.adminVisible) {
            for (Request req : VCServer.adminVisible) {
                listPanel.add(createPendingCard(req));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createPendingCard(Request req) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        card.setBackground(new Color(153, 204, 255));
        card.setOpaque(true);
        // card.setMaximumSize(new Dimension(600, 200));

        String formatted = "<html>" + req.request.replace("||", "<br>") + "</html>"; // changing the format of the
                                                                                     // .toString()
        JLabel label = new JLabel(formatted);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(Box.createVerticalStrut(4));
        card.add(label);

        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBackground(new Color(153, 255, 153));

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(255, 51, 51));

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

        card.add(acceptBtn);
        card.add(rejectBtn);
        card.add(Box.createVerticalStrut(3));

        return card;
    }
}
