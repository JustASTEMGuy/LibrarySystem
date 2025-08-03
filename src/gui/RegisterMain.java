package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.URL;

import dao.UserDAO;

import user.Admin;
import user.Student;
import user.User;

public class RegisterMain extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    public RegisterMain() {

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
        setTitle("O' Days Library Management System - Register");
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
        JLabel title = new JLabel("Ol' Days Library Register", SwingConstants.CENTER);
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

        confirmPasswordField = new JPasswordField();
        formBox.add(createField("Confirm Pass:", confirmPasswordField));

        emailField = new JTextField();
        formBox.add(createField("Email:", emailField));

        // Set Button Size
        Dimension buttonSize = new Dimension(200, 50);  

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 43, 91));
        registerButton.setForeground(new Color(143, 227, 207));
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.setPreferredSize(buttonSize);
        registerButton.setMaximumSize(buttonSize);
        registerButton.setMinimumSize(buttonSize);

        // Hovering "Animation"

        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegister());
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(43, 72, 101));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(0, 43, 91)); 
            }
        });

        JButton loginButton = new JButton("Back to Login");
        loginButton.setBackground(new Color(0, 43, 91));
        loginButton.setForeground(new Color(143, 227, 207));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.setPreferredSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);
        loginButton.setMinimumSize(buttonSize);

        // Hovering "Animation"

        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(formBox);
            topFrame.dispose(); // Close login window

            LoginMain loginMain = new LoginMain(); // Launch registration
            loginMain.setVisible(true);});
            loginButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(43, 72, 101));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 43, 91)); 
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        formBox.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.setBorder(new EmptyBorder(0, 80, 0, 0));
        formBox.add(buttonPanel);

        registerButton.setFocusable(true);
        getRootPane().setDefaultButton(registerButton);

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
        label.setFont(new Font("Consolas", Font.PLAIN, 14));

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

    // Register Method
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User newUser = UserDAO.registerUser(username, password, "student", email);
        
        if (newUser != null) {
            // Completed Register

            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Close registration frame
            
            LoginMain loginMain = new LoginMain();
            loginMain.setVisible(true);
            
        }
        
        else {
            JOptionPane.showMessageDialog(null, "Registration Unsuccessful!");
        }
    } 
}
