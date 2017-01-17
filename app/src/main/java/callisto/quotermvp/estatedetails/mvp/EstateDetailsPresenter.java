package callisto.quotermvp.estatedetails.mvp;

import android.util.Log;

import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.realm.model.Estate;

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
}
