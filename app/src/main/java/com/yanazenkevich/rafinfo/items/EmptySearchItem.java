package com.yanazenkevich.rafinfo.items;

import android.content.Context;
import android.view.View;


import com.yanazenkevich.rafinfo.R;
import com.yanazenkevich.rafinfo.base.BaseListItem;


public class EmptySearchItem implements BaseListItem {

    public EmptySearchItem() {}

    @Override
    public int getViewId() {
        return R.layout.item_empty_search;
    }

    @Override
    public void initViews(Context context, View view) {
    }

    @Override
    public void renderView(final Context context, View view) {
    }
}
