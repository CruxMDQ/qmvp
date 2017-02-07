package callisto.quotermvp.roomdetails.mvp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.realm.model.Room;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;
import callisto.quotermvp.tools.Imagery;

import static callisto.quotermvp.tools.Constants.Strings.DB_ESTATES;
import static callisto.quotermvp.tools.Constants.Strings.DB_ROOMS;

public class RoomDetailsModel {

    private String roomId;
    private String estateId;

    private String currentPhotoPath;

    public RoomDetailsModel(String roomId, String estateId) {
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

    void queryFirebaseForRoom() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(DB_ESTATES.getText())
            .child(estateId)
            .child(DB_ROOMS.getText())
            .child(roomId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // noinspection unchecked
                    map = (HashMap<String, Object>) postSnapshot.getValue();

                    Chamber chamber = new Chamber(
                        postSnapshot.getKey(),
                        (double) map.get("surface"),
                        map.get("name").toString()
                    );

                    if (chamber.getIdentifier().equalsIgnoreCase(roomId)) {
                        BusProvider.getInstance().post(new Events.RoomRetrievedFromFirebaseEvent(chamber));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


    String getPicturePath() {
        return currentPhotoPath;
    }

    void setPicturePath(String picturePath) {
        currentPhotoPath = picturePath;
    }

    String storeInFirebase(double surface, String comments) {
        Chamber chamber = new Chamber(surface, comments);

        DatabaseReference reference = FirebaseDatabase.getInstance()
            .getReference(DB_ESTATES.getText())
            .child(estateId)
            .child(DB_ROOMS.getText())
            .child(roomId);

        reference.setValue(chamber);

        return reference.getKey();
    }

//    public Room getRoom() {
//        return Helper.getInstance().get(Room.class, roomId);
//    }

//    void storeInRealm(String surface, String comments) {
//        final Helper helper = Helper.getInstance();
//
//        Room room = new Room();
//
//        room.setId(roomId);
//
//        room.setSurface(Double.valueOf(surface));
//
//        room.setObservations(comments);
//
//        room.setPicturePath(currentPhotoPath);
//
//        if (helper.get(Room.class, roomId) != null) {
//            helper.updateRoom(estateId, room);
//        } else {
//            helper.addRoom(estateId, room);
//        }
//    }

}
