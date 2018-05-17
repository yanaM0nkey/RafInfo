package com.yanazenkevich.rafinfo.tabs.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.interactions.AuthService;
import com.yanazenkevich.rafinfo.interactions.SaveUserUseCase;
import com.yanazenkevich.rafinfo.interactions.UserUseCase;
import com.yanazenkevich.rafinfo.signup.DepartmentFragment;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_OBJECT_ID;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;

public class EditPersonalInfoFragment extends BaseFragment {

    private Context context;
    private User user;
    private UserUseCase useCase;
    private SaveUserUseCase saveUseCase;
    private AuthService authService;

    private TextInputEditText etDepartment;
    private TextInputEditText etName;
    private TextInputEditText etSurname;
    private TextInputEditText etEmail;
    private TextInputLayout tlName;
    private TextInputLayout tlSurname;
    private TextInputLayout tlEmail;
    private TextInputLayout tlDepartment;
    private TextView tvSaveButton;
    private View vProgress;

    public static EditPersonalInfoFragment newInstance(Context context, @Nullable User user){
        EditPersonalInfoFragment fragment =  new EditPersonalInfoFragment();
        fragment.user = user;
        fragment.context = context;
        return fragment;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.settings_personal_info_title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        etDepartment = view.findViewById(R.id.fpi_department);
        etName = view.findViewById(R.id.fpi_name);
        etSurname = view.findViewById(R.id.fpi_surname);
        etEmail = view.findViewById(R.id.fpi_email);
        tlEmail = view.findViewById(R.id.fpi_email_layout);
        tlName = view.findViewById(R.id.fpi_name_layout);
        tlSurname = view.findViewById(R.id.fpi_surname_layout);
        tvSaveButton = view.findViewById(R.id.fpi_save_button);
        tlDepartment = view.findViewById(R.id.fpi_department_layout);
        vProgress = view.findViewById(R.id.fpi_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(user == null){
            useCase = new UserUseCase();
            showProgress(true);
            getUser();
        }else{
            setData(user);
        }
        etDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout, DepartmentFragment.newInstance(user));
            }
        });
        tvSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                if (user != null && validate()) {
                    tlName.setError(null);
                    tlSurname.setError(null);
                    tlEmail.setError(null);
                    tlDepartment.setError(null);
                    showProgress(true);
                    authService = new AuthService(context);
                    saveUseCase = new SaveUserUseCase(authService);
                    saveUser();
                }
            }
        });
    }

    private void saveUser(){
        saveUseCase.execute(user, new DisposableObserver<User>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull User user) {
                showProgress(false);
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout, SettingsFragment.newInstance());
                Toast.makeText(context, getString(R.string.settings_save_toast), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(getContext(), e);
            }

            @Override
            public void onComplete() {
                saveUseCase.dispose();
            }
        });
    }

    private void getUser(){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        if(preferences != null){
            String objectId = preferences.getString(KEY_OBJECT_ID, null);
            useCase.execute(objectId, new DisposableObserver<User>() {
                @Override
                public void onNext(@io.reactivex.annotations.NonNull User user) {
                    showProgress(false);
                    setData(user);
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.e("aaa", e.getMessage());
                    showProgress(false);
                    ErrorUtils.errorHandling(getContext(), e);
                }

                @Override
                public void onComplete() {
                    useCase.dispose();
                }
            });
        }
    }

    private void checkData(){
        user.setName(etName.getText().toString().trim());
        user.setSurname(etSurname.getText().toString().trim());
        user.setEmail(etEmail.getText().toString().trim());
    }

    private void setData(User mUser){
        user = mUser;
        etName.setText(user.getName());
        etSurname.setText(user.getSurname());
        etEmail.setText(user.getEmail());
        etDepartment.setText(String.valueOf(user.getDepartment()));
    }

    private boolean validate(){
        if (TextUtils.isEmpty(user.getName())) {
            tlName.setError(context.getString(R.string.sign_up_error_name_empty));
            tlSurname.setError(null);
            tlEmail.setError(null);
            tlDepartment.setError(null);
            etName.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(user.getSurname())){
            tlName.setError(null);
            tlSurname.setError(context.getString(R.string.sign_up_error_surname_empty));
            tlEmail.setError(null);
            tlDepartment.setError(null);
            etSurname.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(user.getEmail())){
            tlName.setError(null);
            tlSurname.setError(null);
            tlDepartment.setError(null);
            tlEmail.setError(context.getString(R.string.sign_up_error_email_empty));
            etEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()){
            tlName.setError(null);
            tlSurname.setError(null);
            tlDepartment.setError(null);
            tlEmail.setError(context.getString(R.string.sign_up_wrong_email));
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
