package com.model;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * @author Manish Kumar
 * @since 2/9/17
 */


public class BaseModel {

    public static final int TAG_ONE = 1;
    public static final int TAG_TWO = 2;
    public static final int TAG_THREE = 3;
    public static final int TAG_FOUR = 4;
    public static final int TAG_FIVE = 5;

    public static final String DATE_TIME_ONE = "dd MMM, yyyy HH:mm";
    public static final String DATE_TIME_TWO = "E, MMM dd, hh:mm a";
    public static final String DATE_THREE = "dd-MM-yyyy";
    public static final String TIME_FOUR = "hh:mm a";
    public static final String TIME_FIVE = "dd MMM yyyy";

    public boolean isValidString(String data) {
        return data != null && !data.trim().isEmpty();
    }

    public String getValidString(String data) {
        return data == null ? "" : data;
    }

    public String getValidStringForBal(String data) {
        return data != null && !data.isEmpty() ? data : "0";
    }

    public boolean isValidList(List list) {
        return list != null && list.size() > 0;
    }

    /**
     * get valid decimal format
     *
     * @param value
     * @return
     */
    public String getValidDecimalFormat(String value) {
        if (!isValidString(value)) {
            return "0.00";
        }
        double netValue = Double.parseDouble(value);
        return getValidDecimalFormat(netValue);
    }

    public String getValidDecimalFormat(double value) {
        return String.format(Locale.ENGLISH, "%.2f", value);
    }

    /**
     * getValid Rating
     *
     * @param value
     * @return
     */
    public String getValidRatingFormat(String value) {
        if (!isValidString(value)) {
            return "0.0";
        }
        double netValue = Double.parseDouble(value);
        return getValidFormat(netValue);
    }

    public String getValidFormat(double value) {
        return String.format(Locale.ENGLISH, "%.1f", value);
    }

    /**
     * get formated date and time by format
     *
     * @param format
     * @param timestamp
     * @return
     */

    public String getFormattedCalendar(String format, long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        return simpleDateFormat.format(calendar.getTime());
    }


    public double getValidDecimalFormatDouble(double value) {
        try {
            String data = String.format(Locale.ENGLISH, "%.2f", value);
            return Double.parseDouble(data);
        } catch (NumberFormatException e) {

        }
        return 0.0;
    }

    public String getValidCurrencyFormat(double value) {
        double formattedValue = Double.parseDouble(getValidDecimalFormat(value));
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        format.setCurrency(Currency.getInstance("USD"));
        return format.format(formattedValue).replace("$", "");
    }

    public Calendar getLastMonthCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
