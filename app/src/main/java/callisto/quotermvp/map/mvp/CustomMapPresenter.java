package callisto.quotermvp.map.mvp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import callisto.quotermvp.R;
import callisto.quotermvp.map.LocationDialog;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

import static callisto.quotermvp.app.MapApplication.getAppContext;
import static callisto.quotermvp.tools.Constants.Values.DEFAULT_ZOOM;
import static callisto.quotermvp.tools.Events.AddMarkerEvent;
import static callisto.quotermvp.tools.Events.GeocodingRequestEvent;
import static callisto.quotermvp.tools.Events.OnMapReadyEvent;


public class CustomMapPresenter {
    private CustomMapModel model;
    private CustomMapView view;

    private CompositeSubscription subscriptions;

    public CustomMapPresenter(CustomMapModel customMapModel, CustomMapView customMapView) {
        this.model = customMapModel;
        this.view = customMapView;
        subscriptions = new CompositeSubscription();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onFabAddClickedEvent(AddMarkerEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_map_marker_trigger));

        LocationDialog dialog = LocationDialog
            .newInstance(getString(R.string.dialog_title_address_input), this);

        dialog.show(view.getChildFragmentManager(), dialog.getClass().toString());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onMapReadyEvent(OnMapReadyEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_map_report_ready));

        view.centerOnStartingPosition(model.getStartingPosition(), DEFAULT_ZOOM.getValue());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onGeocodingRequestEvent(GeocodingRequestEvent event) {
        Log.d(getString(R.string.tag_event_fired),
            getString(R.string.tag_event_geocoding_request));

        fireGeocodingRequest(event.getAddress());
    }

    public void fireGeocodingRequest(String address) {
        subscriptions.add(model.getFromLocationName(address).subscribe(new Subscriber<LatLng>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(LatLng latLng) {
                view.addMapMarker(latLng);
            }
        }));
    }

    public void onFragmentDestroyed() {
        subscriptions.unsubscribe();
    }

    private String getString(int resId) {
        return getAppContext().getString(resId);
    }
}
