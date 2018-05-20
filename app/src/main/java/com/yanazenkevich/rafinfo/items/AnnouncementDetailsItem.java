package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.utils.DateUtils;

import java.util.Date;

public class AnnouncementDetailsItem implements BaseListItem {

    private final Announcement announcement;
    private TextView tvStatus;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDate;
    private TextView tvDescription;

    public AnnouncementDetailsItem(Announcement announcement) {
        this.announcement = announcement;
    }

    @Override
    public int getViewId() {
        return R.layout.item_announcement_details;
    }

    @Override
    public void initViews(Context context, View view) {
        tvStatus = view.findViewById(R.id.iad_status);
        tvTitle = view.findViewById(R.id.iad_title);
        tvLocation = view.findViewById(R.id.iad_location);
        tvDate = view.findViewById(R.id.iad_date);
        tvDescription = view.findViewById(R.id.iad_description);
    }

    @Override
    public void renderView(final Context context, View view) {
        tvTitle.setText(announcement.getTitle());
        tvDescription.setText(announcement.getDescription());
        tvLocation.setText(announcement.getLocation());
        tvStatus.setText(DateUtils.getStatus(announcement, context));
        tvDate.setText(DateUtils.getAnnouncementDateFull(announcement));
        isActiveItem(context);
    }

    private void isActiveItem(Context context){
        if(new Date(announcement.getDate()).getTime() < new Date().getTime()){
            tvDate.setTextColor(context.getResources().getColor(R.color.dimGray));
            tvLocation.setTextColor(context.getResources().getColor(R.color.dimGray));
        }else{
            tvDate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            tvLocation.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    public Announcement getAnnouncement() {
        return announcement;
    }
}

