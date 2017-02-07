package callisto.quotermvp.firebase.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class RealEstate {

    private String identifier;
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String owner;
    private String picture;

    private Map<String, Chamber> rooms;

    private RealEstate() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, Chamber> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Chamber> rooms) {
        this.rooms = rooms;
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public String toString() {
        return address + ", " + city + ", " + owner;
    }

    static public class Builder {
        static private long lastId = 0;

        private String identifier;
        private Double latitude;
        private Double longitude;
        private String address;
        private String city;
        private String owner;

        private Map<String, Chamber> rooms = new HashMap<>();

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public Builder setLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public void addRoom(Chamber room) {
            rooms.put(String.valueOf("ROOM" + rooms.size()), room);
        }

        public static String getLastId() {
            return "ID" + lastId;
        }

        public RealEstate build() {
            RealEstate realEstate = new RealEstate();
            realEstate.identifier = identifier;
            realEstate.address = address;
            realEstate.city = city;
            realEstate.latitude = latitude;
            realEstate.longitude = longitude;
            realEstate.owner = owner;

            if (rooms != null){
                realEstate.rooms = rooms;
            }

            lastId++;

            return realEstate;
        }
    }
}
