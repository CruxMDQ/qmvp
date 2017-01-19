package callisto.quotermvp.base.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseView {
    private final Unbinder unbinder;
    private WeakReference<Fragment> fragmentRef;

    protected BaseView(Fragment fragment, View view) {
        fragmentRef = new WeakReference<>(fragment);
        unbinder = ButterKnife.bind(this, view);
    }

    @Nullable
    protected Activity getActivity() {
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

    @Nullable
    public FragmentManager getFragmentManager() {
        Activity activity = getActivity();
        return (activity != null) ? activity.getFragmentManager() : null;
    }

    protected void startActivityForResult(Intent intent, int requestCode) {
        fragmentRef.get().startActivityForResult(intent, requestCode);
    }
//    @SuppressWarnings("ConstantConditions")
//    public void setToolbarTitle(String title) {
//        TextView txtToolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar).getRootView()
//                .findViewById(R.id.txtToolbarTitle);
//
//        txtToolbarTitle.setText(title);
//    }
}