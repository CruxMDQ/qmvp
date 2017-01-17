package callisto.quotermvp.estatedetails.mvp;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Bus;

import butterknife.BindView;
import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.estatedetails.EstateDetailsFragment;

public class EstateDetailsView extends BaseView {
    @BindView(R.id.txtLatitude) TextView txtLatitude;
    @BindView(R.id.txtLongitude) TextView txtLongitude;
    @BindView(R.id.txtCity) TextView txtCity;
    @BindView(R.id.txtStreet) TextView txtStreet;
    @BindView(R.id.editOwnerName) EditText editOwnerName;

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
        editOwnerName.setText(ownerName);
    }

    String getOwnerName() {
        return editOwnerName.getText().toString();
    }
}
