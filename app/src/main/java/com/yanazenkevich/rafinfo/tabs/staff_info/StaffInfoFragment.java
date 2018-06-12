package com.yanazenkevich.rafinfo.tabs.staff_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.adapters.BaseRecyclerAdapter;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.interactions.StaffInfoBySurnameUseCase;
import com.yanazenkevich.rafinfo.interactions.StaffInfoUseCase;
import com.yanazenkevich.rafinfo.items.EmptySearchItem;
import com.yanazenkevich.rafinfo.items.StaffItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_ADMIN;
import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_DEPARTMENT;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;

public class StaffInfoFragment extends BaseFragment {

    private static final int MENU_ITEM_ADD_KEY = 35;

    private static String searchText;
    private StaffInfoUseCase useCase;
    private StaffInfoBySurnameUseCase useCaseBySurname;
    private View vProgress;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private SpinnerAdapter spinnerAdapter;
    private Spinner navigationSpinner;
    private SearchView searchView;
    private int selectedDepartment;
    private boolean isAdmin;
    private final Handler handler = new Handler();
    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(searchText)) {
                getStaffInfoBySurname(searchText);
            } else {
                getStaffInfo(selectedDepartment);
            }
        }
    };

    public static StaffInfoFragment newInstance(){
        return new StaffInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_info, container, false);
        vProgress = view.findViewById(R.id.fsi_progressBar);
        recyclerView = view.findViewById(R.id.fsi_recycler_view);
        searchView = view.findViewById(R.id.fsi_search_view);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.category, R.layout.spinner_dropdown_item);
        navigationSpinner = new Spinner(getBaseActivity().getSupportActionBar().getThemedContext());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationSpinner.setAdapter(spinnerAdapter);
        getBaseActivity().getToolbar().addView(navigationSpinner, 0);
        SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        final int sharedDepartment = preferences.getInt(KEY_DEPARTMENT, 0);
        selectedDepartment = sharedDepartment;
        navigationSpinner.setSelection(sharedDepartment - 1);
        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgress(true);
                selectedDepartment = position + 1;
                getStaffInfo(selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        useCase = new StaffInfoUseCase();
        useCaseBySurname = new StaffInfoBySurnameUseCase();
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                searchStaffInfoBySurname();
                return true;
            }
        });
        isAdmin = preferences.getBoolean(KEY_ADMIN, false);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getBaseActivity().getToolbar().removeViewAt(0);
    }

    @Override
    public String title() {
        return "";
    }

    private void getStaffInfo(int department){
        useCase.execute(String.valueOf(department), new DisposableObserver<List<Staff>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Staff> staffInfo) {
                showProgress(false);
                showItems(staffInfo);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(getContext(), e);
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

    private void showItems(List<Staff> staffInfo){
        if (staffInfo.size() != 0) {
            final List<BaseListItem> items = new ArrayList<>(StaffItem.getItems(staffInfo, getBaseActivity(), isAdmin));
            adapter.replaceElements(items);
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void searchStaffInfoBySurname(){
        showProgress(true);
        int SEARCH_TIMEOUT = 1000;
        handler.removeCallbacks(searchRunnable);
        handler.postDelayed(searchRunnable, SEARCH_TIMEOUT);
    }

    private void getStaffInfoBySurname(String searchText){
        useCaseBySurname.execute(searchText, new DisposableObserver<List<Staff>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Staff> staffInfo) {
                showProgress(false);
                showItems(staffInfo);
                if(staffInfo.size() == 0){
                    showEmptySearchItem();
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                showProgress(false);
                ErrorUtils.errorHandling(getContext(), e);
            }

            @Override
            public void onComplete() {
                useCase.dispose();
            }
        });
    }

    private void showEmptySearchItem(){
        final List<BaseListItem> items = new ArrayList<>();
        items.add(new EmptySearchItem());
        adapter.replaceElements(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if(isAdmin) {
            MenuItem item = menu.add(0, MENU_ITEM_ADD_KEY, Menu.NONE, "Add");
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setIcon(R.drawable.ic_add);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case MENU_ITEM_ADD_KEY: {
                    addNewStaff(getBaseActivity());
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewStaff(final AppCompatActivity activity){
        NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                StaffAddFragment.newInstance(new Staff()));
    }

}