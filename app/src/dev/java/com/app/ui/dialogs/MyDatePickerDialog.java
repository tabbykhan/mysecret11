package com.app.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class MyDatePickerDialog extends AppBaseDialogFragment implements DatePicker.OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";


    TextView tv_title;
    DatePicker date_picker;
    TextView tv_cancel;
    TextView tv_ok;

    long maxDate = -1;

    String title;
    int year;
    int monthOfYear;
    int dayOfMonth;
    DatePickerDialog.OnDateSetListener onDateSetListener;


    public MyDatePickerDialog(String title, @Nullable DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        this.title = title;
        this.onDateSetListener = listener;
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    public void setOnDateSetListener(@Nullable DatePickerDialog.OnDateSetListener listener) {
        onDateSetListener = listener;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_date_picker;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }

    public DatePicker getDatePicker() {
        return date_picker;
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        date_picker.updateDate(year, month, dayOfMonth);
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = DeviceScreenUtil.getInstance().getWidth(0.70f);
        wlmp.dimAmount = 0.8f;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();


        tv_title = getView().findViewById(R.id.tv_title);
        tv_title.setText(title == null ? "" : title);
        date_picker = getView().findViewById(R.id.date_picker);
        date_picker.init(year, monthOfYear, dayOfMonth, this);


        tv_cancel = getView().findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

        tv_ok = getView().findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);

        if (maxDate != -1) {
            date_picker.setMaxDate(maxDate);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                date_picker.clearFocus();
                onDateSetListener.onDateSet(date_picker, date_picker.getYear(),
                        date_picker.getMonth(), date_picker.getDayOfMonth());
                dismiss();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
        date_picker.init(year, month, dayOfMonth, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (date_picker != null) {
            outState.putInt(YEAR, date_picker.getYear());
            outState.putInt(MONTH, date_picker.getMonth());
            outState.putInt(DAY, date_picker.getDayOfMonth());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && date_picker != null) {
            final int year = savedInstanceState.getInt(YEAR);
            final int month = savedInstanceState.getInt(MONTH);
            final int day = savedInstanceState.getInt(DAY);
            date_picker.init(year, month, day, this);
        }
    }
}
