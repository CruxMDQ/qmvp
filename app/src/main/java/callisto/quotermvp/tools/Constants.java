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
        DEFAULT_ZOOM(12),
        RQ_PICK_CONTACT(10001),
        RQ_CAMERA_ESTATE(10002),
        RQ_CAMERA_ROOM(10003);

        private int value;

        Values(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Strings {
        DATE_FORMAT_ARG("ddMMyyyy_HHmmss"),
        DB_ESTATES("Estates"),
        DB_ROOMS("Rooms"),
        ESTATE_KEY("RealEstate key"),
        ROOM_KEY("Chamber key"),
        FIELD_ID("id"),
        FIELD_ADDRESS("address"),
        FIELD_CITY("city"),
        FIELD_IDENTIFIER("identifier"),
        FIELD_LATITUDE("latitude"),
        FIELD_LONGITUDE("longitude"),
        FIELD_OWNER("owner"),
        FIELD_PICTURE("picture"),
        FILE_PROVIDER("callisto.quotermvp.fileprovider"),
        FLAG_MODE("Mode"),
        FLAG_MODE_ADDITION("Addition"),
        FLAG_MODE_EDITION("Edition"),
        FRAGMENT_MAP("MapFragment"),
        MVP_ESTATE_DETAILS("MvpEstateDetails"),
        MVP_MAP("MvpMap"),
        MVP_ROOM_CREATE("MvpRoomCreate"),
        MVP_ROOM_LIST("MvpRoomList");

        private String text;

        Strings(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
