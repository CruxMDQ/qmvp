package callisto.quotermvp.app;

import android.app.Application;
import android.content.Context;

import callisto.quotermvp.realm.Helper;

/**
 * Created by emiliano.desantis on 10/01/2017.
 */

public class MapApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        Helper.getInstance();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
