package com.myhindlab.abkat.models.doortodoor;

import java.util.Calendar;

public class AgeCalculation {

    private int startYear;
    private int startMonth;
    private int startDay;

    private int resYear;
    private int resMonth;
    private int resDay;

    public void setDateOfBirth(int sYear, int sMonth, int sDay) {
        startYear = sYear;
        startMonth = sMonth;
        startDay = sDay;
    }

    public void calculateAge() {
        Calendar dob = Calendar.getInstance();
        dob.set(startYear, startMonth - 1, startDay); // Calendar months are 0-based

        Calendar today = Calendar.getInstance();

        resYear = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        resMonth = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        resDay = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

        // If day is negative, borrow days from previous month
        if (resDay < 0) {
            today.add(Calendar.MONTH, -1);
            int daysInPrevMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);
            resDay += daysInPrevMonth;
            resMonth--;
        }

        // If month is negative, borrow from year
        if (resMonth < 0) {
            resMonth += 12;
            resYear--;
        }
    }

    public String getResult() {
        return resDay + ":" + resMonth + ":" + resYear;
    }

    public String getResDay() {
        return String.valueOf(resDay);
    }

    public String getResMonth() {
        return String.valueOf(resMonth);
    }

    public String getResYear() {
        return String.valueOf(resYear);
    }
}
