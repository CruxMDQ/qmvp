package callisto.quotermvp.roomlist.mvp;

import com.squareup.otto.Subscribe;

import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.roomdetails.RoomDetailsFragment;

import static callisto.quotermvp.tools.Constants.Strings.MVP_ROOM_CREATE;
import static callisto.quotermvp.tools.Events.RoomCreationRequestedEvent;
import static callisto.quotermvp.tools.Events.RoomEditionRequestedEvent;

public class RoomListPresenter extends BasePresenter {
    private final RoomListModel model;
    private final RoomListView view;

    public RoomListPresenter(RoomListModel model, RoomListView view) {
        this.model = model;
        this.view = view;
        view.updateRV(model.getRoomsList());
    }

    @Subscribe
    public void onRoomCreationRequestedEvent(RoomCreationRequestedEvent event) {
        android.app.FragmentManager fragmentManager = view.getFragmentManager();

        if (fragmentManager == null) {
            return;
        }

        RoomDetailsFragment fragment = RoomDetailsFragment.newInstance(model.getEstateId());

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
            .addToBackStack(MVP_ROOM_CREATE.getText()).commit();
    }

    @Subscribe
    public void onRoomEditionRequestedEvent(RoomEditionRequestedEvent event) {
        // TODO Implement room edition
    }
}
