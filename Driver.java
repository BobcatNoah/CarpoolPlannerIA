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
}
