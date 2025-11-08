package OracLib;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.io.FileDeleteStrategy;

public class AdminViewController {
    @FXML
    private Label editLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Label bookNameLabel;
    @FXML
    private Label bookAuthorLabel;
    @FXML
    private Label bookGenreLabel;
    @FXML
    private Label bookDescriptionLabel;
    @FXML
    private Label bookYearLabel;
    @FXML
    private Label statusChangeLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField bioField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField imageField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField linkField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField coverPhotoField;
    @FXML
    private TextField pdfNameField;
    @FXML
    private TextField bookInput;
    @FXML
    private TextField userInput;
    @FXML
    private ImageView bookImageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private AnchorPane bookAdminView;
    @FXML
    private AnchorPane userAdminView;
    @FXML
    private Button saveChangesButton;
    @FXML
    private Button saveUserButton;
    @FXML
    private Button deleteUserNameButton;
    @FXML
    private Button profilePicture;
    @FXML
    private Button saveBookButton;
    @FXML
    private Button saveBookChanges;
    @FXML
    private Button deleteBookCodeButton;
    @FXML
    private Button coverPhotoButton;
    @FXML
    private Button pdfFileButton;
    @FXML
    private Button bookSearch;
    @FXML
    private Button userSearch;
    private Stage stage;
    private Main main;
    private String adminName;
    private String currentUserName;
    private String currentPassword;
    private String currentBookCode;
    private String currentImage;
    private FileInputStream currentInputStream;
    private UserProfiles userProfiles;
    private BookProfiles bookProfiles;
    private List<List<String>> profileInfo;
    private List<List<String>> bookInfo;

    public void setMain(Main main) {
        this.main = main;
        bookAdminView.setVisible(false);
        userAdminView.setVisible(false);
        bookSearch.setVisible(false);
        userSearch.setVisible(false);
        bookInput.setVisible(false);
        userInput.setVisible(false);
        codeField.setTextFormatter(textFormatter);
    }

    public void setUsername(String adminName) {
        this.adminName = adminName;
        userProfiles = main.getUserProfiles();
        bookProfiles = main.getBookProfiles();
    }

    @FXML
    private void handleAddBook(ActionEvent actionEvent) {
        if (!bookAdminView.isVisible() || (bookAdminView.isVisible() && (saveBookChanges.isVisible() || deleteBookCodeButton.isVisible()))) {
            editLabel.setText("Add Book Mode");
            userInput.setVisible(false);
            userSearch.setVisible(false);
            bookAdminView.setVisible(true);
            userAdminView.setVisible(false);
            saveBookButton.setVisible(true);
            saveBookChanges.setVisible(false);
            deleteBookCodeButton.setVisible(false);
            bookSearch.setVisible(false);
            bookInput.setVisible(false);
            codeField.setVisible(true);
            titleField.setVisible(true);
            authorField.setVisible(true);
            genreField.setVisible(true);
            yearField.setVisible(true);
            descriptionField.setVisible(true);
            linkField.setVisible(true);
            coverPhotoField.setVisible(true);
            coverPhotoButton.setVisible(true);
            pdfNameField.setVisible(true);
            pdfFileButton.setVisible(true);
            clearBookFields();
            clearBookDisplay();
        } else if (bookAdminView.isVisible()) {
            clearBookFields();
            clearBookDisplay();
            bookAdminView.setVisible(false);
        }
    }

    @FXML
    private void handleEditBook(ActionEvent actionEvent) {
        if (!bookAdminView.isVisible() || (bookAdminView.isVisible() && (saveBookButton.isVisible() || deleteBookCodeButton.isVisible()))) {
            editLabel.setText("Edit Book Mode");
            userInput.setVisible(false);
            userSearch.setVisible(false);
            bookAdminView.setVisible(true);
            userAdminView.setVisible(false);
            saveBookButton.setVisible(false);
            saveBookChanges.setVisible(true);
            deleteBookCodeButton.setVisible(false);
            bookSearch.setVisible(true);
            bookInput.setVisible(true);
            codeField.setVisible(true);
            titleField.setVisible(true);
            authorField.setVisible(true);
            genreField.setVisible(true);
            yearField.setVisible(true);
            descriptionField.setVisible(true);
            linkField.setVisible(true);
            coverPhotoField.setVisible(true);
            coverPhotoButton.setVisible(true);
            pdfNameField.setVisible(true);
            pdfFileButton.setVisible(true);
            clearBookFields();
            clearBookDisplay();
        } else if (bookAdminView.isVisible()) {
            clearBookFields();
            clearBookDisplay();
            bookAdminView.setVisible(false);
        }
    }

