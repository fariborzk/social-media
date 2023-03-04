import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import view.*;

import java.io.IOException;
import java.text.ParseException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, ParseException {
        Menu.stage = stage;
       new GroupView("3").run();
        //RegisterMenu.getInstance().run();
        //new GroupSettingView("1", "3").run();
        /*
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

 */


    }

    public static void main(String[] args) {
        launch();
    }
}