package OracLib;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;


public class MainController {

    public Button adminViewButton;
    @FXML
    private TableView<Books> libraryTableView;
    @FXML
    private TableColumn<Books, String> titleColumn;
    @FXML
    private TableColumn<Books, String> authorColumn;
    @FXML
    private TableColumn<Books, String> datePublishedColumn;
    @FXML
    private TableColumn<Books, String> genreColumn;
    @FXML
    private TableColumn<Books, String> bookCodeColumn;
    @FXML
    private TableView<Books> favoriteTableView;
    @FXML
    private TableColumn<Books, String> favTitleColumn;
    @FXML
    private TableColumn<Books, String> favAuthorColumn;
    @FXML
    private TableColumn<Books, String> favDatePublishedColumn;
    @FXML
    private TableColumn<Books, String> favGenreColumn;
    @FXML
    private TableColumn<Books, String> favBookCodeColumn;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetFields;
    @FXML
    private ImageView userProfile;

    private Main main;
    private String name;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setProfile(String name) throws IOException {
        this.name = name;
        System.out.println("Main: " + name);
        adminViewButton.setVisible(false);
        retrieveProfilePicture(name);
        if (name.equalsIgnoreCase("admin") || name.equalsIgnoreCase("administrator")) {
            adminViewButton.setVisible(true);
        }
        List<Books> favBookLibrary = getUserBookData("src/main/resources/UserDatabase.txt", "BookLibrary.txt");
        populateFavTableView(favBookLibrary);
        favoriteTableView.setOnMouseClicked(this::handleFavBookClick);
    }

