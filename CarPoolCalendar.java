import java.util.ArrayList;
import java.util.UUID;

public class CarPoolCalendar {
    private String name;
    private ArrayList<Event> events;
    private ArrayList<UUID> sharedEventIds;
    private UUID calendarId;

    public CarPoolCalendar() {
        this.name = "undefined";
        this.events = new ArrayList<>();
        this.sharedEventIds = new ArrayList<>();
        this.calendarId = UUID.randomUUID();
    }

    public CarPoolCalendar(String name) {
        this.name = name;
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
}
