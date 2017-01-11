package callisto.quotermvp.map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import callisto.quotermvp.R;
import callisto.quotermvp.map.mvp.CustomMapModel;
import callisto.quotermvp.map.mvp.CustomMapPresenter;
import callisto.quotermvp.map.mvp.CustomMapView;
import callisto.quotermvp.tools.BusProvider;

public class CustomMapFragment extends Fragment {
    private CustomMapPresenter presenter;

    public CustomMapFragment() {}

//    public static CustomMapFragment newInstance() {
//        CustomMapFragment fragment = new CustomMapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_map, container, false);
        createPresenter(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.register(presenter);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.unregister(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onFragmentDestroyed();
    }

    private void createPresenter(View view) {
        presenter = new CustomMapPresenter(new CustomMapModel(), new CustomMapView(this, view, BusProvider.getInstance()));
    }
}
