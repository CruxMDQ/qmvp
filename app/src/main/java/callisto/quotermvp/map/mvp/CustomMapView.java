package callisto.quotermvp.map.mvp;

import android.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;

import java.util.List;

import callisto.quotermvp.R;
import callisto.quotermvp.components.MapWrapperLayout;
import callisto.quotermvp.components.OnInfoWindowElemTouchListener;
import callisto.quotermvp.fragments.FragmentView;
import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;

import static callisto.quotermvp.tools.Constants.Values.DEFAULT_ZOOM;
import static callisto.quotermvp.tools.Events.AddMarkerEvent;
import static callisto.quotermvp.tools.Events.OnMapReadyEvent;

public class CustomMapView extends FragmentView
    implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Otto bus is used to forward actions to the model
    private final Bus bus;

    private GoogleMap mMap;

    FloatingActionMenu fabMenu;

    FloatingActionButton fabAdd;
    FloatingActionButton fabEdit;
    FloatingActionButton fabDelete;

    MapWrapperLayout mapWrapperLayout;
    private final ViewGroup infoWindow;
    TextView txtLocation;
    TextView txtLatLng;
    Button btnInfoWindow;
    OnInfoWindowElemTouchListener infoButtonListener;

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

        //noinspection ConstantConditions
        infoWindow = (ViewGroup) getActivity().getLayoutInflater()
            .inflate(
                R.layout.info_window_custom,
                (ViewGroup) getActivity().findViewById(R.id.flContent)  // Replaced 'null'
            );

        txtLocation = (TextView) infoWindow.findViewById(R.id.txtLocation);
        txtLatLng = (TextView) infoWindow.findViewById(R.id.txtLatLng);

        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.mapWrapperLayout);

        btnInfoWindow = (Button) infoWindow.findViewById(R.id.btnInfoWindow);

        //noinspection ConstantConditions
        infoButtonListener = new OnInfoWindowElemTouchListener(btnInfoWindow,
            getContext().getResources().getDrawable(R.drawable.btn_default_normal_holo_light),
            getContext().getResources().getDrawable(R.drawable.btn_default_pressed_holo_light)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(getContext(), "Marker button clicked!",
                    Toast.LENGTH_SHORT).show();
            }
        };
        btnInfoWindow.setOnTouchListener(infoButtonListener);

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

        // Source: http://stackoverflow.com/a/6327095
        @SuppressWarnings("ConstantConditions")
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 59,
            getContext().getResources().getDisplayMetrics());

        mapWrapperLayout.init(mMap, px);

        mMap.setOnMarkerClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LatLng position = marker.getPosition();
                Estate estate = Helper.getInstance().get(position.latitude, position.longitude);
                setInfoWindow(estate);
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        bus.post(new OnMapReadyEvent());
    }

    void addMapMarker(LatLng latLng) {
        //noinspection ConstantConditions
        Log.d(getContext().getString(R.string.tag_event_fired),
            getContext().getString(R.string.tag_event_created_placed_marker));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Location");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    void centerOnStartingPosition(LatLng startingPosition, int defaultZoom) {
//        MarkerOptions markerOptions = new MarkerOptions().position(startingPosition);
//        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, defaultZoom));
    }

    void populateMap(List<Estate> markers) {
        for (Estate estate : markers) {
            addMapMarker(new LatLng(estate.getLatitude(), estate.getLongitude()));
        }
    }

    private void setInfoWindow(Estate estate)
        throws NullPointerException {

        if (estate != null) {
            txtLocation.setText(estate.getAddress());
            txtLatLng.setText(estate.getLatitude() + ", " + estate.getLongitude());
        }

        //handle dispatched touch event for view click
        infoWindow.findViewById(R.id.txtOwner)
            .setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = MotionEventCompat.getActionMasked(event);
                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            //noinspection ConstantConditions
                            Log.d(getContext().getString(R.string.tag_event_fired),
                                "any_view clicked");
                            break;
                    }
                    return true;
                }
            });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        Log.d(TAG, "on Marker Click called");
        marker.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(marker.getPosition())      // Sets the center of the map to Mountain View
            .zoom(DEFAULT_ZOOM.getValue())
            .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
        return true;
    }
}
