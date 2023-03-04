package enums;

public enum securityQuestions {
    FAVORITE_CHILDHOOD_PLACE("What was your favorite place to visit as a child?"),
    FAVORITE_ARTIST("Who is your favorite actor, musician, or artist?"),
    FAVORITE_PET("Who is your favorite actor, musician, or artist?"),
    BORN_CITY("In what city were you born?"),
    HIGH_SCHOOL("What high school did you attend?"),
    FAVORITE_MOVIE("What is your favorite movie?"),
    FAVORITE_COLOR("What is your favorite color?"),
    BEST_FRIEND("What is the first name of your best friend");
    private String message;
    securityQuestions(String message)
    {
        this.message = message;
    }
    @Override
    public String toString(){
        return this.message;
    }
}
