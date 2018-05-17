package com.yanazenkevich.rafinfo.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;


public interface BaseListItem {

    /**
     * Get this item layout resource id.
     *
     * @return id
     */
    @LayoutRes
    int getViewId();

    /**
     * Called one time per view, use here binding of findViewById()
     *
     * @param context context to use
     * @param view    inflated view
     */
    void initViews(Context context, View view);

    /**
     * Render view to display. (Updating it's fields).
     *
     * @param context context to use
     * @param view    view to render
     */
    void renderView(Context context, View view);

}