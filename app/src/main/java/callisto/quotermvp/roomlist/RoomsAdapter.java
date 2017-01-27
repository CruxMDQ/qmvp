package callisto.quotermvp.roomlist;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import callisto.quotermvp.R;
import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.realm.model.Room;
import callisto.quotermvp.tools.Events;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RoomsAdapter extends RealmBasedRecyclerViewAdapter<Room, RoomsAdapter.RoomViewHolder>{

    private final int idLayout;
    private final Bus bus;
    private final long estateId;

    public RoomsAdapter(RealmResults<Room> results, Activity activity,
                        int idLayout, boolean automaticUpdate, boolean animateIdType,
                        Bus bus, long estateId) {
        super(activity, results, automaticUpdate, animateIdType);
        this.idLayout = idLayout;
        this.estateId = estateId;
        this.bus = bus;
    }

    @Override
    public RoomViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(MapApplication.getAppContext());
        final View view = inflater.inflate(idLayout, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(RoomViewHolder viewHolder, int position) {
        final Room room = realmResults.get(position);

        Picasso
            .with(MapApplication.getAppContext())
            .load(new File(room.getPicturePath()))
            .into(viewHolder.imgRoomView);

        viewHolder.txtSurface.setText(String.valueOf(room.getSurface()));

        viewHolder.txtComments.setText(room.getObservations());
        viewHolder.cardRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new Events.RoomEditionRequestedEvent(estateId, room.getId()));
            }
        });
    }

    static class RoomViewHolder extends RealmViewHolder {
        @BindView(R.id.cardRoomInfo) CardView cardRoomInfo;
        @BindView(R.id.imgRoomView) ImageView imgRoomView;
        @BindView(R.id.txtSurface) TextView txtSurface;
        @BindView(R.id.txtComments) TextView txtComments;

        RoomViewHolder(View view) {
            super(view);
            imgRoomView = (ImageView) view.findViewById(R.id.imgRoomView);
            ButterKnife.bind(this, view);
        }
    }
}
