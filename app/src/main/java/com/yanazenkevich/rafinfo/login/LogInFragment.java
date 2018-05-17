package com.yanazenkevich.rafinfo.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.entities.AuthState;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.interactions.AuthService;
import com.yanazenkevich.rafinfo.interactions.LogInUseCase;
import com.yanazenkevich.rafinfo.interactions.ValidateUseCase;
import com.yanazenkevich.rafinfo.signup.SignUpActivity;
import com.yanazenkevich.rafinfo.tabs.HomeActivity;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_ACCESS_TOKEN;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;


public class LogInFragment extends BaseFragment{

    private User user = new User();

    private ProgressBar vProgress;
    private TextInputEditText etLogin;
    private TextInputEditText etPassword;
    private TextInputLayout tlLogin;
    private TextInputLayout tlPassword;
    private TextView tvLogInButton;
    private TextView tvSignIn;

    private Disposable logInDisposable;
    private AuthService authService;
    private LogInUseCase useCase;
    private ValidateUseCase validateUseCase;

    private boolean isValidate;

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        vProgress = view.findViewById(R.id.fl_progress_bar);
        etLogin = view.findViewById(R.id.fl_login);
        etPassword = view.findViewById(R.id.fl_password);
        tlLogin = view.findViewById(R.id.fl_login_layout);
        tlPassword = view.findViewById(R.id.fl_password_layout);
        tvLogInButton = view.findViewById(R.id.fl_login_button);
        tvSignIn = view.findViewById(R.id.fl_sign_in_button);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        authService = new AuthService(getContext());
        useCase = new LogInUseCase(authService);
        validateUseCase = new ValidateUseCase();

        tvLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setLogin(etLogin.getText().toString().trim());
                user.setPassword(etPassword.getText().toString().trim());
                if (user != null && validateLoginPassword(getContext())) {
                    tlLogin.setError(null);
                    tlPassword.setError(null);
                    showProgress(true);
                    onLogIn(getContext());
                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null)
                    getActivity().finish();
                startActivity(SignUpActivity.getLaunchIntent(getContext()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        logInDisposable = authService.observeState().subscribeWith(new DisposableObserver<AuthState>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull AuthState authState) {
                validate(getContext(), authState);
            }
            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {}
            @Override
            public void onComplete() {}
        });
    }

    private void validate(final Context context, final AuthState authState){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        final String token = preferences.getString(KEY_ACCESS_TOKEN, null);
        if(token != null) {
            showProgress(true);
            validateUseCase.execute(token, new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean response) {
                    showProgress(false);
                    isValidate = response;
                }

                @Override
                public void onError(Throwable e) {
                    showProgress(false);
                    ErrorUtils.errorHandling(context, e);
                }

                @Override
                public void onComplete() {
                    validateUseCase.dispose();
                    if(authState.isSigned() && isValidate){
                        if(getActivity() != null)
                            getActivity().finish();
                        startActivity(HomeActivity.getLaunchIntent(getContext()));
                    }
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(logInDisposable != null && !logInDisposable.isDisposed())
            logInDisposable.dispose();
    }

    public boolean validateLoginPassword(Context context) {
        if (TextUtils.isEmpty(user.getLogin())) {
            tlLogin.setError(context.getString(R.string.log_in_error_login_empty));
            tlPassword.setError(null);
            etLogin.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(user.getPassword())){
            tlPassword.setError(context.getString(R.string.log_in_error_password_empty));
            tlLogin.setError(null);
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void onLogIn(final Context context){
        useCase.execute(user, new DisposableObserver<User>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull User user) {
                showProgress(false);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandlingLogin(context, e);
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
}
