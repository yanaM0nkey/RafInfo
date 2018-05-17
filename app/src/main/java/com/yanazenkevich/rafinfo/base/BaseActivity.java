package com.yanazenkevich.rafinfo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.yanazenkevich.rafinfo.R;

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    private View mStatusBar;
    private Toolbar mToolbar;
    private boolean mAllowCommit;
    private StateObserver stateObserver;

    @Override
    public void setSupportActionBar(@Nullable android.support.v7.widget.Toolbar toolbar) {
        mToolbar = toolbar;
        super.setSupportActionBar(toolbar);
    }

    public void setStatusBar(View statusBar) {
        this.mStatusBar = statusBar;
    }

    public View getStatusBar() {
        return mStatusBar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAllowCommit = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mAllowCommit = false;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResumeFragments() {
        mAllowCommit = true;
        super.onResumeFragments();
        if (stateObserver != null) {
            stateObserver.onStateChanged(State.RESUMED);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stateObserver != null) {
            stateObserver.onStateChanged(State.PAUSED);
        }
    }

    public boolean allowFragmentCommit() {
        return mAllowCommit && !isFinishing();
    }

    public void clearStackAndReplaceFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
        shouldDisplayHomeUp();
    }

    public void replaceFragmentWithBackStack(int containerViewId, Fragment fragment) {
        replaceFragmentWithBackStack(containerViewId, fragment, false);
    }

    public void replaceFragmentWithBackStack(int containerViewId, Fragment fragment, boolean animate) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        if (animate) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        fragmentTransaction.replace(containerViewId, fragment, fragment.getClass().getName());
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
        shouldDisplayHomeUp();
    }

    public boolean shouldDisplayHomeUp() {
        return false;
    }

    @Nullable
    protected Fragment getLastFragmentInBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        final int bseCount = fm.getBackStackEntryCount();
        if (bseCount > 0) {
            final FragmentManager.BackStackEntry topFragmentEntry = fm.getBackStackEntryAt(bseCount - 1);
            final String topFragmentTag = topFragmentEntry.getName();
            return fm.findFragmentByTag(topFragmentTag);
        }
        return null;
    }

    public int getTopFragmentMargin() {
        return 0;
    }

    public void setStateObserver(StateObserver stateObserver) {
        this.stateObserver = stateObserver;
    }

    public enum State {
        RESUMED, PAUSED;
    }

    public interface StateObserver {

        void onStateChanged(State state);
    }
}