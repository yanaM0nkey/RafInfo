package com.yanazenkevich.rafinfo.tabs.announcement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.interactions.AnnouncementNewUseCase;
import com.yanazenkevich.rafinfo.listeners.DatePickerListener;
import com.yanazenkevich.rafinfo.listeners.TimePickerListener;
import com.yanazenkevich.rafinfo.pickers.DatePickerFragment;
import com.yanazenkevich.rafinfo.pickers.TimePickerFragment;
import com.yanazenkevich.rafinfo.utils.DateUtils;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

public class AnnouncementAddFragment extends BaseFragment implements DatePickerListener, TimePickerListener {

    private AnnouncementNewUseCase useCase;
    private Announcement announcement;
    private View vProgress;
    private TextView tvSave;
    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etLocation;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputLayout tlTitle;
    private TextInputLayout tlLocation;
    private TextInputLayout tlDate;
    private TextInputLayout tlTime;
    private DatePickerFragment datePicker;
    private TimePickerFragment timePicker;

    public static AnnouncementAddFragment newInstance(){
        return new AnnouncementAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);
        vProgress = view.findViewById(R.id.faea_progress_bar);
        tvSave = view.findViewById(R.id.faea_save_button);
        etTitle = view.findViewById(R.id.faea_title);
        etLocation = view.findViewById(R.id.faea_location);
        etDate = view.findViewById(R.id.faea_date);
        etTime = view.findViewById(R.id.faea_time);
        etDescription = view.findViewById(R.id.faea_description);
        tlTitle = view.findViewById(R.id.faea_title_layout);
        tlLocation = view.findViewById(R.id.faea_location_layout);
        tlDate = view.findViewById(R.id.faea_date_layout);
        tlTime = view.findViewById(R.id.faea_time_layout);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.announcement_new);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new AnnouncementNewUseCase();
        announcement = new Announcement();
        datePicker = DatePickerFragment.newInstance(this);
        timePicker = TimePickerFragment.newInstance(this);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                checkData();
                if (announcement != null && validate(getContext())) {
                    showProgress(true);
                    tlTitle.setError(null);
                    tlLocation.setError(null);
                    tlDate.setError(null);
                    tlTime.setError(null);
                    newAnnouncement(getContext(), getBaseActivity());
                }
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getBaseActivity().getFragmentManager(), "datePicker");
            }
        });
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getBaseActivity().getFragmentManager(), "timePicker");
            }
        });
    }

    private void newAnnouncement(final Context context, final AppCompatActivity activity) {
        useCase.execute(announcement, new DisposableObserver<Announcement>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Announcement announcement) {
                showProgress(false);
                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                        AnnouncementFragment.newInstance());
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(context, e);
            }

            @Override
            public void onComplete() {
                useCase.dispose();
            }
        });
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void checkData(){
        announcement.setTitle(etTitle.getText().toString().trim());
        announcement.setLocation(etLocation.getText().toString().trim());
        announcement.setDescription(etDescription.getText().toString().trim());
        announcement.setDate(getDate());
    }

    private long getDate(){
        return DateUtils.getDateInMS(etDate.getText().toString() + " " + etTime.getText().toString());
    }

    private boolean validate(Context context){
        if (TextUtils.isEmpty(announcement.getTitle())) {
            tlTitle.setError(context.getString(R.string.announcement_error_title_empty));
            tlLocation.setError(null);
            tlDate.setError(null);
            tlTime.setError(null);
            etTitle.requestFocus();
            return false;
        }
        if(announcement.getDate() == 0 && TextUtils.isEmpty(etDate.getText().toString().trim())){
            tlTitle.setError(null);
            tlLocation.setError(null);
            tlDate.setError(context.getString(R.string.announcement_error_date_empty));
            tlTime.setError(null);
            etDate.requestFocus();
            return false;
        }
        if(announcement.getDate() == 0 && TextUtils.isEmpty(etTime.getText().toString().trim())){
            tlTitle.setError(null);
            tlLocation.setError(null);
            tlDate.setError(null);
            tlTime.setError(context.getString(R.string.announcement_error_time_empty));
            etTime.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(announcement.getLocation())){
            tlTitle.setError(null);
            tlLocation.setError(context.getString(R.string.announcement_error_location_empty));
            tlDate.setError(null);
            tlTime.setError(null);
            etLocation.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onDateSelected(int day, int month, int year) {
        etDate.setText(DateUtils.getSelectedDate(day, month, year));
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        etTime.setText(DateUtils.getSelectedTime(hour, minute));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && announcement != null) {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    getBaseActivity().onBackPressed();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getBaseActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null && getBaseActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getBaseActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
