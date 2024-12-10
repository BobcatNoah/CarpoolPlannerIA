import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Event {
    private String name;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private UUID eventId;
    private UUID parentCalendarId;
    private ArrayList<Driver> drivers;
    private ArrayList<Rider> riders;

    public Event() {
        this.name = "undefined";
        this.beginDate = null;
        this.endDate = null;
        this.eventId = UUID.randomUUID();
        this.parentCalendarId = null;
        this.drivers = new ArrayList<Driver>();
        this.riders = new ArrayList<Rider>();
    }

    public Event(String name, LocalDateTime beginDate, LocalDateTime endDate, UUID parentCalendarId) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.eventId = UUID.randomUUID();
        this.parentCalendarId = parentCalendarId;
        this.drivers = new ArrayList<Driver>();
        this.riders = new ArrayList<Rider>();
    }

    public Event(UUID uuid) {
        this.name = "undefined";
        this.beginDate = null;
        this.endDate = null;
        this.eventId = uuid;
        this.parentCalendarId = null;
        this.drivers = new ArrayList<Driver>();
        this.riders = new ArrayList<Rider>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getEventId() {
        return eventId;
    }

    //public void setEventId(UUID eventId) {
        //this.eventId = eventId;
    //}

    public UUID getParentCalendarId() {
        return parentCalendarId;
    }

    public void setParentCalendarId(UUID parentCalendarId) {
        this.parentCalendarId = parentCalendarId;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public ArrayList<Rider> getRiders() {
        return riders;
    }


    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("beginDate", beginDate.toString());
        obj.put("endDate", endDate.toString());
        obj.put("eventId", eventId.toString());
        obj.put("parentCalendarId", parentCalendarId.toString());

        JSONArray jsonDrivers = new JSONArray();
        for (Driver driver : drivers) {
            jsonDrivers.add(driver.getJSONOBJ());
        }
        JSONArray jsonRiders = new JSONArray();
        for (Rider rider : riders) {
            jsonRiders.add(rider.getJSONOBJ());
        }
        obj.put("drivers", jsonDrivers);
        obj.put("riders", jsonRiders);

        return obj;
    }

    public static Event toJavaEvent(JSONObject obj) {
        Event event = new Event(UUID.fromString((String) obj.get("eventId")));
        event.setName((String) obj.get("name"));
        event.setBeginDate(LocalDateTime.parse((String) obj.get("beginDate")));
        event.setEndDate(LocalDateTime.parse((String) obj.get("endDate")));
        event.setParentCalendarId(UUID.fromString((String) obj.get("parentCalendarId")));

        // For each JSONObject in the JSONArray of drivers and riders
        // Convert to java object and add to the event
        for (Object driver : ((JSONArray)obj.get("drivers"))) {
            event.getDrivers().add(Driver.toJavaDriver((JSONObject) driver));
        }
        for (Object rider : (JSONArray) obj.get("riders")) {
            event.getRiders().add(Rider.toJavaRider((JSONObject) rider));
        }

        return event;
    }

}
