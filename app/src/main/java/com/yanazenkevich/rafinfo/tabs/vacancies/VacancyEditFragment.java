package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import com.yanazenkevich.rafinfo.entities.Relation;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.interactions.VacancyDeleteUseCase;
import com.yanazenkevich.rafinfo.interactions.VacancyEditUseCase;
import com.yanazenkevich.rafinfo.interactions.VacancyRelationUseCase;
import com.yanazenkevich.rafinfo.tabs.announcement.AnnouncementFragment;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

public class VacancyEditFragment extends BaseFragment {

    private VacancyEditUseCase useCase;
    private VacancyDeleteUseCase deleteUseCase;
    private VacancyRelationUseCase relationUseCase;
    private Vacancy vacancy;
    private RequestRelation requestRelation;
    private View vProgress;
    private TextView tvSave;
    private TextView tvDelete;
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

    public static VacancyEditFragment newInstance(Vacancy vacancy, RequestRelation requestRelation){
        VacancyEditFragment  fragment = new VacancyEditFragment();
        fragment.requestRelation = requestRelation;
        fragment.vacancy = vacancy;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_vacancy, container, false);
        vProgress = view.findViewById(R.id.fev_progress_bar);
        tvSave = view.findViewById(R.id.fev_save_button);
        tvDelete = view.findViewById(R.id.fev_delete_button);
        etTitle = view.findViewById(R.id.fev_title);
        etLocation = view.findViewById(R.id.fev_address);
        etDescription = view.findViewById(R.id.fev_description);
        etContactInfo = view.findViewById(R.id.fev_contact);
        tlTitle = view.findViewById(R.id.fev_title_layout);
        tlLocation = view.findViewById(R.id.fev_address_layout);
        tlDescription = view.findViewById(R.id.fev_description_layout);
        tlContactInfo = view.findViewById(R.id.fev_contact_layout);
        etEmployer = view.findViewById(R.id.fev_employer);
        tlEmployer = view.findViewById(R.id.fev_employer_layout);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.vacancy_edit);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new VacancyEditUseCase();
        deleteUseCase = new VacancyDeleteUseCase();
        relationUseCase = new VacancyRelationUseCase();
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setVacancy();
        etEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout,
                        ListEmployersFragment.newInstance(vacancy, requestRelation, false));
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
                    saveVacancy(getContext(), getBaseActivity());
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                createDialog(getContext());
            }
        });
    }

    private void createDialog(final Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(getString(R.string.vacancy_delete));
        dialog.setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                showProgress(true);
                deleteVacancy(getContext(), getBaseActivity());
            };
        });
        dialog.setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteVacancy(final Context context, final AppCompatActivity activity) {
        deleteUseCase.execute(vacancy.getId(), new DisposableObserver<Vacancy>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Vacancy vacancy) {
                showProgress(false);
                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                        VacanciesFragment.newInstance());
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(context, e);
            }

            @Override
            public void onComplete() {
                deleteUseCase.dispose();
            }
        });
    }

    private void saveVacancy(final Context context, final AppCompatActivity activity) {
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
        if(requestRelation.getmRelation() == null){
            Relation relation = new Relation();
            relation.setObjectId(vacancy.getEmployer().getId());
            requestRelation.setmRelation(relation);
        }
        relationUseCase.execute(requestRelation, new DisposableObserver<Integer>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Integer response) {
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
        if(vacancy.getEmployer() != null && vacancy.getEmployer().getName() != null){
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
                    NavigationUtils.removeFragment(getBaseActivity(), VacancyEditFragment.this);
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
