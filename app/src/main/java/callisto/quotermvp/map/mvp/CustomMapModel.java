package callisto.quotermvp.map.mvp;

import android.location.Address;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.firebase.model.RealEstate;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;
import callisto.quotermvp.tools.Geoloc;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static callisto.quotermvp.tools.Constants.Positions.START;
import static callisto.quotermvp.tools.Constants.Strings.DB_ESTATES;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_ADDRESS;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_CITY;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LATITUDE;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LONGITUDE;

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

    RealEstate storeInFirebase(String address, String city, double lat, double lng) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        RealEstate.Builder builder =
            new RealEstate.Builder()
                .setAddress(address)
                .setCity(city)
                .setLatitude(lat)
                .setLongitude(lng);

        final RealEstate realEstate = builder.build();

        // TODO Before uploading, introduce check to validate that it does not exist

        DatabaseReference reference = add(database, realEstate);

        realEstate.setIdentifier(reference.getKey());

        return realEstate;
    }

    @NonNull
    private DatabaseReference add(FirebaseDatabase database, RealEstate realEstate) {
        DatabaseReference reference = database.getReference(DB_ESTATES.getText());

        reference.push().setValue(realEstate);

        return reference;
    }

    void requestMarkersFromFirebase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child(DB_ESTATES.getText());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map;

                List<RealEstate> result = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    map = (HashMap<String, Object>) postSnapshot.getValue();

                    RealEstate.Builder builder = new RealEstate.Builder();

                    builder.setIdentifier(postSnapshot.getKey());

                    builder.setAddress(map.get(FIELD_ADDRESS.getText()).toString());
                    builder.setCity(map.get(FIELD_CITY.getText()).toString());
                    builder.setLatitude((double) map.get(FIELD_LATITUDE.getText()));
                    builder.setLongitude((double) map.get(FIELD_LONGITUDE.getText()));

                    try {
                        Chamber room = new Chamber((double) map.get("surface"),
                            map.get("name").toString());
                        builder.addRoom(room);
                    } catch (NullPointerException NPE) {
                        Log.d("Caught null", "Estate has no rooms");
                    }
                    result.add(builder.build());
                }

                BusProvider.getInstance().post(new Events.EstatesRetrievedFromFirebaseEvent(result));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

//    Estate storeInRealm(String address, String city, double lat, double lng) {
//        Estate estate = new Estate();
//
//        estate.setId((long) Helper.getInstance().getCount(Estate.class));
//
//        estate.setAddress(address);
//
//        estate.setCity(city);
//
//        estate.setLatitude(lat);
//
//        estate.setLongitude(lng);
//
//        Helper.getInstance().save(estate);
//
//        return estate;
//    }
//
//    List<Estate> getAllMarkersFromRealm() {
//        return Helper.getInstance().getList(Estate.class);
//    }
}
