package callisto.quotermvp.map.mvp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BasePresenter;
import callisto.quotermvp.estatedetails.EstateDetailsFragment;
import callisto.quotermvp.map.LocationDialog;
import callisto.quotermvp.realm.model.Estate;
import rx.Subscriber;

import static callisto.quotermvp.tools.Constants.Strings.MVP_ESTATE_DETAILS;
import static callisto.quotermvp.tools.Constants.Values.DEFAULT_ZOOM;
import static callisto.quotermvp.tools.Events.AddMarkerEvent;
import static callisto.quotermvp.tools.Events.EstateDetailsQueried;
import static callisto.quotermvp.tools.Events.GeocodingRequestEvent;
import static callisto.quotermvp.tools.Events.OnMapReadyEvent;


public class CustomMapPresenter extends BasePresenter {
    private CustomMapModel model;
    private CustomMapView view;

    public CustomMapPresenter(CustomMapModel customMapModel, CustomMapView customMapView) {
        super();
        this.model = customMapModel;
        this.view = customMapView;
    }

    @Subscribe
    public void onFabAddClickedEvent(AddMarkerEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_map_marker_trigger));

        LocationDialog dialog = LocationDialog
            .newInstance(getString(R.string.dialog_title_address_input), this);

        dialog.show(view.getChildFragmentManager(), dialog.getClass().toString());
    }

    @Subscribe
    public void onMapReadyEvent(OnMapReadyEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_map_report_ready));

        view.centerOnStartingPosition(model.getStartingPosition(), DEFAULT_ZOOM.getValue());
        view.populateMap(model.getAllMarkers());
    }

    @Subscribe
    public void onGeocodingRequestEvent(GeocodingRequestEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_geocoding_request));

        fireGeocodingRequest(event.getAddress(), event.getCity());
    }

    @Subscribe
    public void onEstateDetailsQueried(EstateDetailsQueried event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_estate_details_query));

        android.app.FragmentManager fragmentManager = view.getFragmentManager();

        if (fragmentManager == null) {
            return;
        }

        EstateDetailsFragment fragment = EstateDetailsFragment.newInstance(event.getEstate().getId());

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
            .addToBackStack(MVP_ESTATE_DETAILS.getText()).commit();
    }

    private void fireGeocodingRequest(final String address, final String city) {
        subscriptions.add(model.getFromLocationName(address, city).subscribe(new Subscriber<LatLng>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(LatLng latLng) {
                Estate estate = model.storeInRealm(address, city, latLng.latitude, latLng.longitude);
                view.addMapMarker(estate);
            }
        }));
    }

    public void onViewCreated() {
        view.onViewCreated();
    }
}
