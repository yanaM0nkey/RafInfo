package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;
import com.yanazenkevich.rafinfo.tabs.settings.EditEmployerInfoFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;


import java.util.ArrayList;
import java.util.List;

public class EmployerEditItem implements BaseListItem {

    private final Employer employer;
    private final BaseActivity activity;
    private TextView tvName;
    private ConstraintLayout layout;

    public EmployerEditItem(Employer employer, BaseActivity activity) {
        this.employer = employer;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_employer_of_list;
    }

    @Override
    public void initViews(Context context, View view) {
        tvName = view.findViewById(R.id.ieol_employer_name);
        layout = view.findViewById(R.id.ieol_layout);
    }

    @Override
    public void renderView(final Context context, View view) {
        if(employer != null) {
            tvName.setText(employer.getName());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationUtils.replaceWithFragment(activity, R.id.frame_layout,
                            EditEmployerInfoFragment.newInstance(employer));
                }
            });
        }
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Employer> employers, BaseActivity activity) {
        List<BaseListItem> items = new ArrayList<>();
        if (employers != null) {
            for (int i = 0; i < employers.size(); i++) {
                Employer employer = employers.get(i);
                items.add(new EmployerEditItem(employer, activity));
            }
        }
        return items;
    }

    public Employer getEmployer() {
        return employer;
    }
}

