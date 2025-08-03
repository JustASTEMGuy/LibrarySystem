package gui.panels;

import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;

import dao.BookDAO;
import dao.UserDAO;
import gui.AdminDashboard;
import obj.Book;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import user.Student;
import user.Admin;
import user.User;

public class MembersPanel extends JPanel{
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton memberButton, sortButton;
    private boolean isStudent = true;
    private boolean isSortActive, isAscending, placeholderActive;
    private int currentSortColumn;
    private ArrayList<Student> visibleUsers = new ArrayList<>();
    
    public MembersPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JTextField searchField = new JTextField("Search by Name");
        searchField.setPreferredSize(new Dimension(200, 30));
        placeholderActive = true;

        
        // Placeholder Illusion idea
        searchField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                placeholderActive = false;
                searchField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    placeholderActive = true;
                    searchField.setText("Search by Name");
                }
            }
        });

        searchField.addActionListener(e-> {
            System.out.println("Action!");
            searchField.transferFocus();
            if (searchField.getText().equals("")) {
                placeholderActive = true;
                searchField.setText("Search by Name");
            }
        });

        // Dynamic Search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateText(searchField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateText(searchField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used
            }

            public void updateText(JTextField s){ // Link to SQL statement update
                if(!placeholderActive && !(s.getText().equals(""))) { // IF there's NO PLACEHOLDER
                    handleSearch(s.getText());
                }

                else {
                    if(s.getText().equals("")) {
                        update(isStudent);
                    }
                }  
            }
        });

        // Sort Button
        sortButton = new JButton(new ImageIcon("src\\resources\\sorticon.png"));
        sortButton.setPreferredSize(new Dimension(32, 32));
        sortButton.setContentAreaFilled(false);
        sortButton.setBorderPainted(false);
        sortButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sortButton.setToolTipText("Sort Books");

        sortButton.addActionListener(e -> {
            handleSort();
        });

        addButton = new JButton("Add Student");

        memberButton = new JButton("View Admins");

        memberButton.addActionListener(e -> {
            if (isStudent == true) {
                addButton.setText("Add Admin");
                isStudent = false;
                update(false);
                memberButton.setText("View Students");
            }

            else {
                addButton.setText("Add Student");
                update(true);
                isStudent = true;
                memberButton.setText("View Admins");
            }
        });

        addButton.addActionListener(e -> {
            addMember(isStudent);
        });
        
        leftToolbar.add(addButton);
        leftToolbar.add(searchField);

        rightToolbar.add(memberButton);
        rightToolbar.add(sortButton);

        topPanel.add(leftToolbar, BorderLayout.WEST);
        topPanel.add(rightToolbar, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        initTable(true);

    }

    // Create Table
    private void initTable(boolean student) {
        tableModel = new DefaultTableModel(new String[]{"User ID", "Username", "Password", "Email", "Banned Status"}, 0);
        memberTable = new JTable(tableModel);

        memberTable.setDefaultEditor(Object.class, null);

        memberTable.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        memberTable.setRowHeight(24);

        memberTable.getTableHeader().setReorderingAllowed(false);
        memberTable.getSelectionModel().clearSelection();
        
        memberTable.addMouseListener(new MouseAdapter() {
            
            @Override // Double Click Functions
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && memberTable.getSelectedRow() != -1) {
                    handleDoubleClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        update(student);
    }

    // Double Click Handler
    private void handleDoubleClick() {
        int row = memberTable.getSelectedRow();
        try {
            
            User selectedUser = visibleUsers.get(row);
            int userID = selectedUser.getID();
            String userName = selectedUser.getUsername();
            String userPass = selectedUser.getPassword();
            String userEmail = selectedUser.getEmail();
            boolean banned = selectedUser.getBannedStatus();

            int choice = JOptionPane.showOptionDialog(null,"Choose an action for: " + userName,"User Options",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"Edit", "Delete"},"Edit");
            switch (choice) {
                case 0: // Edit Function
                    if (isStudent) {
                        showEditDialog(new Student(userID, userName, userPass, userEmail, banned));
                    }
                    else {
                        showEditDialog(new Admin(userID, userName, userPass, userEmail, banned));
                    }
                    break;

                case 1:
                    deleteUser(isStudent);
                    break;

                } 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }

    // Sort Handler
    private void handleSort() {
        String[] options = {"Sort by ID", "Sort by Name", "Sort by Email"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        ImageIcon icon = new ImageIcon("src\\resources\\sorticon.png");

        int result = JOptionPane.showOptionDialog(null, comboBox, "Sort Option", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, null, null);
        
        if (result == JOptionPane.OK_OPTION) {
            int selectedOption = comboBox.getSelectedIndex();
            currentSortColumn = selectedOption;
            
            if (selectedOption == 0) {
                isSortActive = false;
            }
            
            else {
                isSortActive = true;
            }
            
            String[] sortOptions = {"Ascending", "Descending"};
            JComboBox<String> newComboBox = new JComboBox<>(sortOptions);
            int sortBox = JOptionPane.showOptionDialog(null, newComboBox, "Sort Direction", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, null, null);

            if (sortBox == JOptionPane.OK_OPTION) {
                ArrayList<Student> user;

                int sortDirection = newComboBox.getSelectedIndex();
                if (sortDirection == 0) {
                    isAscending = true;
                    user = UserDAO.sortUser(selectedOption, true, isStudent);
                }

                else {
                    isAscending = false;
                    user = UserDAO.sortUser(selectedOption, false, isStudent);
                }

                tableModel.setRowCount(0);
                for (Student s : user) {
                    tableModel.addRow(new Object[]{s.getID(), s.getUsername(), s.getPassword(), s.getEmail(), s.getBannedStatus()});
                }
            }
        }   
    }

    private void handleSearch(String phrase) {
        visibleUsers = UserDAO.searchUpdate(phrase, isStudent);
        tableModel.setRowCount(0);
        for (Student s : visibleUsers) {
            tableModel.addRow(new Object[]{s.getID(), s.getUsername(), s.getPassword(), s.getEmail(), s.getBannedStatus()});
        }
    }

    private void showEditDialog(User user) {
        
        JTextField nameField = new JTextField(user.getUsername());
        JPasswordField passField = new JPasswordField(user.getPassword());
        JTextField emailField = new JTextField(user.getEmail());
        JComboBox bannedField = new JComboBox<>(new String[]{"true", "false"});

        Object[] fields = {
            "Name:", nameField,
            "Password:", passField,
            "Email:", emailField,
            "Banned Status:", bannedField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            User updated;
            char[] pass = passField.getPassword();
            String password = new String(pass);
            String statusChoice = (String) bannedField.getSelectedItem();
            boolean status = statusChoice.equalsIgnoreCase("true");
            if (isStudent) {
                updated = new Student(user.getID(), nameField.getText(), password, emailField.getText(), status);
            }
            else {
                updated = new Admin(user.getID(), nameField.getText(), password, emailField.getText(), status);
            }

            boolean updatedSuccess = UserDAO.updateUser(updated, isStudent);

            if (updatedSuccess) {
                if(isStudent) {
                    JOptionPane.showMessageDialog(null, "Student updated successfully.");            
                }
                else {
                    JOptionPane.showMessageDialog(null, "Admin updated successfully.");            
                }
                update(isStudent);

            } else {
                JOptionPane.showMessageDialog(null, "Failed to update.");
            }

        }
    }

    private void deleteUser(boolean isStudent) {
        int index = memberTable.getSelectionModel().getLeadSelectionIndex();
        ArrayList<Student> user;
        
        if(isStudent) {
            user = UserDAO.fetchUsers("Student");
        }
        else{
            user = UserDAO.fetchUsers("Admin");
        }
        
        int userID = user.get(index).getID();
        String userName = user.get(index).getUsername();

        UserDAO.deleteUser(userID);
        JOptionPane.showMessageDialog(null, "Deleted " + userName);
        update(isStudent);
    }

    // Add Members
    private void addMember(boolean student) {
        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JComboBox bannedField = new JComboBox<String>(new String[]{"True", "False"});

        Object[] fields = {
            "Name:", nameField, "Password:", passwordField, "Email:", emailField,"Banned Status:", bannedField
        };

        Icon icon;
        
        if (student) {
            icon = new ImageIcon("src\\resources\\studentadd.png");
        }
        else {
            icon = new ImageIcon("src\\resources\\adminadd.png");
        }

        Image iconimage = ((ImageIcon) icon).getImage();
        Image newImage = iconimage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        Icon newIcon = new ImageIcon(newImage);

        int result;
        if (student) {
            result = JOptionPane.showConfirmDialog(null, fields, "Add Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, newIcon);
        }

        else {
            result = JOptionPane.showConfirmDialog(null, fields, "Add Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, newIcon);

        }

        if (result == JOptionPane.OK_OPTION) {
            char[] pass = passwordField.getPassword();
            String password = new String(pass);
            String statusChoice = (String) bannedField.getSelectedItem();
            boolean status = statusChoice.equalsIgnoreCase("true");

            UserDAO.addUser(nameField.getText(), password, emailField.getText(), status, isStudent);
            update(isStudent);

            if (isStudent) {
                AdminDashboard.setSubtitle("Total Students Registered: " + UserDAO.fetchTotalUser("student"));
            }
        }
    }

    private void update(boolean isStudent) {
        tableModel.setRowCount(0);
        
        if (isStudent) {
            System.out.println("Switched to Student");
            visibleUsers = UserDAO.fetchUsers("Student");
        }

        else {
            System.out.println("Switched to Admin");
            visibleUsers = UserDAO.fetchUsers("Admin");
        }
        
        for (Student s : visibleUsers) {
            tableModel.addRow(new Object[]{s.getID(), s.getUsername(), s.getPassword(), s.getEmail(), s.getBannedStatus()});
        }
    }
}
