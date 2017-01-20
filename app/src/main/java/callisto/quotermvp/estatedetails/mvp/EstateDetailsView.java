package callisto.quotermvp.estatedetails.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.estatedetails.EstateDetailsFragment;
import callisto.quotermvp.tools.Events;

import static callisto.quotermvp.tools.Constants.Values.RQ_PICK_CONTACT;

public class EstateDetailsView extends BaseView {
    private Bus bus;

    @BindView(R.id.txtLatitude) TextView txtLatitude;
    @BindView(R.id.txtLongitude) TextView txtLongitude;
    @BindView(R.id.txtCity) TextView txtCity;
    @BindView(R.id.txtStreet) TextView txtStreet;
    @BindView(R.id.txtOwnerName) TextView txtOwnerName;

    @BindView(R.id.cardOwner) CardView cardOwner;

    @BindView(R.id.imgFrontView) ImageView imgFrontView;

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
        // Get the dimensions of the View
        int targetW = imgFrontView.getWidth();
        int targetH = imgFrontView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imgFrontView.setImageBitmap(bitmap);
    }

    @OnClick(R.id.cardOwner)
    void cardOwnerClicked() {

        startActivityForResult(
            new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
            RQ_PICK_CONTACT.getValue());
    }

    @OnClick(R.id.cardFrontView)
    void imgFrontViewClicked() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (intent.resolveActivity(view.getActivity().getPackageManager()) != null) {
//            view.startActivityForResult(intent, RQ_CAMERA.getValue());
//        }

        bus.post(new Events.CameraRequestedEvent());
    }
}
