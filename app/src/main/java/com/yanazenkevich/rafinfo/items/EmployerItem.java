package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Employer;

public class EmployerItem implements BaseListItem {

    private final Employer employer;
    private TextView tvEmployer;
    private TextView tvSite;
    private TextView tvDescription;

    public EmployerItem(Employer employer) {
        this.employer = employer;
    }

    @Override
    public int getViewId() {
        return R.layout.item_employer;
    }

    @Override
    public void initViews(Context context, View view) {
        tvEmployer = view.findViewById(R.id.ie_employer);
        tvSite = view.findViewById(R.id.ie_site);
        tvDescription = view.findViewById(R.id.ie_description);
    }

    @Override
    public void renderView(final Context context, View view) {
        tvEmployer.setText(employer.getName());
        tvSite.setText(employer.getSite());
        tvDescription.setText(employer.getInfo());
    }

    public Employer getEmployer() {
        return employer;
    }
}
