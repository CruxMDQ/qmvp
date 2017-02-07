package callisto.quotermvp.roomlist.mvp;

import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.tools.Events;

public class RoomListView extends BaseView {

    private Bus bus;

//    private RoomsAdapter roomsAdapter;
    private FirebaseRecyclerAdapter<Chamber, RoomViewHolder> roomsAdapter;

    @BindView(R.id.rvRooms) RecyclerView rvRooms;
    @BindView(R.id.fabAddRoom) FloatingActionButton fabAddRoom;

//    public RoomListView(Fragment fragment, View view, Bus instance, long estateId) {
//        super(fragment, view);
//
//        this.bus = instance;
//
//        setUpRV(estateId);
//    }

    public RoomListView(Fragment fragment, View view, Bus instance, DatabaseReference reference) {
        super(fragment, view);

        this.bus = instance;

        setUpRV(reference);
    }

    private void setUpRV(DatabaseReference reference) {
//        roomsAdapter = new RoomsAdapter(null, getActivity(), R.layout.row_adapter_rooms, true, false,
//            BusProvider.getInstance(),  estateId);

        roomsAdapter = new FirebaseRecyclerAdapter<Chamber, RoomViewHolder>(
            Chamber.class,
            R.layout.row_adapter_rooms,
            RoomViewHolder.class,
            reference) {
            @Override
            public void populateViewHolder(RoomViewHolder holder, Chamber room, int position) {
                holder.txtComments.setText(room.getName());
                holder.txtSurface.setText(String.valueOf(room.getSurface()));
            }
        };

        rvRooms.setLayoutManager(new LinearLayoutManager(MapApplication.getAppContext()));
        rvRooms.setAdapter(roomsAdapter);
    }

    @OnClick(R.id.fabAddRoom)
    void onAddRoomClicked() {
        bus.post(new Events.RoomCreationRequestedEvent());
    }

//    void updateRV(RealmResults<Room> roomsList) {
//        roomsAdapter.updateRealmResults(roomsList);
//    }

    void updateRV() {
        roomsAdapter.notifyDataSetChanged();
    }

    static public class RoomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardRoomInfo) CardView cardRoomInfo;
        @BindView(R.id.imgRoomView) ImageView imgRoomView;
        @BindView(R.id.txtSurface) TextView txtSurface;
        @BindView(R.id.txtComments) TextView txtComments;

        public RoomViewHolder(View view) {
            super(view);
            imgRoomView = (ImageView) view.findViewById(R.id.imgRoomView);
            ButterKnife.bind(this, view);
        }
    }

}
