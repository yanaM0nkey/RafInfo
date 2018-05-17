package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.User;
import com.yanazenkevich.rafinfo.signup.SignUpActivity;
import com.yanazenkevich.rafinfo.signup.SignUpFirstFragment;
import com.yanazenkevich.rafinfo.tabs.settings.EditPersonalInfoFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

public class DepartmentItem implements BaseListItem {

    private final Department department;
    private final BaseActivity activity;
    private User user;
    private TextView tvName;
    private ImageView ivAction;
    private ConstraintLayout layout;

    public DepartmentItem(Department department, User user, BaseActivity activity) {
        this.department = department;
        this.user = user;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_department;
    }

    @Override
    public void initViews(Context context, View view) {
        tvName = view.findViewById(R.id.id_department_name);
        ivAction = view.findViewById(R.id.id_action);
        layout = view.findViewById(R.id.id_layout);
    }

    @Override
    public void renderView(final Context context, View view) {
        if(user != null && user.getDepartment() != 0 && department.getId() == user.getDepartment()){
            ivAction.setVisibility(View.VISIBLE);
        }else{
            ivAction.setVisibility(View.GONE);
        }
        tvName.setText(department.getName());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setDepartment(department.getId());
                if(SignUpActivity.class.getName().contains(activity.getLocalClassName())){
                    NavigationUtils.replaceWithFragment(activity, R.id.alo_fragment_container, SignUpFirstFragment.newInstance(user));
                }else{
                    NavigationUtils.replaceWithFragment(activity, R.id.frame_layout, EditPersonalInfoFragment.newInstance(context, user));
                }
            }
        });
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Department> departments, User user, BaseActivity activity) {
        List<BaseListItem> items = new ArrayList<>();
        if (departments != null) {
            for (int i = 0; i < departments.size(); i++) {
                Department department = departments.get(i);
                items.add(new DepartmentItem(department, user, activity));
            }
        }
        return items;
    }

    public Department getDepartment() {
        return department;
    }
}
