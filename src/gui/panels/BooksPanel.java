package gui.panels;

import dao.BookDAO;
import obj.Book;
import dao.TransDAO;
import gui.StudentDashboard;
import gui.UserSession;
import user.Student;

import javax.swing.*;
import javax.swing.event.*;


import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;


public class BooksPanel extends JPanel {

    private JTable bookTable;
    public DefaultTableModel tableModel;
    private JButton addButton, sortButton;
    private boolean isSortActive, isAscending, placeholderActive;
    private int currentSortColumn;
    private ArrayList<Book> visibleBooks = new ArrayList<>();
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

        // Search Field
        JTextField searchField = new JTextField("Search by Title");
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
                    searchField.setText("Search by Title");
                }
            }
        });

        searchField.addActionListener(e-> {
            searchField.transferFocus();
            if (searchField.getText().equals("")) {
                placeholderActive = true;
                searchField.setText("Search by Title");
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
                        update();
                    }
                }  
            }
        });

        leftbar.add(addButton);
        leftbar.add(searchField);
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

    // Create Table (Setup)
    private void initTable() {
        String[] titlecolumn = {"Book ID", "Title", "Author", "Genre", "Quantity"};
        tableModel = new DefaultTableModel(titlecolumn, 0);
        bookTable = new JTable(tableModel);

        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookTable.setRowHeight(24);

        bookTable.getTableHeader().setReorderingAllowed(false); //Disallow user to reorder column (cause slight confusion)
        bookTable.setDefaultEditor(Object.class, null);

        bookTable.getSelectionModel().clearSelection(); // Clear all selection before loading

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

        visibleBooks = isSortActive
        ? BookDAO.sortBook(currentSortColumn, isAscending)
        : BookDAO.fetchBooks();

        tableModel.setRowCount(0);
        for (Book b : visibleBooks) {
            tableModel.addRow(new Object[]{b.getID(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getQuantity()});
        }
    }

    // Double Click Method (after clicking)
    private void handleDoubleClick() {
        int row = bookTable.getSelectedRow();

        Book selectedBook = visibleBooks.get(row);
        int bookID = selectedBook.getID();
        String booktitle = selectedBook.getTitle();
        String bookAuthor = selectedBook.getAuthor();
        String bookGenre = selectedBook.getGenre();
        int bookQty = selectedBook.getQuantity();

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

    // USER borrows book
    private void borrowBook(int bookID){
        Student stu = UserSession.getStudent();
        int userID = stu.getID();
        boolean success = BookDAO.borrowBook(bookID);

        LocalDate today = LocalDate.now();
        boolean transSuccess = TransDAO.insertTransaction(userID, bookID, today);

        if(success && transSuccess){
            JOptionPane.showMessageDialog(null, "Book borrowed successfully!");
            update();
        }
        else {
            if (!transSuccess) {
                JOptionPane.showMessageDialog(null, "Transaction Error!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
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

        Icon icon = new ImageIcon("src\\resources\\book-stack.png");
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


            int quantity;

            try {
                quantity = Integer.parseInt(q);

                if (Integer.parseInt(q) < 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Quantity (< 0)");
                    return;
                }

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

    // Search Box Handling
    private void handleSearch(String phrase) {
        visibleBooks = BookDAO.searchUpdate(phrase);
        tableModel.setRowCount(0);
        for (Book b : visibleBooks) {
            tableModel.addRow(new Object[]{b.getID(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getQuantity()});
        }
    }
}