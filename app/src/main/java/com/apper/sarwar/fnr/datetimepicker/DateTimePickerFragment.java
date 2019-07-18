package com.apper.sarwar.fnr.datetimepicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;


import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "Sign Up";
    private int _day;
    private int _month;
    private int _birthYear;
    DatePickerIService datePickerIService;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePickerIService = (DatePickerIService) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        try {
            _birthYear = year;
            _month = month;
            _day = dayOfMonth;
            updateDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
        }

    }

    private void updateDisplay() {
        String dateStr = new StringBuilder()
                .append(_day).append("-").append(_month + 1).append("-").append(_birthYear).append(" ").toString();
        datePickerIService.getDate(dateStr);
       /* _editText.setText(new StringBuilder()
                .append(_birthYear).append("-").append(_month + 1).append("-").append(_day).append(" "));*/
    }
}
