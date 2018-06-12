package com.yanazenkevich.rafinfo.tabs.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.interactions.EmployerNewUseCase;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

public class AddEmployerFragment extends BaseFragment {

    private EmployerNewUseCase useCase;
    private Employer employer;
    private View vProgress;
    private TextView tvSave;
    private TextInputEditText etEmail;
    private TextInputEditText etDescription;
    private TextInputLayout tlEmail;
    private TextInputLayout tlDescription;
    private TextInputEditText etEmployer;
    private TextInputLayout tlEmployer;

    public static AddEmployerFragment newInstance(Employer employer){
        AddEmployerFragment  fragment = new AddEmployerFragment();
        fragment.employer = employer;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_employer, container, false);
        vProgress = view.findViewById(R.id.fae_progress_bar);
        tvSave = view.findViewById(R.id.fae_save_button);
        etEmail = view.findViewById(R.id.fae_email);
        etDescription = view.findViewById(R.id.fae_description);
        tlEmail = view.findViewById(R.id.fae_email_layout);
        tlDescription = view.findViewById(R.id.fae_description_layout);
        etEmployer = view.findViewById(R.id.fae_title);
        tlEmployer = view.findViewById(R.id.fae_title_layout);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.settings_new_employer);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new EmployerNewUseCase();
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setEmployer();
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                checkData();
                if (employer != null && validate(getContext())) {
                    showProgress(true);
                    tlEmail.setError(null);
                    tlEmployer.setError(null);
                    tlDescription.setError(null);
                    newEmployer(getContext(), getBaseActivity());
                }
            }
        });
    }

    private void newEmployer(final Context context, final AppCompatActivity activity) {
        useCase.execute(employer, new DisposableObserver<Employer>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Employer employer) {
                showProgress(false);
                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                        EditEmployerInfoListFragment.newInstance());
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
        employer.setName(etEmployer.getText().toString().trim());
        employer.setSite(etEmail.getText().toString().trim());
        employer.setInfo(etDescription.getText().toString().trim());
    }

    private void setEmployer(){
        etEmployer.setText(employer.getName());
        etDescription.setText(employer.getInfo());
        etEmail.setText(employer.getSite());
    }

    private boolean validate(Context context){
        if (TextUtils.isEmpty(employer.getName())) {
            tlEmployer.setError(context.getString(R.string.settings_error_title_empty));
            tlEmail.setError(null);
            tlDescription.setError(null);
            etEmployer.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(employer.getSite())){
            tlEmail.setError(context.getString(R.string.settings_error_site_empty));
            tlDescription.setError(null);
            tlEmployer.setError(null);
            etEmail.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(employer.getInfo())){
            tlEmail.setError(null);
            tlDescription.setError(context.getString(R.string.settings_error_info_empty));
            tlEmployer.setError(null);
            etDescription.requestFocus();
            return false;
        }
        if(!TextUtils.isEmpty(employer.getSite()) && !Patterns.WEB_URL.matcher(employer.getSite()).matches()){
            tlDescription.setError(null);
            tlEmployer.setError(null);
            tlEmail.setError(context.getString(R.string.settings_wrong_email));
            etEmail.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && employer != null) {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    getBaseActivity().onBackPressed();
                    NavigationUtils.removeFragment(getBaseActivity(), AddEmployerFragment.this);
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
