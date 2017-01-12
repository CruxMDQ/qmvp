package callisto.quotermvp.realm;

import android.content.Context;

import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.realm.model.Estate;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class Helper {
    private static Helper instance;

    private Realm realm;

    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper(MapApplication.getAppContext());
        }
        return instance;
    }

    private Helper(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        try {
            realm = Realm.getInstance(realmConfig);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (RealmMigrationNeededException e) {
            Realm.deleteRealm(realmConfig);
            realm = Realm.getInstance(realmConfig);
        }
    }

    public <T extends RealmObject> long save(T item) {
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(item);

        realm.commitTransaction();

        return 0;
    }

    public int getCount(Class className) {
        return getList(className).size();
    }

    public <T extends RealmObject> RealmResults<T> getList(Class<T> klass) {
        return realm.where(klass).findAll();
    }

    public Estate get(double latitude, double longitude) {
        return realm.where(Estate.class)
            .findAll().where().equalTo("latitude", latitude)
            .findAll().where().equalTo("longitude", longitude)
            .findFirst();
    }
}
