package callisto.quotermvp.map.mvp;

import android.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;

import callisto.quotermvp.R;
import callisto.quotermvp.fragments.FragmentView;

/**
 * Created by emiliano.desantis on 06/01/2017.
 */

public class CustomMapView extends FragmentView
    implements OnMapReadyCallback {

    // Otto bus is used to forward actions to the model
    private final Bus bus;

//    static final int MAP_OFFSET = 30;

    private GoogleMap mMap;

    public CustomMapView(Fragment fragment, Bus bus) {
        super(fragment);
        this.bus = bus;

        MapFragment mapFragment = (MapFragment) fragment.getChildFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
