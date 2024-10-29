USE bookinventorymanagement;
CREATE TABLE IF NOT EXISTS Inventory (
    EntryID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL,
    Genre VARCHAR(100),
    PublicationDate DATE,
    ISBN VARCHAR(20) UNIQUE NOT NULL
);
