package com.yanazenkevich.rafinfo.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

public class LogInActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context){
        return new Intent(context, LogInActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Fragment loginFragment = LogInFragment.newInstance();
        NavigationUtils.addFragment(this,R.id.alo_fragment_container, loginFragment);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
