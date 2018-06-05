package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.tabs.vacancies.EmployerFragment;
import com.yanazenkevich.rafinfo.utils.DateUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

public class VacancyDetailsItem implements BaseListItem {

    private final Vacancy vacancy;
    private final BaseActivity activity;
    private TextView tvStatus;
    private TextView tvName;
    private TextView tvLocation;
    private LinearLayout llEmployer;
    private TextView tvEmployer;
    private TextView tvContactInfo;
    private TextView tvDescription;
    private ImageView ivMore;

    public VacancyDetailsItem(Vacancy vacancy, BaseActivity activity) {
        this.vacancy = vacancy;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_vacancy_details;
    }

    @Override
    public void initViews(Context context, View view) {
        tvStatus = view.findViewById(R.id.ivd_status);
        tvName = view.findViewById(R.id.ivd_name);
        tvLocation = view.findViewById(R.id.ivd_location);
        llEmployer = view.findViewById(R.id.ivd_employer_layout);
        tvEmployer = view.findViewById(R.id.ivd_employer);
        tvContactInfo = view.findViewById(R.id.ivd_contact_info);
        tvDescription = view.findViewById(R.id.ivd_description);
        ivMore = view.findViewById(R.id.ivd_more);
    }

    @Override
    public void renderView(final Context context, View view) {
        tvName.setText(vacancy.getName());
        tvDescription.setText(vacancy.getDescription());
        tvLocation.setText(vacancy.getLocation());
        tvStatus.setText(DateUtils.getStatus(vacancy, context));
        tvContactInfo.setText(vacancy.getContactInfo());
        if(vacancy.getEmployer() != null){
            ivMore.setVisibility(View.VISIBLE);
            tvEmployer.setText(vacancy.getEmployer().getName());
            llEmployer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                            EmployerFragment.newInstance(vacancy));
                }
            });
        }else{
            ivMore.setVisibility(View.GONE);
        }
    }

    public Vacancy getVacancy() {
        return vacancy;
    }
}
