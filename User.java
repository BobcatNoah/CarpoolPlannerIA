import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class User {
    private String username;
    private String password;
    private UUID calendarId;
    private ArrayList<String> groups;
    // these were for autofill
    // so that the user doesn't need to enter the info for every event
    // However, this feature is inaccessible to the user to simply and make the application more user friendly
    private ArrayList<Driver> drivers;
    private ArrayList<Rider> riders;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.groups = new ArrayList<>();
        this.drivers = new ArrayList<>();
        this.riders = new ArrayList<>();
        this.calendarId = null;
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

    public ArrayList<String> getGroups() {
        return groups;
    }

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("username", username);
        obj.put("password", password);

        if (calendarId == null) {
            obj.put("calendarId", null);
        } else {
            obj.put("calendarId", calendarId.toString());
        }


        JSONArray jsonDrivers = new JSONArray();
        for (Driver driver : drivers) {
            jsonDrivers.add(driver.getJSONOBJ());
        }
        JSONArray jsonRiders = new JSONArray();
        for (Rider rider : riders) {
            jsonRiders.add(rider.getJSONOBJ());
        }
        JSONArray jsonGroups = new JSONArray();
        jsonGroups.addAll(groups);

        obj.put("drivers", jsonDrivers);
        obj.put("riders", jsonRiders);
        obj.put("groups", jsonGroups);

        return obj;
    }

    public static User toJavaUser(JSONObject obj) {
        // Takes in a JSON object and converts it to a java User object
        User user = new User((String) obj.get("username"), (String) obj.get("password"));
        if ((obj.get("calendarId").equals("null"))) {
            user.setCalendarId(null);
        } else {
            user.setCalendarId(UUID.fromString((String) obj.get("calendarId")));
        }
        for (Object driver : ((JSONArray)obj.get("drivers"))) {
            user.getDrivers().add(Driver.toJavaDriver((JSONObject) driver));
        }
        for (Object rider : (JSONArray) obj.get("riders")) {
            user.getRiders().add(Rider.toJavaRider((JSONObject) rider));
        }
        for (Object group : (JSONArray) obj.get("groups")) {
            user.getGroups().add((String) group);
        }

        return user;
    }

    public String toString() {
        return getJSONOBJ().toString();
    }
}
