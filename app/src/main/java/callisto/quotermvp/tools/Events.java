package callisto.quotermvp.tools;

import callisto.quotermvp.realm.model.Estate;

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
        final String address;
        final String city;

        public GeocodingRequestEvent(String address, String city) {
            this.address = address;
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }
    }

    public static class EstateDetailsQueried {
        final Estate estate;

        public EstateDetailsQueried(Estate estate) {
            this.estate = estate;
        }

        public Estate getEstate() {
            return estate;
        }
    }

    public static class CameraRequestedEvent {
        public CameraRequestedEvent() { }
    }
}
