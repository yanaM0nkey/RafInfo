package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.interactions.VacancyUseCase;
import com.yanazenkevich.rafinfo.items.VacancyItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_ADMIN;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;

public class VacanciesFragment extends BaseFragment {

    private static final int MENU_ITEM_ADD_KEY = 3;

    private boolean isAdmin;
    private VacancyUseCase useCase;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View vProgress;

    public static VacanciesFragment newInstance(){
        return new VacanciesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacancies, container, false);
        recyclerView = view.findViewById(R.id.fv_recycler_view);
        vProgress = view.findViewById(R.id.fv_progressBar);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.vacancies);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new VacancyUseCase();
        showProgress(true);
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getVacancies(getContext());
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        isAdmin = preferences.getBoolean(KEY_ADMIN, false);
    }

    private void getVacancies(final Context context) {
        useCase.execute(null, new DisposableObserver<List<Vacancy>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Vacancy> vacancies) {
                showProgress(false);
                showItems(vacancies);
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

    private void showItems(List<Vacancy> vacancies) {
        if (vacancies.size() != 0) {
            final List<BaseListItem> items = new ArrayList<>(VacancyItem.getItems(vacancies, getBaseActivity()));
            adapter.replaceElements(items);
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
        }
    }

    private void showProgress(boolean show) {
        vProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case MENU_ITEM_ADD_KEY: {
                    addNewVacancy(getBaseActivity());
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
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

    private void addNewVacancy(final AppCompatActivity activity){
        Vacancy vacancy = new Vacancy();
        vacancy.setEmployer(new Employer());
        NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                VacancyAddFragment.newInstance(vacancy, new RequestRelation()));
    }
}
