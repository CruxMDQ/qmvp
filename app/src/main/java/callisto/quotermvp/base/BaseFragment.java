package callisto.quotermvp.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.tools.BusProvider;

public abstract class BaseFragment extends Fragment {
    protected BasePresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
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
        presenter.onFragmentPaused();
        BusProvider.unregister(presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onFragmentDestroyed();
    }

    protected abstract int getLayout();

    protected abstract void createPresenter(View view);
}
