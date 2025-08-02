package gui.panels;

import dao.BookDAO;
import obj.Book;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BooksPanel extends JPanel{

    private JTable bookTable;
    public DefaultTableModel tableModel;
    private JButton addButton, sortButton;
    private boolean isSortActive, isAscending;
    private int currentSortColumn;
    private String role;

    public BooksPanel(String role) {
        this.role = role;
        setLayout(new BorderLayout());
        JPanel toolbar = new JPanel(new BorderLayout());
        JPanel leftbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Books");
        sortButton = new JButton(new ImageIcon("src\\resources\\sorticon.png"));
        sortButton.setPreferredSize(new Dimension(32, 32));
        sortButton.setContentAreaFilled(false);
        sortButton.setBorderPainted(false);
        sortButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sortButton.setToolTipText("Sort Books");

        leftbar.add(addButton);
        rightbar.add(sortButton);
        isSortActive = false;
        isAscending = true;

        addButton.addActionListener(e->{
            addBook();
        });

        sortButton.addActionListener(e -> {
            handleSort();
        });

        if(!"admin".equalsIgnoreCase(role)){
            addButton.setVisible(false);
            sortButton.setVisible(false);
        }

        toolbar.add(leftbar, BorderLayout.WEST);
        toolbar.add(rightbar, BorderLayout.EAST);
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

        bookTable.addMouseListener(new MouseAdapter() {
            
            @Override // Double Click Functions
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && bookTable.getSelectedRow() != -1) {
                    handleDoubleClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        
        update();

    }

    // Update Method
    private void update() {
        tableModel.setRowCount(0);
        System.out.println("Table Refreshed!");
        ArrayList<Book> book;
        if (isSortActive) {
            if (isAscending) {
                book = BookDAO.sortBook(currentSortColumn, true);
            }
            else {
                book = BookDAO.sortBook(currentSortColumn, false);
            }
            
        }

        else {
            book = BookDAO.fetchBooks();
        }
        for (Book b : book) {
            tableModel.addRow(new Object[]{b.getID(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getQuantity()});
        }

    }

    private void handleDoubleClick() {
        int row = bookTable.getSelectedRow();
        ArrayList<Book> book;
        if (isSortActive) {
            if (isAscending) {
                book = BookDAO.sortBook(currentSortColumn, true);
            }
            else {
                book = BookDAO.sortBook(currentSortColumn, false);
            }
        }

        else {
            book = BookDAO.fetchBooks();
        }
        
        int bookID = book.get(row).getID();
        String booktitle = book.get(row).getTitle();
        String bookAuthor = book.get(row).getAuthor();
        String bookGenre = book.get(row).getGenre();
        int bookQty = book.get(row).getQuantity();

        if("admin".equalsIgnoreCase(role)){
        int choice = JOptionPane.showOptionDialog(null,"Choose an action for: " + booktitle,"Book Options",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"Edit", "Delete"},"Edit");
        switch (choice) {
            case 0: // Edit Function
                showEditDialog(new Book(bookID, booktitle, bookAuthor, bookGenre, bookQty));
                break;

            case 1:
                deleteBook();
                break;
        
            }
        } 
        else{
            int choice = JOptionPane.showOptionDialog(null,"Choose an action for: " + booktitle,"Book Options",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"Borrow", "View"},"Borrow");
        switch (choice) {
            case 0: // Borrow
                borrowBook(bookID);
                break;
            case 1: // View
                JOptionPane.showMessageDialog(null, "Title: " + booktitle + "\nAuthor: " + bookAuthor + "\nGenre: " + bookGenre + "\nQuantity: " + bookQty, "Book Details", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        }
    }

    private void borrowBook(int bookID){
        boolean success = BookDAO.borrowBook(bookID);
        if(success){
            JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
            update();
        }
        else {
            JOptionPane.showMessageDialog(null, "Failed to borrow book.");
        }
    }

    private void deleteBook() {
        int index = bookTable.getSelectionModel().getLeadSelectionIndex();
        ArrayList<Book> book;

        if (isSortActive) {
            if (isAscending) {
                book = BookDAO.sortBook(currentSortColumn, true);
            }
            else {
                book = BookDAO.sortBook(currentSortColumn, false);
            }
        }

        else {
            book = BookDAO.fetchBooks();
        }
        
        int bookID = book.get(index).getID();
        String booktitle = book.get(index).getTitle();

        BookDAO.deleteBook(bookID);
        JOptionPane.showMessageDialog(null, "Deleted " + booktitle);
        update();
    }

    // Sort Method
    private void handleSort() {
        String[] options = {"Sort by ID", "Sort by Title", "Sort by Author", "Sort by Genre", "Sort by Quantity"};
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
                ArrayList<Book> book;

                int sortDirection = newComboBox.getSelectedIndex();
                if (sortDirection == 0) {
                    isAscending = true;
                    book = BookDAO.sortBook(selectedOption, true);
                }

                else {
                    isAscending = false;
                    book = BookDAO.sortBook(selectedOption, false);
                }

                tableModel.setRowCount(0);
                for (Book b : book) {
                    tableModel.addRow(new Object[]{b.getID(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getQuantity()});
                }
            }
            
        }
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
            
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantity is required.");
                return;
            }

            else if (Integer.parseInt(q) < 0) {
                JOptionPane.showMessageDialog(null, "Invalid Quantity (< 0)");
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

    // Edit Fields
    private void showEditDialog(Book book) {
        
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField genreField = new JTextField(book.getGenre());
        JTextField qtyField = new JTextField(String.valueOf(book.getQuantity()));

        Object[] fields = {
            "Title:", titleField,
            "Author:", authorField,
            "Genre:", genreField,
            "Quantity:", qtyField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int newQty = Integer.parseInt(qtyField.getText().trim());

                Book updated = new Book(book.getID(), titleField.getText().trim(), authorField.getText().trim(), genreField.getText().trim(), newQty);

                boolean updatedSuccess = BookDAO.updateBook(updated);
                if (updatedSuccess) {
                    JOptionPane.showMessageDialog(null, "Book updated successfully.");
                    update();

                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update book.");

                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity must be a number.");
            }
        }
    }
}