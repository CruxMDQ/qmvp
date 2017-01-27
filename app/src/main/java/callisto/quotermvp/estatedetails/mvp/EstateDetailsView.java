package callisto.quotermvp.estatedetails.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.estatedetails.EstateDetailsFragment;

import static callisto.quotermvp.tools.Constants.Values.RQ_PICK_CONTACT;
import static callisto.quotermvp.tools.Events.EstatePictureCaptureEvent;
import static callisto.quotermvp.tools.Events.RoomsListRequestedEvent;

public class EstateDetailsView extends BaseView {
    private Bus bus;

    @BindView(R.id.txtLatitude) TextView txtLatitude;
    @BindView(R.id.txtLongitude) TextView txtLongitude;
    @BindView(R.id.txtCity) TextView txtCity;
    @BindView(R.id.txtStreet) TextView txtStreet;
    @BindView(R.id.txtOwnerName) TextView txtOwnerName;

    @BindView(R.id.cardOwner) CardView cardOwner;

    @BindView(R.id.imgFrontView) ImageView imgFrontView;

    @BindView(R.id.cardRooms) CardView cardRooms;

    public EstateDetailsView(EstateDetailsFragment fragment, View view, Bus instance) {
        super(fragment, view);
        this.bus = instance;
    }

    void setCity(String city) {
        txtCity.setText(city);
    }

    String getCity() {
        return txtCity.getText().toString();
    }

    void setLatitude(double latitude) {
        txtLatitude.setText(String.valueOf(latitude));
    }

    double getLatitude() {
        return Double.valueOf(txtLatitude.getText().toString());
    }

    void setLongitude(double longitude) {
        txtLongitude.setText(String.valueOf(longitude));
    }

    double getLongitude() {
        return Double.valueOf(txtLongitude.getText().toString());
    }

    void setStreet(String street) {
        txtStreet.setText(street);
    }

    String getStreet() {
        return txtStreet.getText().toString();
    }

    void setOwnerName(String ownerName) {
        txtOwnerName.setText(ownerName);
    }

    String getOwnerName() {
        return txtOwnerName.getText().toString();
    }

    void setFrontView(Bitmap bitmap) {
        imgFrontView.setImageBitmap(bitmap);
    }

    void setPic(String photoPath) {
//        imgFrontView.setImageBitmap(Imagery.getScaledPic(imgFrontView, photoPath));
        Picasso
            .with(MapApplication.getAppContext())
            .load(new File(photoPath))
            .into(imgFrontView);
    }

    @OnClick(R.id.cardOwner)
    void cardOwnerClicked() {

        startActivityForResult(
            new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
            RQ_PICK_CONTACT.getValue());
    }

    @OnClick(R.id.cardFrontView)
    void imgFrontViewClicked() {
        bus.post(new EstatePictureCaptureEvent());
    }

    @OnClick(R.id.cardRooms)
    void cardRoomsClicked() {
        bus.post(new RoomsListRequestedEvent());
    }
}