    @FXML
    private void handleDeleteBook(ActionEvent actionEvent) {
        if (!bookAdminView.isVisible() || (bookAdminView.isVisible() && (saveBookButton.isVisible() || saveBookChanges.isVisible()))) {
            editLabel.setText("Delete Book Mode");
            userInput.setVisible(false);
            userSearch.setVisible(false);
            bookAdminView.setVisible(true);
            userAdminView.setVisible(false);
            saveBookButton.setVisible(false);
            saveBookChanges.setVisible(false);
            deleteBookCodeButton.setVisible(true);
            bookSearch.setVisible(true);
            bookInput.setVisible(true);
            codeField.setVisible(false);
            titleField.setVisible(false);
            authorField.setVisible(false);
            genreField.setVisible(false);
            yearField.setVisible(false);
            descriptionField.setVisible(false);
            linkField.setVisible(false);
            coverPhotoField.setVisible(false);
            coverPhotoButton.setVisible(false);
            pdfNameField.setVisible(false);
            pdfFileButton.setVisible(false);
            clearBookFields();
            clearBookDisplay();
        }  else if (bookAdminView.isVisible()) {
            clearBookFields();
            clearBookDisplay();
            bookAdminView.setVisible(false);
        }
    }

    @FXML
    private void handleAddUser(ActionEvent actionEvent) {
        if (!userAdminView.isVisible() || (userAdminView.isVisible() && (saveChangesButton.isVisible() || deleteUserNameButton.isVisible()))) {
            bookInput.setVisible(false);
            bookSearch.setVisible(false);
            editLabel.setText("Add User Mode");
            userAdminView.setVisible(true);
            bookAdminView.setVisible(false);
            saveUserButton.setVisible(true);
            saveChangesButton.setVisible(false);
            deleteUserNameButton.setVisible(false);
            userSearch.setVisible(false);
            userInput.setVisible(false);
            usernameField.setVisible(true);
            passwordField.setVisible(true);
            ageField.setVisible(true);
            imageField.setVisible(true);
            bioField.setVisible(true);
            profilePicture.setVisible(true);
            clearUserFields();
            clearUserDisplay();
        } else if (userAdminView.isVisible()) {
            clearUserFields();
            clearUserDisplay();
            userAdminView.setVisible(false);
        }
    }

    @FXML
    private void handleEditUser(ActionEvent actionEvent) {
        if (!userAdminView.isVisible() || (userAdminView.isVisible() && (saveUserButton.isVisible() || deleteUserNameButton.isVisible()))) {
            editLabel.setText("Edit User Mode");
            bookInput.setVisible(false);
            bookSearch.setVisible(false);
            userAdminView.setVisible(true);
            bookAdminView.setVisible(false);
            saveUserButton.setVisible(false);
            saveChangesButton.setVisible(true);
            deleteUserNameButton.setVisible(false);
            userSearch.setVisible(true);
            userInput.setVisible(true);
            usernameField.setVisible(true);
            passwordField.setVisible(true);
            ageField.setVisible(true);
            imageField.setVisible(true);
            bioField.setVisible(true);
            profilePicture.setVisible(true);
            clearUserFields();
            clearUserDisplay();
        } else if (userAdminView.isVisible()) {
            clearUserFields();
            clearUserDisplay();
            userAdminView.setVisible(false);
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent actionEvent) {
        if (!userAdminView.isVisible() || (userAdminView.isVisible() && (saveChangesButton.isVisible() || saveUserButton.isVisible()))) {
            editLabel.setText("Delete User Mode");
            bookInput.setVisible(false);
            bookSearch.setVisible(false);
            userAdminView.setVisible(true);
            bookAdminView.setVisible(false);
            saveUserButton.setVisible(false);
            saveChangesButton.setVisible(false);
            deleteUserNameButton.setVisible(true);
            userSearch.setVisible(true);
            userInput.setVisible(true);
            usernameField.setVisible(false);
            passwordField.setVisible(false);
            ageField.setVisible(false);
            imageField.setVisible(false);
            bioField.setVisible(false);
            profilePicture.setVisible(false);
            clearUserFields();
            clearUserDisplay();
        } else if (userAdminView.isVisible()) {
            userAdminView.setVisible(false);
            clearUserFields();
            clearUserDisplay();
        }
    }

    @FXML
    private void handleProfilePicture(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (!userImageView.getImage().toString().endsWith(file.getName())) {
                try {
                    Path sourcePath = file.toPath();
                    Path destinationPath = new File("src/main/resources/img/ProfilePics/" + file.getName()).toPath();
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    imageField.setText(file.getName());
                    statusChangeLabel.setText("User image changed to " + file.getName());
                    statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                statusChangeLabel.setText("Error: Current PFP is the same with inputted pfp!");
                statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            }
        }
        else {
            statusChangeLabel.setText("Error: Invalid image file (png, jpg, jpeg)");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
        }
    }

    @FXML
    private void handleCoverPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            if (!bookImageView.getImage().toString().endsWith(file.getName())) {
                if (!codeField.getText().trim().isEmpty()){
                    try {
                        Path sourcePath = file.toPath();
                        Path destinationPath = new File("src/main/resources/img/" + codeField.getText().trim() + ".jpg").toPath();
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        coverPhotoField.setText(file.getName());
                        statusChangeLabel.setText("Book image changed to " + file.getName());
                        statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    statusChangeLabel.setText("Error: No book code inputted yet!");
                    statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                }
            } else {
                statusChangeLabel.setText("Error: Current image is the same with inputted image!");
                statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            }
        } else {
            statusChangeLabel.setText("Error: Invalid image file (jpg only)");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
        }
    }

