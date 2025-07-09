package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;

public class LoginMain extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public void LoginFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Library Management System - Login");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Center panel in frame

        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        add(createFormBox()); // Box-like panel
        setSize(screenSize);
        setResizable(false);
        setVisible(true);
    }

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
        JLabel title = new JLabel("Library Login", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
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

        roleComboBox = new JComboBox<>(new String[] { "admin", "student" });
        formBox.add(createField("Role:", roleComboBox));

        // Login button
        JButton loginButton = new JButton("Login");

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(140, 40);  // Example size
        loginButton.setPreferredSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);
        loginButton.setMinimumSize(buttonSize);

        loginButton.setForeground(new Color(143, 227, 207));              // Text color
        loginButton.setBackground(new Color(0, 43, 91));                  // Primary background
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(37, 109, 133), 2)); // Accent border

        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(43, 72, 101)); // Hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 43, 91)); // Default
            }
        });



        formBox.add(Box.createRigidArea(new Dimension(0, 20)));
        formBox.add(loginButton);

        return formBox;
    }

    private JPanel createField(String labelText, JComponent inputComponent) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setMaximumSize(new Dimension(500, 50));
        fieldPanel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(80, 30));
        label.setForeground(new Color(143, 227, 207));

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

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (UserDAO.loginUser(username, password, role)) {
            JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (role.equals("admin")) {
            //    new AdminDashboard(username);
            } else {
             //   new StudentDashboard(username);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
