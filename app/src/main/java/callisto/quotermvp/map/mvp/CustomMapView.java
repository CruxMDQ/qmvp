package callisto.quotermvp.map.mvp;

import android.app.Fragment;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;

import callisto.quotermvp.R;
import callisto.quotermvp.fragments.FragmentView;
import callisto.quotermvp.tools.Events;

/**
 * Created by emiliano.desantis on 06/01/2017.
 */

public class CustomMapView extends FragmentView
    implements OnMapReadyCallback {

    // Otto bus is used to forward actions to the model
    private final Bus bus;

//    static final int MAP_OFFSET = 30;

    private GoogleMap mMap;

    FloatingActionMenu fabMenu;

    FloatingActionButton fabAdd;
    FloatingActionButton fabEdit;
    FloatingActionButton fabDelete;

    public CustomMapView(Fragment fragment, View view, final Bus bus) {
        super(fragment);
        this.bus = bus;

        fabMenu = (FloatingActionMenu) view.findViewById(R.id.fabMenu);
        
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bus.post(new Events.AddMarkerEvent());
            }
        });

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fabEdit);
        fabDelete = (FloatingActionButton) view.findViewById(R.id.fabDelete);

        MapFragment mapFragment = (MapFragment) fragment.getChildFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fabMenu.setVisibility(View.VISIBLE);
    }

    void addMapMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Location");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
