package view;

import controller.WelcomeController;
import enums.Messages;

import java.text.ParseException;

public class WelcomeMenu extends Menu{
    private static WelcomeMenu instance = null;
    private WelcomeController welcomeController = null;
    private RegisterMenu registerMenu = null;
    private LoginMenu loginMenu = null;
    private WelcomeMenu()
    {
        this.welcomeController = WelcomeController.getInstance();
        this.registerMenu = RegisterMenu.getInstance();
        this.loginMenu = LoginMenu.getInstance();
    }
    private static void setInstance(WelcomeMenu instance)
    {
        WelcomeMenu.instance = instance;
    }
    public static WelcomeMenu getInstance()
    {
        if (WelcomeMenu.instance == null)
            WelcomeMenu.setInstance(new WelcomeMenu());
        return WelcomeMenu.instance;
    }

    @Override
    public void run() throws ParseException {
        this.showOptions();
        String choice = this.getChoice();
        switch (choice) {
            case "1":
            case "login":
                this.loginMenu.run();
                break;
            case "2":
            case "register":
                this.registerMenu.run();
                break;
            case "3":
            case "exit":
                this.exit();
                break;
            default:
                Object Message;
                System.out.println(Messages.INVALID_CHOICE);
        }
        this.run();
    }



    private void exit() {
        Menu.getScanner().close();
    }

    protected void showOptions() {
        System.out.println("Hi,Welcome...");
        System.out.println("Do you Already Have An Account? ---> login");
        System.out.println("IF you dont Have An Account Please Create One First ---> rigester");
        System.out.println("1. login");
        System.out.println("2. register");
        System.out.println("3. exit");
    }
}
