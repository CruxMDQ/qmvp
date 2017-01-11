package callisto.quotermvp.tools;

import android.location.Geocoder;

import callisto.quotermvp.app.MapApplication;

/**
 * Created by emiliano.desantis on 11/01/2017.
 */

public class Geoloc {
    static public Geocoder geocoder = new Geocoder(MapApplication.getAppContext());
}
