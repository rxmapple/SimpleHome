package com.sprd.simple.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.sprd.simple.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SPRD on 8/16/2016.
 */
public class PackageInfoUtil {
    public static final int FILTER_ALL_APP = 0; // all app
    public static final int FILTER_SYSTEM_APP = 1; // system app
    public static final int FILTER_THIRD_APP = 2; // the third app
    public static final int FILTER_SDCARD_APP = 3; // app in sdcard

    public static void getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mAllApps = packageManager.queryIntentActivities(
                intent, 0);
        for (int i = 0; i < mAllApps.size(); i++) {
            Log.i("wangkai", "mAllApps = " + mAllApps.get(i).toString());
        }
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(
                packageManager));
    }

    public static ArrayList<AppInfo> queryFilterAppInfo(Context context,
            int filter) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> listAppcations = packageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(packageManager));
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        switch (filter) {
            case FILTER_ALL_APP:
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    appInfos.add(getAppInfo(app, packageManager, context));
                }
                return appInfos;
            case FILTER_SYSTEM_APP:
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appInfos.add(getAppInfo(app, packageManager, context));
                    }
                }
                return appInfos;
            case FILTER_THIRD_APP:
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        appInfos.add(getAppInfo(app, packageManager, context));
                    }
                }
                break;
            case FILTER_SDCARD_APP:
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                        appInfos.add(getAppInfo(app, packageManager, context));
                    }
                }
                return appInfos;
            default:
                return null;
        }
        return appInfos;
    }

    private static AppInfo getAppInfo(ApplicationInfo app, PackageManager pm,
            Context context) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppLabel((String) app.loadLabel(pm));
        appInfo.setAppIcon(app.loadIcon(pm));
        appInfo.setAppPkg(app.packageName);
        return appInfo;
    }
}
