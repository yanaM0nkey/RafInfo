package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseActivity;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.RequestRelation;
import com.yanazenkevich.rafinfo.entities.Staff;
import com.yanazenkevich.rafinfo.tabs.staff_info.StaffEditFragment;
import com.yanazenkevich.rafinfo.tabs.vacancies.VacancyEditFragment;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.List;

public class StaffItem implements BaseListItem {

    private final Staff staff;
    private final boolean isAdmin;
    private final BaseActivity activity;
    private TextView tvPosition;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private ImageView ivEdit;

    public StaffItem(Staff staff, BaseActivity activity, boolean isAdmin) {
        this.staff = staff;
        this.isAdmin = isAdmin;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_staff;
    }

    @Override
    public void initViews(Context context, View view) {
        tvPosition = view.findViewById(R.id.is_position);
        tvName = view.findViewById(R.id.is_name);
        tvLocation = view.findViewById(R.id.is_location);
        tvEmail = view.findViewById(R.id.is_email);
        tvPhoneNumber = view.findViewById(R.id.is_phone_number);
        ivEdit = view.findViewById(R.id.is_edit);
    }

    @Override
    public void renderView(final Context context, View view) {
        ivEdit.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        tvLocation.setVisibility(!TextUtils.isEmpty(staff.getAddress()) ? View.VISIBLE : View.GONE);
        tvEmail.setVisibility(!TextUtils.isEmpty(staff.getEmail()) ? View.VISIBLE : View.GONE);
        tvPhoneNumber.setVisibility(!TextUtils.isEmpty(staff.getPhoneNumber()) ? View.VISIBLE : View.GONE);
        tvName.setText(staff.getFullName());
        tvLocation.setText(staff.getAddress());
        tvEmail.setText(staff.getEmail());
        tvPhoneNumber.setText(staff.getPhoneNumber());
        if(!TextUtils.isEmpty(staff.getDescription())){
            tvPosition.setText(context.getString(R.string.staff_position, staff.getPosition(), staff.getDescription()));
        }else{
            tvPosition.setText(staff.getPosition());
        }
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                        StaffEditFragment.newInstance(staff));
            }
        });
    }

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Staff> listStaff, BaseActivity activity, boolean isAdmin) {
        List<BaseListItem> items = new ArrayList<>();
        if (listStaff != null) {
            for (int i = 0; i < listStaff.size(); i++) {
                Staff staff = listStaff.get(i);
                items.add(new StaffItem(staff, activity, isAdmin));
            }
        }
        return items;
    }

    public Staff getStaff() {
        return staff;
    }
}
