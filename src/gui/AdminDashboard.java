package gui;

import user.Admin;
import gui.panels.*;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    private final Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Top Panel Begins
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(63, 81, 181));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        topPanel.setPreferredSize(new Dimension(800, 160));

        // Text + SubText
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("Welcome, " + admin.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitles
        JLabel subtitle1 = new JLabel("Books Borrowed: ");
        subtitle1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle1.setForeground(Color.WHITE);
        subtitle1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle2 = new JLabel("Members Registered: ");
        subtitle2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle2.setForeground(Color.WHITE);
        subtitle2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle3 = new JLabel("Transactions Owed: ");
        subtitle3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle3.setForeground(Color.WHITE);
        subtitle3.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(subtitle1);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitle2);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitle3);

        // Sign Out Button Setup
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setFocusPainted(false);
        signOutButton.setBackground(new Color(0xD9534F));

        signOutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signOutButton.setForeground(Color.WHITE);

        signOutButton.setMargin(new Insets(2, 10, 2, 10));
        signOutButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        signOutButton.setPreferredSize(new Dimension(100, 35));

        // Hovering Mouse
        signOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signOutButton.setBackground(new Color(0xB9534F));
                
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signOutButton.setBackground(new Color(0xD9534F));
                
            }
        });

        // Sign Out Function
        signOutButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose(); // Dispose this window

            LoginMain loginMain = new LoginMain(); // Redirect to Login Page
            loginMain.setVisible(true);
            JOptionPane.showMessageDialog(null,"Signed out successfully!");
        });

        JPanel rightButtonPanel = new JPanel(new BorderLayout());
        rightButtonPanel.setOpaque(false);
        rightButtonPanel.setPreferredSize(new Dimension(120, 40));
        rightButtonPanel.add(signOutButton, BorderLayout.NORTH);

        // === Add to topPanel ===
        topPanel.add(textPanel, BorderLayout.WEST);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        // === Add topPanel to main panel ===
        add(topPanel, BorderLayout.NORTH);

        // Book/Member/Transaction tab Panels
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Books", new BooksPanel(getName()));
        tabbedPane.add("Members", new MembersPanel());
        tabbedPane.add("Transactions", new TransactionsPanel());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(197, 202, 233));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }
}
