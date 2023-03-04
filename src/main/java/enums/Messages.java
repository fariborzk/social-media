package enums;

public enum Messages {
    INVALID_CHOICE("you entered an invalid choice ...please try again"),
    SUCCESS("success"),
    USERID_EXIST("this user ID exist"),
    INVALID_TYPE("invalid user type"),
    MISMATCH_PASSWORD("mismatch password"),
    SHORT_PASSWORD("short password"),
    LONG_PASSWORD("long password"),
    JUST_NUMBER("just contains number"), JUST_ALPHA("just contains alpha"),
    WRONG_CREDENTIALS("wrong credentials"),
    FIRST_NAME_CANT_BE_EMPTY("first name cannot be empty ... please enter your first name"),
    LAST_NAME_CANT_BE_EMPTY("last name cannot be empty ... please enter your last name"),
    USER_NAME_CANT_BE_EMPTY("username cannot be empty"),
    BIRTHDAY_CANT_BE_EMPTY("birthday cannot be empty ... please enter your birthday"),
    PASSWORD_CANT_BE_EMPTY("password cannot be empty"),
    REPEAT_PASSWORD("please repeat your password"),
    GENDER_CANT_BE_EMPTY("gender cannot be empty ... please enter your message"),
    INVALID_PHONE_NUMBER("please enter a valid phone number"),
    INVALID_GENDER("please Enter a valid gender... F for female / M for male "),
    NO_USER_EXIST("no user exist with that username..."),
    INVALID_GROUP_ID("group id format is invalid"),
    GROUP_ID_EXISTS("this user id exists"),
    SQL_EXCEPTION("SQL exception"),
    GROUP_ID_DO_NOT_EXIST("this group_id do not exist"),
    GROUP_NAME_CANNOT_BE_EMPTY("group name cannot be empty"),
    GROUP_ID_CANNOT_BE_EMPTY("group id cannot be empty"),
    INVALID_MESSAGE_ID("you entered an invalid message id"),
    NOT_A_MEMBER_OF_GROUP("this user is not a member of group"),
    NOT_BEEN_BANED("this user has not been baned"), BAN(" you are baned by admin..."),
    WRONG_ANSWER("WRONG ANSWER"),
    INVALID_DATE_FORMAT("date format is invalid"),
    PHONE_NUMBER_CAN_NOT_BE_EMPTY("phone number cannot be empty"),
    EMAIL_CANNOT_BE_EMPTY("email cannot be empty"), INVALID_EMAIL("please enter valid email");
    private String message;
    Messages(String message)
    {
        this.message = message;
    }
    @Override
    public String toString(){
        return this.message;
    }
}
