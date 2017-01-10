package callisto.quotermvp.map.mvp;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import callisto.quotermvp.app.MapApplication;
import rx.Observable;
import rx.Subscriber;

public class CustomMapModel {

    public Observable<LatLng> getFromLocationName(final String address) {

        // TODO Create a singleton for this; unnecessary to instantiate it every time
        final Geocoder gc = new Geocoder(MapApplication.getAppContext());

        Observable<LatLng> myObservable = Observable.create(
            new Observable.OnSubscribe<LatLng>() {
                @Override
                public void call(Subscriber<? super LatLng> subscriber) {
                    List<Address> list;

                    try {
                        list = gc
                            .getFromLocationName(address, 1);

                        Address address = list.get(0);

                        double lat = address.getLatitude();
                        double lng = address.getLongitude();

                        subscriber.onNext(new LatLng(lat, lng));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        );

        return myObservable;
    }
}
