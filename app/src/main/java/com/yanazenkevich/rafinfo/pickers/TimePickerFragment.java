package com.yanazenkevich.rafinfo.pickers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.yanazenkevich.rafinfo.listeners.TimePickerListener;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerListener listener;

    private int hour;
    private int minute;

    public static TimePickerFragment newInstance(TimePickerListener listener){
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        listener.onTimeSelected();
    }

    public String getSelectedTime(){
        return formatTime(hour) + ":" + formatTime(minute);
    }

    private String formatTime(int time){
        String selected;
        if(time == 0){
            selected = "00";
        }else{
            selected = time < 10 ? "0"+String.valueOf(time) : String.valueOf(time);
        }
        return selected;
    }
}
