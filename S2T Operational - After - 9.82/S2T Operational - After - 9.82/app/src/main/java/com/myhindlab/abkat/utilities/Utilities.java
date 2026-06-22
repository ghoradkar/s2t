package com.myhindlab.abkat.utilities;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.myhindlab.abkat.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;


public class Utilities {

    public static final int CAMERA_REQUEST = 100;
    public static final int GALLERY_REQUEST = 200;
    public static final int VIDEO_REQUEST = 300;

    public static SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dfDate2 = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat dfDate3 = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat  dfDate4 = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat dfDate6 = new SimpleDateFormat("dd-MMMM-yyyy");
    public static SimpleDateFormat dfDate7 = new SimpleDateFormat("dd-MM-yyyy");

    public static SimpleDateFormat dfDate8 = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    public static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);



    public static SimpleDateFormat dfTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");



    public static SimpleDateFormat dfDDMMYYYYDash = new SimpleDateFormat("dd-MM-yyyy");

    public static SimpleDateFormat dfDate5 = new SimpleDateFormat("dd/MM/yy");

    public static SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static SimpleDateFormat dfDate10 = new SimpleDateFormat("dd-MMM-yyyy");


    public static boolean isMobileNo(String mobileno) {
        if ((mobileno.length() == 10)
                && (isValidMobileno(mobileno.trim())))
            return true;
        else {
            return false;
        }
    }

    public static boolean isEmpty(EditText... edt) {
        int cnt = 0;
//        for (int i = 0; i < edt.length; i++)
//            edt[i].setError(null);

        for (int i = 0; i < edt.length; i++)
            if (edt[i].getText().toString().trim().length() == 0
                    || edt[i].getText().toString().trim().equalsIgnoreCase("")
                    || edt[i].getText().toString().trim().equalsIgnoreCase(" ")) {
//                edt[i].setError("Please enter mandatory fields");
//                edt[i].requestFocus();
                cnt++;
            }
        return (cnt == 0) ? false : true;
    }


    public static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat sdf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            }
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String isoTimestamp = sdf.format(new Date());
            return isoTimestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    public static void showAlertDialogMandatory(Context context, String title,
                                                String message, Boolean status, String buttonTitle, DialogInterface.OnClickListener onClickListener) {
        alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(true);
        alertDialog.setMessage(message);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_fail);
        alertDialog.setButton(buttonTitle, onClickListener);
        alertDialog.show();
    }
    public static boolean isValidABHAAddress(String abhaAddress) {
//        ^: Asserts the start of the string.
//        (?=\S*[a-zA-Z0-9]): Positive lookahead to ensure at least one alphanumeric character is present.
//        (?=\S*[\._]): Positive lookahead to ensure at least one dot or underscore is present.
//        (?!.*[\._]{2,}): Negative lookahead to prevent consecutive dots or underscores.
//        (?!^[\._]): Negative lookahead to prevent a dot or underscore at the beginning.
//        (?!.*[\._]$): Negative lookahead to prevent a dot or underscore at the end.
//        [a-zA-Z0-9\._]{8,18}: Matches alphanumeric characters, dots, and underscores between 8 and 18 characters in length.
//        $: Asserts the end of the string.

//        String ABHA_PATTERN = "^(?=\\S*[a-zA-Z0-9])(?=\\S*[\\._])(?!.*[\\._]{2,})(?!^[\\._])(?!.*[\\._]$)[a-zA-Z0-9\\._]{8,18}$";
        String ABHA_PATTERN = "^(?=.{8,18}$)(?![._])[a-zA-Z0-9]*[._]?[a-zA-Z0-9]+(?<![._])$";

//        String ABHA_PATTERN = "^(?!.*[._]{2,})[a-zA-Z0-9]+(?:[._][a-zA-Z0-9]+)?$";
        Pattern pattern = Pattern.compile(ABHA_PATTERN);
        Matcher matcher = pattern.matcher(abhaAddress);
        return matcher.matches();
    }


    public static boolean isValidMobileno(String mobileno) {
        String Mobile_PATTERN = "^[6-9]{1}[0-9]{9}$";                                               //^[+]?[0-9]{10,13}$
        Pattern pattern = Pattern.compile(Mobile_PATTERN);
        Matcher matcher = pattern.matcher(mobileno);
        return matcher.matches();
    }

    public static boolean isDateValid(String date) {
        String Mobile_PATTERN = "^[0-9]{4}[/]{1}[0-9]{2}[/]{1}[0-9]{2}$";                                               //^[+]?[0-9]{10,13}$
        Pattern pattern = Pattern.compile(Mobile_PATTERN);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /* show message int*/
    public static void showMessage(int msg, Context context) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

//        RelativeLayout toastLayout = (RelativeLayout) toast.getView();
//        TextView toastTV = (TextView) toastLayout.getChildAt(0);
//        toastTV.setTextSize(12);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isValidPincode(String pincode) {
        String Pincode_PATTERN = "[4]{1}[0-9]{5}";
        Pattern pattern = Pattern.compile(Pincode_PATTERN);
        Matcher matcher = pattern.matcher(pincode);
        return matcher.matches();
    }

    public static boolean isBarCodeValid(String barcode) {
        String Pincode_PATTERN = "^[C]{1}[W]{1}[H]{1}[0-9]{11}";
        Pattern pattern = Pattern.compile(Pincode_PATTERN);
        Matcher matcher = pattern.matcher(barcode);
        return matcher.matches();
    }


    public static boolean isBarCodeValidWithThreeZero(String barcode) {
        String BARCODE_PATTERN = "^CWH000[0-9]{8}$";
        Pattern pattern = Pattern.compile(BARCODE_PATTERN);
        Matcher matcher = pattern.matcher(barcode);
        return matcher.matches();
    }


    public static boolean isBarCodeValidConfirmaatory(String barcode) {
        String Pincode_PATTERN = "^[T]{1}[2]{1}[T]{1}[0-9]{11}";
        Pattern pattern = Pattern.compile(Pincode_PATTERN);
        Matcher matcher = pattern.matcher(barcode);
        return matcher.matches();
    }


    public static void showAlertDialogNew(Context context, String title,
                                          String message, String positiveBtnName,
                                          DialogInterface.OnClickListener positiveOnClickListener,
                                          String negativeBtnName,
                                          DialogInterface.OnClickListener negativeOnClickListener) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return; // Don't show dialog if activity is finishing or destroyed
            }
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveBtnName, positiveOnClickListener);
        builder.setNegativeButton(negativeBtnName, negativeOnClickListener);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    static class VerhoeffAlgorithm {
        int[][] d = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
                };
        int[][] p = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
                };
        int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

        private boolean validateVerhoeff(String num) {
            int c = 0;
            int[] myArray = StringToReversedIntArray(num);
            for (int i = 0; i < myArray.length; i++) {
                c = d[c][p[(i % 8)][myArray[i]]];
            }

            return (c == 0);
        }

        private int[] StringToReversedIntArray(String num) {
            int[] myArray = new int[num.length()];
            for (int i = 0; i < num.length(); i++) {
                myArray[i] = Integer.parseInt(num.substring(i, i + 1));
            }
            myArray = Reverse(myArray);
            return myArray;
        }

        private int[] Reverse(int[] myArray) {
            int[] reversed = new int[myArray.length];
            for (int i = 0; i < myArray.length; i++) {
                reversed[i] = myArray[myArray.length - (i + 1)];
            }
            return reversed;
        }
    }

