package OracLib;

import javafx.stage.Stage;

// Abstraction - Interface
public interface LibraryView {
    void setMain(Main main);
    void setBookCode(String bookCode);
    void setBookData(Books book);
    void setBookTitle(String title);
    void setBookImage(String bookCode);
    void setPrimaryStage(Stage stage);
}
