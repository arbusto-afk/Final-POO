import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class TestApp extends Application {
    @Override
    public void start(Stage stage) {
        ChoiceBox<String> test1 = new ChoiceBox<>();
        test1.getItems().addAll("Option 1", "Option 2");
        stage.setScene(new Scene(test1, 200, 100));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
