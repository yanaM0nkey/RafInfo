package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.entities.Relation;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Vacancy;
import com.yanazenkevich.rafinfo.tabs.vacancies.VacancyAddFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployerOfListItem implements BaseListItem {

    private final Employer employer;
    private Vacancy vacancy;
    private  RequestRelation requestRelation;
    private final BaseActivity activity;
    private TextView tvName;
    private ImageView ivAction;
    private ConstraintLayout layout;

    public EmployerOfListItem(Employer employer, Vacancy vacancy, RequestRelation requestRelation, BaseActivity activity) {
        this.employer = employer;
        this.vacancy = vacancy;
        this.requestRelation = requestRelation;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_employer_of_list;
    }

    @Override
    public void initViews(Context context, View view) {
        tvName = view.findViewById(R.id.ieol_employer_name);
        ivAction = view.findViewById(R.id.ieol_action);
        layout = view.findViewById(R.id.ieol_layout);
    }

    @Override
    public void renderView(final Context context, View view) {
        if(employer != null && !TextUtils.isEmpty(employer.getName()) &&
                vacancy.getEmployer().getName() != null && vacancy.getEmployer().getName().equals(employer.getName())){
            ivAction.setVisibility(View.VISIBLE);
        }else{
            ivAction.setVisibility(View.GONE);
        }
        if(employer != null) {
            tvName.setText(employer.getName());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vacancy.setEmployer(employer);
                    Relation relation = new Relation();
                    relation.setObjectId(employer.getId());
                    requestRelation.setmRelation(relation);
                    NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                            VacancyAddFragment.newInstance(vacancy, requestRelation));
//                if(SignUpActivity.class.getName().contains(activity.getLocalClassName())){
//                    NavigationUtils.replaceWithFragment(activity, R.id.alo_fragment_container, SignUpFirstFragment.newInstance(user));
//                }else{
//                    NavigationUtils.replaceWithFragment(activity, R.id.frame_layout, EditPersonalInfoFragment.newInstance(context, user));
//                }
                }
            });
        }
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Employer> employers, Vacancy vacancy, RequestRelation requestRelation, BaseActivity activity) {
        List<BaseListItem> items = new ArrayList<>();
        if (employers != null) {
            for (int i = 0; i < employers.size(); i++) {
                Employer employer = employers.get(i);
                items.add(new EmployerOfListItem(employer, vacancy, requestRelation, activity));
            }
        }
        return items;
    }

    public Employer getEmployer() {
        return employer;
    }
}
