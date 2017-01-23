package callisto.quotermvp.estatedetails.mvp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import callisto.quotermvp.app.MapApplication;
import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;

import static callisto.quotermvp.tools.Constants.Strings.DATE_FORMAT_ARG;

public class EstateDetailsModel {
    private long estateId;

    private Uri uriContact;

    private String currentPhotoPath;

    public EstateDetailsModel(long estateId) {
        this.estateId = estateId;
    }

    public Estate getEstate() {
        return Helper.getInstance().get(estateId);
    }

    void storeInRealm(String address, String city, double lat, double lng, String owner) {
        Estate estate = new Estate();

        estate.setId(estateId);

        estate.setAddress(address);

        estate.setCity(city);

        estate.setLatitude(lat);

        estate.setLongitude(lng);

        estate.setOwner(owner);

        estate.setPicturePath(currentPhotoPath);

        Helper.getInstance().save(estate);
    }

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

                // TODO Move to view
//                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//                imageView.setImageBitmap(photo);
                inputStream.close();
            }

//            assert inputStream != null;
//            inputStream.close();

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

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(DATE_FORMAT_ARG.getText(), Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = MapApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);   //.getFilesDir();
        File storageDir = MapApplication.getAppContext().getFilesDir(); //getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    String getPicturePath() {
        return currentPhotoPath;
    }

    void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }
}
