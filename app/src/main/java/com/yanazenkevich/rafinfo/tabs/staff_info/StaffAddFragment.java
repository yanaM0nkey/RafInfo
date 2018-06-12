package com.yanazenkevich.rafinfo.tabs.staff_info;

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
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.interactions.StaffNewUseCase;
import com.yanazenkevich.rafinfo.signup.DepartmentFragment;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

public class StaffAddFragment extends BaseFragment {

    private StaffNewUseCase useCase;
    private Staff staff;
    private View vProgress;
    private TextView tvSave;
    private TextInputEditText etSurname;
    private TextInputEditText etName;
    private TextInputEditText etPatronymic;
    private TextInputEditText etDescription;
    private TextInputEditText etPosition;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etDepartment;
    private TextInputEditText etEmail;
    private TextInputEditText etAddress;
    private TextInputLayout tlSurname;
    private TextInputLayout tlName;
    private TextInputLayout tlPatronymic;
    private TextInputLayout tlDepartment;
    private TextInputLayout tlPosition;
    private TextInputLayout tlEmail;

    public static StaffAddFragment newInstance(Staff staff){
        StaffAddFragment  fragment = new StaffAddFragment();
        fragment.staff = staff;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_staff, container, false);
        vProgress = view.findViewById(R.id.fas_progress_bar);
        tvSave = view.findViewById(R.id.fas_save_button);

        etSurname = view.findViewById(R.id.fas_surname);
        etName = view.findViewById(R.id.fas_name);
        etPosition = view.findViewById(R.id.fas_position);
        etPatronymic = view.findViewById(R.id.fas_patronymic);
        etDescription = view.findViewById(R.id.fas_description);
        etPhoneNumber = view.findViewById(R.id.fas_phone_number);
        etDepartment = view.findViewById(R.id.fas_department);
        etEmail = view.findViewById(R.id.fas_email);
        etAddress = view.findViewById(R.id.fas_address);

        tlSurname = view.findViewById(R.id.fas_surname_layout);
        tlName = view.findViewById(R.id.fas_name_layout);
        tlPatronymic = view.findViewById(R.id.fas_patronymic_layout);
        tlDepartment = view.findViewById(R.id.fas_department_layout);
        tlPosition = view.findViewById(R.id.fas_position_layout);
        tlEmail = view.findViewById(R.id.fas_email_layout);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.staff_new);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new StaffNewUseCase();
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStaff();
        etDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout,
                        DepartmentFragment.newInstance(null, staff, 1));
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                checkData();
                if (staff != null && validate(getContext())) {
                    showProgress(true);
                    tlSurname.setError(null);
                    tlName.setError(null);
                    tlPatronymic.setError(null);
                    tlDepartment.setError(null);
                    tlPosition.setError(null);
                    newStaff(getContext(), getBaseActivity());
                }
            }
        });
    }

    private void newStaff(final Context context, final AppCompatActivity activity) {
        useCase.execute(staff, new DisposableObserver<Staff>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Staff staff) {
                showProgress(false);
                NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                        StaffInfoFragment.newInstance());
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
        String surname = etSurname.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String patronymic = etPatronymic.getText().toString().trim();
        staff.setSurname(surname);
        staff.setName(name);
        staff.setPatronymic(patronymic);
        staff.setFullName(getString(R.string.staff_full_name, surname, name, patronymic));
        staff.setAddress(etAddress.getText().toString().trim());
        staff.setDescription(etDescription.getText().toString().trim());
        staff.setPosition(etPosition.getText().toString().trim());
        staff.setEmail(etEmail.getText().toString().trim());
        staff.setPhoneNumber(etPhoneNumber.getText().toString().trim());
    }

    private void setStaff(){
        etSurname.setText(staff.getSurname());
        etName.setText(staff.getName());
        etPatronymic.setText(staff.getPatronymic());
        etDescription.setText(staff.getDescription());
        etPosition.setText(staff.getPosition());
        etAddress.setText(staff.getAddress());
        etDepartment.setText(String.valueOf(staff.getDepartment()));
        etEmail.setText(staff.getEmail());
        etPhoneNumber.setText(staff.getPhoneNumber());
    }

    private boolean validate(Context context){
        if(TextUtils.isEmpty(staff.getSurname())){
            tlName.setError(null);
            tlSurname.setError(context.getString(R.string.staff_error_surname_empty));
            tlPatronymic.setError(null);
            tlDepartment.setError(null);
            tlPosition.setError(null);
            tlEmail.setError(null);
            etSurname.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(staff.getName())) {
            tlName.setError(context.getString(R.string.staff_error_name_empty));
            tlSurname.setError(null);
            tlPatronymic.setError(null);
            tlDepartment.setError(null);
            tlPosition.setError(null);
            tlEmail.setError(null);
            etName.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(staff.getPatronymic())){
            tlName.setError(null);
            tlSurname.setError(null);
            tlPatronymic.setError(context.getString(R.string.staff_error_patronymic_empty));
            tlDepartment.setError(null);
            tlPosition.setError(null);
            tlEmail.setError(null);
            etPatronymic.requestFocus();
            return false;
        }
        if(staff.getDepartment() == 0){
            tlName.setError(null);
            tlSurname.setError(null);
            tlPatronymic.setError(null);
            tlDepartment.setError(context.getString(R.string.staff_error_department_empty));
            tlPosition.setError(null);
            tlEmail.setError(null);
            etDepartment.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(staff.getPosition())){
            tlName.setError(null);
            tlSurname.setError(null);
            tlPatronymic.setError(null);
            tlDepartment.setError(null);
            tlPosition.setError(context.getString(R.string.staff_error_position_empty));
            tlEmail.setError(null);
            etPosition.requestFocus();
            return false;
        }
        if(!TextUtils.isEmpty(staff.getEmail()) && !Patterns.EMAIL_ADDRESS.matcher(staff.getEmail()).matches()){
            tlName.setError(null);
            tlSurname.setError(null);
            tlPatronymic.setError(null);
            tlDepartment.setError(null);
            tlPosition.setError(null);
            tlEmail.setError(context.getString(R.string.sign_up_wrong_email));
            etEmail.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && staff != null) {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    getBaseActivity().onBackPressed();
                    NavigationUtils.removeFragment(getBaseActivity(), StaffAddFragment.this);
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