    @FXML
    private void initialize() {
        // Call the method to read book data and populate the TableView
        List<Books> bookLibrary = readBookDataFromCSV("BookLibrary.txt");
        populateTableView(bookLibrary);

        //Click event handler to the TableView
        libraryTableView.setOnMouseClicked(this::handleBookClick);
        resetFields.setVisible(false);

        // Set up the search bar event handler to clear initial text
        searchBar.setOnMouseClicked(event -> {
            if (searchBar.getText().equals("Any book that you are looking for?")) {
                searchBar.setText("");
            }
        });

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text changed: " + newValue);
            resetFields.setVisible(true);
        });
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String searchInput = searchBar.getText().trim().toLowerCase();
        List<Books> bookLibrary = readBookDataFromCSV("BookLibrary.txt");

        // Check if the search input is empty
        if (searchInput.isEmpty()) {
            // If the search bar is empty, display all books again
            ObservableList<Books> observableBookLibrary = FXCollections.observableArrayList(bookLibrary);
            libraryTableView.setItems(observableBookLibrary);
        } else {
            // Search for books based on similarity
            List<Books> filteredBooks = new ArrayList<>();
            double maxSimilarity = 0.0;

            for (Books book : bookLibrary) {
                String title = book.getTitle().toLowerCase();
                double similarity = calculateJaccardSimilarity(searchInput, title);

                // Keep track of the maximum similarity found so far
                maxSimilarity = Math.max(maxSimilarity, similarity);

                // Set a threshold for similarity (you can adjust this based on your preference)
                double similarityThreshold = 0.3; // For example, 0.3 means 30% similarity
                if (similarity >= similarityThreshold) {
                    filteredBooks.add(book);
                }
            }

            // If no book above the threshold was found, display the books with the highest similarity
            if (filteredBooks.isEmpty()) {
                for (Books book : bookLibrary) {
                    String title = book.getTitle().toLowerCase();
                    double similarity = calculateJaccardSimilarity(searchInput, title);
                    if (similarity == maxSimilarity) {
                        filteredBooks.add(book);
                    }
                }
            }

            ObservableList<Books> observableFilteredBooks = FXCollections.observableArrayList(filteredBooks);
            libraryTableView.setItems(observableFilteredBooks);
        }
    }

    @FXML
    private void handleClearReset (ActionEvent event) {
        searchBar.clear();
        resetFields.setVisible(false);

        List<Books> bookLibrary = readBookDataFromCSV("BookLibrary.txt");
        ObservableList<Books> observableBookLibrary = FXCollections.observableArrayList(bookLibrary);
        libraryTableView.setItems(observableBookLibrary);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text changed: " + newValue);
            resetFields.setVisible(true);
        });
    }

    @FXML
    private void showRandomBook(ActionEvent event) {
        // Get the number of books in the library
        List<Books> bookLibrary = readBookDataFromCSV("BookLibrary.txt");
        int numberOfBooks = bookLibrary.size();
        System.out.println(bookLibrary);
        System.out.println(numberOfBooks);

        // If the library is empty, return
        if (numberOfBooks == 0) {
            return;
        }

        // Generate a random index within the range of the number of books
        Random random = new Random();
        int randomIndex = random.nextInt(numberOfBooks);

        // Get the random book from the library
        Books randomBook = bookLibrary.get(randomIndex);
        System.out.println(randomBook.getBookCode());


        // Load and show the book details view using the LibraryController
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library-view.fxml"));
            Parent root = fxmlLoader.load();
            LibraryController libraryController = fxmlLoader.getController();
            libraryController.setBookData(randomBook);
            libraryController.setBookCode(randomBook.getBookCode());
            libraryController.setBookTitle(randomBook.getTitle());
            libraryController.setBookImage(randomBook.getBookCode()); // Add this line to set the book image


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(randomBook.getTitle());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showQuoteOfTheDay(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quote-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Quote of The Day");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateJaccardSimilarity(String str1, String str2) {
        String[] tokens1 = str1.split("\\s+");
        String[] tokens2 = str2.split("\\s+");

        HashSet<String> set1 = new HashSet<>(Arrays.asList(tokens1));
        HashSet<String> set2 = new HashSet<>(Arrays.asList(tokens2));

        int intersectionSize = 0;

        for (String token : set1) {
            if (set2.contains(token)) {
                intersectionSize++;
            }
        }

        double unionSize = set1.size() + set2.size() - intersectionSize;

        return intersectionSize / unionSize;
    }

    private void populateTableView(List<Books> bookLibrary) {
        bookCodeColumn.setCellValueFactory(new PropertyValueFactory<>("bookCode"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        datePublishedColumn.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        ObservableList<Books> observableBookLibrary = FXCollections.observableArrayList(bookLibrary);
        libraryTableView.setItems(observableBookLibrary);
    }

    private void populateFavTableView(List<Books> bookLibrary) {
        favBookCodeColumn.setCellValueFactory(new PropertyValueFactory<>("bookCode"));
        favTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        favAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        favDatePublishedColumn.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
        favGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        ObservableList<Books> observableBookLibrary = FXCollections.observableArrayList(bookLibrary);
        favoriteTableView.setItems(observableBookLibrary);
    }

    private List<Books> readBookDataFromCSV(String filename) {
        List<Books> bookLibrary = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getResourceAsStream("/Library/" + filename);
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 7) {
                        String bookCode = data[0].trim();
                        String title = data[1].trim().replaceAll("^\"|\"$", "");
                        String author = data[2].trim().replaceAll("^\"|\"$", "");
                        String yearPublished = data[3].trim();
                        String genre = data[4].trim().replaceAll("^\"|\"$", "");
                        String description = data[5].trim().replaceAll("^\"|\"$", "");
                        String link = data[6].trim().replaceAll("^\"|\"$", "");


                        Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                        bookLibrary.add(books);

                    }
                }

                bufferedReader.close();
            } else {
                System.err.println("File not found: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookLibrary;
    }

    private List<Books> getUserBookData(String filename1, String filename2) throws IOException {
        List<Books> bookLibrary = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (name.equals(userData[0])) {
                    String book1 = "";
                    String book2 = "";
                    String book3 = "";
                    String book4 = "";
                    String book5 = "";
                    if (!userData[5].equals("book1")) {
                        book1 = userData[5];
                    }
                    if (!userData[6].equals("book2")) {
                        book2 = userData[6];
                    }
                    if (!userData[7].equals("book3")) {
                        book3 = userData[7];
                    }
                    if (!userData[8].equals("book4")) {
                        book4 = userData[8];
                    }
                    if (!userData[9].equals("book5")) {
                        book5 = userData[9];
                    }
                    try {
                        InputStream inputStream = getClass().getResourceAsStream("/Library/" + filename2);
                        if (inputStream != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                            String line1;
                            while ((line1 = bufferedReader.readLine()) != null) {
                                String[] data = line1.split(",");
                                if (book1.equals(data[0])) {
                                    String bookCode = data[0].trim();
                                    String title = data[1].trim().replaceAll("^\"|\"$", "");
                                    String author = data[2].trim().replaceAll("^\"|\"$", "");
                                    String yearPublished = data[3].trim();
                                    String genre = data[4].trim().replaceAll("^\"|\"$", "");
                                    String description = data[5].trim().replaceAll("^\"|\"$", "");
                                    String link = data[6].trim().replaceAll("^\"|\"$", "");


                                    Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                                    bookLibrary.add(books);
                                }
                                if (book2.equals(data[0])) {
                                    String bookCode = data[0].trim();
                                    String title = data[1].trim().replaceAll("^\"|\"$", "");
                                    String author = data[2].trim().replaceAll("^\"|\"$", "");
                                    String yearPublished = data[3].trim();
                                    String genre = data[4].trim().replaceAll("^\"|\"$", "");
                                    String description = data[5].trim().replaceAll("^\"|\"$", "");
                                    String link = data[6].trim().replaceAll("^\"|\"$", "");


                                    Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                                    bookLibrary.add(books);
                                }
                                if (book3.equals(data[0])) {
                                    String bookCode = data[0].trim();
                                    String title = data[1].trim().replaceAll("^\"|\"$", "");
                                    String author = data[2].trim().replaceAll("^\"|\"$", "");
                                    String yearPublished = data[3].trim();
                                    String genre = data[4].trim().replaceAll("^\"|\"$", "");
                                    String description = data[5].trim().replaceAll("^\"|\"$", "");
                                    String link = data[6].trim().replaceAll("^\"|\"$", "");


                                    Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                                    bookLibrary.add(books);
                                }
                                if (book4.equals(data[0])) {
                                    String bookCode = data[0].trim();
                                    String title = data[1].trim().replaceAll("^\"|\"$", "");
                                    String author = data[2].trim().replaceAll("^\"|\"$", "");
                                    String yearPublished = data[3].trim();
                                    String genre = data[4].trim().replaceAll("^\"|\"$", "");
                                    String description = data[5].trim().replaceAll("^\"|\"$", "");
                                    String link = data[6].trim().replaceAll("^\"|\"$", "");


                                    Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                                    bookLibrary.add(books);
                                }
                                if (book5.equals(data[0])) {
                                    String bookCode = data[0].trim();
                                    String title = data[1].trim().replaceAll("^\"|\"$", "");
                                    String author = data[2].trim().replaceAll("^\"|\"$", "");
                                    String yearPublished = data[3].trim();
                                    String genre = data[4].trim().replaceAll("^\"|\"$", "");
                                    String description = data[5].trim().replaceAll("^\"|\"$", "");
                                    String link = data[6].trim().replaceAll("^\"|\"$", "");


                                    Books books = new Books(bookCode, title, author, yearPublished, genre, description, link);
                                    bookLibrary.add(books);
                                }
                            }

                            bufferedReader.close();

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return bookLibrary;
    }

    @FXML
    private void handleBookClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Books selectedBook = libraryTableView.getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                String bookCode = selectedBook.getBookCode();
                System.out.println("Clicked book code: " + bookCode);

                // Load and show the book details view using the LibraryController
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Polymorphism - Creates Instances of LibraryController utilizing the LibraryView Interface
                    LibraryController libraryController = fxmlLoader.getController();
                    libraryController.setMain(main);
                    libraryController.setBookCode(bookCode);
                    libraryController.setBookData(selectedBook);
                    libraryController.setBookTitle(selectedBook.getTitle());
                    libraryController.setBookImage(selectedBook.getBookCode());

                    Stage stage = new Stage();
                    libraryController.setPrimaryStage(stage);
                    stage.setScene(new Scene(root));
                    stage.setTitle(selectedBook.getTitle());
                    Image icon = new Image(getClass().getResource("/img/logo.png").toExternalForm());
                    stage.getIcons().add(icon);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleFavBookClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Books selectedBook = favoriteTableView.getSelectionModel().getSelectedItem();

            if (selectedBook != null) {
                String bookCode = selectedBook.getBookCode();
                System.out.println("Clicked book code: " + bookCode);

                // Load and show the book details view using the LibraryController
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Polymorphism - Creates Instances of LibraryController utilizing the LibraryView Interface
                    LibraryController libraryController = fxmlLoader.getController();
                    libraryController.setMain(main);
                    libraryController.setBookCode(bookCode);
                    libraryController.setBookData(selectedBook);
                    libraryController.setBookTitle(selectedBook.getTitle());
                    libraryController.setBookImage(selectedBook.getBookCode());

                    Stage stage = new Stage();
                    libraryController.setPrimaryStage(stage);
                    stage.setScene(new Scene(root));
                    stage.setTitle(selectedBook.getTitle());
                    Image icon = new Image(getClass().getResource("/img/logo.png").toExternalForm());
                    stage.getIcons().add(icon);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        Main main = new Main();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            try {
                main.sceneToProfile("user-profile.fxml", 895, 575, name);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
    }

    private void retrieveProfilePicture(String fullName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length >= 2 && fullName.equals(user[0])) {
                    if (!user[2].equals("none")) {
                        String profilePicture = user[2];
                        try {
                            FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/ProfilePics/" + profilePicture);
                            Image image = new Image(fileInputStream);
                            userProfile.setImage(image);
                            userProfile.setFitHeight(50);
                            userProfile.setFitWidth(50);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminAccess(ActionEvent actionEvent) {
        Main main = new Main();
        if (name.equalsIgnoreCase("admin") || name.equalsIgnoreCase("administrator")) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    main.sceneToAdminView("admin-view.fxml", 895, 575, name);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            pause.play();
        } else {
            showErrorDialog("Current user is not an Admin.");
            System.out.println("WARNING! Unauthorized Access Detected | Current User : '" + name + "'");
        }
    }

    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    @FXML
    private void onLogout(ActionEvent e) throws IOException {
        Main main = new Main();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            main.sceneToLogin("login-view.fxml", 781, 530);
        }
    }
}
