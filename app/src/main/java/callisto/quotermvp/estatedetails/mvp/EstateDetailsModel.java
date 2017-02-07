package callisto.quotermvp.estatedetails.mvp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.firebase.model.RealEstate;
import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;
import callisto.quotermvp.tools.Imagery;

import static callisto.quotermvp.tools.Constants.Strings.DB_ESTATES;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_ADDRESS;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_CITY;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LATITUDE;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_LONGITUDE;
import static callisto.quotermvp.tools.Constants.Strings.FIELD_OWNER;

public class EstateDetailsModel {
    private String identifier;
    private long estateId;

    private Uri uriContact;

    private String currentPhotoPath;

    private RealEstate realEstate;

    public EstateDetailsModel(String identifier) {
        this.identifier = identifier;
    }

    public Estate getEstate() {
        return Helper.getInstance().get(Estate.class, estateId);
    }

    void queryFirebaseForRealEstate() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child(DB_ESTATES.getText());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //noinspection unchecked
                    map = (HashMap<String, Object>) postSnapshot.getValue();

                    RealEstate.Builder builder = new RealEstate.Builder();

                    builder.setIdentifier(postSnapshot.getKey());

                    builder.setAddress(map.get(FIELD_ADDRESS.getText()).toString());
                    builder.setCity(map.get(FIELD_CITY.getText()).toString());
                    builder.setLatitude((double) map.get(FIELD_LATITUDE.getText()));
                    builder.setLongitude((double) map.get(FIELD_LONGITUDE.getText()));
                    builder.setOwner(map.get(FIELD_OWNER.getText()).toString());

                    try {
                        Chamber room = new Chamber((double) map.get("surface"),
                            map.get("name").toString());
                        builder.addRoom(room);
                    } catch (NullPointerException NPE) {
                        Log.d("Caught null", "Estate has no rooms");
                    }

                    RealEstate estate = builder.build();

                    if (estate.getIdentifier().equalsIgnoreCase(identifier)) {
                        realEstate = estate;
                        BusProvider.getInstance().post(new Events.QueriedEstateRetrievedEvent(estate));
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    String storeInFirebase(String address, String city, double lat, double lng, String owner) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        RealEstate.Builder builder =
            new RealEstate.Builder()
            .setAddress(address)
            .setCity(city)
            .setLatitude(lat)
            .setLongitude(lng)
            .setOwner(owner);

        DatabaseReference reference = database.getReference(DB_ESTATES.getText()).child(identifier);
//        reference.push().setValue(builder.build());
        reference.setValue(builder.build());

        return reference.getKey();
    }

    RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

//    void storeInRealm(String address, String city, double lat, double lng, String owner) {
//        Estate estate = new Estate();
//        final Helper helper = Helper.getInstance();
//
//        estate.setId(estateId);
//        estate.setAddress(address);
//        estate.setCity(city);
//        estate.setLatitude(lat);
//        estate.setLongitude(lng);
//        estate.setOwner(owner);
//        estate.setPicturePath(currentPhotoPath);
//
//        if (helper.get(Estate.class, estateId) != null) {
//            helper.updateEstate(estate);
//        } else {
//            helper.save(estate);
//        }
////        helper.save(estate);
//    }

    private ContentResolver getContentResolver() {
        return MapApplication.getAppContext().getContentResolver();
    }

    @SuppressWarnings("unused")
    // Reserved for future use
    Bitmap retrieveContactPhoto(String contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);

                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }

    @SuppressWarnings("unused")
    // Reserved for future use
    String retrieveContactNumber() {

        String contactNumber = "";

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
            new String[]{ContactsContract.Contacts._ID},
            null, null, null);

        String contactID = "";

        assert cursorID != null;
        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));

        }

        cursorID.close();

//        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

            new String[]{contactID},
            null);

        assert cursorPhone != null;

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        return contactNumber;
//        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        assert cursor != null;
        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        return contactName;

//        Log.d(TAG, "Contact Name: " + contactName);
    }

    void setUriContact(Uri uriContact) {
        this.uriContact = uriContact;
    }

    File createImageFile() {
        return getFile(identifier);
    }

    @Nullable
    private File getFile(String identifier) {
        File image = null;
        try {
            image = Imagery.getFile(Estate.class, identifier, 0);
            currentPhotoPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    String getPicturePath() {
        return currentPhotoPath;
    }

    void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
}
