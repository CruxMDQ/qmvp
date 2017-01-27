package callisto.quotermvp.roomdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import callisto.quotermvp.R;
import callisto.quotermvp.base.BaseFragment;
import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Room;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsModel;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsPresenter;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsView;
import callisto.quotermvp.tools.BusProvider;

import static android.app.Activity.RESULT_OK;
import static callisto.quotermvp.tools.Constants.Strings.ESTATE_KEY;
import static callisto.quotermvp.tools.Constants.Strings.ROOM_KEY;
import static callisto.quotermvp.tools.Constants.Values.RQ_CAMERA_ROOM;

public class RoomDetailsFragment extends BaseFragment {
    public static RoomDetailsFragment newInstance(long estateKey) {
        RoomDetailsFragment fragment = new RoomDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ESTATE_KEY.getText(), estateKey);
        fragment.setArguments(args);
        return fragment;
    }

    public static RoomDetailsFragment newInstance(boolean mode, long estateKey, long roomId) {
        RoomDetailsFragment fragment = new RoomDetailsFragment();
        Bundle args = new Bundle();

        args.putLong(ESTATE_KEY.getText(), estateKey);
        args.putLong(ROOM_KEY.getText(), roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_CAMERA_ROOM.getValue() &&
            resultCode == RESULT_OK) {
            ((RoomDetailsPresenter) presenter).setFrontView();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_room_creation;
    }

    @Override
    protected void createPresenter(View view) {
        Bundle args = this.getArguments();
        long estateId = args.getLong(ESTATE_KEY.getText());
        final int roomCount = Helper.getInstance().getCount(Room.class);

        presenter = new RoomDetailsPresenter(new RoomDetailsModel(roomCount, estateId),
            new RoomDetailsView(this, view, BusProvider.getInstance()));
    }
}
