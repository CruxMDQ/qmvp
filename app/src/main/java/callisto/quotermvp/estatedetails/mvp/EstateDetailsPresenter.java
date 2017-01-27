package callisto.quotermvp.estatedetails.mvp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.squareup.otto.Subscribe;

import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.realm.model.Estate;
import callisto.quotermvp.roomlist.RoomListFragment;
import callisto.quotermvp.tools.Events.EstatePictureCaptureEvent;
import callisto.quotermvp.tools.Imagery;

import static callisto.quotermvp.tools.Constants.Strings.MVP_ROOM_LIST;
import static callisto.quotermvp.tools.Constants.Values.RQ_CAMERA_ESTATE;
import static callisto.quotermvp.tools.Events.RoomsListRequestedEvent;

public class EstateDetailsPresenter extends BasePresenter {
    private EstateDetailsModel model;
    private EstateDetailsView view;

    public EstateDetailsPresenter(EstateDetailsModel estateDetailsModel, EstateDetailsView estateDetailsView) {
        super();
        this.model = estateDetailsModel;
        this.view = estateDetailsView;
        restoreRealmObject(model.getEstate());
    }

    private void restoreRealmObject(Estate estate) {
        view.setCity(estate.getCity());
        view.setLatitude(estate.getLatitude());
        view.setLongitude(estate.getLongitude());
        view.setStreet(estate.getAddress());
        view.setOwnerName(estate.getOwner());
        view.setFrontView(Imagery.fileToBitmap(estate.getPicturePath()));
        model.setCurrentPhotoPath(estate.getPicturePath());
//        if (estate.getFrontPicture() != null) {
//            view.setFrontView(Imagery.arrayToBitmap(estate.getFrontPicture()));
//        }
    }

    public void onFragmentPaused() {
        persistRealmObject();
    }

    private void persistRealmObject() {
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

    public void setFrontView() {
        view.setPic(model.getPicturePath());
    }

    @Subscribe
    public void onCameraRequestedEvent(EstatePictureCaptureEvent event) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final int code = RQ_CAMERA_ESTATE.getValue();

        startCameraActivity(view, intent, code, model.createImageFile());
    }

    @Subscribe
    public void onRoomsListRequestedEvent(RoomsListRequestedEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_rooms_list_query));

        android.app.FragmentManager fragmentManager = view.getFragmentManager();

        if (fragmentManager == null) {
            return;
        }

        RoomListFragment fragment = RoomListFragment.newInstance(model.getEstate().getId());

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
            .addToBackStack(MVP_ROOM_LIST.getText()).commit();
    }
}
