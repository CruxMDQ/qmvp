package callisto.quotermvp.roomlist.mvp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;

import static callisto.quotermvp.tools.Constants.Strings.DB_ESTATES;
import static callisto.quotermvp.tools.Constants.Strings.DB_ROOMS;

public class RoomListModel {
    private final String estateId;

    public RoomListModel(String estateId) {
        this.estateId = estateId;
    }

    void queryFirebaseForRooms() {
        DatabaseReference ref = getDatabaseReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map;

                List<Chamber> rooms = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    map = (HashMap<String, Object>) postSnapshot.getValue();

                    try {
                        Chamber room = new Chamber(
                            (double) map.get("surface"),
                            map.get("name").toString()
                        );
                        rooms.add(room);
                    } catch (NullPointerException NPE) {
                        Log.d("Caught null", "Estate has no rooms");
                    }
                }

                if (rooms.size() != 0) {
                    BusProvider.getInstance().post(new Events.RoomListRetrievedFromFirebaseEvent(rooms));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public DatabaseReference getDatabaseReference() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        return db.getReference(DB_ESTATES.getText())
            .child(estateId)
            .child(DB_ROOMS.getText());
    }

    void requestEmptyRoom() {
        final DatabaseReference reference = getDatabaseReference();

        DatabaseReference ref = reference.push();

        String key = ref.getKey();

//        ref.setValue(new Chamber(0d, ""));

        BusProvider.getInstance().post(new Events.KeyCreatedForNewRoom(estateId, key));
    }

//    RealmResults<Room> getRoomsList() {
//        return Helper.getInstance().getRooms(estateId);
//    }

//    String getEstateId() {
//        return estateId;
//    }
}
