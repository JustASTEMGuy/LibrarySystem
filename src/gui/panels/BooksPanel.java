package gui.panels;

import javax.swing.*;
import java.awt.*;

public class BooksPanel extends JPanel{

    private JTable bookTable;
    private JButton addButton, editButton, deleteButton;

    public BooksPanel() {
        setLayout(new BorderLayout());
        initToolbar();
        initTable();
    }

    private void initToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Book");
        editButton = new JButton("Edit Book");
        deleteButton = new JButton("Delete Book");

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);

        add(toolbar, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columns = {"Book ID", "Title", "Author", "Genre", "Status"};
        Object[][] data = {}; // empty for now

        bookTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
