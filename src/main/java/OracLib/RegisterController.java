package OracLib;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.regex.Pattern;
import java.io.*;

public class RegisterController {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField rePassword;
    @FXML
    private Label prompt;
    @FXML
    private MediaView mediaView;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
        Media media = new Media(getClass().getResource("/img/Bocchi.mp4").toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);
        player.setMute(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    @FXML
    private void handleRegister(ActionEvent actionEvent) {
        Main main = new Main();
        String name = username.getText();
        String pass = password.getText();
        String rePass = rePassword.getText();
        if (name.isEmpty() || pass.isEmpty() || rePass.isEmpty()) {
            prompt.setText("Fill in all input fields!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (!pass.equals(rePass)){
            prompt.setText("Re-Enter your Password!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
            rePassword.clear();
        } else if (name.equalsIgnoreCase("admin") || name.equalsIgnoreCase("administrator")){
            prompt.setText("You can't take the admin name!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (name.equalsIgnoreCase("username")){
            prompt.setText("Why would you take such a basic 'username'?");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (name.equals(pass)){
            prompt.setText("Username and Password are the same!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (name.length() < 4 || pass.length() < 4){
            prompt.setText("Inputs can't be 3 letters or less!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
            username.clear();
            password.clear();
            rePassword.clear();
        } else if (isNameExists(name)) {
            prompt.setText("User Already Exists!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
            username.clear();
            password.clear();
            rePassword.clear();
        } else if (isNotValidString(name) || isNotValidString(pass)) {
            prompt.setText("No Spaces or Special Characters!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        }
        else {
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/UserDatabase.txt", true))) {
                writer.println(name + "," + pass + ",none,\"empty\",00,book1,book2,book3,book4,book5");
                prompt.setText("User successfully registered!");
                prompt.setTextFill(Color.rgb(35, 152, 46));

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    showAlert("Returning to Log-In Screen");
                });
                pause.play();

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                    try {
                        main.sceneToLogin("login-view.fxml", 781, 530);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }));

                timeline.setCycleCount(1);
                timeline.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onReturn(ActionEvent event) throws IOException {
        Main main = new Main();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            try {
                main.sceneToLogin("login-view.fxml", 781, 530);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
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

    private boolean isNotValidString(String string) {
        String regex = "[^a-zA-Z0-9]";

        Pattern pattern = Pattern.compile(regex);
        return string.contains(" ") || pattern.matcher(string).find();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Complete");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> alert.close()));
        timeline.play();
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }
}
