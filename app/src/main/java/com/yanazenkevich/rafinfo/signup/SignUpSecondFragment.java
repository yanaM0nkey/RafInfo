package com.yanazenkevich.rafinfo.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.interactions.SignUpUseCase;
import com.yanazenkevich.rafinfo.login.LogInActivity;
import com.yanazenkevich.rafinfo.login.LogInFragment;
import com.yanazenkevich.rafinfo.tabs.HomeActivity;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public class SignUpSecondFragment extends BaseFragment {

    private User user;
    private SignUpUseCase useCase;

    private TextInputEditText etLogin;
    private TextInputEditText etPassword;
    private TextInputEditText etPasswordSecond;
    private TextInputLayout tlLogin;
    private TextInputLayout tlPassword;
    private TextInputLayout tlPasswordSecond;
    private TextView tvSignUpButton;
    private View vProgress;

    public static SignUpSecondFragment newInstance(User user) {
        SignUpSecondFragment fragment = new SignUpSecondFragment();
        fragment.user = user;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_second, container, false);
        etLogin = view.findViewById(R.id.fss_login);
        etPassword = view.findViewById(R.id.fss_password_first);
        etPasswordSecond = view.findViewById(R.id.fss_password_second);
        tlLogin = view.findViewById(R.id.fss_login_layout);
        tlPassword = view.findViewById(R.id.fss_password_first_layout);
        tlPasswordSecond = view.findViewById(R.id.fss_password_second_layout);
        tvSignUpButton = view.findViewById(R.id.fss_sign_up_button);
        vProgress = view.findViewById(R.id.fss_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new SignUpUseCase();
        tvSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && checkEmpty(getContext()) && validatePassword(getContext())) {
                    user.setLogin(etLogin.getText().toString().trim());
                    user.setPassword(etPassword.getText().toString().trim());
                    showProgress(true);
                    tlLogin.setError(null);
                    tlPassword.setError(null);
                    tlPasswordSecond.setError(null);
                    signUp(getContext(), getActivity());
                }
            }
        });

    }

    private void signUp(final Context context, final FragmentActivity activity){
        useCase.execute(user, new DisposableObserver<User>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull User user) {
                showProgress(false);
                Toast.makeText(context, context.getResources()
                        .getString(R.string.sign_up_ok), Toast.LENGTH_LONG).show();
                if(getActivity() != null)
                    getActivity().finish();
                startActivity(LogInActivity.getLaunchIntent(getContext()));
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                if(e instanceof HttpException){
                    tlLogin.setError(context.getString(R.string.sign_up_same_login));
                    tlPassword.setError(null);
                    tlPasswordSecond.setError(null);
                    etLogin.requestFocus();
                }else {
                    ErrorUtils.errorHandling(context, e);
                }
            }

            @Override
            public void onComplete() {
                useCase.dispose();
            }
        });
    }

    private boolean checkEmpty(Context context){
        if (TextUtils.isEmpty(etLogin.getText())) {
            tlLogin.setError(context.getString(R.string.log_in_error_login_empty));
            tlPassword.setError(null);
            tlPasswordSecond.setError(null);
            etLogin.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(etPassword.getText())){
            tlLogin.setError(null);
            tlPassword.setError(context.getString(R.string.log_in_error_password_empty));
            tlPasswordSecond.setError(null);
            etPassword.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(etPasswordSecond.getText())){
            tlLogin.setError(null);
            tlPassword.setError(null);
            tlPasswordSecond.setError(context.getString(R.string.sign_up_password_empty));
            etPasswordSecond.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(Context context){
        if(!etPassword.getText().toString().trim().equals(etPasswordSecond.getText().toString().trim())){
            tlLogin.setError(null);
            tlPassword.setError(null);
            tlPasswordSecond.setError(context.getString(R.string.sign_up_validate_password));
            etPasswordSecond.requestFocus();
            return false;
        }
        return true;
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
