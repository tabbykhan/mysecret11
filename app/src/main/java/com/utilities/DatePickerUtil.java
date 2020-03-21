package com.utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.app.ui.dialogs.MyDatePickerDialog;

import java.util.Calendar;

/**
 * Created by ubuntu on 31/3/18.
 */

public class DatePickerUtil {


    public static MyDatePickerDialog getDatePicker(String title,Context context, final View clickedView,
                                                 final OnDateSelectedListener onDateSelectedListener) {

        Calendar previousSelectedCal = null;
        Calendar now = Calendar.getInstance();
        if (clickedView.getTag() != null) {
            previousSelectedCal = (Calendar) clickedView.getTag();
        }

        MyDatePickerDialog datePickerDialog=new MyDatePickerDialog(title,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar now = Calendar.getInstance();
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, month);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                now.set(Calendar.HOUR_OF_DAY, 00);
                now.set(Calendar.MINUTE, 00);
                now.set(Calendar.SECOND, 00);
                now.set(Calendar.MILLISECOND, 000);
                if (onDateSelectedListener != null)
                    onDateSelectedListener.onDateSelected(now);


            }
        }, previousSelectedCal == null ? now.get(Calendar.YEAR) : previousSelectedCal.get(Calendar.YEAR),
                previousSelectedCal == null ? now.get(Calendar.MONTH) : previousSelectedCal.get(Calendar.MONTH),
                previousSelectedCal == null ? now.get(Calendar.DAY_OF_MONTH) : previousSelectedCal.get(Calendar.DAY_OF_MONTH));

//        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                R.style.SpinnerDatePickerDialogTheme,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        Calendar now = Calendar.getInstance();
//                        now.set(Calendar.YEAR, year);
//                        now.set(Calendar.MONTH, month);
//                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        now.set(Calendar.HOUR_OF_DAY, 00);
//                        now.set(Calendar.MINUTE, 00);
//                        now.set(Calendar.SECOND, 00);
//                        now.set(Calendar.MILLISECOND, 000);
//                        if (onDateSelectedListener != null)
//                            onDateSelectedListener.onDateSelected(now);
//
//
//                    }
//                }, previousSelectedCal == null ? now.get(Calendar.YEAR) : previousSelectedCal.get(Calendar.YEAR),
//                previousSelectedCal == null ? now.get(Calendar.MONTH) : previousSelectedCal.get(Calendar.MONTH),
//                previousSelectedCal == null ? now.get(Calendar.DAY_OF_MONTH) : previousSelectedCal.get(Calendar.DAY_OF_MONTH));


        return datePickerDialog;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }
}
