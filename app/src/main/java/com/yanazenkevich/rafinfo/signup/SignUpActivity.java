package com.yanazenkevich.rafinfo.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.NavigationActivity;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.login.LogInActivity;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

public class SignUpActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context){
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        User user = new User();
        final Fragment signUpFirstFragment = SignUpFirstFragment.newInstance(user);
        NavigationUtils.addFragment(this,R.id.alo_fragment_container, signUpFirstFragment);
    }

    @Override
    public void onBackPressed() {
        startActivity(LogInActivity.getLaunchIntent(this));
        this.finish();
    }
}
