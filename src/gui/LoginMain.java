package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;

import dao.UserDAO;

import user.Admin;
import user.Student;
import user.User;

public class LoginMain extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginMain() {

        // JFrame Icon
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        URL iconURL = getClass().getClassLoader().getResource("resources/bookicon.png");

        // Check whether it can load the Icon (top left of the JFrame)
        if (iconURL != null) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
        } else {
            System.err.println("Icon resource not found!");
        }

        // JFrame Setup
        getContentPane().setBackground(new Color(217, 250, 250));
        setTitle("O' Days Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized by default
        add(createFormBox());
        setSize(screenSize);
        setResizable(false); // Not Resizable
        setVisible(true); // Show GUI
    }

    // Create the Box for Login
    private JPanel createFormBox() {
        JPanel formBox = new JPanel();
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setBackground(new Color(43, 72, 101));
        formBox.setBorder(new LineBorder(Color.GRAY, 2, true)); // Box border only

        // Single point of dimension control
        Dimension boxSize = new Dimension(600, 350); // Size of Panel Box
        formBox.setPreferredSize(boxSize);
        formBox.setMaximumSize(boxSize);
        formBox.setMinimumSize(boxSize);

        // Title
        JLabel title = new JLabel("Ol' Days Library Login", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(143, 227, 207));
        formBox.add(Box.createRigidArea(new Dimension(0, 20)));
        formBox.add(title);
        formBox.add(Box.createRigidArea(new Dimension(0, 25)));

        // Fields
        usernameField = new JTextField();
        formBox.add(createField("Username:", usernameField));

        passwordField = new JPasswordField();
        formBox.add(createField("Password:", passwordField));

        // Login button
        JButton loginButton = new JButton("Login");

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set Button Size
        Dimension buttonSize = new Dimension(200, 50);  
        loginButton.setPreferredSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);
        loginButton.setMinimumSize(buttonSize);

        // Set Button Colour
        loginButton.setForeground(new Color(143, 227, 207));              
        loginButton.setBackground(new Color(0, 43, 91));                  
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(37, 109, 133), 2)); // Accent border

        // cursor:pointer; xD
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        
        // Use "Enter" to submit the button (Request by Yau Mun)
        loginButton.setFocusable(true);
        getRootPane().setDefaultButton(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 43, 91));
        registerButton.setForeground(new Color(143, 227, 207));
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.setPreferredSize(buttonSize);
        registerButton.setMaximumSize(buttonSize);
        registerButton.setMinimumSize(buttonSize);

        // Hovering "Animation"
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(43, 72, 101)); // Hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 43, 91)); // Default
            }

        });

        // Register Button!
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> {

            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(formBox);
            topFrame.dispose(); // Close login window

            RegisterMain registerMain = new RegisterMain(); // Launch registration
            registerMain.setVisible(true);

        });
        
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(43, 72, 101));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(0, 43, 91)); 
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formBox.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.setBorder(new EmptyBorder(0, 80, 0, 0));
        formBox.add(buttonPanel);

        return formBox;

    }

    // Text Fields
    private JPanel createField(String labelText, JComponent inputComponent) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setMaximumSize(new Dimension(500, 50));
        fieldPanel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(80, 30));
        label.setForeground(new Color(143, 227, 207));
        label.setFont(new Font("Consolas", Font.PLAIN, 16));

        if (inputComponent instanceof JTextField) {
            ((JTextField) inputComponent).setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // padding inside text field
            ));
        }

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(inputComponent, BorderLayout.CENTER);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        return fieldPanel;
    }

    // Inside the LOGIN button's event listener
    private void handleLogin() { 
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if Field is Empty
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        User user = UserDAO.loginUser(username, password);

        System.out.println("Username: " + username + "Pass: " + password);
        System.out.println(user);

        if (user != null) {

            JOptionPane.showMessageDialog(null, "Login Successful! " + username);

            JFrame dashboardFrame = new JFrame();
            dashboardFrame.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
            dashboardFrame.setExtendedState(MAXIMIZED_BOTH);
            
            dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            if (user instanceof Admin admin) {
                dashboardFrame.setTitle("O' Days Library Management System - Admin Dashboard");
                dashboardFrame.setContentPane(new AdminDashboard(admin));
                
                URL iUrl = getClass().getClassLoader().getResource("resources/adminicon.png");

                // Check for Icon
                if (iUrl != null) {
                    dashboardFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(iUrl));
                } else {
                    System.out.println("Icon resource not found!");
                }
                
            } else if (user instanceof Student student) {
                dashboardFrame.setTitle("Student Dashboard");
                dashboardFrame.setContentPane(new StudentDashboard(student));
            }

            dashboardFrame.setVisible(true);
            dispose();
    
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

}
