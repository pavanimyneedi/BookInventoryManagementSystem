import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO Inventory(Title, Author, Genre, PublicationDate, ISBN) VALUES (?,?,?,?,?)";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getAuthor());
        preparedStatement.setString(3, book.getGenre());
        preparedStatement.setString(4, book.getPublicationDate().toString());
        preparedStatement.setString(5, book.getIsbn());
        preparedStatement.executeUpdate();
    }

    //method for filtering the books
    public List<Book> filterBooks(String title, String author, String genre) throws SQLException {
        String query = "SELECT * FROM Inventory WHERE "
                + "(? IS NULL OR Title LIKE ?) AND "
                + "(? IS NULL OR Author LIKE ?) AND "
                + "(? IS NULL OR Genre LIKE ?)";

        List<Book> filteredBooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, title);
            statement.setString(2, "%" + title + "%");
            statement.setString(3, author);
            statement.setString(4, "%" + author + "%");
            statement.setString(5, genre);
            statement.setString(6, "%" + genre + "%");

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getString("Genre"),
                        rs.getString("PublicationDate"),
                        rs.getString("ISBN")
                );
                book.setEntryId(rs.getInt("EntryID"));
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    //method to update the existing book details
    public boolean updateBook(Book book) throws SQLException{
        String query = "UPDATE Inventory SET Title = ?, Author = ?, Genre = ?, PublicationDate = ?, ISBN = ? WHERE EntryId = ?";

        Connection connection =DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getGenre());
        statement.setString(4, book.getPublicationDate());
        statement.setString(5, book.getIsbn());
        statement.setInt(6, book.getEntryId());

        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0;
    }

    //method to export books to csv
    public  void exportToCSV(String filePath) throws SQLException, IOException{
        String query = "SELECT * FROM Inventory";

        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.append("EntryId,Title,Author,Genre,PublicationDate,ISBN\n");
        while (rs.next()){
            fileWriter.append(rs.getInt("EntryId") + ",")
                    .append(rs.getString("Title") + ",")
                    .append(rs.getString("Author") + ",")
                    .append(rs.getString("Genre") + ",")
                    .append(rs.getString("PublicationDate") + ",")
                    .append(rs.getString("ISBN") + "\n");
        }
        fileWriter.flush();
    }

    //method to export it into json
    public void exportToJSON(String filePath) throws SQLException,IOException{
        String query = "SELECT * FROM Inventory";
        JSONArray jsonArray = new JSONArray();
        Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EntryId", resultSet.getInt("EntryId"));
            jsonObject.put("Title", resultSet.getString("Title"));
            jsonObject.put("Author",resultSet.getString("Author"));
            jsonObject.put("Genre", resultSet.getString("Genre"));
            jsonObject.put("PublicationDate",resultSet.getString("PublicationDate"));
            jsonObject.put("ISBN",resultSet.getString("ISBN"));

            jsonArray.add(jsonObject);

        }
        try(FileWriter jsonFile = new FileWriter(filePath)) {
            jsonFile.write(jsonArray.toString());
            jsonFile.flush();
        }
    }
}






























