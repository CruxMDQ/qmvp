package callisto.quotermvp.base.mvp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

import rx.subscriptions.CompositeSubscription;

import static callisto.quotermvp.app.MapApplication.getAppContext;
import static callisto.quotermvp.tools.Constants.Strings.FILE_PROVIDER;

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

    protected void startCameraActivity(BaseView view, Intent intent, int code, File photoFile) {
        assert view.getActivity() != null;
        if (intent.resolveActivity(view.getActivity().getPackageManager()) != null) {

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                    getAppContext(),
                    FILE_PROVIDER.getText(),
                    photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                view.startActivityForResult(intent, code);
            }
        }
    }
}
