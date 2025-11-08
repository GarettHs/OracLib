package OracLib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;

public class LibraryController implements LibraryView {
    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private Label description;

    @FXML
    private ImageView bookImageView;

    @FXML
    private Hyperlink link;
    private Main main;
    private Stage primaryStage;
    private String bookCode;
    private String bookTitle;

    @Override
    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    // Abstraction - Implementation of setBookData from LibraryView
    @Override
    public void setBookData(Books book) {
        title.setText(book.getTitle());
        author.setText("Author: " + book.getAuthor());
        description.setText(book.getDescription());
        link.setText(book.getLink());
    }

    @Override
    public void setBookTitle(String title) {
        this.bookTitle = title;
    }

    @Override
    public void setBookImage(String bookCode) {
        String imagePath = "/img/" + bookCode + ".jpg";
        try {
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                bookImageView.setImage(image);
            } else {
                System.out.println("Image file not found: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePDF(ActionEvent actionEvent) {
        String bookC = bookCode;
        String bookT = bookTitle;
        BookViewer bookViewer = new BookViewer();
        bookViewer.setBook(bookC);
        bookViewer.setTitle(bookT);
        bookViewer.openBook();

        primaryStage.close();
    }

    @FXML
    private void handleFavoriteButton(ActionEvent actionEvent) {
        try {
            // Read user profiles from UserDatabase.txt
            UserProfiles userProfiles = readUserProfilesFromTXT("src/main/resources/UserDatabase.txt");

            // Get the current username using the Main class
            String currentUsername = Main.getCurrentUsername();

            // Get the current user index from the UserProfiles
            int currentUserIndex = userProfiles.searchUser(currentUsername);

            if (currentUserIndex != -1) {
                UserData currentUser = userProfiles.getUser(currentUserIndex);

                String code = bookCode;

                // Debugging: Print the book title and current user books before replacement
                System.out.println("Clicked book title: " + code);
                System.out.println("Username: " + currentUser.getUsername());
                System.out.println("Book1: " + currentUser.getBook1());
                System.out.println("Book2: " + currentUser.getBook2());
                System.out.println("Book3: " + currentUser.getBook3());
                System.out.println("Book4: " + currentUser.getBook4());
                System.out.println("Book5: " + currentUser.getBook5());

                // Replace the first available empty slot for favorite book
                replaceFirstFavoriteBook(currentUser, code);

                // Save the updated user data back to the UserDatabase.txt
                saveUserProfilesToTXT("src/main/resources/UserDatabase.txt", userProfiles);

                // Debugging: Print the current user books after replacement
                System.out.println("Updated Book1: " + currentUser.getBook1());
                System.out.println("Updated Book2: " + currentUser.getBook2());
                System.out.println("Updated Book3: " + currentUser.getBook3());
                System.out.println("Updated Book4: " + currentUser.getBook4());
                System.out.println("Updated Book5: " + currentUser.getBook5());

                // Show a confirmation message or update the UI accordingly
                // (e.g., disable the favorite button for the same book)
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    private void replaceFirstFavoriteBook(UserData currentUser, String code) {
        // Check if the book is already in favorites and return if it is
        if (code.equalsIgnoreCase(currentUser.getBook1())
                || code.equalsIgnoreCase(currentUser.getBook2())
                || code.equalsIgnoreCase(currentUser.getBook3())
                || code.equalsIgnoreCase(currentUser.getBook4())
                || code.equalsIgnoreCase(currentUser.getBook5())) {
            System.out.println("The book is already a favorite.");
            return;
        }

        // Check and replace the first slot that contains the corresponding "bookX" string
        for (int i = 1; i <= 5; i++) {
            String bookX = "book" + i;
            if (currentUser.getBook1().contains(bookX)) {
                currentUser.setBook1(code);
                return;
            } else if (currentUser.getBook2().contains(bookX)) {
                currentUser.setBook2(code);
                return;
            } else if (currentUser.getBook3().contains(bookX)) {
                currentUser.setBook3(code);
                return;
            } else if (currentUser.getBook4().contains(bookX)) {
                currentUser.setBook4(code);
                return;
            } else if (currentUser.getBook5().contains(bookX)) {
                currentUser.setBook5(code);
                return;
            }
        }

        // If none of the slots contains "bookX" string, replace Book1 on the first click,
        // Book2 on the second click, and so on up to Book5 on the fifth click.
        currentUser.setBook1(code);
    }

    private UserProfiles readUserProfilesFromTXT(String filename) throws IOException {
        UserProfiles userProfiles = new UserProfiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 10) {
                    String username = userData[0].trim();
                    String password = userData[1].trim();
                    String profilePic = userData[2].trim();
                    String userBio = userData[3].trim();
                    String age = userData[4].trim();
                    String book1 = userData[5].trim();
                    String book2 = userData[6].trim();
                    String book3 = userData[7].trim();
                    String book4 = userData[8].trim();
                    String book5 = userData[9].trim();

                    UserData user = new UserData(username, password, profilePic, userBio, age, book1, book2, book3, book4, book5);
                    userProfiles.addUser(user);
                }
            }
        }

        return userProfiles;
    }

    private void saveUserProfilesToTXT(String filename, UserProfiles userProfiles) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (UserData user : userProfiles.getUsers()) {
                String userDataString = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        user.getUsername(), user.getPassword(), user.getProfilePic(), user.getUserBio(),
                        user.getAge(), user.getBook1(), user.getBook2(), user.getBook3(), user.getBook4(), user.getBook5());
                writer.write(userDataString);
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle the case where an error occurs while writing to the UserDatabase.txt file (optional)
            e.printStackTrace();
        }
    }
}


