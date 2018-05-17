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
import com.yanazenkevich.rafinfo.entities.Announcement;
import com.yanazenkevich.rafinfo.tabs.announcement.AnnouncementDetailsFragment;
import com.yanazenkevich.rafinfo.utils.DateUtils;
import com.yanazenkevich.rafinfo.utils.NavigationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementItem implements BaseListItem {

    private final Announcement announcement;
    private final BaseActivity activity;
    private TextView tvStatus;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDate;
    private CardView cardView;


    public AnnouncementItem(Announcement announcement, BaseActivity activity) {
        this.announcement = announcement;
        this.activity = activity;
    }

    @Override
    public int getViewId() {
        return R.layout.item_announcement;
    }

    @Override
    public void initViews(Context context, View view) {
        tvStatus = view.findViewById(R.id.ia_status);
        tvTitle = view.findViewById(R.id.ia_title);
        tvLocation = view.findViewById(R.id.ia_location);
        tvDate = view.findViewById(R.id.ia_date);
        cardView = view.findViewById(R.id.ia_card_view);
    }

    @Override
    public void renderView(final Context context, View view) {
        tvTitle.setText(announcement.getTitle());
        tvLocation.setText(announcement.getLocation());
        tvStatus.setText(DateUtils.getStatus(announcement, context));
        tvDate.setText(DateUtils.getAnnouncementDate(announcement));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.replaceWithFragmentAndAddToBackStack(activity, R.id.frame_layout,
                        AnnouncementDetailsFragment.newInstance(announcement));
            }
        });
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

    @NonNull
    public static List<BaseListItem> getItems(@Nullable List<Announcement> announcements, BaseActivity activity) {
        List<BaseListItem> items = new ArrayList<>();
        if (announcements != null) {
            for (int i = 0; i < announcements.size(); i++) {
                Announcement announcement = announcements.get(i);
                items.add(new AnnouncementItem(announcement, activity));
            }
        }
        return items;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }
}
