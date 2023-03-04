package view;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;

import java.io.File;
import java.text.ParseException;
import java.util.Scanner;

public abstract class Menu {
    public static Stage stage;
    public static boolean Theme = false;
    private static final Scanner scanner = new Scanner(System.in);
    private static User LoggedInUser = null;

    public static User getLoggedInUser() {
        return LoggedInUser;
    }

    protected static Scanner getScanner() {
        return scanner;
    }

    public static void setLoggedInUser(User user) {
        LoggedInUser = user;
    }

    public String getInput(String Input) {
        System.out.println(Input);
        return Menu.getScanner().nextLine().trim();
    }
    public String getChoice() {
        return Menu.getScanner().nextLine().trim();
    }
    public abstract void run() throws ParseException;
    public String fileChooser(Scene scene) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("photo");
        fileChooser.setInitialDirectory(new File("C:\\Users\\tejan system\\Desktop\\social_media_mavenJavaFX\\social_media_mavenJavaFX\\src\\main\\resources\\pictures"));
        File file = null;
        try {
            file = fileChooser.showOpenDialog(scene.getWindow());
            System.out.println("in try");
        }
        catch (Exception e) {
            System.out.println(e + " in catch");
        }
        System.out.println("file chooser : " + file.getAbsolutePath());

        return file.getAbsolutePath();
    }
}