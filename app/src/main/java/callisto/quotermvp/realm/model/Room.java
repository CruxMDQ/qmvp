package callisto.quotermvp.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Room extends RealmObject {

    @PrimaryKey
    @Required
    private Long id;

    private String picturePath;
    private Double surface;
    private String observations;

    private RealmList<Room> facilities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public RealmList<Room> getFacilities() {
        return facilities;
    }

    public void setFacilities(RealmList<Room> facilities) {
        this.facilities = facilities;
    }
}
