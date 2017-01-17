package callisto.quotermvp.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import callisto.quotermvp.R;
import callisto.quotermvp.base.BaseFragment;
import callisto.quotermvp.map.mvp.CustomMapModel;
import callisto.quotermvp.map.mvp.CustomMapPresenter;
import callisto.quotermvp.map.mvp.CustomMapView;
import callisto.quotermvp.tools.BusProvider;

public class CustomMapFragment extends BaseFragment {
    public CustomMapFragment() {}

//    public static CustomMapFragment newInstance() {
//        CustomMapFragment fragment = new CustomMapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_custom_map;
    }

    @Override
    public void createPresenter(View view) {
        presenter = new CustomMapPresenter(new CustomMapModel(), new CustomMapView(this, view, BusProvider.getInstance()));
    }
}
