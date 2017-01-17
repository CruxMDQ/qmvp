package callisto.quotermvp.base.mvp;

import android.view.View;

import rx.subscriptions.CompositeSubscription;

import static callisto.quotermvp.app.MapApplication.getAppContext;

public class BasePresenter {
    protected CompositeSubscription subscriptions;

    public BasePresenter() {
        this.subscriptions = new CompositeSubscription();
    }

    public void onFragmentDestroyed() {
        subscriptions.unsubscribe();
    }

    protected String getString(int resId) {
        return getAppContext().getString(resId);
    }

    public void onFragmentPaused() { }

    public void onViewCreated() { }
}
