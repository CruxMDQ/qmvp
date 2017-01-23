package callisto.quotermvp.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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
}
