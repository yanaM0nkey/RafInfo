package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.adapters.BaseRecyclerAdapter;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.items.EmployerItem;

import java.util.ArrayList;
import java.util.List;

public class EmployerFragment extends BaseFragment {

    private Vacancy vacancy;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public static EmployerFragment newInstance(Vacancy vacancy) {
        EmployerFragment fragment = new EmployerFragment();
        fragment.vacancy = vacancy;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer, container, false);
        recyclerView = view.findViewById(R.id.fe_recycler_view);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.vacancies_details_title);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        showItems(vacancy);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showItems(Vacancy vacancy) {
        final List<BaseListItem> items = new ArrayList<>();
        if (vacancy != null) {
            items.add(new EmployerItem(vacancy));
            adapter.replaceElements(items);
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && vacancy != null) {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    getBaseActivity().onBackPressed();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
