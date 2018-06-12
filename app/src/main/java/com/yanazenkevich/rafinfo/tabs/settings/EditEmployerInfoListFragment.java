package com.yanazenkevich.rafinfo.tabs.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.adapters.BaseRecyclerAdapter;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.interactions.EmployersUseCase;
import com.yanazenkevich.rafinfo.items.EmployerEditItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class EditEmployerInfoListFragment extends BaseFragment {

    private static final int MENU_ITEM_ADD_KEY = 88;

    private EmployersUseCase useCase;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View vProgress;

    public static EditEmployerInfoListFragment newInstance() {
        return new EditEmployerInfoListFragment();
    }

    @Override
    public String title() {
        return getResources().getString(R.string.employers);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employers, container, false);
        recyclerView = view.findViewById(R.id.fe_recycler_view);
        vProgress = view.findViewById(R.id.fe_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        useCase = new EmployersUseCase();
        showProgress(true);
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getEmployers(getContext());
    }

    private void getEmployers(final Context context) {
        useCase.execute(null, new DisposableObserver<List<Employer>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Employer> employers) {
                showProgress(false);
                showItems(employers, getBaseActivity());
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

    private void showItems(List<Employer> employers, final BaseActivity activity) {
        if (employers.size() != 0) {
            final List<BaseListItem> items = new ArrayList<>(EmployerEditItem.getItems(employers, activity));
            adapter.replaceElements(items);
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        MenuItem item = menu.add(0, MENU_ITEM_ADD_KEY, Menu.NONE, "Add");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_add);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case MENU_ITEM_ADD_KEY: {
                    addNewEmployer(getBaseActivity());
                    return true;
                }
                case android.R.id.home: {
                    NavigationUtils.removeFragment(getBaseActivity(), EditEmployerInfoListFragment.this);
                    getBaseActivity().onBackPressed();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewEmployer(final AppCompatActivity activity){
        NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                AddEmployerFragment.newInstance(new Employer()));
    }
}

