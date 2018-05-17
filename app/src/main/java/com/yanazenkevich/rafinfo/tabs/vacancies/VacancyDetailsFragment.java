package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.items.VacancyDetailsItem;


import java.util.ArrayList;
import java.util.List;

public class VacancyDetailsFragment extends BaseFragment {

    private static final int MENU_ITEM_SHARE_KEY = 1;

    private Vacancy vacancy;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public static VacancyDetailsFragment newInstance(Vacancy vacancy) {
        VacancyDetailsFragment fragment = new VacancyDetailsFragment();
        fragment.vacancy = vacancy;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancy_details, container, false);
        recyclerView = view.findViewById(R.id.fvd_recycler_view);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.vacancies_details);
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
            items.add(new VacancyDetailsItem(vacancy, getBaseActivity()));
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
                case MENU_ITEM_SHARE_KEY: {
                    shareAction();
                    return true;
                }
                case android.R.id.home: {
                    getBaseActivity().onBackPressed();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (vacancy != null) {
            MenuItem item = menu.add(0, MENU_ITEM_SHARE_KEY, Menu.NONE, "Share");
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            item.setIcon(R.drawable.ic_action_share);
        }
    }

    private void shareAction() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, createShareText());
        startActivity(Intent.createChooser(shareIntent, "ShareVacancy"));
    }

    private String createShareText() {
        return vacancy.getName() +
                "\n" +
                getResources().getString(R.string.vacancies_location, vacancy.getLocation()) +
                "\n" +
                getResources().getString(R.string.vacancies_employer, vacancy.getEmployer()) +
                "\n" +
                getResources().getString(R.string.vacancies_contact_info, vacancy.getContactInfo()) +
                "\n" +
                vacancy.getDescription();
    }
}
