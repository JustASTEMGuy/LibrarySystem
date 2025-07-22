package gui.panels;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.UserDAO;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import user.Student;

public class MembersPanel extends JPanel{
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private JButton memberButton, adminButton;
    private boolean isStudent = true;

    public MembersPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Add Student");
        editButton = new JButton("Edit Student");
        deleteButton = new JButton("Delete Student");

        memberButton = new JButton("View Admins");

        memberButton.addActionListener(e -> {
            if (isStudent == true) {
                addButton.setText("Add Admin");
                editButton.setText("Edit Admin");
                deleteButton.setText("Delete Admin");
                isStudent = false;
                update(false);
                memberButton.setText("View Students");
            }

            else {
                addButton.setText("Add Student");
                editButton.setText("Edit Student");
                deleteButton.setText("Delete Student");
                update(true);
                isStudent = true;
                memberButton.setText("View Admins");
            }
        });
        
        leftToolbar.add(addButton);
        leftToolbar.add(editButton);
        leftToolbar.add(deleteButton);

        rightToolbar.add(memberButton);

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
        memberTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        update(student);

    }

    private void update(boolean isStudent) {
        tableModel.setRowCount(0);
        ArrayList<Student> user;
        if (isStudent == true) {
            System.out.println("Switched to Student");
            user = UserDAO.fetchUsers("Student");
        }

        else {
            System.out.println("Switched to Admin");
            user = UserDAO.fetchUsers("Admin");
        }
        
        for (Student s : user) {
            tableModel.addRow(new Object[]{s.getID(), s.getUsername(), s.getPassword(), s.getEmail(), s.getBannedStatus()});
        }
    }
}
