package com.myhindlab.abkat.utilities.safey;

import android.content.Context;
import android.widget.Toast;

import com.myhindlab.abkat.HealthCheckup;

import java.text.DecimalFormat;

public class Constants {
    public static String Id ="";
    public static String emailId="";
    public static String otp="123456";
    public  static String selectedavatar = "";
    public static Boolean fromAvatar = false;
    public static String AVATAR = "avatar";
    public static String AVATAR_JSON = "Avatar.json";
    public static String sympton = "sympton";
    public static Boolean isPost = false;
    public static String imageURL = "imageURL";
    public static float alphaBlur = 0.4F;
    public  static float alphaClear = 0.9F;
    public static String  DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public  static String  DAY_FORMAT_PATTERN = "dd";
    public  static DecimalFormat decimalFormat = new DecimalFormat("#.####");
    public  static DecimalFormat decimalTimeFormat = new DecimalFormat("#.##");
    public static int postTrialCount=0;
    public static int trialNo=0;


    public static void showToast(String msg, Context context){

        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public  static String getStringResourceById(int resId) {
        Context myContext = HealthCheckup.context;
        return myContext.getString(resId);
    }


}
