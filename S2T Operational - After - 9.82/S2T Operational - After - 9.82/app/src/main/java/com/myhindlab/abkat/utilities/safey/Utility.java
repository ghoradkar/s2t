package com.myhindlab.abkat.utilities.safey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.myhindlab.abkat.HealthCheckup;
import com.myhindlab.abkat.models.safey.AirGraphData;
import com.myhindlab.abkat.models.safey.AirTestResult;
import com.myhindlab.abkat.models.safey.TestMeasurements;
import com.myhindlab.abkat.models.safey.TrialResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.safey.safey_sdk.FlowVolumeData;
import info.safey.safey_sdk.ResultData;
import info.safey.safey_sdk.TestResult;
import info.safey.safey_sdk.Variance;
import kotlin.jvm.internal.Intrinsics;


public class Utility {
    @NotNull
    public static SimpleDateFormat sdformat;
    public static final String imageURL = "imageURL";
    @NotNull
    public static final Utility INSTANCE;

    @NotNull
    public final SimpleDateFormat getSdformat() {
        return sdformat;
    }

    public final void setSdformat(@NotNull SimpleDateFormat var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        sdformat = var1;
    }

    @NotNull
    public static String getAgeStr(long millis) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTimeInMillis(millis);
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();

        now.setTimeInMillis(currentTime);
        int years = now.get(1) - dob.get(1);
        int currMonth = now.get(2) + 1;
        int birthMonth = dob.get(2) + 1;
        int months = currMonth - birthMonth;
        if (months < 0) {
            --years;
            months = 12 - birthMonth + currMonth;
            if (now.get(5) < dob.get(5)) {
                --months;
            }
        } else if (months == 0 && now.get(5) < dob.get(5)) {
            --years;
            months = 11;
        }

