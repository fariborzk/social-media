package view;

import controller.WelcomeController;
import enums.Messages;
import enums.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.text.ParseException;



public class RegisterMenu extends Menu {
    private static RegisterMenu instance = null;
    public TextField userNameTextBox;
    public TextField passwordTextBox;
    public TextField repeatedPasswordTextBox;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label repeatedPasswordLabel;
    public RadioButton simpleRadioButton;
    public RadioButton businessRadioButton;
    public RadioButton femaleRadioButton;
    public RadioButton maleRadioButton;
    public TextField emailTextBox;
    public Label emailLabel;
    public TextField phoneNumberTextBox;
    public Label phoneNumberLabel;
    public AnchorPane bg;
    public RadioButton changeTheme;
    @FXML
    private TextField firstNameTextBox;
    public TextField lastNameTextBox;
    public TextField birthdayTextBox;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label birthdayLabel;
    private  static String userName;
    private static String first_name;
    private static String last_name;
    private static  String birthday;
    private static String phoneNumber;
    private static String email;
    private static String password;
    private static String repeatedPassword;
    private static String gender;
    private static Type type;
    private static Scene scene1;
    private static Scene scene2;
    private static Scene scene3;
    private static Scene emailScene;
    private static Scene phoneNumberScene;
    String address = null;
    private WelcomeController welcomeController = null;
    public void initialize(){
        if (Theme)
            lightTheme();
        else
            darkTheme();
    }
    public RegisterMenu()
    {
        welcomeController = WelcomeController.getInstance();
    }
    private static void setInstance(RegisterMenu instance)
    {
        RegisterMenu.instance = instance;
    }
    public static RegisterMenu getInstance()
    {
        if (RegisterMenu.instance == null)
            RegisterMenu.setInstance(new RegisterMenu());
        return RegisterMenu.instance;
    }
    public void setTheme(ActionEvent event){

        if (changeTheme.isSelected()){
            Theme = !Theme;
            changeTheme.setSelected(false);
            if (Theme)
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
    @Override
    public void run() throws ParseException {
        try {
            if (scene1 == null){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginMenu.class.getResource("/social_media_javafx/registerMenuScene1.fxml"));
                scene1 = new Scene(fxmlLoader.load());
            }

            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene1);
            Menu.stage.show();
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void nextScene2(ActionEvent event) {
    setFirstName();
    setLastName();
    setBirthday();
    Messages messages = welcomeController.handleRegisterScene1(first_name, last_name, birthday);
    if (messages == Messages.FIRST_NAME_CANT_BE_EMPTY) firstNameLabel.setText(messages.toString());
    if (messages == Messages.LAST_NAME_CANT_BE_EMPTY) lastNameLabel.setText(messages.toString());
    if (messages == Messages.BIRTHDAY_CANT_BE_EMPTY) birthdayLabel.setText(messages.toString());
    else if (messages == Messages.INVALID_DATE_FORMAT) birthdayLabel.setText(messages.toString());
    if (messages == Messages.SUCCESS){
        runScene2();
    }

    }
    private void runScene2() {
        try {
            if (scene2 == null){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginMenu.class.getResource("/social_media_javafx/registerMenuScene2.fxml"));
                scene2 = new Scene(fxmlLoader.load());
            }
            if (Theme)
                lightTheme();
            else
                darkTheme();
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene2);
            Menu.stage.show();

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void setGender(ActionEvent event){
        if (femaleRadioButton.isSelected()){

          this.gender = "F";
          maleRadioButton.setSelected(false);
        }
        if (maleRadioButton.isSelected()){
            this.gender = "M";
            femaleRadioButton.setSelected(false);
        }
    }
    
    private void setFirstName(){
        this.first_name = firstNameTextBox.getText();
    }
    private void setLastName(){
        this.last_name = lastNameTextBox.getText();

    }
    private void setBirthday(){
        this.birthday = birthdayTextBox.getText();
    }

    private void setUserName(){
        this.userName = userNameTextBox.getText();
    }
    private void setPassword(){
        this.password = passwordTextBox.getText();
    }
    private void setRepeatedPassword(){
        this.repeatedPassword = repeatedPasswordTextBox.getText();
    }
    public void cancel(ActionEvent event) {
    }
    private void setEmail(){
        this.email = emailTextBox.getText();
    }
    public void nextScene3(ActionEvent event) {
        setUserName();
        setPassword();
        setRepeatedPassword();
        Messages messages = welcomeController.handleRegisterScene2(userName, password, repeatedPassword);
        if (messages == Messages.SUCCESS){
            runScene3();
            return;
        }
        if (messages == Messages.USER_NAME_CANT_BE_EMPTY || messages == Messages.USERID_EXIST) userNameLabel.setText(messages.toString());
        if (messages == Messages.REPEAT_PASSWORD || messages == Messages.MISMATCH_PASSWORD) repeatedPasswordLabel.setText(messages.toString());
        else passwordLabel.setText(messages.toString());
    }

    private void runScene3() {
        try {
            if (scene3 == null){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginMenu.class.getResource("/social_media_javafx/registerMenuScene3.fxml"));
                scene3 = new Scene(fxmlLoader.load());
                System.out.println(first_name + last_name + " in  3");
            }
            if (Theme)
                lightTheme();
            else
                darkTheme();
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene3);
            Menu.stage.show();

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void backScene1(ActionEvent event) throws ParseException {
        run();
    }
    public void runSignInWithEmail(){
        try {
            if (emailScene == null){
                FXMLLoader fxmlLoader = new FXMLLoader(LoginMenu.class.getResource("/social_media_javafx/registerMenuSceneEmail.fxml"));
                emailScene = new Scene(fxmlLoader.load());
            }
            if (Theme)
                lightTheme();
            else
                darkTheme();
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(emailScene);
            Menu.stage.show();

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void nextSignInWithEmail(ActionEvent event) {
        System.out.println(first_name + last_name);
       runSignInWithEmail();
    }

    public void backScene2(ActionEvent event) {
        runScene2();
    }
   
    public void setType(ActionEvent event) {
        if (simpleRadioButton.isSelected()){
            this.type = Type.SIMPLE;
            businessRadioButton.setSelected(false);
        }
        if (businessRadioButton.isSelected()){
            this.type = Type.BUSINESS;
            simpleRadioButton.setSelected(false);
        }
    }
    private void setPhoneNumber(){
        this.phoneNumber = phoneNumberTextBox.getText();
    }

    public void signInWithPhoneNumber(ActionEvent event) {
      setPhoneNumber();

      Messages messages = welcomeController.handlePhoneNumber(phoneNumber);
      if (messages == Messages.SUCCESS)
          welcomeController.handleRegister(first_name,last_name, userName, birthday, email, phoneNumber, password, repeatedPassword, gender, type, address);
      else phoneNumberLabel.setText(messages.toString());
        System.out.println(messages);
    }
    public void runSignInWithPhoneNumber(){
        try {
            if (phoneNumberScene == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/social_media_javafx/registerMenuSceneEmail.fxml"));
                phoneNumberScene = new Scene(fxmlLoader.load());
            }
            if (Theme)
                lightTheme();
            else
                darkTheme();
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(phoneNumberScene);
            Menu.stage.show();

        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void singIn(ActionEvent event) {
      
    }

    public void signInWithEmail(ActionEvent event) {
        setEmail();
        Messages messages = welcomeController.handleEmail(email);
        if (messages == Messages.SUCCESS)
            welcomeController.handleRegister(first_name,last_name, userName, birthday, email, phoneNumber, password, repeatedPassword, gender, type, address);
        else emailLabel.setText(messages.toString());
        System.out.println(messages);
    }

    public void backScene3(ActionEvent event) {
        runScene3();
    }

    public void signInWithPhoneNumberMenu(ActionEvent event) {
        runSignInWithPhoneNumber();
    }

    public void signInWithEmailMenu(ActionEvent event) {
        runSignInWithEmail();
    }

    public void logInMenu(ActionEvent event) {
    }

    public void setProfilePicture(ActionEvent event) {
         this.address = this.fileChooser(scene3);
    }
}
