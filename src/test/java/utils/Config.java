package utils;

/** All settings in one place. Browser and headless can be changed from the command line. */
public final class Config {

    public static final String BASE_URL = "http://the-internet.herokuapp.com";
    public static final String API_URL = "https://jsonplaceholder.typicode.com";

    // mvn test -Dbrowser=firefox   or   mvn test -Dheadless=true
    public static final String BROWSER = System.getProperty("browser", "chrome");
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));

    public static final int WAIT_SECONDS = 10;          // how long to wait for an element
    public static final int MAX_REFRESH_ATTEMPTS = 5;   // dynamic content test

    private Config() {
    }
}
