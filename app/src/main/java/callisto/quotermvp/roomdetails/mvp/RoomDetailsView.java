package callisto.quotermvp.roomdetails.mvp;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.roomdetails.RoomDetailsFragment;
import callisto.quotermvp.tools.Events;

public class RoomDetailsView extends BaseView {
    private final Bus bus;

    @BindView(R.id.imgRoom) ImageView imgRoom;
    @BindView(R.id.cardRoomView) CardView cardRoomView;
    @BindView(R.id.editSurface) EditText editSurface;
    @BindView(R.id.editComments) EditText editComments;

    public RoomDetailsView(RoomDetailsFragment fragment, View view, Bus instance) {
        super(fragment, view);

        this.bus = instance;
    }

    @OnClick(R.id.cardRoomView)
    void onImgRoomClicked() {
        bus.post(new Events.RoomPictureCaptureEvent());
    }

    void setPic(String picturePath) {
//        imgRoom.setImageBitmap(Imagery.getScaledPic(imgRoom, picturePath));
        Picasso
            .with(MapApplication.getAppContext())
            .load(new File(picturePath))
            .into(imgRoom);

    }

    void setSurface(double surface) {
        editSurface.setText(String.valueOf(surface));
    }

    void setComments(String comments) {
        editComments.setText(comments);
    }

    String getComments() {
        return editComments.getText().toString();
    }

    String getSurface() {
        return editSurface.getText().toString();
    }
}
