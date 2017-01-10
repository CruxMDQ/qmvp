package callisto.quotermvp.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by emiliano.desantis on 10/01/2017.
 */

public class MapApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
