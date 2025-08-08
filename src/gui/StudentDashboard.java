package gui;

import dao.TransDAO;
import user.Student;
import gui.panels.*;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JPanel {
    private final Student student;
    private JLabel subtitle1, subtitle2, subtitle3;

    public StudentDashboard(Student student) {
        this.student = student;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Top Panel Begins
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(181, 63, 63));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        topPanel.setPreferredSize(new Dimension(800, 160));

        // Text + SubText
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("Welcome, " + student.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitles
        subtitle1 = new JLabel("A book is a dream you hold in your hands. - Yau Mun");
        subtitle1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle1.setForeground(Color.WHITE);
        subtitle1.setAlignmentX(Component.LEFT_ALIGNMENT);

        subtitle2 = new JLabel("Book Borrowed: " + TransDAO.fetchTotalBookBorrowed(student.getID()));
        subtitle2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitle2.setForeground(Color.WHITE);
        subtitle2.setAlignmentX(Component.LEFT_ALIGNMENT);

        subtitle3 = new JLabel("Book Overdue: " + TransDAO.fetchTotalBookOverDue(student.getID()));
        subtitle3.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitle3.setForeground(Color.WHITE);
        subtitle3.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(subtitle1);
        textPanel.add(subtitle2);
        textPanel.add(subtitle3);
        
        // Sign Out Button Setup
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setFocusPainted(false);
        signOutButton.setBackground(new Color(79, 109, 217));

        signOutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signOutButton.setForeground(Color.WHITE);
        signOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signOutButton.setMargin(new Insets(2, 10, 2, 10));
        signOutButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        signOutButton.setPreferredSize(new Dimension(100, 35));

        // Hovering Mouse
        signOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signOutButton.setBackground(new Color(79, 120, 185));
                
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signOutButton.setBackground(new Color(79, 109, 217));
                
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

        // Add to topPanel 
        topPanel.add(textPanel, BorderLayout.WEST);
        topPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Book and Transaction Tab Panels
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Books", new BooksPanel(getName()));
        tabbedPane.add("Borrow Transactions", new TransactionsPanel("Student"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(233, 203, 197));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        centerPanel.add(tabbedPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }
}
