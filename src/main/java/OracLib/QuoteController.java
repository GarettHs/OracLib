package OracLib;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BaseQuote {

    @FXML
    protected Label quoteTextArea;

    @FXML
    protected void initialize() {
        loadRandomQuote();
    }

    protected void loadRandomQuote() {
        List<String> quotes = readQuotesFromTXT("Quotes.txt");

        if (quotes.isEmpty()) {
            quoteTextArea.setText("No quotes found.");
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());
        String randomQuote = quotes.get(randomIndex);
        quoteTextArea.setText(randomQuote);
    }

    @FXML
    protected void closeQuoteView() {
        Stage stage = (Stage) quoteTextArea.getScene().getWindow();
        stage.close();
    }

    private List<String> readQuotesFromTXT(String filename) {
        List<String> quotes = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getResourceAsStream("/Library/" + filename);
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    quotes.add(line);
                }

                bufferedReader.close();
            } else {
                System.err.println("File not found: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return quotes;
    }
}

// Inheritance
public class QuoteController extends BaseQuote {

    @FXML
    private Label quoteTextArea;

    @FXML
    protected void initialize() {
        super.initialize();
    }

    @FXML
    protected void closeQuoteView() {
        super.closeQuoteView();
    }
}
