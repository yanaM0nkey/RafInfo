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
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseFragment;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.interactions.EmployersUseCase;
import com.yanazenkevich.rafinfo.items.EmployerOfListItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class ListEmployersFragment extends BaseFragment {

    private Vacancy vacancy;
    private RequestRelation requestRelation;
    private EmployersUseCase useCase;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View vProgress;

    public static ListEmployersFragment newInstance(Vacancy vacancy, RequestRelation requestRelation) {
        ListEmployersFragment fragment = new ListEmployersFragment();
        fragment.vacancy = vacancy;
        fragment.requestRelation = requestRelation;
        return fragment;
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
            final List<BaseListItem> items = new ArrayList<>(EmployerOfListItem.getItems(employers, vacancy, requestRelation, activity));
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