        return "" + years + '.' + months;
    }


    public static void moveFile(@NotNull File file, @NotNull File dir) throws IOException {

        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = (FileChannel) null;
        FileChannel inputChannel = (FileChannel) null;

        try {
            outputChannel = (new FileOutputStream(newFile)).getChannel();
            inputChannel = (new FileInputStream(file)).getChannel();
            inputChannel.transferTo(0L, inputChannel.size(), (WritableByteChannel) outputChannel);
            inputChannel.close();
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }

            if (outputChannel != null) {
                outputChannel.close();
            }

        }

    }

    @Nullable
    public static String getFormattedDateTime(@Nullable Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        int day = cal.get(5);
        String var10000;
        if (day > 10 && day < 19) {
            var10000 = (new SimpleDateFormat("d'th'  MMMM yy    hh:mm a")).format(date);
        } else {
            switch (day % 10) {
                case 1:
                    var10000 = (new SimpleDateFormat("d'st' MMM'' yy    hh:mm a")).format(date);
                    break;
                case 2:
                    var10000 = (new SimpleDateFormat("d'nd' MMM'' yy    hh:mm a")).format(date);
                    break;
                case 3:
                    var10000 = (new SimpleDateFormat("d'rd' MMM'' yy    hh:mm a")).format(date);
                    break;
                default:
                    var10000 = (new SimpleDateFormat("d'th' MMM'' yy    hh:mm a")).format(date);
            }
        }

        return var10000;
    }

    @Nullable
    public static String getFormattedDate(@Nullable Date date) {
        Calendar cal = Calendar.getInstance();
        Intrinsics.checkNotNullExpressionValue(cal, "cal");
        cal.setTime(date);
        int day = cal.get(5);
        String var10000;
        if (day > 10 && day < 19) {
            var10000 = (new SimpleDateFormat("d'th'  MMMM yyyy")).format(date);
        } else {
            switch (day % 10) {
                case 1:
                    var10000 = (new SimpleDateFormat("d'st' MMM'' yyyy")).format(date);
                    break;
                case 2:
                    var10000 = (new SimpleDateFormat("d'nd' MMM'' yyyy")).format(date);
                    break;
                case 3:
                    var10000 = (new SimpleDateFormat("d'rd' MMM'' yyyy")).format(date);
                    break;
                default:
                    var10000 = (new SimpleDateFormat("d'th' MMM'' yyyy")).format(date);
            }
        }

        return var10000;
    }

    @NotNull
    public static String[] getpreviousweek() {
        Calendar c = Calendar.getInstance();
        c.set(7, 2);
        c.add(5, -7);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        ArrayList<String> listDate = new ArrayList();


        for (int i = 0; i < 7; i++) {
            listDate.add(df.format(c.getTime()));
            c.add(5, 1);
        }


        String MONDAY = listDate.get(0);

        String SUNDAY = listDate.get(6);
        return new String[]{MONDAY, SUNDAY};
    }

    @Nullable
    public static String getCurrentWeek() {
        Calendar mCalendar = Calendar.getInstance();
        Date date = new Date();
        mCalendar.setTime(date);
        int day_of_week = mCalendar.get(7);

        int monday_offset = day_of_week == 1 ? -6 : 2 - day_of_week;
        mCalendar.add(6, monday_offset);
        Date mDateMonday = mCalendar.getTime();
        mCalendar.add(6, 6);
        Date mDateSunday = mCalendar.getTime();
        String strDateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String MONDAY = sdf.format(mDateMonday);
        sdf.format(mDateSunday);
        return String.valueOf(MONDAY);
    }

    @Nullable
    public static String getFormattedMonthDate(@Nullable Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (new SimpleDateFormat("MMM''yy")).format(cal.getTime());
    }

    @Nullable
    public static String getFormattedDay(@Nullable Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(5);
        String var10000;
        if (day > 10 && day < 19) {
            var10000 = (new SimpleDateFormat("d'th'  MMM")).format(date);
        } else {
            switch (day % 10) {
                case 1:
                    var10000 = (new SimpleDateFormat("d'st' MMM")).format(date);
                    break;
                case 2:
                    var10000 = (new SimpleDateFormat("d'nd' MMM")).format(date);
                    break;
                case 3:
                    var10000 = (new SimpleDateFormat("d'rd' MMM")).format(date);
                    break;
                default:
                    var10000 = (new SimpleDateFormat("d'th' MMM")).format(date);
            }
        }

        return var10000;
    }

    public static double rounded(double number) {
        return (double) Math.round(number * 100.0D) / 100.0D;
    }


    public static TextView addImage(TextView textView, Context context, @NotNull String atText, @DrawableRes int imgSrc, int imgWidth, int imgHeight) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());
        Drawable var10000 = ContextCompat.getDrawable(context, imgSrc);
        if (var10000 != null) {
            Drawable drawable = var10000;
            drawable.mutate();
            drawable.setBounds(0, 0, imgWidth, imgHeight);
            CharSequence var9 = atText;
            int start = textView.getText().toString().indexOf(atText);
            ssb.setSpan(new VerticalImageSpan(drawable), start, start + atText.length(), 17);
            textView.setText((CharSequence) ssb, BufferType.SPANNABLE);
        }
        return textView;
    }

    @NotNull
    public static Date getDaysAgo(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -daysAgo);
        Date var10000 = calendar.getTime();
        return var10000;
    }

    public static double getAge(long millis) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        Intrinsics.checkNotNullExpressionValue(dob, "dob");
        dob.setTimeInMillis(millis);
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        Intrinsics.checkNotNullExpressionValue(now, "now");
        now.setTimeInMillis(currentTime);
        int years = now.get(1) - dob.get(1);
        int currMonth = now.get(2) + 1;
        int birthMonth = dob.get(2) + 1;
        int months = currMonth - birthMonth;
        if (months < 0) {
            --years;
            months = 12 - birthMonth + currMonth;
            if (now.get(5) < dob.get(5)) {
                --months;
            }
        } else if (months == 0 && now.get(5) < dob.get(5)) {
            --years;
            months = 11;
        }

        for (double dp = (double) months; dp > (double) 1; dp /= 10.0D) {
        }

        String yearmonth = "" + years + '.' + months;
        return Double.parseDouble(yearmonth);
    }

    public static void showDialog(@NotNull Context context, @NotNull String title, @NotNull String message) {


        (new Builder(context)).setTitle((CharSequence) title).setMessage((CharSequence) message).setCancelable(false).setPositiveButton("Ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    public static double feetToCentimeter(@NotNull String feet) {


        double feett = Double.parseDouble(feet);
        double dCentimeter = feett * 30.48;
        return dCentimeter;

//        double dCentimeter = 0.0D;
//        if (!TextUtils.isEmpty(feet)) {
//            Double var8;
//            if (feet.contains(".")) {
//                String[] tempfeet = feet.split("\\.");
//
//
//
//                if (!TextUtils.isEmpty((CharSequence)tempfeet[0])) {
//                    if (Integer.parseInt(tempfeet[0]) < 8) {
//                        dCentimeter += Double.parseDouble(tempfeet[0]) * 30.48D;
//                    } else {
//                        var8 = Double.valueOf(tempfeet[0]);
//                        dCentimeter = var8;
//                    }
//                }
//
//                if (!TextUtils.isEmpty((CharSequence)tempfeet[1]) && Integer.parseInt(tempfeet[0]) < 8) {
//                    dCentimeter += Double.parseDouble("0." + tempfeet[1]) * 2.54D;
//                }
//            } else if (!TextUtils.isEmpty((CharSequence)feet)) {
//                if (Integer.parseInt(feet) < 8) {
//                    dCentimeter += Double.parseDouble(feet) * 30.48D;
//                } else {
//                    var8 = Double.valueOf(feet);
//                    dCentimeter = var8;
//                }
//            }
//        }
//
//        return dCentimeter;
    }

    @Nullable
    public static List getImageData(@NotNull Context context, @NotNull String jsonFile, @Nullable String objName, @NotNull String characterType) {

        List AvatarList = (new ArrayList());

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(context, jsonFile));
            JSONObject var10000 = jsonArray.getJSONObject(0);
            JSONObject jsonObject = var10000;
            JSONObject jsonObjectData = jsonObject.getJSONObject(objName);
            JSONArray jsonArray1 = jsonObjectData.getJSONArray("data");
            int i = 0;

            for (int var11 = jsonArray1.length(); i < var11; ++i) {
                if (Intrinsics.areEqual(jsonFile, "Avatar.json")) {
                    JSONObject jsonObj = jsonArray1.getJSONObject(i);
                    String var16 = jsonObj.getString("type");
                    Intrinsics.checkNotNullExpressionValue(var16, "jsonObj.getString(\"type\")");
                    String imageUrl = var16;
                    var16 = imageUrl.toLowerCase();
                    String var10001 = characterType.toLowerCase();
                    if (Intrinsics.areEqual(var16, var10001)) {
                        imageUrl = jsonObj.getString("imageURL");
                        Avatar avatar = new Avatar();
                        avatar.setResourceId(resoureId(context, imageUrl));
                        avatar.setImageURL(imageUrl);
                        avatar.setAvatarColor(jsonObj.getString("color"));
                        AvatarList.add(avatar);
                    }
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
        }

        return Intrinsics.areEqual(jsonFile, "Avatar.json") ? AvatarList : null;
    }

    public static int resoureId(@NotNull Context context, @Nullable String imageUrl) {

        Resources resources = context.getResources();

        try {
            return resources.getIdentifier(imageUrl, "drawable", context.getPackageName());
        } catch (Exception var5) {
            return resources.getIdentifier("ic_male", "drawable", context.getPackageName());
        }
    }

    @Nullable
    public static String loadJSONFromAsset(@NotNull Context context, @Nullable String jsonFile) {
        Intrinsics.checkNotNullParameter(context, "context");
        String json = (String) null;
        InputStream inputStream = (InputStream) null;
        boolean var14 = false;

        String var6;
        label143:
        {
            try {
                var14 = true;
                AssetManager var10000 = context.getAssets();
                Intrinsics.checkNotNull(jsonFile);
                inputStream = var10000.open(jsonFile);
                int size = inputStream.available();
                Log.i("Load", "----->" + size);
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                Charset var25 = Charset.defaultCharset();
                Intrinsics.checkNotNullExpressionValue(var25, "Charset.defaultCharset()");
                Charset var7 = var25;
                json = new String(buffer, var7);
                var14 = false;
                break label143;
            } catch (IOException var18) {
                var18.printStackTrace();
                var6 = null;
                var14 = false;
            } finally {
                if (var14) {
                    boolean var22 = inputStream != null;

                    try {

                        inputStream.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }

                }
            }


            try {

                inputStream.close();
            } catch (IOException var16) {
                var16.printStackTrace();
            }

            return var6;
        }


        try {

            inputStream.close();
        } catch (IOException var17) {
            var17.printStackTrace();
        }

        return json;

    }

    public static void show(@Nullable Context context, @Nullable String s) {
        Toast.makeText(context, (CharSequence) s, Toast.LENGTH_LONG).show();
    }

    @NotNull
    public static String longToDateyyyymmdd(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return date == 0L ? "" : formatter.format(new Date(date)).toString();
    }

    @NotNull
    public static String longToDateddmmyyyy(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return date == 0L ? "" : formatter.format(new Date(date)).toString();
    }

    public static void getTrialResult(
            List<TestResult> testResult,
            int trialCount,
            Context context,
            int devicetype,
            String testType,
            String sessionScore,
            String suggestedDiagnosis,
            List<Variance> variance
    ) {

        List<TrialResult> listTrialResult = new ArrayList<TrialResult>();
        AirTestResult airTestResult = new AirTestResult();
        for (int i = 0; i < testResult.size(); i++) {
            List<AirGraphData> airGraphDataList = new ArrayList();


            TrialResult trialResults = new TrialResult();
            for (FlowVolumeData airgraphdata : testResult.get(i).getGraphPoints()) {
                AirGraphData airGraphData = new AirGraphData();
                airGraphData.setVolume(airgraphdata.getVolume());
                airGraphData.setFlow(airgraphdata.getFlow());
                airGraphData.setSecond(airgraphdata.getSecond());
                airGraphData.setDirection(airgraphdata.getDirection());

                airGraphDataList.add(airGraphData);
            }
            List<TestMeasurements> testMeasurementsList = new ArrayList();
            for (ResultData measuredValues : testResult.get(i).getMeasuredValues()) {
                TestMeasurements testMeasurement = new TestMeasurements();
                testMeasurement.setMeasurement(measuredValues.getMeasurement());
                testMeasurement.setMeasuredValue(measuredValues.getMeasuredValue());
                testMeasurement.setUnit(measuredValues.getUnit());
                //if(measuredValues.predicted != " -  ")
                testMeasurement.setPredictedValue(measuredValues.getPredicted());
                testMeasurement.setLln(measuredValues.getLLN());
                testMeasurement.setUln(measuredValues.getULN());
                testMeasurement.setzScore(measuredValues.getZScore());
                testMeasurement.setPredictedPer(measuredValues.getPredictedPer());
                testMeasurementsList.add(testMeasurement);
            }


            TrialResult trialResult = new TrialResult();
            trialResult.setGraphDataList(airGraphDataList);
            trialResult.setMesurementlist(testMeasurementsList);
            trialResult.setBest(testResult.get(i).isBest());
            trialResult.setPost(testResult.get(i).isPost());
            if (trialResult.getPost()) {


                AirTestResult airTestResult1 = new AirTestResult();
                  /*  if (SafeyApplication.postTestResult==null){
                        if (trialCount-1 == i) {
                            Constants.postTrialCount = i*/
                Date currentDate = new Date();
                String time = new SimpleDateFormat("hh:mm a").format(currentDate);
                String date = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN).format(currentDate);
                Constants.isPost = true;
                trialResult.setPost(true);
                List<TrialResult> postTrialResult = new ArrayList<TrialResult>();
                postTrialResult.add(trialResult);
                airTestResult1.setTrialResult(postTrialResult);
                airTestResult1.setCreationDate(System.currentTimeMillis());
                airTestResult1.setCreatedDate(date);
                airTestResult1.setCreatedAt(new Date());
                airTestResult1.setTesttime(time);
                airTestResult1.setType(devicetype);
                airTestResult1.setSessionScore(sessionScore);
                airTestResult1.setSuggestedDiagnosis(suggestedDiagnosis);
                switch (testType) {
                    case "FEVC": {
                        airTestResult1.setTesttype(1);
                        break;
                    }
                    case "SVC": {
                        airTestResult1.setTesttype(2);
                        break;
                    }
                    case "FVL": {
                        airTestResult1.setTesttype(3);
                        break;
                    }
                    case "MVV": {
                        airTestResult1.setTesttype(4);
                        break;
                    }
                    default: {
                        airTestResult1.setTesttype(1);
                        break;
                    }

                }
                HealthCheckup.postTestResult = airTestResult1;
                        /*}
                        else
                            listTrialResult.add(trialResult)
                    }
                    else*/
                {
                    if (i == Constants.postTrialCount) {
                        //Constants.isPost = true
                    } else
                        listTrialResult.add(trialResult);
                }
            } else {
                listTrialResult.add(trialResult);
                Constants.isPost = false;
            }

        }

        List<Variance> listVariance = new ArrayList();
        for (Variance varance : variance) {
            listVariance.add(new Variance(varance.getMeasurement(), varance.getMeasurementValue(), varance.getPercentage()));
        }
        Date currentDate = new Date();
        String time = new SimpleDateFormat("hh:mm a").format(currentDate);
        String date = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN).format(currentDate);
        airTestResult.setCreatedDate(date);
        airTestResult.setTesttime(time);
        airTestResult.setTrialResult(listTrialResult);
        airTestResult.setCreatedAt(new Date());
        airTestResult.setCreationDate(System.currentTimeMillis());
        airTestResult.setType(devicetype);
        airTestResult.setSessionScore(sessionScore);
        airTestResult.setSuggestedDiagnosis(suggestedDiagnosis);
        switch (testType) {
            case "FEVC": {
                airTestResult.setTesttype(1);
                break;
            }
            case "SVC": {
                airTestResult.setTesttype(2);
                break;
            }
            case "FVL": {
                airTestResult.setTesttype(3);
                break;
            }
            case "MVV": {
                airTestResult.setTesttype(4);
                break;
            }
            default: {
                airTestResult.setTesttype(1);
                break;
            }
        }

        airTestResult.setVariance(listVariance);
        //airTestResult.type = testResult[0].testType

        HealthCheckup.firstTestResult = airTestResult;

