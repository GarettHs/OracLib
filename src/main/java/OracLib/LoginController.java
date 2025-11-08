package OracLib;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.*;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private Label prompt;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button forgotPass;
    private int clickCount = 0;
    private final int maxClicks = 4;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
        forgotPass.setVisible(false);
    }


    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        Main main = new Main();
        String user = username.getText();
        String pass = password.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            prompt.setText("Enter your username & password!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (isNotValidString(user) || isNotValidString(pass)) {
            prompt.setText("Invalid Username/Password!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
        } else if (isValid(user, pass)) {
            Main.setCurrentUsername(user);
            prompt.setText("Welcome to OracLib, " + user + "!");
            prompt.setTextFill(Color.rgb(35, 152, 46));
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    main.sceneToMenu("oraclib-main.fxml", 895, 575, user);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            pause.play();
        } else {
            prompt.setText("Wrong Username/Password!");
            prompt.setTextFill(Color.rgb(205, 35, 35));
            forgotPass.setVisible(true);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        Main main = new Main();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            try {
                main.sceneToRegister("register-view.fxml", 781, 530);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        pause.play();
    }

    public void onForgotPass(ActionEvent event) {
        clickCount++;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String[] alertMessage = {
                "Try to remember it.",
                "No seriously, try to remember it.",
                "What do you mean you can't?",
                "Try emailing the admin or keep trying I dunno"
        };

        String[] alertTitle = {
                "Forgot Password?",
                "Forget... Password?",
                "Are you dumb?",
                "Bruh that's your fault"
        };

        if (clickCount <= maxClicks) {
            alert.setTitle(alertTitle[clickCount - 1]);
            alert.setHeaderText(null);
            alert.setContentText(alertMessage[clickCount - 1]);
        } else {
            alert.setTitle("Forgot Password?");
            alert.setHeaderText(null);
            alert.setContentText("Try to remember it.");
        }
        alert.showAndWait();
        forgotPass.setVisible(false);
    }

    private boolean isValid(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/UserDatabase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length >= 2 && username.equals(user[0]) && password.equals(user[1])) {
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

    @FXML
    private void onExit() {
        System.exit(0);
    }
}