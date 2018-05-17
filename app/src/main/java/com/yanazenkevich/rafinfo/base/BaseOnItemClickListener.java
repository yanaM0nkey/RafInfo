package com.yanazenkevich.rafinfo.base;

import com.yanazenkevich.rafinfo.base.BaseListItem;

public interface BaseOnItemClickListener {

    /**
     * Callback for item was clicked.
     *
     * @param item clicked item
     */
    void onClick(BaseListItem item);
}