    @FXML
    private void handlePDFFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Document Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Path sourcePath = file.toPath();
                Path destinationPath = new File("src/main/resources/Library/" + codeField.getText().trim() + ".pdf").toPath();
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                pdfNameField.setText(file.getName());
                statusChangeLabel.setText("Book image changed to " + file.getName());
                statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            statusChangeLabel.setText("Error: Invalid document file (pdf only)");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
        }
    }

    @FXML
    private void displayBook(String code) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Library/BookLibrary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] book = line.split(",");
                if (code.equals(book[0])) {
                    currentBookCode = book[0];
                    try {
                        FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/" + code + ".jpg");
                        currentInputStream = fileInputStream;
                        Image image = new Image(fileInputStream);
                        bookImageView.setImage(image);
                        bookImageView.setFitHeight(178);
                        bookImageView.setFitWidth(143);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    bookNameLabel.setText(book[1].replaceAll("^\"|\"$", ""));
                    bookAuthorLabel.setText(book[2].replaceAll("^\"|\"$", ""));
                    bookYearLabel.setText(book[3]);
                    bookGenreLabel.setText(book[4].replaceAll("^\"|\"$", ""));
                    bookDescriptionLabel.setText(book[5].replaceAll("^\"|\"$", "").replace("_", ","));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void displayInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (username.equals(user[0])) {
                    currentUserName = user[0];
                    currentPassword = user[1];
                    nameLabel.setText(user[0]);
                    if (!user[2].equals("none")) {
                        String profilePic = user[2];
                        try {
                            FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/ProfilePics/" + profilePic);
                            currentInputStream = fileInputStream;
                            Image image = new Image(fileInputStream);
                            userImageView.setImage(image);
                            userImageView.setFitHeight(116);
                            userImageView.setFitWidth(116);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    bioLabel.setText(user[3].replaceAll("^\"|\"$", ""));
                    ageLabel.setText("Age: " + user[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUserSearch(ActionEvent actionEvent) {
        if (userInput.getText().trim().isEmpty()) {
            statusChangeLabel.setText("Error: No inputted text");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        if (!isNameExists(userInput.getText().trim())) {
            statusChangeLabel.setText("Error: User doesn't exist");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        if (isNotValidString(userInput.getText().trim())) {
            statusChangeLabel.setText("Error: Invalid name format");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        clearUserDisplay();
        displayInfo(userInput.getText().trim());
        statusChangeLabel.setText(userInput.getText().trim() + "'s profile successfully loaded");
        statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
        userInput.clear();
        countProfiles("src/main/resources/UserDatabase.txt");
        loadProfiles("src/main/resources/UserDatabase.txt");
    }

    @FXML
    private void handleSaveUser(ActionEvent actionEvent) {
        updateProfile("AddUser");
    }

    @FXML
    private void handleUserChanges(ActionEvent actionEvent) {
        updateProfile("EditUser");
    }

    @FXML
    private void handleDelUser(ActionEvent actionEvent) {
        updateProfile("DelUser");
    }

    @FXML
    private void handleBookSearch(ActionEvent actionEvent) {
        if (bookInput.getText().trim().isEmpty()) {
            statusChangeLabel.setText("Error: No inputted text");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        if (bookInput.getText().trim().length() != 4 ) {
            statusChangeLabel.setText("Error: Invalid book code");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        if (!isBookExists(bookInput.getText().trim())) {
            statusChangeLabel.setText("Error: Book with code " + bookInput.getText().trim() + " doesn't exist");
            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
            return;
        }
        clearBookDisplay();
        displayBook(bookInput.getText().trim());
        currentBookCode = bookInput.getText();
        statusChangeLabel.setText("Book " + bookInput.getText().trim() + " successfully loaded");
        statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
        bookInput.clear();
        countBooks("src/main/resources/Library/BookLibrary.txt");
        loadBooks("src/main/resources/Library/BookLibrary.txt");
    }

    @FXML
    private void handleNewBook(ActionEvent actionEvent) {
        updateBook("AddBook");
    }

    @FXML
    private void handleBookChanges(ActionEvent actionEvent) {
        updateBook("EditBook");
    }

    @FXML
    private void handleDelBook(ActionEvent actionEvent) {
        updateBook("DelBook");
    }

    private void updateBook(String editMode) {
            File file = new File("src/main/resources/Library/BookLibrary.txt");
            try {
                bookInfo = readDatabase(file);
                System.out.println(bookInfo);

                if (!editMode.equals("DelBook")) {
                    String code = codeField.getText().trim();
                    String title = titleField.getText().trim();
                    String author = authorField.getText().trim();
                    String genre = genreField.getText().trim();
                    String year = yearField.getText().trim();
                    String desc = descriptionField.getText().trim();
                    String link = linkField.getText().trim();
                    String covPhoto = coverPhotoField.getText().trim();
                    String pdf = pdfNameField.getText();

                    if (editMode.equals("AddBook") || editMode.equals("EditBook")) {
                        if (code.isEmpty() && title.isEmpty() && author.isEmpty() && genre.isEmpty() && year.isEmpty() && desc.isEmpty() && link.isEmpty() && covPhoto.isEmpty() && pdf.isEmpty()) {
                            statusChangeLabel.setText("Error: No Book Info to Add");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    }

                    if (editMode.equals("EditBook")) {
                        if (!bookNameLabel.getText().equals("BookName")) {
                            code = currentBookCode;
                        } else {
                            statusChangeLabel.setText("Error: No book loaded yet");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    }

                    boolean a = code.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty() || year.isEmpty() || desc.isEmpty() || link.isEmpty() || covPhoto.isEmpty() || pdf.isEmpty();
                    if (editMode.equals("AddBook")) {
                        if (isBookExists(code)) {
                            statusChangeLabel.setText("Error: Book already exists!");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                        if (a) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Missing Inputs");
                            alert.setHeaderText("Fill in all input fields!");
                            alert.showAndWait();
                            return;
                        }
                        if (year.length() != 4) {
                            statusChangeLabel.setText("Error: Invalid Year");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    } else if (a){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Missing Inputs");
                        alert.setHeaderText("Would you like to only update these?");
                        alert.showAndWait();
                    }

                    //Necessary checks need to be made to ensure that all fields have inputs

                    switch (editMode) {
                        case "AddBook" -> {
                            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/Library/BookLibrary.txt", true))) {
                                writer.println(code + ",\"" + title + "\",\"" + author + "\"," + year + ",\"" + genre + "\",\"" + desc.replace(",", "_") + "\",\"" + link + "\"");
                                bookNameLabel.setText(title);
                                bookAuthorLabel.setText(author);
                                bookYearLabel.setText(year);
                                    try {
                                        FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/" + code + ".jpg");
                                        Image image = new Image(fileInputStream);
                                        userImageView.setImage(image);
                                        userImageView.setFitHeight(178);
                                        userImageView.setFitWidth(143);
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                statusChangeLabel.setText("\"" + title + "\" successfully added to database.");
                                statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case "EditBook" -> {
                            int i = 0;
                            if (!codeField.getText().isEmpty()) {
                                modifyBookDatabase(0, code, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!titleField.getText().isEmpty()) {
                                title = "\"" + title + "\"";
                                modifyBookDatabase(1, title, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!authorField.getText().isEmpty()) {
                                author = "\"" + author + "\"";
                                modifyBookDatabase(2, author, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!yearField.getText().isEmpty()) {
                                modifyBookDatabase(3, year, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!genreField.getText().isEmpty()) {
                                genre = "\"" + genre + "\"";
                                modifyBookDatabase(4, genre, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!descriptionField.getText().isEmpty()) {
                                desc = "\"" + desc + "\"";
                                modifyBookDatabase(5, desc, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (!linkField.getText().isEmpty()) {
                                link = "\"" + link + "\"";
                                modifyBookDatabase(6, link, currentBookCode);
                                saveBookDatabase(file);
                                i++;
                            }
                            if (i > 0) {
                                statusChangeLabel.setText("Changes made on book " + code);
                                statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                            } else {
                                statusChangeLabel.setText("No changes made on book " + code);
                                statusChangeLabel.setTextFill(Color.rgb(0, 0, 0));
                            }
                        }
                    }
                } else {
                    if (!bookNameLabel.getText().equals("BookName")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Book");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure you want to delete this book?");

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            List<String> lines = new ArrayList<>();
                            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Library/BookLibrary.txt"))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String[] user = line.split(",");
                                    if (user.length >= 2 && !currentBookCode.equals(user[0])) {
                                        lines.add(line);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/Library/BookLibrary.txt"))) {
                                for (String line : lines) {
                                    writer.println(line);
                                    String projectDir = System.getProperty("user.dir");
                                    File imgFile = new File(projectDir + "\\src\\main\\resources\\img\\" + currentBookCode + ".jpg");
                                    File pdfFile = new File(projectDir + "\\src\\main\\resources\\Library\\" + currentBookCode + ".pdf");
                                    clearBookDisplay();
                                    currentInputStream.close();
                                    try {
                                        FileDeleteStrategy.FORCE.delete(imgFile);
                                        FileDeleteStrategy.FORCE.delete(pdfFile);
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    statusChangeLabel.setText(currentBookCode + " successfully deleted.");
                                    statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        statusChangeLabel.setText("Error: No book loaded yet");
                        statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
    }

    private void updateProfile(String editMode) {
            File file = new File("src/main/resources/UserDatabase.txt");
            try {
                profileInfo = readDatabase(file);
                System.out.println(profileInfo);

                if (!editMode.equals("DelUser")) {
                    String name = usernameField.getText().trim();
                    String pass = passwordField.getText().trim();
                    String age = ageField.getText().trim();
                    String pfp = imageField.getText();
                    String bio = bioField.getText().trim();

                    if (editMode.equals("EditUser")) {
                        if (!nameLabel.getText().equals("Username")) {
                            name = currentUserName;
                            pass = currentPassword;
                        } else {
                            statusChangeLabel.setText("Error: No user loaded yet");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    }

                    if (editMode.equals("AddUser") || editMode.equals("EditUser")) {
                        if (name.isEmpty() && pass.isEmpty() && age.isEmpty() && pfp.isEmpty() && bio.isEmpty()) {
                            statusChangeLabel.setText("Error: No User Info to Add");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    }

                    if (!age.isEmpty() && (Double.parseDouble(age) < 0 || Double.parseDouble(age) > 100)) {
                        statusChangeLabel.setText("Error: Invalid Age");
                        statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                        return;
                    }

                    if (isNotValidString(name) || isNotValidString(pass)) {
                        statusChangeLabel.setText("Error: No Special Characters on Name and Password");
                        statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                        return;
                    }

                    if (editMode.equals("AddUser")) {
                        if (isNameExists(name)) {
                            statusChangeLabel.setText("Error: User already exists!");
                            statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                            return;
                        }
                    }

                    if (age.isEmpty() || pfp.isEmpty() || bio.isEmpty()) {
                        if (name.isEmpty() || pass.isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Missing Inputs");
                            alert.setHeaderText(null);
                            alert.setContentText("Name and Password Fields cannot be empty");
                            alert.showAndWait();
                            return;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Missing Inputs");
                            alert.setHeaderText("Missing inputs in text fields. Proceed anyway?");
                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                if (age.isEmpty()) {
                                    age = "00";
                                }
                                if (pfp.isEmpty()) {
                                    pfp = "none";
                                }
                                if (bio.isEmpty()) {
                                    bio = "\"empty\"";
                                }
                            }
                        }
                    }

                    //Necessary checks need to be made to ensure that all fields have inputs

                    switch (editMode) {
                        case "AddUser" -> {
                            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/UserDatabase.txt", true))) {
                                writer.println(name + "," + pass + "," + pfp + ",\"" + bio + "\"," + age + ",book1,book2,book3,book4,book5");
                                nameLabel.setText(name);
                                bioLabel.setText(bio);
                                ageLabel.setText(age);
                                if (!pfp.equals("none")) {
                                    try {
                                        FileInputStream fileInputStream = new FileInputStream("src/main/resources/img/ProfilePics/" + pfp);
                                        Image image = new Image(fileInputStream);
                                        userImageView.setImage(image);
                                        userImageView.setFitHeight(116);
                                        userImageView.setFitWidth(116);
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                statusChangeLabel.setText(name + " successfully added to database.");
                                statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case "EditUser" -> {
                            int i = 0;
                            if (!usernameField.getText().isEmpty()) {
                                modifyDatabase(0, name, currentUserName);
                                saveDatabase(file);
                                currentUserName = name;
                                i++;
                            }
                            if (!passwordField.getText().isEmpty()) {
                                modifyDatabase(1, pass, currentUserName);
                                saveDatabase(file);
                                i++;
                            }
                            if (!imageField.getText().isEmpty()) {
                                System.out.println(currentUserName);
                                modifyDatabase(2, pfp, currentUserName);
                                saveDatabase(file);
                                i++;
                            }
                            if (!bioField.getText().isEmpty()) {
                                modifyDatabase(3, bio, currentUserName);
                                saveDatabase(file);
                                i++;
                            }
                            if (!ageField.getText().isEmpty()) {
                                modifyDatabase(4, age, currentUserName);
                                saveDatabase(file);
                                i++;
                            }
                            if (i > 0) {
                                statusChangeLabel.setText("Changes made on " + name + "'s profile.");
                                statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                            } else {
                                statusChangeLabel.setText("No changes made on " + name + "'s profile.");
                                statusChangeLabel.setTextFill(Color.rgb(0, 0, 0));
                            }
                        }
                    }
                } else {
                    if (!nameLabel.getText().equals("Username")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Profile");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure you want to delete this user?");

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            List<String> lines = new ArrayList<>();
                            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String[] user = line.split(",");
                                    if (user.length >= 2 && !nameLabel.getText().equals(user[0])) {
                                        lines.add(line);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/UserDatabase.txt"))) {
                                for (String line : lines) {
                                    writer.println(line);
                                    try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
                                        String line1;
                                        while ((line1 = reader.readLine()) != null) {
                                            String[] user = line1.split(",");
                                            if (user.length >= 2 && currentUserName.equals(user[0]) && !user[2].equals("none")) {
                                                currentImage = user[2];
                                                String projectDir = System.getProperty("user.dir");
                                                File imgFile = new File(projectDir + "\\src\\main\\resources\\img\\ProfilePics\\" + currentImage);
                                                clearUserDisplay();
                                                currentInputStream.close();
                                                try {
                                                    FileDeleteStrategy.FORCE.delete(imgFile);
                                                }
                                                catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    statusChangeLabel.setText(currentUserName + "'s profile successfully deleted.");
                                    statusChangeLabel.setTextFill(Color.rgb(35, 152, 46));
                                    clearUserDisplay();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        statusChangeLabel.setText("Error: No user loaded yet");
                        statusChangeLabel.setTextFill(Color.rgb(205, 35, 35));
                    }
                }
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

    private void modifyDatabase(int index, String newValue, String name) {
        if (profileInfo != null && index >= 0 && index < 10) {
            List<String> row = profileInfo.get(userProfiles.searchUser(name));
            if (row != null && row.size() > 2) {
                row.set(index, newValue);
            }
        }
    }

    private void saveDatabase(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (List<String> row : profileInfo) {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void modifyBookDatabase(int index, String newValue, String code) {
        if (bookInfo != null && index >= 0 && index < 10) {
            List<String> row = bookInfo.get(bookProfiles.searchBook(code));
            if (row != null && row.size() > 2) {
                row.set(index, newValue);
            }
        }
    }

    private void saveBookDatabase(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (List<String> row : bookInfo) {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void clearUserFields() {
        usernameField.clear();
        passwordField.clear();
        ageField.clear();
        imageField.clear();
        bioField.clear();
        userInput.clear();
    }

    private void clearUserDisplay() {
        nameLabel.setText("Username");
        ageLabel.setText("Age: 00");
        bioLabel.setText("Lorem Ipsum");
        userImageView.setImage(new Image(getClass().getResource("/img/unknown.png").toExternalForm()));
    }

    private void clearBookFields() {
        codeField.clear();
        titleField.clear();
        authorField.clear();
        genreField.clear();
        yearField.clear();
        descriptionField.clear();
        linkField.clear();
        coverPhotoField.clear();
        pdfNameField.clear();
        bookInput.clear();
    }

    private void clearBookDisplay() {
        bookNameLabel.setText("BookName");
        bookAuthorLabel.setText("Author");
        bookGenreLabel.setText("Genre");
        bookYearLabel.setText("Year");
        bookDescriptionLabel.setText("Lorem ipsum dolor sit amet.");
        bookImageView.setImage(new Image(getClass().getResource("/img/Untitled.png").toExternalForm()));
    }

    private boolean isNameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length >= 2 && username.equals(user[0])) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isBookExists(String code) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Library/BookLibrary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] book = line.split(",");
                if (book.length >= 2 && code.equals(book[0])) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isNotValidString(String string) {
        String regex = "[^a-zA-Z0-9]";

        Pattern pattern = Pattern.compile(regex);
        return string.contains(" ") || pattern.matcher(string).find();
    }

    private void loadProfiles(String filename) {
        userProfiles = new UserProfiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");
                String[] favoriteBooks = Arrays.copyOfRange(userInfo, 5, 10); // Get favorite books from the CSV

                // Find empty favorite slots and replace them with empty strings
                for (int j = 0; j < favoriteBooks.length; j++) {
                    if (favoriteBooks[j].isEmpty()) {
                        favoriteBooks[j] = "";
                    }
                }

                UserData user = new UserData(
                        userInfo[0],
                        userInfo[1],
                        userInfo[2],
                        userInfo[3],
                        userInfo[4],
                        favoriteBooks[0], // First favorite book
                        favoriteBooks[1], // Second favorite book
                        favoriteBooks[2], // Third favorite book
                        favoriteBooks[3], // Fourth favorite book
                        favoriteBooks[4]); // Fifth favorite book

                userProfiles.addUser(user);
            }
        } catch (java.io.FileNotFoundException e) {
            showErrorDialog("File does not Exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void countProfiles(String filename) {
        userProfiles = new UserProfiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int i = 0;
            while ((reader.readLine()) != null) {
                i++;
            }
            // No need to set size for the ArrayList anymore
        } catch (java.io.FileNotFoundException e) {
            showErrorDialog("File does not Exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBooks(String filename){
        Books books[] = new Books[bookProfiles.getSize()];

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] bookInfo = line.split(",");
                books[i] = new Books(
                        bookInfo[0],
                        bookInfo[1],
                        bookInfo[2],
                        bookInfo[3],
                        bookInfo[4],
                        bookInfo[5],
                        bookInfo[6]);
                bookProfiles.setBook(books[i], i);
                i++;
            }
        } catch (java.io.FileNotFoundException e) {
            showErrorDialog("File does not Exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void countBooks(String filename) {
        bookProfiles = new BookProfiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int i = 0;
            while ((reader.readLine()) != null) {
                i++;
            }
            bookProfiles.setSize(i);
        } catch (java.io.FileNotFoundException e) {
            showErrorDialog("File does not Exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    TextFormatter<String> textFormatter = new TextFormatter<>(new StringConverter<String>() {
        @Override
        public String toString(String object) {
            return object;
        }

        @Override
        public String fromString(String string) {
            if (string.length() > 4) {
                return string.substring(0, 4);
            }
            return string;
        }
    });

    @FXML
    private void handleReturn(ActionEvent actionEvent) {
        Main main = new Main();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            try {
                main.sceneToMenu("oraclib-main.fxml", 895, 575, adminName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
    }
}
