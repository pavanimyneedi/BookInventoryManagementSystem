//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//public class Main {
//    public  static  void main(String[] args) throws SQLException, IOException {
//        InventoryDAO inventoryDAO = new InventoryDAO();
//        try {
//            Connection connection = DatabaseConnection.getConnection();
//            if(connection!= null){
//                System.out.println("Connected to Database!");
//                connection.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            //add a new book
//            Book newBook = new Book("Java Programming","John Doe", "Programing", "2023-01-01","12309876");
//            inventoryDAO.addBook(newBook);
//            System.out.println("Book Added Successfully.!!");
//
//            //filter book
//            List<Book> filterBooks = inventoryDAO.filterBooks(null,"John Doe",null);
//            System.out.println("Filtered Books: ");
//            for (Book book: filterBooks){
//                System.out.println(book.getTitle() + "by " + book.getAuthor());
//            }
//
//
//            //update book information
//            newBook.setTitle("Advanced Java Programming");
//            boolean isUpdated = inventoryDAO.updateBook(newBook);
//            System.out.println("Book Updated: " + isUpdated);
//
//
//            //export to csv
//            inventoryDAO.exportToCSV("books.csv");
//            System.out.println("Data exported to books.csv");
//
//            //export to json
//            inventoryDAO.exportToJSON("books.json");
//            System.out.println("Data exported to books.json");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//}
