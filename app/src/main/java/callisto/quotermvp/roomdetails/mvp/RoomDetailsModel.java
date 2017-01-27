package callisto.quotermvp.roomdetails.mvp;

import java.io.File;
import java.io.IOException;

import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Room;
import callisto.quotermvp.tools.Imagery;

public class RoomDetailsModel {

    private long roomId;
    private long estateId;

    private String currentPhotoPath;

    public RoomDetailsModel(long roomId, long estateId) {
        this.roomId = roomId;
        this.estateId = estateId;
    }

    File createImageFile() {
        File image = null;
        try {
            image = Imagery.getFile(Room.class, roomId, 0);
            currentPhotoPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public Room getRoom() {
        return Helper.getInstance().get(Room.class, roomId);
    }

    String getPicturePath() {
        return currentPhotoPath;
    }

    void setPicturePath(String picturePath) {
        currentPhotoPath = picturePath;
    }

    void storeInRealm(String surface, String comments) {
        final Helper helper = Helper.getInstance();

        Room room = new Room();

        room.setId(roomId);

        room.setSurface(Double.valueOf(surface));

        room.setObservations(comments);

        room.setPicturePath(currentPhotoPath);

        if (helper.get(Room.class, roomId) != null) {
            helper.updateRoom(estateId, room);
        } else {
            helper.addRoom(estateId, room);
        }
    }
}
