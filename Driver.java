import org.json.simple.JSONObject;

import java.util.Objects;

public class Driver {
    private String name;
    private int carCapacity;
    private boolean transportToEvent;
    private boolean transportFromEvent;
    // Driver is able to pick up each rider, rather than the riders coming to them
    private boolean canPickUpRiders;

    public Driver() {
        name = "undefined";
        carCapacity = -1;
        transportFromEvent = false;
        transportToEvent = false;
        canPickUpRiders = false;
    }

    public Driver(String name, int cap, boolean from, boolean to, boolean pickUp) {
        this.name = name;
        this.carCapacity = cap;
        this.transportFromEvent = from;
        this.transportToEvent = to;
        this.canPickUpRiders = pickUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public void setCarCapacity(int carCapacity) {
        this.carCapacity = carCapacity;
    }

    public boolean isTransportToEvent() {
        return transportToEvent;
    }

    public void setTransportToEvent(boolean transportToEvent) {
        this.transportToEvent = transportToEvent;
    }

    public boolean isTransportFromEvent() {
        return transportFromEvent;
    }

    public void setTransportFromEvent(boolean transportFromEvent) {
        this.transportFromEvent = transportFromEvent;
    }

    public boolean isCanPickUpRiders() {
        return canPickUpRiders;
    }

    public void setCanPickUpRiders(boolean canPickUpRiders) {
        this.canPickUpRiders = canPickUpRiders;
    }

    public JSONObject getJSONOBJ() {
        JSONObject obj = new JSONObject();

        obj.put("name",  name);
        obj.put("carCapacity", carCapacity);
        obj.put("transportToEvent", transportToEvent);
        obj.put("transportFromEvent",transportFromEvent);
        obj.put("canPickUpRiders", canPickUpRiders);

        return obj;
    }

    public static Driver toJavaDriver(JSONObject obj) {
        Driver driver = new Driver();
        driver.setName((String) obj.get("name"));
        driver.setCarCapacity(((Number)obj.get("carCapacity")).intValue());
        driver.setTransportToEvent((boolean) obj.get("transportToEvent"));
        driver.setTransportFromEvent((boolean) obj.get("transportFromEvent"));
        driver.setCanPickUpRiders((boolean) obj.get("canPickUpRiders"));
        return driver;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((Driver) obj).getName());
    }

    @Override
    public String toString() {
        return  name + ", Capacity: " + carCapacity + ", To: " + transportToEvent + ", From: " +
                transportFromEvent + ", Pick Up: " + canPickUpRiders;
    }
}
