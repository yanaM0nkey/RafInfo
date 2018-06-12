package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Department;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.tabs.staff_info.StaffAddFragment;
import com.yanazenkevich.rafinfo.tabs.staff_info.StaffEditFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yanazenkevich.rafinfo.signup.DepartmentFragment.ACTION_ADD;
import static com.yanazenkevich.rafinfo.signup.DepartmentFragment.ACTION_EDIT;

public class DepartmentStaffItem implements BaseListItem {

    private final Department department;
    private final BaseActivity activity;
    private final int action;
    private Staff staff;
    private TextView tvName;
    private ImageView ivAction;
    private ConstraintLayout layout;

    public DepartmentStaffItem(Department department, Staff staff, BaseActivity activity, int action) {
        this.department = department;
        this.staff = staff;
        this.activity = activity;
        this.action = action;
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
        if(staff != null && staff.getDepartment() != 0 && department.getId() == staff.getDepartment()){
            ivAction.setVisibility(View.VISIBLE);
        }else{
            ivAction.setVisibility(View.GONE);
        }
        tvName.setText(department.getName());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                staff.setDepartment(department.getId());
                switch (action){
                    case ACTION_ADD:
                        NavigationUtils.replaceWithFragment(activity, R.id.frame_layout, StaffAddFragment.newInstance(staff));
                        break;
                    case ACTION_EDIT:
                        NavigationUtils.replaceWithFragment(activity, R.id.frame_layout, StaffEditFragment.newInstance(staff));
                        break;
                }
            }
        });
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Department> departments, Staff staff, BaseActivity activity, int action) {
        List<BaseListItem> items = new ArrayList<>();
        if (departments != null) {
            for (int i = 0; i < departments.size(); i++) {
                Department department = departments.get(i);
                items.add(new DepartmentStaffItem(department, staff, activity, action));
            }
        }
        return items;
    }

    public Department getDepartment() {
        return department;
    }
}
