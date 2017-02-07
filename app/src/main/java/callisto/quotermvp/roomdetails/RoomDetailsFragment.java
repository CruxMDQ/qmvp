package callisto.quotermvp.roomdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import callisto.quotermvp.R;
import callisto.quotermvp.base.BaseFragment;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsModel;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsPresenter;
import callisto.quotermvp.roomdetails.mvp.RoomDetailsView;
import callisto.quotermvp.tools.BusProvider;

import static android.app.Activity.RESULT_OK;
import static callisto.quotermvp.tools.Constants.Strings.ESTATE_KEY;
import static callisto.quotermvp.tools.Constants.Strings.FLAG_MODE;
import static callisto.quotermvp.tools.Constants.Strings.ROOM_KEY;
import static callisto.quotermvp.tools.Constants.Values.RQ_CAMERA_ROOM;

public class RoomDetailsFragment extends BaseFragment {
    public static RoomDetailsFragment newInstance(String mode, String estateKey) {
        RoomDetailsFragment fragment = new RoomDetailsFragment();
        Bundle args = new Bundle();
        args.putString(FLAG_MODE.getText(), mode);
        args.putString(ESTATE_KEY.getText(), estateKey);
        fragment.setArguments(args);
        return fragment;
    }

    public static RoomDetailsFragment newInstance(String mode, String estateKey, String roomId) {
        RoomDetailsFragment fragment = new RoomDetailsFragment();
        Bundle args = new Bundle();
        args.putString(FLAG_MODE.getText(), mode);
        args.putString(ESTATE_KEY.getText(), estateKey);
        args.putString(ROOM_KEY.getText(), roomId);
        fragment.setArguments(args);
        return fragment;
    }

//    public static RoomDetailsFragment newInstance(String mode, long estateKey, long roomId) {
//        RoomDetailsFragment fragment = new RoomDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(FLAG_MODE.getText(), mode);
//        args.putLong(ESTATE_KEY.getText(), estateKey);
//        args.putLong(ROOM_KEY.getText(), roomId);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
//        Bundle args = this.getArguments();
//        long estateId = args.getLong(ESTATE_KEY.getText());
//        final int roomCount = Helper.getInstance().getCount(Room.class);
//
//        String mode = args.getString(FLAG_MODE.getText());
//
//        assert mode != null;
//        if (mode.equals(FLAG_MODE_ADDITION.getText())) {
//            presenter = new RoomDetailsPresenter(new RoomDetailsModel(roomCount, estateId),
//                new RoomDetailsView(this, view, BusProvider.getInstance()));
//        } else if (mode.equals(FLAG_MODE_EDITION.getText())) {
//
//            long roomId = args.getLong(ROOM_KEY.getText());
//
//            presenter = new RoomDetailsPresenter(new RoomDetailsModel(roomId, estateId),
//                new RoomDetailsView(this, view, BusProvider.getInstance()));
//        }
//    }
        Bundle args = this.getArguments();
        String estateId = args.getString(ESTATE_KEY.getText());
        String roomId = args.getString(ROOM_KEY.getText());

        presenter = new RoomDetailsPresenter(new RoomDetailsModel(roomId, estateId),
            new RoomDetailsView(this, view, BusProvider.getInstance()));
    }
}
