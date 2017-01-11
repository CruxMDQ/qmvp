package callisto.quotermvp.tools;

/**
 * Created by emiliano.desantis on 10/01/2017.
 */

public class Events {
    public static class AddMarkerEvent {
        public AddMarkerEvent() {}
    }

    public static class OnMapReadyEvent {
        public OnMapReadyEvent() {}
    }

    public static class GeocodingRequestEvent {
        String address;

        public GeocodingRequestEvent(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
