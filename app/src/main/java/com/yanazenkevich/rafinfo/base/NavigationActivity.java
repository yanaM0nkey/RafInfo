package com.yanazenkevich.rafinfo.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.interfaces.ActivityTitleInterface;
import com.yanazenkevich.rafinfo.interfaces.BackableFragmentInterface;

public abstract class NavigationActivity extends BaseActivity implements ActivityTitleInterface {

    private final static int CONTAINER_ID = R.id.an_fragment_container;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        Toolbar toolbar = findViewById(R.id.na_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @LayoutRes
    protected int getContentView() {
        return R.layout.activity_navigation;
    }

    @Override
    public void updateTitle(String title) {
        setTitle(title);
    }

    @Override
    public void updateSubTitle(String subTitle) {
        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(CONTAINER_ID);
        if (currentFragment instanceof BackableFragmentInterface) {
            if (!((BackableFragmentInterface) currentFragment).onBackPressed()) {
                backPress();
            }
        } else {
            backPress();
        }
    }

    private void backPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
