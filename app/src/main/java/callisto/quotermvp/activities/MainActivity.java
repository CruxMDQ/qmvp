package callisto.quotermvp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import callisto.quotermvp.R;
import callisto.quotermvp.map.CustomMapFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMapFragment();
    }

    private void loadMapFragment() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new CustomMapFragment()).addToBackStack("MapFragment").commit();
    }
}
