import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Group {
    private String name;
    private ArrayList<String> users;
    private UUID calendarId;
    private String password;


    public Group() {
        this.users = new ArrayList<>();
        this.password = "";
        this.calendarId = null;

    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(UUID calendarId) {
        this.calendarId = calendarId;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("id", calendarId.toString());
        obj.put("password", password);

        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(users);

        obj.put("users", jsonArray);


        return obj;
    }

    public static Group toJavaGroup(JSONObject obj) {
        Group group = new Group();

        group.setName((String) obj.get("name"));
        for (Object user : (JSONArray)obj.get("users")) {
            group.getUsers().add((String) user);
        }
        group.setCalendarId(UUID.fromString((String) obj.get("id")));
        group.setPassword((String) obj.get("password"));

        return group;
    }
}
