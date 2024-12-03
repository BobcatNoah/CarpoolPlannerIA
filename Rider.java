import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Rider {
    private String name;

    public Rider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name", name);

        return obj;
    }

    public static Rider toJavaRider(JSONObject obj) {
        return new Rider((String)obj.get("name"));
    }
}
