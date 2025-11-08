package OracLib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import javafx.scene.image.Image;

import java.io.*;
import java.util.Arrays;

public class Main extends Application {
    // Encapsulation - Public & Private Declarations/Methods
    private static Stage window;
    private static Clip clip;
    private UserProfiles userProfiles;
    private BookProfiles bookProfiles;
    private static String currentUsername;
    public static String getCurrentUsername() {
        return currentUsername;
    }
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }
    public UserProfiles getUserProfiles() {
        return userProfiles;
    }
    public BookProfiles getBookProfiles() {
        return bookProfiles;
    }

    @Override // Launches Program
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        window.isResizable();

        Rectangle2D screen = Screen.getPrimary().getBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();

        Image icon = new Image(getClass().getResource("/img/logo.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("OracLib - LogIn");

        LoginController loginController = fxmlLoader.getController();
        loginController.setMain(this);

        Scene scene = new Scene(root, 781, 530);

        window.setScene(scene);
        window.show();
    }

    // Changes Scene to Register Controller
    public void sceneToRegister(String fxml, double sceneWidth, double sceneHeight) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        RegisterController registerController = fxmlLoader.getController();
        registerController.setMain(this);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        window.setTitle("OracLib - Register");
        window.setScene(scene);
        window.sizeToScene();
    }

    // Changes Scene to Menu Controller
    public void sceneToMenu(String fxml, double sceneWidth, double sceneHeight, String name) throws IOException {
        countProfiles("src/main/resources/UserDatabase.txt");
        loadProfiles("src/main/resources/UserDatabase.txt");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        mainController.setMain(this);
        mainController.setProfile(name);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        window.setTitle("OracLib - Main Menu");
        window.setScene(scene);
        window.sizeToScene();
    }

    // Changes Scene to LogIn Controller
    public void sceneToLogin(String fxml, double sceneWidth, double sceneHeight) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        LoginController loginController = fxmlLoader.getController();
        loginController.setMain(this);

        window.setTitle("OracLib - LogIn");
        window.setScene(scene);
        window.sizeToScene();
    }

    // Changes Scene to Admin View Controller
    public void sceneToAdminView(String fxml, double sceneWidth, double sceneHeight, String username) throws IOException {
        countProfiles("src/main/resources/UserDatabase.txt");
        loadProfiles("src/main/resources/UserDatabase.txt");
        countBooks("src/main/resources/Library/BookLibrary.txt");
        loadBooks("src/main/resources/Library/BookLibrary.txt");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        AdminViewController adminViewController = fxmlLoader.getController();
        adminViewController.setMain(this);
        adminViewController.setUsername(username);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        window.setTitle("OracLib - Admin View");
        window.setScene(scene);
        window.sizeToScene();
    }

    // Changes Scene to User Profile Controller
    public void sceneToProfile(String fxml, double sceneWidth, double sceneHeight, String username) throws IOException {
        countProfiles("src/main/resources/UserDatabase.txt");
        loadProfiles("src/main/resources/UserDatabase.txt");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        UserProfileController userProfileController = fxmlLoader.getController();
        userProfileController.setMain(this);
        userProfileController.setUsername(username);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        window.setTitle("OracLib - " + username + "'s Profile");
        window.setScene(scene);
        window.sizeToScene();
    }

    // Loads User Profiles from the Database
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

    // Counts the User Profiles in the Database
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

    // Loads the Books data from the Database
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

    // Counts the books in the Database
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

    // Displays an Alert Window that takes a String as an input
    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}