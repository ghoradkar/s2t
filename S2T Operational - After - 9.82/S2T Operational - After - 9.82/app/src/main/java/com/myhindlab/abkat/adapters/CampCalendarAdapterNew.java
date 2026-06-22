package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampCalenderDatesModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CampCalendarAdapterNew extends BaseAdapter {

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat format2 = new SimpleDateFormat("dd");
    private final int FIRST_DAY_OF_WEEK = 0;
    private Calendar selectedDate;
    private Context context;
    private Calendar month;
    private static String[] days;
    private ArrayList<CampCalenderDatesModel> campDateCountList;

    public CampCalendarAdapterNew(Context context, Calendar month, ArrayList<CampCalenderDatesModel> campDateCountList) {
        this.month = month;
        this.context = context;
        selectedDate = (Calendar) month.clone();
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.campDateCountList = campDateCountList;
        refreshDays();
    }

    public void setItems(ArrayList<CampCalenderDatesModel> campDateCountList) {
        this.campDateCountList = campDateCountList;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView, patientCount;
        CardView cv_dateview;
        LinearLayout ll_dateitem;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_camp_days, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);
        patientCount = (TextView) v.findViewById(R.id.patientCount);
        cv_dateview = v.findViewById(R.id.cv_dateview);
        ll_dateitem = v.findViewById(R.id.ll_dateitem);

        // disable empty days from the beginning
        if (days[position].equals("")) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
            patientCount.setText("");
            v.setBackgroundResource(R.color.white);
            cv_dateview.setVisibility(View.GONE);
        }

        String date = days[position];
        dayView.setText(date);

        for (int i = 0; i < campDateCountList.size(); i++) {
            if (!date.isEmpty())
                if (Integer.parseInt(campDateCountList.get(i).getDay()) == Integer.parseInt(date)) {
                    patientCount.setText(campDateCountList.get(i).getCampCount() + "");
//                    cv_dateview.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
                    try {
                        if (new SimpleDateFormat("yyyy-MM-dd").parse(campDateCountList.get(i).getDate()).after(Calendar.getInstance().getTime())) {
                            ll_dateitem.setBackgroundColor(context.getResources().getColor(R.color.dashb2));
                        } else {
                            ll_dateitem.setBackgroundColor(context.getResources().getColor(R.color.color1));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
        }

        return v;
    }

    public void refreshDays() {

        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) month.get(Calendar.DAY_OF_WEEK);

        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        } else {
            days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                days[j] = "";
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days[j] = "";
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1;
        }

        // populate days
        int dayNumber = 1;
        for (int i = j - 1; i < days.length; i++) {
            days[i] = "" + dayNumber;
            dayNumber++;
        }
    }

    public static String[] getDate() {
        return days;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
