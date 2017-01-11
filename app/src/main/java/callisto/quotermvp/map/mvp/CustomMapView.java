package callisto.quotermvp.map.mvp;

import android.app.Fragment;
import android.util.Log;
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

import static callisto.quotermvp.tools.Events.AddMarkerEvent;
import static callisto.quotermvp.tools.Events.OnMapReadyEvent;

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
                bus.post(new AddMarkerEvent());
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

        // Snippet kept for debugging purposes
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng arg0) {
//                mMap.clear();
//
//                mMap.addMarker(new MarkerOptions()
//                    .position(arg0)
//                    .title("Location")
//                    .snippet(arg0.latitude + ", " + arg0.longitude));
//            }
//        });

        fabMenu.setVisibility(View.VISIBLE);

        bus.post(new OnMapReadyEvent());
    }

    void addMapMarker(LatLng latLng) {
        Log.d(getContext().getString(R.string.tag_event_fired),
            getContext().getString(R.string.tag_event_created_placed_marker));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Location");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    void centerOnStartingPosition(LatLng startingPosition, int defaultZoom) {
        MarkerOptions markerOptions = new MarkerOptions().position(startingPosition);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, defaultZoom));
    }
}
