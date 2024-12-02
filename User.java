import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String username;
    private String password;
    private UUID calendarId;
    // these are for autofill
    // so that the user doesn't need to enter the info for every event
    private ArrayList<Driver> drivers;
    private ArrayList<Rider> riders;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.drivers = new ArrayList<>();
        this.riders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(UUID calendarId) {
        this.calendarId = calendarId;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public ArrayList<Rider> getRiders() {
        return riders;
    }
}
