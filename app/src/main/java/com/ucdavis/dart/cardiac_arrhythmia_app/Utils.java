package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by sam on 7/8/17.
 */
public class Utils {

    public static void makeToast(Context context, String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
