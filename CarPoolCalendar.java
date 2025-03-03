import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class CarPoolCalendar {
    private String name;
    private ArrayList<Event> events;
    private ArrayList<UUID> sharedEventIds;
    private UUID calendarId;

    public CarPoolCalendar() {
        this("My Calendar");
    }

    public CarPoolCalendar(String name) {
        this.name = name;
        this.events = new ArrayList<>();
        this.sharedEventIds = new ArrayList<>();
        this.calendarId = UUID.randomUUID();
    }

    private CarPoolCalendar(UUID uuid) {
        this.name = "My Calendar";
        this.events = new ArrayList<>();
        this.sharedEventIds = new ArrayList<>();
        this.calendarId = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<UUID> getSharedEventIds() {
        return sharedEventIds;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    // Returns an arraylist of events that occur on the date parameter
    public ArrayList<Event> getEventsByDate(LocalDate date) {
        ArrayList<Event> eventsTemp = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            // If an event starts before the end of day on the start date, and ends after the start of day on end date, then add it to the arraylist
            if (events.get(i).getBeginDate().isBefore(date.plusDays(1).atStartOfDay()) && events.get(i).getEndDate().isAfter(date.atStartOfDay()) ) {
                eventsTemp.add(events.get(i));
            }
        }
        return eventsTemp;
    }

    // If the event is in this calendar, set it to the parameter passed to this method
    public void editEvent(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId().equals(event.getEventId())) {
                events.set(i, event);
                return;
            }
        }
    }

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name", name);

        JSONArray jsonEvents = new JSONArray();
        for (Event event : events) {
            jsonEvents.add(event.getJSONOBJ());
        }
        JSONArray jsonSharedEvents = new JSONArray();
        for (UUID sharedEvent : sharedEventIds) {
            jsonSharedEvents.add(sharedEvent.toString());
        }
        obj.put("events", jsonEvents);
        obj.put("sharedEventIds", jsonSharedEvents);
        obj.put("calendarId",calendarId.toString());

        return obj;

    }

    public static CarPoolCalendar toJavaCalendar(JSONObject obj) {
        CarPoolCalendar cal = new CarPoolCalendar(UUID.fromString((String)obj.get("calendarId")));
        cal.setName((String) obj.get("name"));
        for (Object event : (JSONArray) obj.get("events")) {
            cal.getEvents().add(Event.toJavaEvent((JSONObject) event));
        }
        for (Object id : (JSONArray) obj.get("sharedEventIds")) {
            cal.getSharedEventIds().add(UUID.fromString((String)id));
        }


        return cal;
    }

    public boolean containsEvent(Event event) {
        for (Event e : events) {
            if (e.equals(event)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        // The json object is already formatted
        // So I use that instead of formatting it myself
        return getJSONOBJ().toString();
    }
}
