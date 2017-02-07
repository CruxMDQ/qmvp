package callisto.quotermvp.roomlist;

import android.os.Bundle;
import android.view.View;

import callisto.quotermvp.R;
import callisto.quotermvp.base.BaseFragment;
import callisto.quotermvp.roomlist.mvp.RoomListModel;
import callisto.quotermvp.roomlist.mvp.RoomListPresenter;
import callisto.quotermvp.roomlist.mvp.RoomListView;
import callisto.quotermvp.tools.BusProvider;

import static callisto.quotermvp.tools.Constants.Strings.ESTATE_KEY;

public class RoomListFragment extends BaseFragment {
    public static RoomListFragment newInstance (long estateKey) {
        RoomListFragment fragment = new RoomListFragment();
        Bundle args = new Bundle();
        args.putLong(ESTATE_KEY.getText(), estateKey);
        fragment.setArguments(args);
        return fragment;
    }

    public static RoomListFragment newInstance(String identifier) {
        RoomListFragment fragment = new RoomListFragment();
        Bundle args = new Bundle();
        args.putString(ESTATE_KEY.getText(), identifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_room_list;
    }

    @Override
    protected void createPresenter(View view) {
        Bundle args = this.getArguments();
        String estateId = args.getString (ESTATE_KEY.getText());

        final RoomListModel model = new RoomListModel(estateId);
        presenter = new RoomListPresenter(model,
            new RoomListView(this, view, BusProvider.getInstance(), model.getDatabaseReference()));
    }

}
