package pages;

import classes.User;
import classes.UserManager;
import classes.Vehicle;

import java.util.Map;
import javax.swing.*;
import java.util.ArrayList;

import classes.Request;
import classes.VCServer;

import javax.swing.*;
import java.awt.*;

public class AdminPending extends JPanel implements Refreshable {

    private JPanel listPanel;

    public AdminPending(JPanel cards, User user, UserManager users, Map<String, Refreshable> registry) {

        setLayout(new BorderLayout());

        add(new NavBar(cards, user, registry), BorderLayout.NORTH);

        JLabel title = new JLabel("Pending Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.SOUTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(listPanel);
        add(scroll, BorderLayout.CENTER);

        //Load all requests already moved into adminVisible
        synchronized (VCServer.adminVisible) {
            if (!VCServer.adminVisible.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You have pending job requests.",
                        "Pending Jobs",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            for (Request req : VCServer.adminVisible) {
                listPanel.add(createPendingCard(req));
            }
        }

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
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        card.setBackground(new Color(153, 204, 255));
        card.setOpaque(true);
        card.setMaximumSize(new Dimension(600, 200));

        JLabel label = new JLabel(req.request);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(label);

        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setBackground(new Color(153, 255, 153));

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(255, 51, 51));

        //Accept logic
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

        //Reject logic
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

        return card;
    }
}
