import org.json.simple.JSONObject;

import java.util.UUID;

public class DBM {
    private String path = "database";

    public static User loadUser(String username) {
        return null;
    }

    public static void saveUser(User user) {

    }

    public static boolean createUserFile(User user) {
        // testing
        JSONObject obj = new JSONObject();

        obj.put("username", user.getUsername());
        System.out.println(obj);
        return true;
    }

    public static CarPoolCalendar loadCalendar(UUID calendarId) {
        return null;
    }

    public static void createCalendarFile(CarPoolCalendar cal) {

    }

    public static void saveCalendar(CarPoolCalendar cal) {

    }

    public static Event getEventById(UUID id) {
        return null;
    }
}
