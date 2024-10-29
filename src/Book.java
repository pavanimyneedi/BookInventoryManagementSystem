import java.util.Date;

public class Book {
        private int entryId;
        private String title;
        private String author;
        private String genre;
        private String publicationDate;
        private String isbn;
        // Getters and Setters
        public Book(String title, String author, String genre, String publicationDate,String isbn){
                this.author = author;
                this.genre = genre;
                this.title = title;
                this.publicationDate = publicationDate;
                this.isbn = isbn;
        }



        public void setEntryId(int entryId){
                this.entryId = entryId;
        }

        public int getEntryId(){
                return entryId;
        }

        public void setAuthor(String author){
                this.author = author;
        }

        public String getAuthor(){
                return author;
        }

        public void setTitle(String title){
                this.title = title;
        }
        public String getTitle() {
                return title;
        }

        public void setGenre(String genre){
                this.genre = genre;
        }

        public  String getGenre(){
                return genre;
        }
        public void setPublicationDate(String publicationDate){
                this.publicationDate = publicationDate;
        }

        public String getPublicationDate(){
                return publicationDate;
        }

        public void setIsbn(String isbn){
                this.isbn = isbn;
        }

        public String getIsbn(){
                return isbn;
        }
}
