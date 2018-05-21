package com.yanazenkevich.rafinfo.tabs.vacancies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.adapters.BaseRecyclerAdapter;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.interactions.VacancyUseCase;
import com.yanazenkevich.rafinfo.items.VacancyItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class VacanciesFragment extends BaseFragment {

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
}
