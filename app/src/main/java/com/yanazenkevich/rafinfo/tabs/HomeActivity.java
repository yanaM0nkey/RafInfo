package com.yanazenkevich.rafinfo.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.NavigationActivity;
import com.yanazenkevich.rafinfo.tabs.announcement.AnnouncementFragment;
import com.yanazenkevich.rafinfo.tabs.settings.SettingsFragment;
import com.yanazenkevich.rafinfo.tabs.staff_info.StaffInfoFragment;
import com.yanazenkevich.rafinfo.tabs.vacancies.VacanciesFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

public class HomeActivity extends NavigationActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_announcement:
                    cleanBackStack();
                    selectedFragment = AnnouncementFragment.newInstance();
                    break;
                case R.id.navigation_vacancies:
                    cleanBackStack();
                    selectedFragment = VacanciesFragment.newInstance();
                    break;
                case R.id.navigation_staff_info:
                    cleanBackStack();
                    selectedFragment = StaffInfoFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    cleanBackStack();
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            NavigationUtils.replaceWithFragment(HomeActivity.this, R.id.frame_layout, selectedFragment);
            return true;
        }
    };

    public static Intent getLaunchIntent(Context context){
        return new Intent(context, HomeActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFirstFragment();
    }

    private void setFirstFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, AnnouncementFragment.newInstance());
        transaction.commit();
    }

    private void cleanBackStack(){
        if(getSupportFragmentManager().getBackStackEntryCount() != 0){
            String name = getSupportFragmentManager().getBackStackEntryAt(0).getName();
            getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
