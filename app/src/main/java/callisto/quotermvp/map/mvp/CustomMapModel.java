package callisto.quotermvp.map.mvp;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;
import callisto.quotermvp.tools.Geoloc;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static callisto.quotermvp.tools.Constants.Positions.START;

public class CustomMapModel {

    LatLng getStartingPosition() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        LatLng startingPosition = new LatLng(
            START.getLatitude(),
            START.getLongitude()
        );

        return startingPosition;
    }

    Observable<LatLng> getFromLocationName(final String address, final String city) {

        @SuppressWarnings("UnnecessaryLocalVariable")
        Observable<LatLng> myObservable = Observable.create(
            new Observable.OnSubscribe<LatLng>() {
                @Override
                public void call(Subscriber<? super LatLng> subscriber) {
                    List<Address> list;

                    try {
                        list = Geoloc.geocoder
                            .getFromLocationName(address + " " + city, 1);

                        Address address = list.get(0);

                        double lat = address.getLatitude();
                        double lng = address.getLongitude();

                        // Gobbledygook-to-English: here's where the RxAndroid magic happens
                        subscriber.onNext(new LatLng(lat, lng));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        )
        /** Gobbledygook-to-English:
         * - subscribeOn tells code to use one among a pool of threads to run the observable;
         * thing is, you don't get to choose on which thread, but who cares?
         * - observeOn tells code which THREAD -and yes, this time you get to choose which- should
         * keep an eye out for the results
         */
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

        return myObservable;
    }

    Estate storeInRealm(String address, String city, double lat, double lng) {
        Estate estate = new Estate();

        estate.setId((long) Helper.getInstance().getCount(Estate.class));

        estate.setAddress(address);

        estate.setCity(city);

        estate.setLatitude(lat);

        estate.setLongitude(lng);

        Helper.getInstance().save(estate);

        return estate;
    }

    List<Estate> getAllMarkers() {
        return Helper.getInstance().getList(Estate.class);
    }
}
