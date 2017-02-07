package callisto.quotermvp.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import callisto.quotermvp.app.MapApplication;

/**
 * Toolbox for image manipulation. If not already, anything here may be used on the future.
 */
@SuppressWarnings("unused")
public class Imagery {
    static public byte[] fileToArray(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // TODO Set up a setting for establishing format type and compression ratio
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            return stream.toByteArray();
        }
        else return null;
    }

    static public Bitmap arrayToBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static Bitmap fileToBitmap(String picturePath) {
        return BitmapFactory.decodeFile(picturePath);
    }

    public static Bitmap getScaledPic(ImageView imageView, String photoPath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    @NonNull
    public static File getFile(Class klass, long id, int index) throws IOException {
        // TODO Find a solution for orphaned images cluttering up the app dir
        String imageFileName = klass.getName() + "_" + id + "_" + index;
        File storageDir = MapApplication.getAppContext().getFilesDir();

        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        );
    }

    @NonNull
    public static File getFile(Class klass, String id, int index) throws IOException {
        String imageFileName = klass.getName() + "_" + id + "_" + index;
        File storageDir = MapApplication.getAppContext().getFilesDir();

        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        );
    }
}
