package callisto.quotermvp.realm;

import android.content.Context;

import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.realm.model.Estate;
import callisto.quotermvp.realm.model.Room;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import static callisto.quotermvp.tools.Constants.Strings.FIELD_ID;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LATITUDE;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LONGITUDE;

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

    public <T extends RealmObject> T get(Class<T> klass, long id) {
        return realm.where(klass).equalTo(FIELD_ID.getText(), id).findFirst();
    }

    public <T extends RealmObject> RealmResults<T> getList(Class<T> klass) {
        return realm.where(klass).findAll();
    }

    public long getLastId(Class className) {
        long key;
        try {
            RealmQuery query = realm.where(className);
            Number max = query.max(FIELD_ID.getText());
            key = max != null ? max.longValue() + 1 : 0;
        } catch (ArrayIndexOutOfBoundsException ex) {
            key = 0;
        }
        return key;
    }

    public Estate get(double latitude, double longitude) {
        return realm.where(Estate.class)
            .findAll().where().equalTo(FIELD_LATITUDE.getText(), latitude)
            .findAll().where().equalTo(FIELD_LONGITUDE.getText(), longitude)
            .findFirst();
    }

    // Method broken down for debugging purposes - do not compress
    public RealmResults<Room> getRooms(long estateId) {
        final Estate first = realm.where(Estate.class)
            .findAll().where().equalTo(FIELD_ID.getText(), estateId)
            .findFirst();
        final RealmList<Room> rooms = first
            .getRooms();
        return rooms.sort(FIELD_ID.getText());
    }

    public void addRoom(long estateId, Room room) {
        realm.beginTransaction();

        Estate target = get(Estate.class, estateId);

        target.getRooms().add(room);

        realm.commitTransaction();
    }

    public void updateRoom(long estateId, Room room) {
        realm.beginTransaction();

        Estate targetEstate = get(Estate.class, estateId);

        Room targetRoom = targetEstate.getRooms().where().equalTo(FIELD_ID.getText(), room.getId()).findFirst();

        targetRoom.setPicturePath(room.getPicturePath());
        targetRoom.setObservations(room.getObservations());
        targetRoom.setSurface(room.getSurface());

        realm.commitTransaction();
    }

    public void updateEstate(Estate update) {
        realm.beginTransaction();

        Estate target = get(Estate.class, update.getId());

        target.setAddress(update.getAddress());
        target.setCity(update.getCity());
        target.setPicturePath(update.getPicturePath());
        target.setLatitude(update.getLatitude());
        target.setLongitude(update.getLongitude());

        target.setOwner(update.getOwner());

        realm.commitTransaction();
    }
}
