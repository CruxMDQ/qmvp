package callisto.quotermvp.tools;

public class Constants {
    private Constants() {
        throw new AssertionError("This class is NOT MEANT FOR INSTANTIATION!");
    }

    public enum Positions {
        START(-38.010481, -57.553307);

        private final double latitude;
        private final double longitude;

        Positions(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public enum Values {
        DEFAULT_ZOOM(12);

        private int value;

        Values(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Strings {
        ESTATE_KEY("Estate key"),
        FRAGMENT_MAP("MapFragment"),
        MVP_MAP("MvpMap"),
        MVP_ESTATE_DETAILS("MvpEstateDetails");

        private String text;

        Strings(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
