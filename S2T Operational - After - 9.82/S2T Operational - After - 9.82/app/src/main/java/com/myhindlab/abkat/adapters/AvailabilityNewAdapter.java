package com.myhindlab.abkat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myhindlab.abkat.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AvailabilityNewAdapter extends BaseAdapter {
    static final int FIRST_DAY_OF_WEEK = 0;
    private Context mContext;
    private Calendar month;
    private Calendar selectedDate;
    private ArrayList<String> UserAttendanceitems;
    private ArrayList<String> isInOutMarked;


    String temp = "";

    public AvailabilityNewAdapter(Context c, Calendar monthCalendar) {
        month = monthCalendar;
        selectedDate = (Calendar) monthCalendar.clone();
        mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.UserAttendanceitems = new ArrayList<String>();
        this.isInOutMarked = new ArrayList<String>();

        refreshDays();
    }

    public void setItems(ArrayList<String> UserAttendanceitems,
                         ArrayList<String> inoutItem) {

        for (int i = 0; i != UserAttendanceitems.size(); i++) {
            if (UserAttendanceitems.get(i).length() == 1) {
                UserAttendanceitems.set(i, "0" + UserAttendanceitems.get(i));
            }
        }
        this.UserAttendanceitems = UserAttendanceitems;


        for (int i = 0; i != inoutItem.size(); i++) {
            if (inoutItem.get(i).length() == 1) {
                inoutItem.set(i, "0" + inoutItem.get(i));
            }
        }

        this.isInOutMarked = inoutItem;


    }

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);

        // disable empty days from the beginning
        if (days[position].equals("")) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // mark current day as focused
            if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                    && month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                    && days[position].equals("" + selectedDate.get(Calendar.DAY_OF_MONTH))) {
                v.setBackgroundColor(Color.parseColor("#7e7e7e"));
            } else {
                v.setBackgroundResource(R.drawable.list_item_background);
                dayView.setClickable(false);
                dayView.setFocusable(false);
                dayView.setEnabled(false);
            }
        }
        dayView.setText(days[position]);
        String date = days[position];

        if (date.length() == 1) {
            date = "0" + date;
        }


        if (date.length() > 0 && UserAttendanceitems != null && UserAttendanceitems.contains(date)) {

            int index = UserAttendanceitems.indexOf(date);
            String inOutFlag = isInOutMarked.get(index);

            if ("01".equals(inOutFlag)) {
                v.setBackgroundColor(androidx.core.content.ContextCompat.getColor(mContext, R.color.chartyellow));
                dayView.setTextColor(Color.WHITE);

            } else if ("02".equals(inOutFlag)) {
                v.setBackgroundColor(androidx.core.content.ContextCompat.getColor(mContext, R.color.UserAttendance_green));
                dayView.setTextColor(Color.WHITE);
            }

        }


//        if (date.length() > 0 && UserAttendanceitems != null && UserAttendanceitems.contains(date)) {
//
//            v.setBackgroundColor(mContext.getResources().getColor(R.color.UserAttendance_green));
//            dayView.setTextColor(mContext.getResources().getColor(R.color.white));
//        }
//
//        if (date.length() > 0 && isInOutMarked != null && isInOutMarked.contains("1")) {
//
//            v.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
//            dayView.setTextColor(mContext.getResources().getColor(R.color.white));
//        }
//
//        if (date.length() > 0 && isInOutMarked != null && isInOutMarked.contains("2")) {
//
//            v.setBackgroundColor(mContext.getResources().getColor(R.color.green));
//            dayView.setTextColor(mContext.getResources().getColor(R.color.white));
//        }

        return v;
    }

    public void refreshDays() {
        UserAttendanceitems.clear();
        isInOutMarked.clear();

        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) month.get(Calendar.DAY_OF_WEEK);

        // figure size of the array
        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        } else {
            days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        // populate empty days before first real day
        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                days[j] = "";
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days[j] = "";
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        // populate days
        int dayNumber = 1;
        for (int i = j - 1; i < days.length; i++) {
            days[i] = "" + dayNumber;
            dayNumber++;
        }
    }

    // references to our items
    public String[] days;

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}