//         if (HealthCheckup.postTestResult!=null)
//             NavHostFragment.findNavController(context).navigate(HomeFagmentDirections.actionGlobalTestResultsFragment(trialCount-1));
//        else
//             NavHostFragment.findNavController(context).navigate(HomeFagmentDirections.actionGlobalTestResultsFragment(trialCount));


    }

//    public static void getTrialResult(@NotNull List testResult, int trialCount, @NotNull Fragment context, int devicetype, @NotNull String testType, @NotNull String sessionScore, @NotNull String suggestedDiagnosis, @NotNull List variance) {
//
//         List listTrialResult = new ArrayList();
//        AirTestResult airTestResult = new AirTestResult();
//
//
//        Integer var10001;
//        for(int i =0;i<testResult.size(); i++) {
//            List airGraphDataList = new ArrayList();
//            new TrialResult();
//            Iterator var16 = ((TestResult)testResult.get(i)).getGraphPoints().iterator();
//
//            while(var16.hasNext()) {
//                FlowVolumeData airgraphdata = (FlowVolumeData)var16.next();
//                AirGraphData airGraphData = new AirGraphData();
//                airGraphData.setVolume(airgraphdata.getVolume());
//                airGraphData.setFlow(airgraphdata.getFlow());
//                airGraphData.setSecond(airgraphdata.getSecond());
//                airGraphData.setDirection(airgraphdata.getDirection());
//                airGraphDataList.add(airGraphData);
//            }
//
//            List testMeasurementsList = (List)(new ArrayList());
//            Iterator var32 = ((TestResult)testResult.get(i)).getMeasuredValues().iterator();
//
//            while(var32.hasNext()) {
//                ResultData measuredValues = (ResultData)var32.next();
//                TestMeasurements testMeasurement = new TestMeasurements();
//                testMeasurement.setMeasurement(measuredValues.getMeasurement());
//                testMeasurement.setMeasuredValue(measuredValues.getMeasuredValue());
//                testMeasurement.setUnit(measuredValues.getUnit());
//                testMeasurement.setPredictedValue(measuredValues.getPredicted());
//                testMeasurement.setLln(measuredValues.getLLN());
//                testMeasurement.setUln(measuredValues.getULN());
//                testMeasurement.setzScore(measuredValues.getZScore());
//                testMeasurement.setPredictedPer(measuredValues.getPredictedPer());
//                testMeasurementsList.add(testMeasurement);
//            }
//
//            final TrialResult trialResult = new TrialResult();
//            trialResult.setGraphDataList(airGraphDataList);
//            trialResult.setMesurementlist(testMeasurementsList);
//            trialResult.setBest(((TestResult)testResult.get(i)).isBest());
//            trialResult.setPost(((TestResult)testResult.get(i)).isPost());
//            if (!trialResult.getPost()) {
//                listTrialResult.add(trialResult);
//                Constants.isPost = (false);
//            } else {
//                AirTestResult airTestResult1;
//                label93: {
//                    airTestResult1 = new AirTestResult();
//                    Date currentDate = new Date();
//                    String time = (new SimpleDateFormat("hh:mm a")).format(currentDate);
//                    String date = (new SimpleDateFormat("yyyy-MM-dd")).format(currentDate);
//                    Constants.isPost =(true);
//                    trialResult.setPost(true);
//                    List postTrialResult = (List)(new ArrayList());
//                    postTrialResult.add(trialResult);
//                    airTestResult1.setTrialResult(postTrialResult);
//                    airTestResult1.setCreationDate(System.currentTimeMillis());
//                    airTestResult1.setCreatedDate(date);
//                    airTestResult1.setCreatedAt(new Date());
//                    airTestResult1.setTesttime(time);
//                    airTestResult1.setType(devicetype);
//                    airTestResult1.setSessionScore(sessionScore);
//                    airTestResult1.setSuggestedDiagnosis(suggestedDiagnosis);
//                    switch(testType.hashCode()) {
//                        case 70012:
//                            if (testType.equals("FVL")) {
//                                var10001 = 3;
//                                break label93;
//                            }
//                            break;
//                        case 76749:
//                            if (testType.equals("MVV")) {
//                                var10001 = 4;
//                                break label93;
//                            }
//                            break;
//                        case 82496:
//                            if (testType.equals("SVC")) {
//                                var10001 = 2;
//                                break label93;
//                            }
//                            break;
//                        case 2154412:
//                            if (testType.equals("FEVC")) {
//                                var10001 = 1;
//                                break label93;
//                            }
//                    }
//
//                    var10001 = 1;
//                }
//
//                airTestResult1.setTesttype(var10001);
//                SafeyApplication.postTestResult =(airTestResult1);
//
//            }
//        }
//
//        List listVariance = (List)(new ArrayList());
//        Iterator var26 = variance.iterator();
//
//        while(var26.hasNext()) {
//            Variance varance = (Variance)var26.next();
//            listVariance.add(new com.safey.lungmonitoring.data.tables.patient.Variance(varance.getMeasurement(), varance.getMeasurementValue(), varance.getPercentage()));
//        }
//
//        label61: {
//            Date currentDate = new Date();
//            String time = (new SimpleDateFormat("hh:mm a")).format(currentDate);
//            String date = (new SimpleDateFormat("yyyy-MM-dd")).format(currentDate);
//            airTestResult.setCreatedDate(date);
//            airTestResult.setTesttime(time);
//            airTestResult.setTrialResult(listTrialResult);
//            airTestResult.setCreatedAt(new Date());
//            airTestResult.setCreationDate(System.currentTimeMillis());
//            airTestResult.setType(devicetype);
//            airTestResult.setSessionScore(sessionScore);
//            switch(testType.hashCode()) {
//                case 70012:
//                    if (testType.equals("FVL")) {
//                        var10001 = 3;
//                        break label61;
//                    }
//                    break;
//                case 76749:
//                    if (testType.equals("MVV")) {
//                        var10001 = 4;
//                        break label61;
//                    }
//                    break;
//                case 82496:
//                    if (testType.equals("SVC")) {
//                        var10001 = 2;
//                        break label61;
//                    }
//                    break;
//                case 2154412:
//                    if (testType.equals("FEVC")) {
//                        var10001 = 1;
//                        break label61;
//                    }
//            }
//
//            var10001 = 1;
//        }
//
//        airTestResult.setTesttype(var10001);
//        airTestResult.setVariance(listVariance);
//        SafeyApplication.firstTestResult = (airTestResult);
//          if (SafeyApplication.postTestResult!=null)
//              NavHostFragment.findNavController(context).navigate(HomeFagmentDirections.actionGlobalTestResultsFragment(trialCount-1));
//        else
//              NavHostFragment.findNavController(context).navigate(HomeFagmentDirections.actionGlobalTestResultsFragment(trialCount));
//
//
//    }

    public static void showToast(@NotNull Context context, @NotNull String s) {
        Toast.makeText(context, (CharSequence) s, Toast.LENGTH_LONG).show();
    }


    public static void hideSoftKeyBoard(@NotNull Context context, @NotNull View view) {

        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static boolean isNetworkAvailable(@NotNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = (NetworkInfo) null;
        activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }


    static {
        Utility var0 = new Utility();
        INSTANCE = var0;
        sdformat = new SimpleDateFormat("yyyy-MM-dd");
    }


    public static enum ReportType {
        PDF,
        CSV;
    }


    public static enum ResponseType {
        ChangeMobile,
        ChangeEmail,
        verifyMobile,
        verifyEmail,
        sendEmailOTP;
    }

    public static enum TestType {
        FEVC,
        FIVC;
    }


    public static enum ReportRangeType {
        WEEK,
        LASTWEEK,
        THISMONTH,
        LASTMONTH,
        LAST3MONTH,
        LAST6MONTH;
    }


    public static class VerticalImageSpan extends ImageSpan {

        public VerticalImageSpan(Drawable drawable) {
            super(drawable);
        }

        /**
         * update the text line height
         */
        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.descent - fmPaint.ascent;
                int drHeight = rect.bottom - rect.top;
                int centerY = fmPaint.ascent + fontHeight / 2;

                fontMetricsInt.ascent = centerY - drHeight / 2;
                fontMetricsInt.top = fontMetricsInt.ascent;
                fontMetricsInt.bottom = centerY + drHeight / 2;
                fontMetricsInt.descent = fontMetricsInt.bottom;
            }
            return rect.right;
        }

        /**
         * see detail message in android.text.TextLine
         *
         * @param canvas the canvas, can be null if not rendering
         * @param text   the text to be draw
         * @param start  the text start position
         * @param end    the text end position
         * @param x      the edge of the replacement closest to the leading margin
         * @param top    the top of the line
         * @param y      the baseline
         * @param bottom the bottom of the line
         * @param paint  the work paint
         */
        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {

            Drawable drawable = getDrawable();
            canvas.save();
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;
            int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

    }
}
