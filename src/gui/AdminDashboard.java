package gui;

import user.Admin;
import gui.panels.*;

import javax.swing.*;
import java.awt.*;
import org.w3c.dom.css.RGBColor;

public class AdminDashboard extends JPanel {
    private final Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Box Layout is used with Y_AXIS for text alignnment
        topPanel.setBackground(new Color(0x6ad7fb));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        topPanel.setPreferredSize(new Dimension(800, 180)); // Adjust height as needed

        // Left Text Panel
        JPanel txtJPanel = new JPanel();
        txtJPanel.setLayout(new BoxLayout(txtJPanel, BoxLayout.Y_AXIS));
        txtJPanel.setOpaque(false);

        // Title
        JLabel title = new JLabel("Welcome, " + admin.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sign Out button
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setFocusPainted(false);
        signOutButton.setBackground(new Color(0xD9534F)); // Bootstrap-style red
        signOutButton.setForeground(Color.WHITE);
        signOutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signOutButton.setPreferredSize(new Dimension(100, 35));

        // Right Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(signOutButton);

        JLabel subtitle = new JLabel("Admin Control Panel");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(Color.WHITE);

        txtJPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtJPanel.add(title);
        txtJPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        txtJPanel.add(subtitle);

        topPanel.add(txtJPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Book/Member/Transaction Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Books", new BooksPanel());
        tabbedPane.add("Members", new MembersPanel());
        tabbedPane.add("Transactions", new TransactionsPanel());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }
}
