package callisto.quotermvp.realm.model;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Estate extends RealmObject {

    @PrimaryKey
    @Required
    private Long id;

    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String owner;
    private String picturePath;

    private RealmList<Room> rooms = new RealmList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public RealmList<Room> getRooms() {
        return rooms;
    }
}
