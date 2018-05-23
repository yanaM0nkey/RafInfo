package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.interactions.VacancyNewUseCase;
import com.yanazenkevich.rafinfo.interactions.VacancyRelationUseCase;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

public class VacancyAddFragment extends BaseFragment {

    private VacancyNewUseCase useCase;
    private VacancyRelationUseCase relationUseCase;
    private Vacancy vacancy;
    private String vacancyId;
    private View vProgress;
    private TextView tvSave;
    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etLocation;
    private TextInputEditText etContactInfo;
    private TextInputLayout tlTitle;
    private TextInputLayout tlDescription;
    private TextInputLayout tlLocation;
    private TextInputLayout tlContactInfo;
    private TextInputEditText etEmployer;

    public static VacancyAddFragment newInstance(){
        return new VacancyAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vacancy, container, false);
        vProgress = view.findViewById(R.id.fav_progress_bar);
        tvSave = view.findViewById(R.id.fav_save_button);
        etTitle = view.findViewById(R.id.fav_title);
        etLocation = view.findViewById(R.id.fav_address);
        etDescription = view.findViewById(R.id.fav_description);
        etContactInfo = view.findViewById(R.id.fav_contact);
        tlTitle = view.findViewById(R.id.fav_title_layout);
        tlLocation = view.findViewById(R.id.fav_address_layout);
        tlDescription = view.findViewById(R.id.fav_description_layout);
        tlContactInfo = view.findViewById(R.id.fav_contact_layout);
        etEmployer = view.findViewById(R.id.fav_employer);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.announcement_new);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new VacancyNewUseCase();
        vacancy = new Vacancy();
        relationUseCase = new VacancyRelationUseCase();
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                checkData();
                if (vacancy != null && validate(getContext())) {
                    showProgress(true);
                    tlTitle.setError(null);
                    tlLocation.setError(null);
                    tlDescription.setError(null);
                    tlContactInfo.setError(null);
                    newVacancy(getContext());
                    newRelation(getContext(), getBaseActivity());
                }
            }
        });
    }

    private void newVacancy(final Context context) {
        useCase.execute(vacancy, new DisposableObserver<Vacancy>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Vacancy vacancy) {
                vacancyId = vacancy.getId();
//                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
//                        VacanciesFragment.newInstance());
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

    private void newRelation(final Context context, final Activity activity){
        relationUseCase.execute(vacancy, new DisposableObserver<Vacancy>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Vacancy vacancy) {
                vacancyId = vacancy.getId();
//                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
//                        VacanciesFragment.newInstance());
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
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void checkData(){
        vacancy.setName(etTitle.getText().toString().trim());
        vacancy.setLocation(etLocation.getText().toString().trim());
        vacancy.setDescription(etDescription.getText().toString().trim());
        vacancy.setContactInfo(etContactInfo.getText().toString().trim());
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
            tlLocation.setError(context.getString(R.string.announcement_error_time_empty));
            tlDate.setError(null);
            tlTime.setError(null);
            etLocation.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && vacancy != null) {
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
        inputMethodManager.hideSoftInputFromWindow(getBaseActivity().getCurrentFocus().getWindowToken(), 0);
    }
}