//    public static boolean isaadharNumberValidate(String aadharNumber) {
//        Pattern aadharPattern = Pattern.compile("\\d{12}");
//        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
//        if (isValidAadhar) {
//            isValidAadhar = new VerhoeffAlgorithm().validateVerhoeff(aadharNumber);
//        }
//        return isValidAadhar;
//    }


    public static boolean isaadharNumberValidate(String aadharNumber) {

        // Aadhaar must start from 2-9 and contain 12 digits
        Pattern aadharPattern = Pattern.compile("^[2-9]{1}[0-9]{11}$");

        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();

        if (isValidAadhar) {
            isValidAadhar = new VerhoeffAlgorithm().validateVerhoeff(aadharNumber);
        }

        return isValidAadhar;
    }

    public static String ConvertDateFormat(DateFormat dateFormat, int day, int month, int year) {
        String startDateString = String.valueOf(day) + "/"
                + String.valueOf(month) + "/"
                + String.valueOf(year);
        Date startDate;
        String newDateString = "";
        try {
            startDate = dfDate2.parse(startDateString);
            newDateString = dateFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateString;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void showToastMessage(String msg, Context context, boolean status) {
        if (status) {
            Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void showToastMessage(int msg, Context context, boolean status) {
        if (status) {
            Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
        }
    }


    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    /* show message String*/
    public static void showMessageString(String msg, Context context) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

//        RelativeLayout toastLayout = (RelativeLayout) toast.getView();
//        TextView toastTV = (TextView) toastLayout.getChildAt(0);
//        toastTV.setTextSize(12);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
    }

    static android.app.AlertDialog alertDialog;
    static android.app.AlertDialog.Builder dialogBuilder;

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_alertred);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status, String buttonName,
                                       AlertDialog.OnClickListener okListener) {
        if (buttonName == null) {
            buttonName = "Ok";
        }

        alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//        alertDialog.setCancelable(false);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_alertred);
        alertDialog.setButton(buttonName, okListener);
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status, String buttonName,
                                       DialogInterface.OnClickListener okListener, String negativeButtonName, DialogInterface.OnClickListener onNegativeButtonClick) {
        if (buttonName == null) {
            buttonName = "Ok";
        }

        dialogBuilder = new android.app.AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        if (status != null)
            dialogBuilder.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_alertred);

        dialogBuilder.setNegativeButton(negativeButtonName, onNegativeButtonClick);
        dialogBuilder.setPositiveButton(buttonName, okListener);
        android.app.AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialogRequired(Context context, String title,
                                               String message, Boolean status, String buttonName,
                                               AlertDialog.OnClickListener okListener) {
        if (buttonName == null) {
            buttonName = "Ok";
        }

        alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_alertred);
        alertDialog.setButton(buttonName, okListener);
        alertDialog.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
//            return !ipAddr.equals("");

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;

        } catch (Exception e) {
            return false;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void provideCameraAndStorageAccess(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Permission");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.icon_alertred);
        alertDialog.setMessage("Please grant permission for camera and storage");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.getPackageName(), null)));
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog.create();
        alertDialog.show();

    }



    public static String normalize(String input) {
        if (input == null) return "";

        return input
                .replaceAll("\\u00A0", "")   // remove hidden NBSP
                .replaceAll("\\s+", "")     // remove ALL spaces
                .trim()
                .toLowerCase();
    }

    public static void showAlertDialog(Context context, String title,
                                       String message, String positiveBtnName,
                                       DialogInterface.OnClickListener onClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveBtnName, onClickListener);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 1500.0f;
        float maxWidth = 1000.0f;

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = filePath;
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }



    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        return outputStream.toByteArray();
    }


    public static String compressImageNew(Context context, String filePath, String fileName) {
        // Ensure context is not null
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        // Load the original Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int exifRotation = getExifRotation(filePath);
        Bitmap rotatedBitmap = rotateImage(bitmap, exifRotation);


        // Create a new file for the compressed image
        File outputDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CompressedImages");
        if (!outputDir.exists()) {
            outputDir.mkdirs(); // Create the directory if it doesn't exist
        }

        File compressedFile = new File(outputDir, fileName + ".jpg");

        try (FileOutputStream out = new FileOutputStream(compressedFile)) {
            // Compress the bitmap and save it to the new file
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out); // Adjust quality as needed (0-100)
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the path of the compressed file
        return compressedFile.getAbsolutePath();
    }


    private static int getExifRotation(String filePath) {
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotation;
    }


    public static String getDeviceId(Context context) {

        String deviceId = null;

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        deviceId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        new UserSessionManager(context).saveDeviceID(deviceId);
//        }

//        else {
//            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (mTelephony.getDeviceId() != null) {
//                deviceId = mTelephony.getDeviceId();
//            } else {
//                deviceId = Settings.Secure.getString(
//                        context.getContentResolver(),
//                        Settings.Secure.ANDROID_ID);
//                new UserSessionManager(context).saveDeviceID(deviceId);
//
//            }
//        }

        return deviceId;
    }


    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void turnOnLocation(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.icon_alertred);
        alertDialog.setMessage("GPS is not enabled. Please turn on the location from settings.");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }

    public static void provideLocationAccess(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Permission");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.icon_alertred);
        alertDialog.setMessage("Please grant permission for location access");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.getPackageName(), null)));
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    public static ArrayList<String> roundOffValuesSizeTwo(ArrayList<String> item) {
        for (int i = 0; i != item.size(); i++) {
            if (item.get(i).length() == 1) {
                item.set(i, "0" + item.get(i));
            }
        }
        return item;
    }

    public static List<String> GetSundays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<String> saturdaysAndSundays = new ArrayList<String>();
        int count = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                saturdaysAndSundays.add(String.valueOf(day));
                count++;
            }
        }
        return saturdaysAndSundays;
    }

    public static String ConvertDateFormatFacilityVisit(int year, int month, int day) {
        String startDateString = String.valueOf(year) + "-"
                + String.valueOf(month) + "-"
                + String.valueOf(day);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        String newDateString = "";
        try {
            startDate = df.parse(startDateString);
            newDateString = df.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (dateString.equals("")) {
            return "";
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return Integer.parseInt(ageS);
    }

    public static long diffBetweenTwoDates(String startDate, String endDate) {
        try {
            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

            //Setting dates
            date1 = dates.parse(startDate);
            date2 = dates.parse(endDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            return difference / (24 * 60 * 60 * 1000);

        } catch (Exception exception) {
            return 3;
        }
    }


    public static boolean copyFileToDownloads(File sourceFile, String fileName, Context context) {
        if (sourceFile == null || !sourceFile.exists()) return false;

        ContentResolver resolver = context.getContentResolver();
        OutputStream outputStream = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore API for Android 10+
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png"); // Adjust MIME type as needed
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                // Insert into MediaStore
                Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    outputStream = resolver.openOutputStream(uri);
                }
            } else {
                // For Android 9 and below, copy directly to the Downloads folder
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File destinationFile = new File(downloadsDir, fileName);
                outputStream = new FileOutputStream(destinationFile);
            }

            if (outputStream != null) {
                // Copy data from source file to output stream
                FileInputStream inputStream = new FileInputStream(sourceFile);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
                return true; // File copied successfully
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    public static boolean CheckDates(String fromdate, String todate) {
        boolean b = false;
        try {
            if (dfDate.parse(fromdate).before(dfDate.parse(todate))) {
                b = true;//If start date is before end date
            }
            if (dfDate.parse(fromdate).equals(dfDate.parse(todate))) {
                b = true;//If start date is before end date
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public static void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(ApplicationConstants.CHANNEL_ID, ApplicationConstants.CHANNEL_TITLE, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableVibration(true);
            channel1.enableLights(true);
            channel1.setLightColor(R.color.colorPrimary);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(channel1);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ApplicationConstants.CHANNEL_ID)
                .setContentTitle(ApplicationConstants.CHANNEL_TITLE)
                .setContentText("Checking your device")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon_logo2);

        notificationManager.notify(1, builder.build());
    }

    public static boolean isDateBeforeCurrentDate(String date) {
        Calendar dt1 = null, dt2 = null;
        try {
            dt1 = Calendar.getInstance();
            dt1.setTime(Utilities.dfDate4.parse(date));
            dt2 = Calendar.getInstance();
            dt2.add(Calendar.DAY_OF_MONTH, -1);
            if (dt1.getTime().before(dt2.getTime())) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }


//    ------------------------------------Courier Module------------------------------------------

    public static String getAmPmFrom24Hour(String time) {
        String result = "";
        if (time.equals("")) {
            return "";
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public static AlertDialog.Builder ProgressDialog(Context mContext, String progressMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.progress_dialog_layout, null, false);
        TextView textView = dialogView.findViewById(R.id.tvProgressMsg);
        if (progressMsg != null) {
            textView.setText(progressMsg);
        }
        builder.setView(dialogView);


        return builder;
    }

    // check with the package name
    // if app is available or not
    public static boolean isAppInstalled(Context context, String name) {
        boolean available = true;
        try {
            // check if available
            context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // if not available set
            // available as false
            available = false;
        }
        return available;
    }

    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }



    public static void showFileDownloadedNotification(Context context, File file) {
        String channelId = "download_channel";
        String channelName = "Downloads";

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Get URI using FileProvider
        Uri uri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                file
        );

        // Intent to open the file
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, getMimeType(uri.toString()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.download_icon)
                .setContentTitle("Download complete")
                .setContentText("Tap to open file")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            extension = extension.toLowerCase();
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mimeType != null) {
                return mimeType;
            }

            // Handle Excel and other special cases
            switch (extension) {
                case "xls":
                    return "application/vnd.ms-excel";
                case "xlsx":
                    return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                case "csv":
                    return "text/csv";
            }
        }

        return "*/*"; // fallback
    }
    private static String getMimeType(Uri uri) {
        String url = uri.toString();
        String extension = "";

        int lastDot = url.lastIndexOf(".");
        if (lastDot != -1) {
            extension = url.substring(lastDot + 1).toLowerCase();
        }

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "mp4":
                return "video/mp4";
            default:
                return "*/*";
        }
    }


    public static File saveFileFromResponse(ResponseBody responseBody, String fileName, Context context) {
        if (responseBody == null) {
            return null;
        }

        File file = new File(context.getExternalCacheDir(), fileName);
        try (InputStream inputStream = responseBody.byteStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }


            return file; // File successfully saved
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if an error occurs
        }
    }


//    public static String getMimeType(Uri uri) {
//        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
//        if (extension != null) {
//            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
//        }
//        return "*/*"; // fallback
//    }




}
