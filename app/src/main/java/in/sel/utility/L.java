package in.sel.utility;

/**
 * Created by Lokesh on 01-06-2015.
 */

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by USER on 13-May-15.
 */
public class L extends Application {

    private static final String TAG = "Logger";

    public static void sToast(Context context) {
        Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
    }


    public static void log(Context context, String msg) {
        Log.i(TAG, msg);
       // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void log(Context context, String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void LShow(Context context, String msg) {
        Log.i(TAG, msg);
    }

    public static void lshow(Context context, String msg) {
        Log.i(TAG, msg);
    }
}
