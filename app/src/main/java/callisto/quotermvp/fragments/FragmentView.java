package callisto.quotermvp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentView {
    private final Unbinder unbinder;
    private WeakReference<Fragment> fragmentRef;

    protected FragmentView(Fragment fragment) {
        fragmentRef = new WeakReference<>(fragment);
        unbinder = ButterKnife.bind(this, fragment.getView());
    }

    @Nullable
    private Activity getActivity() {
        Fragment f = fragmentRef.get();
        return (f == null) ? null : f.getActivity();
    }

    @Nullable
    public Context getContext() {
        return getActivity();
    }

    @Nullable
    public FragmentManager getChildFragmentManager() {
        Fragment f = fragmentRef.get();
        return (f == null) ? null : f.getChildFragmentManager();
    }


    public void unbind() {
        unbinder.unbind();
    }

//    @Nullable
//    protected FragmentManager getFragmentManager() {
//        Activity activity = getActivity();
//        return (activity != null) ? activity.getFragmentManager() : null;
//    }

//    @SuppressWarnings("ConstantConditions")
//    public void setToolbarTitle(String title) {
//        TextView txtToolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar).getRootView()
//                .findViewById(R.id.txtToolbarTitle);
//
//        txtToolbarTitle.setText(title);
//    }
}