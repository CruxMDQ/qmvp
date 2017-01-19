package callisto.quotermvp.estatedetails.mvp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.OnClick;
import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.estatedetails.EstateDetailsFragment;

import static callisto.quotermvp.tools.Constants.Values.RQ_PICK_CONTACT;

public class EstateDetailsView extends BaseView {
    @BindView(R.id.txtLatitude) TextView txtLatitude;
    @BindView(R.id.txtLongitude) TextView txtLongitude;
    @BindView(R.id.txtCity) TextView txtCity;
    @BindView(R.id.txtStreet) TextView txtStreet;
    @BindView(R.id.txtOwnerName) TextView txtOwnerName;

    @BindView(R.id.cardOwner) CardView cardOwner;

    public EstateDetailsView(EstateDetailsFragment fragment, View view, Bus instance) {
        super(fragment, view);
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

    @OnClick(R.id.cardOwner)
    void cardOwnerClicked() {

        startActivityForResult(
            new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
            RQ_PICK_CONTACT.getValue());
    }
}
