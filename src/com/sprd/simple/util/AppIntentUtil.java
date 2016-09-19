package com.sprd.simple.util;

import android.content.Intent;

/**
 * Created by SPRD on 8/16/2016.
 */
public class AppIntentUtil {

    public static void intentSetFlag(Intent intent) {
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
