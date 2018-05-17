package com.yanazenkevich.rafinfo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.yanazenkevich.rafinfo.interfaces.ActivityTitleInterface;
import com.yanazenkevich.rafinfo.interfaces.FragmentTitleInterface;

public abstract class BaseFragment extends Fragment implements FragmentTitleInterface {

    public static final String FRAGMENT_CONTAINER_ID_KEY = "FRAGMENT_CONTAINER_ID_KEY";
    public static final String TAG = "BaseFragment";

    @Override
    public void updateTitleSubtitle() {
        if (getActivity() instanceof ActivityTitleInterface && allowTitleUpdate()) {
            ((ActivityTitleInterface) getActivity()).updateTitle(title());
            ((ActivityTitleInterface) getActivity()).updateSubTitle(subTitle());
        }
    }

    @Override
    public boolean allowTitleUpdate() {
        return true;
    }

    @Override
    public String title() {
        return "RafInfo";
    }

    @Override
    public String subTitle() {
        return null;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity activity = (Activity) context;
        } catch (ClassCastException e) {
//            throw new ClassCastException("must implement IVolleyErrorsProcessing");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Empty options menu by default
        menu.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, this.getClass().getSimpleName() + " onActivityCreated: ");
        // Has its own menu
        setHasOptionsMenu(true);
        updateTitleSubtitle();
    }

    public boolean allowFragmentCommit() {
        return getActivity() instanceof BaseActivity && ((BaseActivity) getActivity()).allowFragmentCommit();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

//    public void replace(Fragment fragment, FragmentManager fragmentManager){
//        ExampleFragment fragment = new ExampleFragment(); // Фрагмент, которым собираетесь заменить первый фрагмент
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
//        transaction.replace(R.id.fragment_container, fragment); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
//        transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно
//
//        transaction.commit(); // Коммитете
//    }

//    public FadeToolbarRecyclerViewScrollListener initToolbarFadeAnimation(RecyclerView recyclerView) {
//        FadeToolbarRecyclerViewScrollListener listener;
//        recyclerView.addOnScrollListener(listener = new FadeToolbarRecyclerViewScrollListener(
//                recyclerView, getBaseActivity().getStatusBar(), getBaseActivity().getToolbar()));
//        return listener;
//    }
//
//    public FadeToolbarScrollViewScrollListener initToolbarFadeAnimation(ScrollView scrollView) {
//        FadeToolbarScrollViewScrollListener listener;
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(listener = new FadeToolbarScrollViewScrollListener(
//                scrollView, getBaseActivity().getStatusBar(), getBaseActivity().getToolbar()));
//        return listener;
//    }

//    protected void clearToolbarColor() {
//        final Toolbar toolbar = getBaseActivity().getToolbar();
//        if (toolbar != null) {
//            toolbar.setBackgroundColor(Color.TRANSPARENT);
//        }
//    }
//
//    protected void initToolbarColor() {
//        final Toolbar toolbar = getBaseActivity().getToolbar();
//        if (toolbar != null) {
//            toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        }
//        final View statusBar = getBaseActivity().getStatusBar();
//        if(statusBar != null){
//            statusBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//        }
//    }
}
