package view;

import controller.WelcomeController;
import enums.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.text.ParseException;



public class LoginMenu extends Menu {
    private static LoginMenu instance = new LoginMenu();
    @FXML
    public TextField userNameTextBox;
    @FXML
    public TextField passWordTextBox;
    @FXML
    public Button signInButton;
    @FXML
    public Button logInButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label usernameLabel;
    public RadioButton changeTheme;
    public AnchorPane bg;
    private WelcomeController welcomeController = null;
    String username;
    String password;
    @FXML
    public void initialize(){

    }
    public void setTheme(ActionEvent event){

        if (changeTheme.isSelected()){
            Menu.Theme = !Menu.Theme;
            changeTheme.setSelected(false);
            if (Menu.Theme)
                lightTheme();
            else
                darkTheme();
        }
    }
    public void lightTheme(){
      bg.setStyle("-fx-background-color: #DAA520");
    }
    public void darkTheme(){
     bg.setStyle("-fx-background-color:  #636566");
    }
    public LoginMenu()
    {
        welcomeController = WelcomeController.getInstance();
    }
    private static void setInstance(LoginMenu instance)
    {
        LoginMenu.instance = instance;
    }
    public static LoginMenu getInstance()
    {
        if (LoginMenu.instance == null)
            LoginMenu.setInstance(new LoginMenu());
        return LoginMenu.instance;
    }
    @FXML
    private void login() throws ParseException {
        passwordLabel.setText("");
        usernameLabel.setText("");
        getUserName();
        getPassword();
        if (username.isEmpty()) usernameLabel.setText("*"+Messages.USER_NAME_CANT_BE_EMPTY.toString());
        else if (password.isEmpty()) passwordLabel.setText("*" +Messages.PASSWORD_CANT_BE_EMPTY.toString());
        else {
            Messages message = this.welcomeController.handleLogin(username, password);
            if (message == Messages.SUCCESS){
                System.out.println(Messages.SUCCESS);
            }
            else if (message == Messages.MISMATCH_PASSWORD) passwordLabel.setText(message.toString());
            else usernameLabel.setText(message.toString());
        }

    }

    @Override
    public void run() throws ParseException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/social_media_javafx/loginMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene);
            Menu.stage.show();

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void forgetPass() {
       String username = this.getInput("please enter your username");
       Messages messages = welcomeController.getSecurityQ(username);
        System.out.println(messages);
        if (messages == Messages.SUCCESS) changePassword(username);
    }

    private void changePassword(String username) {
        String newPass = this.getInput("enter your new password");
        Messages messages = welcomeController.handleNewPassword(username, newPass);
    }
    private void getUserName(){
        this.username = userNameTextBox.getText();
    }
    private void getPassword(){
        this.password = passWordTextBox.getText();
    }

    public void signIn(ActionEvent event) {

    }



    public void cancle(ActionEvent event) {
    }


}
