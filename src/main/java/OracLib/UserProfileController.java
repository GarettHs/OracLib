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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileController {
    @FXML
    private Button profilePicButton;
    @FXML
    private Button nameButton;
    @FXML
    private Button bioButton;
    @FXML
    private Button ageButton;
    @FXML
    private TextField inputUsername;
    @FXML
    private TextField inputAge;
    @FXML
    private TextField inputBio;
    @FXML
    private ImageView pictureView;
    @FXML
    private Label editMode;
    @FXML
    private Label statusLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label bioLabel;
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

    private Stage stage;
    private Main main;
    private String username;
    private String currentUserName;
    private UserProfiles userProfiles;
    private List<List<String>> profileInfo;

    public void setMain(Main main) {
        this.main = main;
        profilePicButton.setVisible(false);
        nameButton.setVisible(false);
        bioButton.setVisible(false);
        ageButton.setVisible(false);
        inputUsername.setVisible(false);
        inputAge.setVisible(false);
        inputBio.setVisible(false);
        editMode.setVisible(false);
        statusLabel.setVisible(false);
    }

    public void setUsername(String username) throws IOException {
        this.username = username;
        nameLabel.setText(username);
        currentUserName = username;
        userProfiles = main.getUserProfiles();
        displayInfo(username);
        List<Books> favBookLibrary = getUserBookData("src/main/resources/UserDatabase.txt", "BookLibrary.txt");
        populateFavTableView(favBookLibrary);
        favoriteTableView.setOnMouseClicked(this::handleFavBookClick);
    }

    public void displayInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (username.equals(user[0])) {
                    nameLabel.setText(user[0]);
                    if (!user[2].equals("none")) {
                        String profilePic = user[2];
                        try {
                            FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/ProfilePics/" + profilePic);
                            Image image = new Image(fileInputStream);
                            pictureView.setImage(image);
                            pictureView.setFitHeight(146);
                            pictureView.setFitWidth(146);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    bioLabel.setText(user[3].replaceAll("^\"|\"$", ""));
                    ageLabel.setText(user[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleAccountEdit(ActionEvent actionEvent) {
        if (editMode.isVisible()) {
            profilePicButton.setVisible(false);
            nameButton.setVisible(false);
            bioButton.setVisible(false);
            ageButton.setVisible(false);
            inputUsername.setVisible(false);
            inputAge.setVisible(false);
            inputBio.setVisible(false);
            editMode.setVisible(false);
            statusLabel.setVisible(false);
        } else {
            profilePicButton.setVisible(true);
            nameButton.setVisible(true);
            bioButton.setVisible(true);
            ageButton.setVisible(true);
            inputUsername.setVisible(true);
            inputAge.setVisible(true);
            inputBio.setVisible(true);
            editMode.setVisible(true);
            statusLabel.setVisible(true);
        }
    }

    public void handleProfilePicChange(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (!pictureView.getImage().toString().endsWith(file.getName())) {
                try {
                    System.out.println(file.getName());
                    pictureView.setImage(new Image(file.toURI().toString()));
                    pictureView.setFitWidth(146);
                    pictureView.setFitHeight(146);
                    System.out.println(pictureView.getImage().getUrl());

                    Path sourcePath = file.toPath();
                    Path destinationPath = new File("src/main/resources/img/ProfilePics/" + file.getName()).toPath();
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    updateProfile("ProfilePic", file.getName());
                    statusLabel.setText("Picture changed to " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                statusLabel.setText("Error: Current PFP is the same with inputted pfp!");
            }
        }
        else {
            statusLabel.setText("Error: Invalid image file (png, jpg, jpeg)");
        }
    }


    public void handleNameChange(ActionEvent actionEvent) {
        if (!inputUsername.getText().trim().isEmpty() && !inputUsername.getText().equals(nameLabel.getText())) {
            passwordConfirmation();
        } else if (inputUsername.getText().equals(nameLabel.getText())) {
            statusLabel.setText("Error: Current bio is same with inputted bio!");
        } else {
            statusLabel.setText("Error: No inputted text!");
        }
    }

    private void passwordConfirmation() {
        Stage passwordConfirmationStage = new Stage();
        passwordConfirmationStage.initModality(Modality.APPLICATION_MODAL);
        passwordConfirmationStage.setTitle("Confirm Username Change");

        // Create a label and password field for the password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Create a button to confirm the password
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String enteredPassword = passwordField.getText();
            if (isValid(currentUserName, enteredPassword)) {
                statusLabel.setText("Name changed to " + inputUsername.getText());
                nameLabel.setText(inputUsername.getText());
                updateProfile("Username", inputUsername.getText());
                passwordConfirmationStage.close();
                currentUserName = inputUsername.getText();
            } else {
                showErrorDialog("Incorrect password. Please try again.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(passwordLabel, passwordField, confirmButton);

        // Set the layout as the scene content
        Scene scene = new Scene(layout, 250, 150);
        passwordConfirmationStage.setScene(scene);

        passwordConfirmationStage.showAndWait();
    }

    private boolean isValid(String name, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length >= 2 && name.equals(user[0]) && password.equals(user[1])) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void handleBioChange(ActionEvent actionEvent) {
        if (!inputBio.getText().trim().isEmpty() && !inputBio.getText().equals(bioLabel.getText())) {
            statusLabel.setText("Bio changed to " + inputBio.getText());
            bioLabel.setText(inputBio.getText());
            updateProfile("Bio", "\"" + inputBio.getText() + "\"");
        } else if (inputBio.getText().equals(bioLabel.getText())) {
            statusLabel.setText("Error: Current bio is same with inputted bio!");
        } else {
            statusLabel.setText("Error: No inputted text!");
        }
    }

    public void handleAgeChange(ActionEvent actionEvent) {
        if (!inputAge.getText().trim().isEmpty() && !inputAge.getText().equals(ageLabel.getText())) {
            if (Double.parseDouble(inputAge.getText()) < 0 || Double.parseDouble(inputAge.getText()) >= 100) {
                statusLabel.setText("Error: Invalid Age!");
            } else {
                if (inputAge.getText().trim().length() == 1) {
                    inputAge.setText('0' + inputAge.getText().trim());
                }
                statusLabel.setText("Age changed to " + inputAge.getText());
                ageLabel.setText(inputAge.getText());
                updateProfile("Age", inputAge.getText());
            }
        } else if (inputAge.getText().equals(ageLabel.getText())) {
            statusLabel.setText("Error: Current age is same with inputted age!");
        } else {
            statusLabel.setText("Error: No inputted text!");
        }
    }

    private void updateProfile(String infoType, String input) {
        File file = new File("src/main/resources/UserDatabase.txt");
        try {
            profileInfo = readDatabase(file);
            System.out.println(profileInfo);

            int index = -1;
            String sIndex = "";
            if (infoType.equals("Username")) {
                index = 0;
                sIndex = "Username";
            } else if (infoType.equals("Password")) {
                index = 1;
                sIndex = "Password";
            } else if (infoType.equals("ProfilePic")) {
                index = 2;
                sIndex = "ProfilePic";
            } else if (infoType.equals("Bio")) {
                index = 3;
                sIndex = "Bio";
            } else if (infoType.equals("Age")) {
                index = 4;
                sIndex = "Age";
            } else if (infoType.equals("Favorites")) {
                index = 5;
                sIndex = "Favorites";
            }
            System.out.println(index + ": " + input);
            modifyProfile(index, input, currentUserName);

            saveProfile(file);

            System.out.println("'" + currentUserName.toUpperCase() + "' : [" + sIndex + "] modified to [" + input + "].");
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private List<List<String>> readDatabase(File file) throws IOException {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>(Arrays.asList(values));
                data.add(row);
            }
        }

        return data;
    }

    private void modifyProfile(int index, String newValue, String name) {
        if (profileInfo != null && index >= 0 && index < 10) {
            List<String> row = profileInfo.get(userProfiles.searchUser(name));
            if (row != null && row.size() > 2) {
                row.set(index, newValue);
                System.out.println(row);
            }
        }
    }

    private List<Books> getUserBookData(String filename1, String filename2) throws IOException {
        List<Books> bookLibrary = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (username.equals(userData[0])) {
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

    private void populateFavTableView(List<Books> bookLibrary) {
        favBookCodeColumn.setCellValueFactory(new PropertyValueFactory<>("bookCode"));
        favTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        favAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        favDatePublishedColumn.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
        favGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        ObservableList<Books> observableBookLibrary = FXCollections.observableArrayList(bookLibrary);
        favoriteTableView.setItems(observableBookLibrary);
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

    private void saveProfile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (List<String> row : profileInfo) {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    @FXML
    private void handleReturn() {
        Main main = new Main();
        String reName = currentUserName;
        System.out.println(currentUserName);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            try {
                main.sceneToMenu("oraclib-main.fxml", 895, 575, reName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
    }

    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
