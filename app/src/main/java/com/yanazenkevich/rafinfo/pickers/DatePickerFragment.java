package com.yanazenkevich.rafinfo.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.yanazenkevich.rafinfo.listeners.DatePickerListener;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerListener listener;

    private int year;
    private int month;
    private int day;

    public static DatePickerFragment newInstance(DatePickerListener listener){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
       this.year = year;
       this.month = month;
       this.day = day;
       listener.onDateSelected();
    }

    public String getSelectedDate(){
        return formatDate(day)+"."+formatDate(month+1)+"."+String.valueOf(year);
    }

    private String formatDate(int selected){
        return selected < 10 ? "0"+String.valueOf(selected) : String.valueOf(selected);
    }
}