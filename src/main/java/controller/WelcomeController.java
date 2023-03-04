package controller;
import Database.JDBC;

import enums.Messages;
import enums.Type;
import enums.securityQuestions;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Scanner;

public class WelcomeController extends Controller {
    private static WelcomeController instance = null;
    private JDBC jdbc = JDBC.getInstance();
    private WelcomeController() {
    }
    private static void setInstance(WelcomeController welcomeController)
    {
        WelcomeController.instance = welcomeController;
    }
    public static WelcomeController getInstance() {
        if (WelcomeController.instance == null)
            setInstance(new WelcomeController());
        return WelcomeController.instance;
    }
    public Messages handleRegisterScene1(String first_name, String last_name,String birthday){
        if (first_name.isEmpty())
            return Messages.FIRST_NAME_CANT_BE_EMPTY;
        if (last_name.isEmpty())
            return Messages.LAST_NAME_CANT_BE_EMPTY;
        if (birthday.isEmpty())
            return Messages.BIRTHDAY_CANT_BE_EMPTY;
        if (!checkBirthdayFormat(birthday))
            return Messages.INVALID_DATE_FORMAT;
        return Messages.SUCCESS;

    }
    public Messages handleRegisterScene2(String username, String password, String repeatedPassword){
        if (username.isEmpty())
            return Messages.USER_NAME_CANT_BE_EMPTY;
        if (password.isEmpty())
            return Messages.PASSWORD_CANT_BE_EMPTY;
        if (repeatedPassword.isEmpty())
            return Messages.REPEAT_PASSWORD;
        if (this.doesUserNameExist(username)){
            return Messages.USERID_EXIST;
        }
        Messages message = this.validatePassword(password, repeatedPassword);
        if (message  != Messages.SUCCESS)
            return message;
        return Messages.SUCCESS;

    }

    private boolean checkBirthdayFormat(String birthday) {
        return birthday.matches("....-..-..");
    }
   public Messages handlePhoneNumber(String phoneNumber){
        if (phoneNumber != null)  return Messages.PHONE_NUMBER_CAN_NOT_BE_EMPTY;
        if (!isNumeric(phoneNumber))
                return Messages.INVALID_PHONE_NUMBER;
        return Messages.SUCCESS;
    }
    public Messages handleEmail(String email){
        if (email.isEmpty()) return Messages.EMAIL_CANNOT_BE_EMPTY;
        if (!isValidEmail(email)) return Messages.INVALID_EMAIL;
        return Messages.SUCCESS;
    }

    private boolean isValidEmail(String email) {
        return email.matches(".+@.+");
    }

    public Messages handleRegister(String first_name, String last_name, String userName, String birthday, String email, String phoneNumber,
                                   String password, String repeatedPassword, String gender, Type type, String address)  {
        try {
            LocalDate now = LocalDate.now();
            JDBC.getInstance().addNewUserToMySql(first_name, last_name,birthday, email, phoneNumber,
                    now.toString(), password,userName,gender, null,null, type, address);
        }
        catch (Exception e){
            System.out.println(e);
        }

        return Messages.SUCCESS;
    }
    public Messages handleLogin(String userName, String password) {

        if (!doesUserNameExist(userName))
            return Messages.NO_USER_EXIST;
        try {
            ResultSet resultSet = JDBC.getInstance().findUserPasswordFromDatabase(userName);
            while (resultSet.next()){
                if(resultSet.getString("user_password").equals(password)){
                    return Messages.SUCCESS;
                }

                else return Messages.MISMATCH_PASSWORD;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return Messages.WRONG_CREDENTIALS;
    }

    private Messages validatePassword(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword))
            return Messages.MISMATCH_PASSWORD;
        if (password.length() < 5)
            return Messages.SHORT_PASSWORD;
        if (isNumeric(password))
            return Messages.JUST_NUMBER;
        if (isAlphabetic(password))
       return Messages.JUST_ALPHA;
        return Messages.SUCCESS;
    }
    public boolean isAlphabetic (String password){
        return password.matches("[a-zA-z]+");
    }
    private boolean isNumeric(String str) {
        return str.matches("[0-9]+");
    }
    private boolean doesUserNameExist(String userName) {
        ResultSet resultSet = JDBC.getInstance().findUserNameFromDatabase(userName);
        try {
            if (resultSet.next()) return true;
            else return false;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }

    }

    public Messages handleSecurityQuestions(securityQuestions favoriteMovie, String username, String answer) {
        jdbc.addSecurityQuestion(favoriteMovie.toString(), username, answer);
        return Messages.SUCCESS;
    }
    public Messages getSecurityQ(String username) {
        if (!doesUserNameExist(username))  return Messages.NO_USER_EXIST;
        ResultSet resultSet = jdbc.getSecurityQ(username);
        Scanner scanner = new Scanner(System.in);
        try {
            while (resultSet.next()){
                String q = resultSet.getString("forget_password_question");
                String a = resultSet.getString("forget_password_answer");
                System.out.println(q);
                String ans = scanner.nextLine();
                if (ans.equals(a)) return Messages.SUCCESS;
                else return Messages.WRONG_ANSWER;
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return Messages.WRONG_ANSWER;
    }

    public Messages handleNewPassword(String username, String newPass) {
        if (newPass.length() < 5)
            return Messages.SHORT_PASSWORD;
        if (isNumeric(newPass))
            return Messages.JUST_NUMBER;
        if (isAlphabetic(newPass))
            return Messages.JUST_ALPHA;
        jdbc.updatePassword(username, newPass);
        return Messages.SUCCESS;
    }
}
