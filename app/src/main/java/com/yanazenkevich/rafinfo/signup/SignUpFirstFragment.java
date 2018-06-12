package com.yanazenkevich.rafinfo.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;


public class SignUpFirstFragment extends BaseFragment {

    private User user;
    private TextInputEditText etDepartment;
    private TextInputEditText etName;
    private TextInputEditText etSurname;
    private TextInputEditText etEmail;
    private TextInputLayout tlName;
    private TextInputLayout tlSurname;
    private TextInputLayout tlEmail;
    private TextInputLayout tlDepartment;
    private TextView tvNextButton;

    public static SignUpFirstFragment newInstance(User user) {
        SignUpFirstFragment fragment = new SignUpFirstFragment();
        fragment.user = user;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_first, container, false);
        etDepartment = view.findViewById(R.id.fsf_department);
        etName = view.findViewById(R.id.fsf_name);
        etSurname = view.findViewById(R.id.fsf_surname);
        etEmail = view.findViewById(R.id.fsf_email);
        tlEmail = view.findViewById(R.id.fsf_email_layout);
        tlName = view.findViewById(R.id.fsf_name_layout);
        tlSurname = view.findViewById(R.id.fsf_surname_layout);
        tvNextButton = view.findViewById(R.id.fsf_next_button);
        tlDepartment = view.findViewById(R.id.fsf_department_layout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etName.setText(user.getName());
        etSurname.setText(user.getSurname());
        etEmail.setText(user.getEmail());
        if(user.getDepartment() != 0){
            etDepartment.setText(String.valueOf(user.getDepartment()));
        }
        etDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                NavigationUtils.replaceWithFragmentAndAddToBackStack(getBaseActivity(), R.id.alo_fragment_container, DepartmentFragment.newInstance(user, null, 0));
            }
        });
        tvNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
                if (user != null && validate(getContext())) {
                    tlName.setError(null);
                    tlSurname.setError(null);
                    tlEmail.setError(null);
                    tlDepartment.setError(null);
                    NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.alo_fragment_container, SignUpSecondFragment.newInstance(user));
                }
            }
        });
    }

    private void checkData(){
        user.setName(etName.getText().toString().trim());
        user.setSurname(etSurname.getText().toString().trim());
        user.setEmail(etEmail.getText().toString().trim());
    }

    private boolean validate(Context context){
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
        if(user.getDepartment() == 0){
            tlName.setError(null);
            tlSurname.setError(null);
            tlEmail.setError(null);
            tlDepartment.setError(context.getString(R.string.sign_up_error_department_empty));
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
}
