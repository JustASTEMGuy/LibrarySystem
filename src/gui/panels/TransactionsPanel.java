package gui.panels;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import dao.TransDAO;
import gui.UserSession;
import obj.Book;
import obj.Transactions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class TransactionsPanel extends JPanel{

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private boolean isSortActive, isAscending, placeholderActive;
    private int currentSortColumn;
    private String role;
    private JButton sortButton;
    private ArrayList<Transactions> visibleTransactions = new ArrayList<>();

    public TransactionsPanel(String role) {
        this.role = role;
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JTextField searchField = new JTextField("Search by Transactions ID");

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
                    searchField.setText("Search by Transactions ID");
                }
            }

        });

        searchField.addActionListener(e-> {
            searchField.transferFocus();
            if (searchField.getText().equals("")) {
                placeholderActive = true;
                searchField.setText("Search by Transactions ID");
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

        leftToolbar.add(searchField);
        rightToolbar.add(sortButton);
        topPanel.add(leftToolbar, BorderLayout.WEST);
        topPanel.add(rightToolbar, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        initTable();
    }

    private void initTable() {
        if (role.equals("Admin")) {
            tableModel = new DefaultTableModel(new String[]{"Transaction ID", "User ID", "Book ID", "Borrow Date", "Return Date", "Status"}, 0);
        }

        else {
            tableModel = new DefaultTableModel(new String[]{"Transaction ID", "Book ID", "Borrow Date", "Return Date", "Status"}, 0);
        }
        
        transactionTable = new JTable(tableModel);

        transactionTable.setDefaultEditor(Object.class, null);

        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        transactionTable.setRowHeight(24);

        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.getSelectionModel().clearSelection();
        
        transactionTable.addMouseListener(new MouseAdapter() {
            
            @Override // Double Click Functions
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && transactionTable.getSelectedRow() != -1 && "Student".equals(role)) {
                    handleDoubleClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        update();
    }

    private void handleDoubleClick() {
        int row = transactionTable.getSelectedRow();

        Transactions trans = visibleTransactions.get(row);
        int transID = trans.getID();
        int transBookID = trans.getBookID();

        if ("Borrowed".equals(trans.getStatus())) {
            int choice = JOptionPane.showConfirmDialog(
            null,
                "Do you want to return for Transaction " + transID, 
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                
                try {
                    LocalDate today = LocalDate.now();
                    boolean returnStatus = TransDAO.returnTransactions(trans, today);
                    boolean bookUpdateStatus = BookDAO.returnBook(transBookID);

                    if (returnStatus && bookUpdateStatus) {
                        JOptionPane.showMessageDialog(null, "Book Returned Successfully!");
                        update();
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getStackTrace());
                }
            }
        }

        else {
            JOptionPane.showMessageDialog(null, "This transaction has already been solved!");
            return;
        }
    }

    private void update() {
        tableModel.setRowCount(0);
        if ("Admin".equals(role)) {
            visibleTransactions = isSortActive
            ? TransDAO.sortTransactions(currentSortColumn, isAscending)
            : TransDAO.fetchTransactions();

            tableModel.setRowCount(0);
            for (Transactions b : visibleTransactions) {
                if (b.getReturnDate() == null) {
                    tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                }

                else {
                    tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                }
        }
        }

        else {
            visibleTransactions = isSortActive
            ? TransDAO.sortTransactions(currentSortColumn, isAscending, UserSession.getStudent().getID())
            : TransDAO.fetchTransactions(UserSession.getStudent().getID());

            tableModel.setRowCount(0);
            for (Transactions b : visibleTransactions) {
                if (b.getReturnDate() == null) {
                    tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                }

                else {
                    tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                }
            }
        }
    }

    // Search Function
    private void handleSearch(String phrase) {

        // Check for Admin or Student role
        if ("Admin".equals(role)) {
            visibleTransactions = TransDAO.searchUpdate(phrase);
        }

        else {
            visibleTransactions = TransDAO.searchUpdate(phrase, UserSession.getStudent().getID());
        }
        
        tableModel.setRowCount(0);
        for (Transactions b : visibleTransactions) {

            if ("Admin".equals(role)) {
                if (b.getReturnDate() == null) {
                    tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                }

                else {
                    tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                }
            }

            else {
                if (b.getReturnDate() == null) {
                    tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                }

                else {
                    tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                }
            }
            
        }
    }

    private void handleSort() { // Handle this Sort Issue + Return Books = Finish Code
        String[] options;

        if ("Admin".equals(role)) {
            options = new String[]{"Sort by ID", "Sort by BookID", "Sort by UserID", "Sort by Borrow Date", "Sort by Return Date"};
        }
        else {
            options = new String[]{"Sort by ID", "Sort by BookID", "Sort by Borrow Date", "Sort by Return Date"};
        }

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
                ArrayList<Transactions> trans;

                int sortDirection = newComboBox.getSelectedIndex();
                if ("Admin".equals(role)) {
                    if (sortDirection == 0) {
                        isAscending = true;
                        trans = TransDAO.sortTransactions(selectedOption, true);
                    }

                    else {
                        isAscending = false;
                        trans = TransDAO.sortTransactions(selectedOption, false);
                    }
                }

                else {
                    if (sortDirection == 0) {
                        isAscending = true;
                        trans = TransDAO.sortTransactions(selectedOption, true, UserSession.getStudent().getID());
                    }

                    else {
                        isAscending = false;
                        trans = TransDAO.sortTransactions(selectedOption, false, UserSession.getStudent().getID());
                    }
                }

                tableModel.setRowCount(0);
                for (Transactions b : trans) {
                    if ("Admin".equals(role)) {
                        if (b.getReturnDate() == null) {
                            tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                        }

                        else {
                            tableModel.addRow(new Object[]{b.getID(), b.getUserID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                        }
                    }

                    else {
                        if (b.getReturnDate() == null) {
                            tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), "None", b.getStatus()});
                        }

                        else {
                            tableModel.addRow(new Object[]{b.getID(), b.getBookID(), b.getBorrowDate(), b.getReturnDate(), b.getStatus()});
                        }
                    }
                }
            }
        }
    }
}
