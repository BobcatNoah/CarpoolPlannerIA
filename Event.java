import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private String name;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private UUID eventId;
    private UUID parentCalendarId;

    public Event() {
        this.name = "undefined";
        this.beginDate = null;
        this.endDate = null;
        this.eventId = UUID.randomUUID();
        this.parentCalendarId = null;
    }

    public Event(String name, LocalDateTime beginDate, LocalDateTime endDate, UUID parentCalendarId) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.eventId = UUID.randomUUID();
        this.parentCalendarId = parentCalendarId;
    }

    public Event(UUID uuid) {
        this.name = "undefined";
        this.beginDate = null;
        this.endDate = null;
        this.eventId = uuid;
        this.parentCalendarId = null;
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

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("beginDate", beginDate.toString());
        obj.put("endDate", endDate.toString());
        obj.put("eventId", eventId.toString());
        obj.put("parentCalendarId", parentCalendarId.toString());

        return obj;
    }

    public static Event toJavaEvent(JSONObject obj) {
        Event event = new Event(UUID.fromString((String) obj.get("eventId")));
        event.setName((String) obj.get("name"));
        event.setBeginDate(LocalDateTime.parse((String) obj.get("beginDate")));
        event.setEndDate(LocalDateTime.parse((String) obj.get("endDate")));
        event.setParentCalendarId(UUID.fromString((String) obj.get("parentCalendarId")));

        return event;
    }

}
