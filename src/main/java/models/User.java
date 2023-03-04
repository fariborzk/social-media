package models;




import enums.RegisterWith;
import enums.Type;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public  class User {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private String joinDate;
    private String email = null;
    private String username;
    private String password;
    private Integer phoneNumber = null;
    private String first_name;
    private String last_name;
    private String gender;
    private Type type;
    private String forgetPassQ = null;
    private String getForgetPassA = null;
    private RegisterWith registerWith;
    private String birthday;
    public  User(String first_name, String last_name, String username, String password, String birthday, Type type, String gender, Integer phoneNumber, String email, RegisterWith registerWith){
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.password = password;
        this.username = username;
        this.registerWith = registerWith;
        this.gender = gender;
        if (type == Type.BUSINESS) this.type = type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd || HH:mm:ss ");
        if (email != null)
            this.email = email;
        if (phoneNumber != null)
            this.phoneNumber = phoneNumber;
        LocalDateTime now = LocalDateTime.now();
        this.joinDate = now.format(formatter);
        allUsers.add(this);
    }

    public static User getUserByUserName(String username){
        for (User user : allUsers)
            if (user.username.equals(username))
                return user;
        return null;
    }

}



