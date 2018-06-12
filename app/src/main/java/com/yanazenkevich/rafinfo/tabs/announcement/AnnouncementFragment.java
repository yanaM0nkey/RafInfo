package com.yanazenkevich.rafinfo.tabs.announcement;


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
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.interactions.AnnouncementUseCase;
import com.yanazenkevich.rafinfo.items.AnnouncementItem;
import com.yanazenkevich.rafinfo.utils.ErrorUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

import static com.yanazenkevich.rafinfo.interactions.AuthService.KEY_ADMIN;
import static com.yanazenkevich.rafinfo.interactions.AuthService.SHARED_PREFS_NAME;

public class AnnouncementFragment extends BaseFragment{

    private static final int MENU_ITEM_ADD_KEY = 2;

    private boolean isAdmin;
    private AnnouncementUseCase useCase;
    private BaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private View vProgress;

    public static AnnouncementFragment newInstance(){
        return new AnnouncementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);
        recyclerView = view.findViewById(R.id.fa_recycler_view);
        vProgress = view.findViewById(R.id.fa_progressBar);
        return view;
    }

    @Override
    public String title() {
        return getResources().getString(R.string.announcement);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        useCase = new AnnouncementUseCase();
        showProgress(true);
        adapter = new BaseRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getAnnouncements(getContext());
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        SharedPreferences preferences = getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        isAdmin = preferences.getBoolean(KEY_ADMIN, false);
    }

    private void getAnnouncements(final Context context) {
        useCase.execute(null, new DisposableObserver<List<Announcement>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Announcement> announcements) {
                showProgress(false);
                showItems(announcements);
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

    private void showItems(List<Announcement> announcements) {
        if (announcements.size() != 0) {
            final List<BaseListItem> items = new ArrayList<>(AnnouncementItem.getItems(announcements, getBaseActivity(), isAdmin));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            switch (item.getItemId()) {
                case MENU_ITEM_ADD_KEY: {
                    addNewAnnouncement(getBaseActivity());
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

    private void addNewAnnouncement(final AppCompatActivity activity){
        NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                AnnouncementAddFragment.newInstance());
    }
}
