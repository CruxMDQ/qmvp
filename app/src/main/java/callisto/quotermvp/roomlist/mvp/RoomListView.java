package callisto.quotermvp.roomlist.mvp;

import android.app.Fragment;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.realm.model.Room;
import callisto.quotermvp.roomlist.RoomsAdapter;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;

public class RoomListView extends BaseView {

    private Bus bus;

    private RoomsAdapter roomsAdapter;

    @BindView(R.id.rvRooms) RealmRecyclerView rvRooms;
    @BindView(R.id.fabAddRoom) FloatingActionButton fabAddRoom;

    public RoomListView(Fragment fragment, View view, Bus instance, long estateId) {
        super(fragment, view);

        this.bus = instance;

        setUpRV(estateId);
    }

    private void setUpRV(long estateId) {
        roomsAdapter = new RoomsAdapter(null, getActivity(), R.layout.row_adapter_rooms, true, false,
            BusProvider.getInstance(),  estateId);
        rvRooms.setAdapter(roomsAdapter);
    }

    @OnClick(R.id.fabAddRoom)
    void onAddRoomClicked() {
        bus.post(new Events.RoomCreationRequestedEvent());
    }

    void updateRV(RealmResults<Room> roomsList) {
        roomsAdapter.updateRealmResults(roomsList);
    }
}
