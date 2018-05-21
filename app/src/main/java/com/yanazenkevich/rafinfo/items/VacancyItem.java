package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.tabs.vacancies.VacancyDetailsFragment;
import com.yanazenkevich.rafinfo.utils.DateUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

public class VacancyItem implements BaseListItem {

    private final Vacancy vacancy;
    private final BaseActivity activity;
    private TextView tvStatus;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvEmployer;
    private CardView cardView;


    public VacancyItem(Vacancy vacancy, BaseActivity activity) {
        this.vacancy = vacancy;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
       return R.layout.item_vacancy;
    }

    @Override
    public void initViews(Context context, View view) {
        cardView = view.findViewById(R.id.iv_card_view);
        tvStatus = view.findViewById(R.id.iv_status);
        tvName = view.findViewById(R.id.iv_name);
        tvLocation = view.findViewById(R.id.iv_location);
        tvEmployer = view.findViewById(R.id.iv_employer);
    }

    @Override
    public void renderView(final Context context, View view) {
        tvName.setText(vacancy.getName());
        tvLocation.setText(vacancy.getLocation());
        tvStatus.setText(DateUtils.getStatus(vacancy, context));
        tvEmployer.setText(vacancy.getEmployer().getName());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                        VacancyDetailsFragment.newInstance(vacancy));
            }
        });
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Vacancy> vacancies, BaseActivity activity) {
        List<BaseListItem> items = new ArrayList<>();
        if (vacancies != null) {
            for (int i = 0; i < vacancies.size(); i++) {
                Vacancy vacancy = vacancies.get(i);
                items.add(new VacancyItem(vacancy, activity));
            }
        }
        return items;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }
}
