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

    public Event(String name, LocalDateTime beginDate, LocalDateTime endDate) {
        this.name = name;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.eventId = UUID.randomUUID();
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
    //    this.eventId = eventId;
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
}
