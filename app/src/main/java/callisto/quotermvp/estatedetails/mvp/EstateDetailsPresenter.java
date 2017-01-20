package callisto.quotermvp.estatedetails.mvp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;

import callisto.quotermvp.R;
import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.realm.model.Estate;
import callisto.quotermvp.tools.Events.CameraRequestedEvent;

import static callisto.quotermvp.tools.Constants.Values.RQ_CAMERA;

public class EstateDetailsPresenter extends BasePresenter {
    private EstateDetailsModel model;
    private EstateDetailsView view;

    public EstateDetailsPresenter(EstateDetailsModel estateDetailsModel, EstateDetailsView estateDetailsView) {
        super();
        this.model = estateDetailsModel;
        this.view = estateDetailsView;
        setupView(model.getEstate());
    }

    private void setupView(Estate estate) {
        view.setCity(estate.getCity());
        view.setLatitude(estate.getLatitude());
        view.setLongitude(estate.getLongitude());
        view.setStreet(estate.getAddress());
        view.setOwnerName(estate.getOwner());
    }

    public void onFragmentPaused() {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_estate_details_update));

        model.storeInRealm(
            view.getStreet(),
            view.getCity(),
            view.getLatitude(),
            view.getLongitude(),
            view.getOwnerName()
        );
    }

    public void setContact(Uri data) {
        Log.d(getString(R.string.tag_result_activity),
            getString(R.string.tag_result_contact_retrieved));

        model.setUriContact(data);
        view.setOwnerName(model.retrieveContactName());
    }

    public void setFrontView(Intent data) {
        view.setFrontView(model.getBitmapFromCamera(data));
    }

    public void setFrontView() {
        view.setPic(model.getCurrentPhotoPath());
    }

    @Subscribe
    public void onCameraRequestedEvent(CameraRequestedEvent event) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        assert view.getActivity() != null;
        if (intent.resolveActivity(view.getActivity().getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = model.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                    MapApplication.getAppContext(),
                    "callisto.quotermvp.fileprovider",
                    photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                view.startActivityForResult(intent, RQ_CAMERA.getValue());
            }
        }
    }
}
