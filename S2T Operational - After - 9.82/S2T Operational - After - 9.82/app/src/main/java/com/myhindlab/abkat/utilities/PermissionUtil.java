package com.myhindlab.abkat.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.Arrays;

public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();
    public static final int PERMISSION_ALL = 1;

    public static boolean doesAppNeedPermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static String[] getPermissions(Context context)
            throws PackageManager.NameNotFoundException {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_PERMISSIONS);

        return info.requestedPermissions;
    }

    public static boolean askPermissions(Activity activity) {
        if (doesAppNeedPermissions()) {
            try {
                String[] permissions = getPermissions(activity);
//                String[] permissionsStr = {
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                        Manifest.permission.BLUETOOTH_CONNECT,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                };
                Log.d(TAG, "askPermissions: " + Arrays.toString(permissions));

                if (!checkPermissions(activity, permissions)) {
                    activity.requestPermissions(permissions,
                            PERMISSION_ALL);

                    return true;

                } else
                    return false;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;
    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
