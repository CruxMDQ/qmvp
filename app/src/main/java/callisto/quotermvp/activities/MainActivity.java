package callisto.quotermvp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import callisto.quotermvp.R;
import callisto.quotermvp.map.CustomMapFragment;

import static callisto.quotermvp.tools.Constants.Strings.MVP_MAP;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMapFragment();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void loadMapFragment() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new CustomMapFragment())
            .addToBackStack(MVP_MAP.getText()).commit();
    }
}
