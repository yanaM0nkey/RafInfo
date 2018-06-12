package com.yanazenkevich.rafinfo.tabs.vacancies;

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
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
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
    private RequestRelation requestRelation;
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
    private TextInputLayout tlEmployer;

    public static VacancyAddFragment newInstance(Vacancy vacancy, RequestRelation requestRelation){
        VacancyAddFragment  fragment = new VacancyAddFragment();
        fragment.requestRelation = requestRelation;
        fragment.vacancy = vacancy;
        return fragment;
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
        tlEmployer = view.findViewById(R.id.fav_employer_layout);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.vacancy_new);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new VacancyNewUseCase();
        relationUseCase = new VacancyRelationUseCase();
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setVacancy();
        etEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout,
                        ListEmployersFragment.newInstance(vacancy, requestRelation, true));
            }
        });
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
                    newVacancy(getContext(), getBaseActivity());
                }
            }
        });
    }

    private void newVacancy(final Context context, final AppCompatActivity activity) {
        useCase.execute(vacancy, new DisposableObserver<Vacancy>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Vacancy vacancy) {
                requestRelation.setObjectId(vacancy.getId());
                newRelation(context, activity);
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

    private void newRelation(final Context context, final AppCompatActivity activity){
        relationUseCase.execute(requestRelation, new DisposableObserver<Integer>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Integer response) {
                showProgress(false);
                if(response == 1){
                    NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                            VacanciesFragment.newInstance());
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(context, e);
            }

            @Override
            public void onComplete() {
                relationUseCase.dispose();
            }
        });
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

    private void setVacancy(){
        etTitle.setText(vacancy.getName());
        etDescription.setText(vacancy.getDescription());
        etLocation.setText(vacancy.getLocation());
        etContactInfo.setText(vacancy.getContactInfo());
        if(vacancy.getEmployer().getName() != null){
            etEmployer.setText(vacancy.getEmployer().getName());
        }

    }

    private boolean validate(Context context){
        if (TextUtils.isEmpty(vacancy.getName())) {
            tlTitle.setError(context.getString(R.string.vacancy_error_title_empty));
            tlLocation.setError(null);
            tlDescription.setError(null);
            tlContactInfo.setError(null);
            tlEmployer.setError(null);
            etTitle.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(vacancy.getLocation())){
            tlTitle.setError(null);
            tlLocation.setError(context.getString(R.string.vacancy_error_address_empty));
            tlDescription.setError(null);
            tlContactInfo.setError(null);
            tlEmployer.setError(null);
            etLocation.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(vacancy.getContactInfo())){
            tlTitle.setError(null);
            tlLocation.setError(null);
            tlDescription.setError(null);
            tlContactInfo.setError(context.getString(R.string.vacancy_error_contact_info_empty));
            tlEmployer.setError(null);
            etContactInfo.requestFocus();
            return false;
        }
        if(vacancy.getEmployer() == null || TextUtils.isEmpty(vacancy.getEmployer().getName())){
            tlTitle.setError(null);
            tlLocation.setError(null);
            tlDescription.setError(null);
            tlContactInfo.setError(null);
            tlEmployer.setError(context.getString(R.string.vacancy_error_employer_empty));
            etEmployer.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(vacancy.getDescription())){
            tlTitle.setError(null);
            tlLocation.setError(null);
            tlDescription.setError(context.getString(R.string.vacancy_error_description_empty));
            tlContactInfo.setError(null);
            tlEmployer.setError(null);
            etDescription.requestFocus();
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
                    NavigationUtils.removeFragment(getBaseActivity(), VacancyAddFragment.this);
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