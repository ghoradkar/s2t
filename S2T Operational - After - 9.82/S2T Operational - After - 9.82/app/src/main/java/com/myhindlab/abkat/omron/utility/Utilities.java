package com.myhindlab.abkat.omron.utility;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Omron HealthCare Inc
 */

public class Utilities {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    public static float convertlbToKg(int lb) {
        int kg = lb * 4536;
        return kg / 10000.0f;
    }

    public static float convertFeetInchToCm(int feetValue, float inchValue) {

        int gss_arg_value;
        int feet = (int) Math.floor(feetValue);
        short inch = (short) inchValue;
        int us_height_inc = 0;

        if (feet > 0) {
            us_height_inc += feet * 48;
        }
        if (inch > 0) {
            us_height_inc += inch * 4;
        }

        gss_arg_value = (int) us_height_inc * 254;
        gss_arg_value = (gss_arg_value + 100) / 200;
        gss_arg_value = gss_arg_value * 5;

        return (float) (gss_arg_value * 0.1);
    }

    public static double round(double d, int n) {
        return Math.round(d * Math.pow(10, n)) / Math.pow(10, n);
    }

    public static void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(HealthCheckup.AppContext, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getBroadcast(HealthCheckup.AppContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
        else{
            pendingIntent = PendingIntent.getBroadcast(HealthCheckup.AppContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) HealthCheckup.AppContext.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(HealthCheckup.AppContext, Utilities.default_notification_channel_id);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon_logo2);
        builder.setAutoCancel(true);
        builder.setChannelId(Utilities.NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    public static String getDate(Date date) {
        Locale currentLocale = HealthCheckup.getInstance().getApplicationContext().getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static Date getDateOfBirthDateType(String dateOfBirth, String stringDateFormat){
        SimpleDateFormat dateFormat = new SimpleDateFormat(stringDateFormat);

        try {
            Date date = dateFormat.parse(dateOfBirth);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDateTime(Date date){
        Locale currentLocale = HealthCheckup.getInstance().getApplicationContext().getResources().getConfiguration().locale;
        TimeZone timeZone = TimeZone.getDefault();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        dateFormat.setTimeZone(timeZone);
        String formattedDateTime = dateFormat.format(date);
        return formattedDateTime;
    }
}
