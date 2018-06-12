package com.yanazenkevich.rafinfo.signup;

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
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.interactions.DepartmentUseCase;
import com.yanazenkevich.rafinfo.items.DepartmentItem;
import com.yanazenkevich.rafinfo.items.DepartmentStaffItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class DepartmentFragment extends BaseFragment {

    public static final int ACTION_USER = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;

    private int action;
    private User user;
    private Staff staff;
    private DepartmentUseCase useCase;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View vProgress;

    public static DepartmentFragment newInstance(@Nullable User user, @Nullable Staff staff, int action) {
        DepartmentFragment fragment = new DepartmentFragment();
        fragment.user = user;
        fragment.action = action;
        fragment.staff = staff;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        recyclerView = view.findViewById(R.id.fd_recycler_view);
        vProgress = view.findViewById(R.id.fd_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new DepartmentUseCase();
        showProgress(true);
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getDepartments(getContext());
        if(getBaseActivity().getSupportActionBar() != null)
             getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void getDepartments(final Context context) {
        useCase.execute(null, new DisposableObserver<List<Department>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Department> departments) {
                showProgress(false);
                showItems(departments, getBaseActivity());
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

    private void showItems(List<Department> departments, final BaseActivity activity) {
        if (departments.size() != 0) {
            List<BaseListItem> items;
            if(action == ACTION_USER){
                items = new ArrayList<>(DepartmentItem.getItems(departments, user, activity));
            }else{
                items = new ArrayList<>(DepartmentStaffItem.getItems(departments, staff, activity, action));
            }

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
