package com.myhindlab.abkat.utilities;

import android.content.Context;
import android.os.Build;

import java.io.File;

public class DeviceUtils {

        public static Boolean isDeviceRooted(Context context){
            boolean isRooted = isrooted1() || isrooted2();
            return isRooted;
        }

        private static boolean isrooted1() {

            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
            return false;
        }

        // try executing commands
        private static boolean isrooted2() {
            return canExecuteCommand("/system/xbin/which su")
                    || canExecuteCommand("/system/bin/which su")
                    || canExecuteCommand("which su");
        }

        private static boolean canExecuteCommand(String command) {
            boolean executedSuccesfully;
            try {
                Runtime.getRuntime().exec(command);
                executedSuccesfully = true;
            } catch (Exception e) {
                executedSuccesfully = false;
            }

            return executedSuccesfully;
        }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    }
