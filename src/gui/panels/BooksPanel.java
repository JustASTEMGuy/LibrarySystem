package gui.panels;

import dao.BookDAO;
import obj.Book;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.MouseListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BooksPanel extends JPanel{

    private JTable bookTable;
    public DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;

    public BooksPanel() {
        setLayout(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
                bookTable.getSelectionModel().clearSelection();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // No action needed
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // No action needed
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // No action needed
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // No action needed
            }
        });

        deleteButton.setVisible(false);
        editButton.setVisible(false);

        addButton.addActionListener(e->{
            addBook();
            });

        deleteButton.addActionListener(e->{
            deleteBook();
        });

        add(toolbar, BorderLayout.NORTH);
        initTable();
    }

    private void initTable() {
        String[] titlecolumn = {"Book ID", "Title", "Author", "Genre", "Quantity"};
        tableModel = new DefaultTableModel(titlecolumn, 0);
        bookTable = new JTable(tableModel);

        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setRowHeight(24);

        bookTable.getTableHeader().setReorderingAllowed(false);
        bookTable.setDefaultEditor(Object.class, null);

        bookTable.getSelectionModel().clearSelection();

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        
        update();

    }

    private void update() {
        tableModel.setRowCount(0);

        ArrayList<Book> book = BookDAO.fetchBooks();
        
        for (Book b : book) {
            tableModel.addRow(new Object[]{b.getID(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getQuantity()});
        }

    }

    private void deleteBook() {
        int index = bookTable.getSelectionModel().getLeadSelectionIndex();
        ArrayList<Book> book = BookDAO.fetchBooks();
        int bookID = book.get(index).getID();

        BookDAO.deleteBook(bookID);
        JOptionPane.showMessageDialog(null, "Deleted " + bookID);
        update();
    }

    // Add Book Functions
    private void addBook() {
        
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField quantityField = new JTextField();

        Object[] fields = {
            "Title:", titleField, "Author:", authorField, "Genre:", genreField,"Quantity:", quantityField
        };

        Icon icon = new ImageIcon("lib/book-stack.png");
        Image iconimage = ((ImageIcon) icon).getImage();
        Image newImage = iconimage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        Icon newIcon = new ImageIcon(newImage);

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, newIcon);

        if (result == JOptionPane.OK_OPTION) {
            String q = quantityField.getText().trim();

            JOptionPane.showMessageDialog(null, q);
            
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantity is required.");
                return;
            }

            int quantity;

            try {
                quantity = Integer.parseInt(q);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Quantity must be a valid number.");
                return;
            }
            
            BookDAO.addBook(titleField.getText(), authorField.getText(), genreField.getText(), quantity);
            update();
        }
    }
}