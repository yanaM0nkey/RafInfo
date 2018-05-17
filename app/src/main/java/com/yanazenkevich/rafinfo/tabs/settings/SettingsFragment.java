package com.yanazenkevich.rafinfo.tabs.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.interactions.AuthService;
import com.yanazenkevich.rafinfo.interactions.LogOutUseCase;
import com.yanazenkevich.rafinfo.interactions.ValidateUseCase;
import com.yanazenkevich.rafinfo.login.LogInActivity;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_ACCESS_TOKEN;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;

public class SettingsFragment extends BaseFragment {

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    private TextView tvPersonalInfo;
    private TextView tvLogout;
    private LogOutUseCase useCase;
    private ValidateUseCase validateUseCase;
    private View vProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvPersonalInfo = view.findViewById(R.id.fs_personal_info);
        tvLogout = view.findViewById(R.id.fs_logout);
        vProgress = view.findViewById(R.id.fs_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new LogOutUseCase();
        validateUseCase = new ValidateUseCase();
        tvPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.replaceWithFragment(getBaseActivity(), R.id.frame_layout, EditPersonalInfoFragment.newInstance(getContext(), null));
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(getContext(), getActivity());
            }
        });
    }

    @Override
    public String title() {
        return getResources().getString(R.string.settings);
    }

    private void createDialog(final Context context, final FragmentActivity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(getString(R.string.settings_dialog_title));
        dialog.setPositiveButton(getString(R.string.settings_dialog_positive_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                logout(context, activity);
                showProgress(true);
            };
        });
        dialog.setNegativeButton(getString(R.string.settings_dialog_negative_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void logout(final Context context, final FragmentActivity activity){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        if(preferences != null) {
            final String token = preferences.getString(KEY_ACCESS_TOKEN, null);
            useCase.execute(token, new DisposableObserver<Response<Void>>() {
                @Override
                public void onNext(@io.reactivex.annotations.NonNull Response<Void> response) {
                    validate(context, activity, token);
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

    private void validate(final Context context, final FragmentActivity activity, final String token){
        validateUseCase.execute(token, new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull Boolean isValidate) {
                showProgress(false);
                if(!isValidate){
                    activity.finish();
                    AuthService service = new AuthService(context);
                    service.removeAccessToken();
                    service.removeDepartment();
                    service.removeObjectId();
                    service.removeAdmin();
                    startActivity(LogInActivity.getLaunchIntent(context));
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage(getString(R.string.error));
                    dialog.show();
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(context, e);
            }

            @Override
            public void onComplete() {
                validateUseCase.dispose();
            }
        });
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}