import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class BookInventoryUI extends JFrame {
    private JTextField titleField, authorField, genreField, publicationDateField, isbnField;
    private JTextField filterTitleField, filterAuthorField, filterGenreField, filterDateField;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JSONArray booksArray;

    public BookInventoryUI() {
        booksArray = new JSONArray();

        setTitle("Book Inventory");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add Book Form
        JPanel addBookPanel = new JPanel(new GridLayout(6, 2));
        addBookPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        addBookPanel.add(titleField);

        addBookPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        addBookPanel.add(authorField);

        addBookPanel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        addBookPanel.add(genreField);

        addBookPanel.add(new JLabel("Publication Date (YYYY-MM-DD):"));
        publicationDateField = new JTextField();
        addBookPanel.add(publicationDateField);

        addBookPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        addBookPanel.add(isbnField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new AddBookListener());
        addBookPanel.add(addButton);

        // Filter Form
        JPanel filterPanel = new JPanel(new GridLayout(2, 5));
        filterPanel.add(new JLabel("Filter by Title:"));
        filterTitleField = new JTextField();
        filterPanel.add(filterTitleField);

        filterPanel.add(new JLabel("Author:"));
        filterAuthorField = new JTextField();
        filterPanel.add(filterAuthorField);

        filterPanel.add(new JLabel("Genre:"));
        filterGenreField = new JTextField();
        filterPanel.add(filterGenreField);

        filterPanel.add(new JLabel("Publication Date:"));
        filterDateField = new JTextField();
        filterPanel.add(filterDateField);

        JButton filterButton = new JButton("Apply Filters");
        filterButton.addActionListener(new FilterBooksListener());
        filterPanel.add(filterButton);

        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.addActionListener(new ClearFilterListener());
        filterPanel.add(clearFilterButton);

        // Table to display books
        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Genre", "Publication Date", "ISBN"}, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Export Buttons
        JPanel exportPanel = new JPanel();
        JButton exportJsonButton = new JButton("Export to JSON");
        exportJsonButton.addActionListener(new ExportJsonListener());
        exportPanel.add(exportJsonButton);

        JButton exportCsvButton = new JButton("Export to CSV");
        exportCsvButton.addActionListener(new ExportCsvListener());
        exportPanel.add(exportCsvButton);

        // Layout setup
        setLayout(new BorderLayout());
        add(addBookPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(exportPanel, BorderLayout.PAGE_END);
    }

    // Listener for adding a book
    private class AddBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String publicationDate = publicationDateField.getText();
            String isbn = isbnField.getText();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(BookInventoryUI.this, "Title, Author, and ISBN are required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add data to the table
            tableModel.addRow(new Object[]{title, author, genre, publicationDate, isbn});

            // Add data to JSON array
            JSONObject bookJson = new JSONObject();
            bookJson.put("Title", title);
            bookJson.put("Author", author);
            bookJson.put("Genre", genre);
            bookJson.put("PublicationDate", publicationDate);
            bookJson.put("ISBN", isbn);
            booksArray.add(bookJson);

            titleField.setText("");
            authorField.setText("");
            genreField.setText("");
            publicationDateField.setText("");
            isbnField.setText("");
        }
    }

    // Listener for filtering books
    private class FilterBooksListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = filterTitleField.getText().toLowerCase();
            String author = filterAuthorField.getText().toLowerCase();
            String genre = filterGenreField.getText().toLowerCase();
            String date = filterDateField.getText();

            tableModel.setRowCount(0); // Clear the table
            for (Object book : booksArray) {
                JSONObject bookJson = (JSONObject) book;
                if ((title.isEmpty() || bookJson.get("Title").toString().toLowerCase().contains(title)) &&
                        (author.isEmpty() || bookJson.get("Author").toString().toLowerCase().contains(author)) &&
                        (genre.isEmpty() || bookJson.get("Genre").toString().toLowerCase().contains(genre)) &&
                        (date.isEmpty() || bookJson.get("PublicationDate").toString().contains(date))) {

                    tableModel.addRow(new Object[]{
                            bookJson.get("Title"),
                            bookJson.get("Author"),
                            bookJson.get("Genre"),
                            bookJson.get("PublicationDate"),
                            bookJson.get("ISBN")
                    });
                }
            }
        }
    }

    // Listener for clearing filters
    private class ClearFilterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterTitleField.setText("");
            filterAuthorField.setText("");
            filterGenreField.setText("");
            filterDateField.setText("");
            tableModel.setRowCount(0); // Clear the table
            for (Object book : booksArray) {
                JSONObject bookJson = (JSONObject) book;
                tableModel.addRow(new Object[]{
                        bookJson.get("Title"),
                        bookJson.get("Author"),
                        bookJson.get("Genre"),
                        bookJson.get("PublicationDate"),
                        bookJson.get("ISBN")
                });
            }
        }
    }

    // Listener for exporting data to JSON
    private class ExportJsonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter file = new FileWriter("books.json")) {
                file.write(booksArray.toJSONString());
                JOptionPane.showMessageDialog(BookInventoryUI.this, "Data exported to books.json", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(BookInventoryUI.this, "Error exporting data to JSON file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener for exporting data to CSV
    private class ExportCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter file = new FileWriter("books.csv")) {
                file.append("Title,Author,Genre,Publication Date,ISBN\n");
                for (Object book : booksArray) {
                    JSONObject bookJson = (JSONObject) book;
                    file.append(bookJson.get("Title").toString()).append(",");
                    file.append(bookJson.get("Author").toString()).append(",");
                    file.append(bookJson.get("Genre").toString()).append(",");
                    file.append(bookJson.get("PublicationDate").toString()).append(",");
                    file.append(bookJson.get("ISBN").toString()).append("\n");
                }
                JOptionPane.showMessageDialog(BookInventoryUI.this, "Data exported to books.csv", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(BookInventoryUI.this, "Error exporting data to CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookInventoryUI ui = new BookInventoryUI();
            ui.setVisible(true);
        });
    }
}
