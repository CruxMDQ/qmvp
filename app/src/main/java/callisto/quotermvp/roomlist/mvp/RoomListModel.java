package callisto.quotermvp.roomlist.mvp;

import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Room;
import io.realm.RealmResults;

public class RoomListModel {
    private final long estateId;

    public RoomListModel(long estateId) {
        this.estateId = estateId;
    }

    RealmResults<Room> getRoomsList() {
        return Helper.getInstance().getRooms(estateId);
    }

    long getEstateId() {
        return estateId;
    }
}
