package callisto.quotermvp.roomdetails.mvp;

import android.content.Intent;
import android.provider.MediaStore;

import com.squareup.otto.Subscribe;

import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.firebase.model.Chamber;

import static callisto.quotermvp.tools.Constants.Values.RQ_CAMERA_ROOM;
import static callisto.quotermvp.tools.Events.RoomPictureCaptureEvent;
import static callisto.quotermvp.tools.Events.RoomRetrievedFromFirebaseEvent;

public class RoomDetailsPresenter extends BasePresenter {
    private final RoomDetailsModel model;
    private final RoomDetailsView view;

    public RoomDetailsPresenter(RoomDetailsModel model, RoomDetailsView view) {
        this.model = model;
        this.view = view;
        model.queryFirebaseForRoom();
//        restoreRealmObject(model.getRoom());
    }

    private void restoreFirebaseObject(Chamber chamber) {
        if (chamber != null) {
            view.setComments(chamber.getName());
            view.setSurface(chamber.getSurface());
        }
    }

    public void onFragmentPaused() {
        persistRealmObject();
    }

    private void persistRealmObject() {
        model.storeInFirebase(Double.valueOf(view.getSurface()), view.getComments());
    }

    @Subscribe
    public void onCameraRequestedEvent(RoomPictureCaptureEvent event) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final int code = RQ_CAMERA_ROOM.getValue();

        startCameraActivity(view, intent, code, model.createImageFile());
    }

    @Subscribe
    public void onRoomRetrievedFromFirebase(RoomRetrievedFromFirebaseEvent event) {
        restoreFirebaseObject(event.room);
    }

    public void setFrontView() {
        view.setPic(model.getPicturePath());
    }

//    private void restoreRealmObject(Room room) {
//        if (room != null) {
//            view.setPic(room.getPicturePath());
//            view.setComments(room.getObservations());
//            view.setSurface(room.getSurface());
//            model.setPicturePath(room.getPicturePath());
//        }
//    }
}
