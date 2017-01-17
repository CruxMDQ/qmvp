package callisto.quotermvp.map.mvp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.List;

import callisto.quotermvp.R;
import callisto.quotermvp.base.mvp.BaseView;
import callisto.quotermvp.components.MapWrapperLayout;
import callisto.quotermvp.components.OnInfoWindowElemTouchListener;
import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;

import static callisto.quotermvp.tools.Constants.Strings.FRAGMENT_MAP;
import static callisto.quotermvp.tools.Constants.Values.DEFAULT_ZOOM;
import static callisto.quotermvp.tools.Events.AddMarkerEvent;
import static callisto.quotermvp.tools.Events.EstateDetailsQueried;
import static callisto.quotermvp.tools.Events.OnMapReadyEvent;

public class CustomMapView extends BaseView
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
    TextView txtOwner;
    Button btnInfoWindow;
    OnInfoWindowElemTouchListener infoButtonListener;

    private HashMap<Marker, Estate> markers;

    @SuppressLint("InflateParams")
    public CustomMapView(Fragment fragment, View view, final Bus bus) {
        super(fragment, view);
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
                null // (ViewGroup) getActivity().findViewById(R.id.flContent)  // Implementing this results in crash - probably I did something wrong somewhere, but not worth the hassle
            );

        txtLocation = (TextView) infoWindow.findViewById(R.id.txtLocation);
        txtLatLng = (TextView) infoWindow.findViewById(R.id.txtLatLng);
        txtOwner = (TextView) infoWindow.findViewById(R.id.txtOwner);

        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.mapWrapperLayout);

        btnInfoWindow = (Button) infoWindow.findViewById(R.id.btnInfoWindow);

        //noinspection ConstantConditions
        infoButtonListener = new OnInfoWindowElemTouchListener(btnInfoWindow,
            getContext().getResources().getDrawable(R.drawable.btn_default_normal_holo_light),
            getContext().getResources().getDrawable(R.drawable.btn_default_pressed_holo_light)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
//                markers.get(marker);
                Estate estate = (Estate) marker.getTag();

                if (estate != null) {
                    bus.post(new EstateDetailsQueried(estate));
                }
            }
        };
        btnInfoWindow.setOnTouchListener(infoButtonListener);
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

    Marker addMapMarker(Estate estate) {
        //noinspection ConstantConditions
        Log.d(getContext().getString(R.string.tag_event_fired),
            getContext().getString(R.string.tag_event_created_placed_marker));

        LatLng position = estate.getPosition();

        MarkerOptions markerOptions = new MarkerOptions().position(position).title("Location");

        Marker marker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

        marker.setTag(estate);
//        markers.put(marker, estate);

        return marker;
    }

    void centerOnStartingPosition(LatLng startingPosition, int defaultZoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, defaultZoom));
    }

    void populateMap(List<Estate> data) {
        markers = new HashMap<>();
        for (Estate estate : data) {
            addMapMarker(estate);
        }
    }

    private void setInfoWindow(Estate estate)
        throws NullPointerException {

        if (estate != null) {
            txtLocation.setText(estate.getAddress());
            txtLatLng.setText(estate.getLatitude() + ", " + estate.getLongitude());
            txtOwner.setText(estate.getOwner());
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
        marker.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(marker.getPosition())
            .zoom(DEFAULT_ZOOM.getValue())
            .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

        infoButtonListener.setMarker(marker);

        return true;
    }

    void onViewCreated() {
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentByTag(FRAGMENT_MAP.getText());

        if (mapFragment == null) {
            mapFragment = new MapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapWrapperLayout, mapFragment, FRAGMENT_MAP.getText());
            ft.commit();
            fm.executePendingTransactions();
        }

        mapFragment.getMapAsync(this);
    }
}
