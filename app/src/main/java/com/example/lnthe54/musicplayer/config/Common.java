package com.example.lnthe54.musicplayer.config;

import android.app.Activity;
import android.view.WindowManager;

import com.example.lnthe54.musicplayer.view.activity.MainActivity;

/**
 * Created by IceMan on 12/10/2016.
 */

public class Common {
    //    public static Bitmap getDefaultBg(Context mContext) {
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_wallpaper);
//        return bitmap;
//    }
    public static void setStatusBarTranslucent(boolean makeTranslucent, Activity activity) {
        if (makeTranslucent) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static String miliSecondToString(int totalTimeInSec) {
        int min = totalTimeInSec / 60;
        int sec = totalTimeInSec - min * 60;
        return String.format("%02d:%02d", min, sec);
    }

    public static void updateMainActivity() {
        MainActivity mainActivity = (MainActivity) AppController.getInstance().getMainActivity();
        if (mainActivity != null) {
            mainActivity.showCurrentSong();
        }
    }
}
