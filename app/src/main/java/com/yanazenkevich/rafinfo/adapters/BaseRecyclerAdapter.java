package com.yanazenkevich.rafinfo.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanazenkevich.rafinfo.base.BaseListItem;
import com.yanazenkevich.rafinfo.base.BaseOnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {

    public final static int HOLDER_TAG_KEY = 0x03000289;

    /**
     * Default context for adapter.
     */
    private final Context mContext;


    /**
     * List of items needed to represent.
     */
    private final List<BaseListItem> mDataset = new ArrayList<>();

    /**
     * ClickListener for adapter.
     */
    private BaseOnItemClickListener mClickListener;

    /**
     * Getter for mDataset.
     *
     * @return list of BaseListItem of this adapter
     */
    public List<BaseListItem> getmDataset() {
        //noinspection unchecked
        return mDataset;
    }

    /**
     * Provide a reference to the views for each data item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View holderView;
        private BaseListItem holderItem;
        private final BaseOnItemClickListener holderClickListener;

        public ViewHolder(View v, BaseOnItemClickListener holderClickListener) {
            super(v);
            holderView = v;
            this.holderClickListener = holderClickListener;
            holderView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (holderClickListener != null && holderItem != null) {
                holderClickListener.onClick(holderItem);
            }
        }

        public BaseOnItemClickListener getClickListener() {
            return holderClickListener;
        }
    }


    public BaseRecyclerAdapter(Context context, List<? extends BaseListItem> dataset, BaseOnItemClickListener listener) {
        mContext = context;
        mDataset.addAll(dataset);
        mClickListener = listener;

    }

    public BaseRecyclerAdapter(Context context, List<? extends BaseListItem> dataset) {
        this(context, dataset, null);
    }

    public BaseRecyclerAdapter(Context context, BaseOnItemClickListener listener) {
        mContext = context;
        mClickListener = listener;
    }

    public BaseRecyclerAdapter(Context context) {
        this(context, new ArrayList<BaseListItem>());
    }

    public void setmClickListener(BaseOnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public BaseOnItemClickListener getmClickListener() {
        return mClickListener;
    }

    /**
     * Create new views (invoked by the layout manager).
     *
     * @param parent   parent
     * @param viewType viewType
     * @return viewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ViewDataBinding binding = null;
        try {
            binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), viewType, parent, false);
        } catch (RuntimeException e) {
            e.printStackTrace(); //handle the case when we delete biding from xml and the biding file is still in project. To remove the file we need to clean the project
        }
        View v = binding != null ? binding.getRoot() : LayoutInflater.from(mContext).inflate(viewType, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mClickListener);
        v.setTag(HOLDER_TAG_KEY, viewHolder);
        return viewHolder;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager).
     *
     * @param holder   viewHolder
     * @param position position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseListItem item = mDataset.get(position);
        boolean renderFirstTime = holder.holderItem == null || holder.holderItem != item;
        holder.holderItem = item;
        View holderV = holder.holderView;
        if (renderFirstTime) {
            holder.holderItem.initViews(mContext, holderV);
        }
        holder.holderItem.renderView(mContext, holderV);
    }

    /**
     * (invoked by the layout manager).
     *
     * @return the size of dataset
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getViewId();
    }

    /**
     * Get BaseListItem by position.
     *
     * @param position position in mDataset
     * @return BaseListItem by position
     */
    public BaseListItem getItem(int position) {
        return (BaseListItem) mDataset.get(position);
    }

    @Nullable
    public BaseListItem getLastItem() {
        return (mDataset.size() > 0 ? mDataset.get(mDataset.size() - 1) : null);
    }

    /**
     * Add list of BaseListItem to existing mDataset.
     *
     * @param newElements list of BaseListItem to add
     */
    public void addElements(List<? extends BaseListItem> newElements) {
        mDataset.addAll(newElements);
    }

    /**
     * Add elements to dataset
     *
     * @param position position to start insertion
     * @param items    added items
     */
    public void addElements(int position, List<? extends BaseListItem> items) {
        mDataset.addAll(position, items);
    }

    /**
     * Replace all existing mDataSet elements with newElements.
     *
     * @param newElements list of BaseListItem
     */
    public void replaceElements(List<? extends BaseListItem> newElements) {
        mDataset.clear();
        mDataset.addAll(newElements);
    }

    public void clear() {
        mDataset.clear();
    }

    /**
     * Add element to dataset
     *
     * @param position position of added element
     * @param item     added element
     */
    public void addElement(int position, BaseListItem item) {
        mDataset.add(position, item);
    }

    /**
     * Remove element from dataset
     *
     * @param item removed element
     */
    public void removeElement(BaseListItem item) {
        mDataset.remove(item);
    }


    public void removeElements(List<BaseListItem> items) {
        mDataset.removeAll(items);
    }

    /**
     * Remove element from dataset by position
     *
     * @param position removed element position
     */
    public void removeElementByPosition(int position) {
        if (mDataset.size() > position) {
            mDataset.remove(position);
        }
    }

    /**
     * Delete all elements except one in selected position.
     *
     * @param exceptPosition position of element which will not be deleted
     * @return count of deleted elements
     */
    public int deleteAllElementsExcept(int exceptPosition) {
        List<BaseListItem> deletedList = new ArrayList<>(mDataset.size());
        for (int i = 0; i < mDataset.size(); i++) {
            if (i != exceptPosition) {
                deletedList.add((BaseListItem) mDataset.get(i));
            }
        }
        mDataset.removeAll(deletedList);
        return deletedList.size();
    }

    @NonNull
    public List<BaseListItem> getItemsAfter(BaseListItem item) {
        List<BaseListItem> itemsAfter = new ArrayList<>();
        final int position = mDataset.indexOf(item);
        if (position >= 0 && (position + 1) < mDataset.size()) {
            for (int i = (position + 1); i < mDataset.size(); i++) {
                itemsAfter.add((BaseListItem) mDataset.get(i));
            }
        }
        return itemsAfter;
    }

    //Animation

    /**
     * @param pivotItem Element after which add addItems
     */
    public void addElementsAnimated(BaseListItem pivotItem, BaseListItem addItem) {
        addElementsAnimated(pivotItem, Collections.singletonList(addItem));
    }

    /**
     * Add item before the pivot item
     */
    public void addElementsBeforePivotItemAnimated(BaseListItem pivotItem, BaseListItem addItem) {
        final int pivotPosition = mDataset.indexOf(pivotItem);
        addElement(pivotPosition, addItem);
        notifyItemRangeInserted(pivotPosition, 1);
    }

    /**
     * @param pivotItem Element after which add items
     */
    public void addElementsAnimated(BaseListItem pivotItem, List<? extends BaseListItem> items) {
        int pivotPosition = mDataset.indexOf(pivotItem);
        if (pivotPosition >= 0) {
            final int startPosition = pivotPosition + 1;
            addElements(startPosition, items);
            notifyItemRangeInserted(startPosition, items.size());
        }
    }

    public void deleteElementsAnimated(BaseListItem... items) {
        deleteElementsAnimated(Arrays.asList(items));
    }

    public void deleteElementsAnimated(@Nullable List<? extends BaseListItem> items) {
        final List<Integer> findPos = new ArrayList<>();
        final List<BaseListItem> findItems = new ArrayList<>();
        for (BaseListItem item : items) {
            final int position = mDataset.indexOf(item);
            if (position >= 0) {
                findPos.add(position);
                findItems.add(item);
            }
        }
        final int size = findPos.size();
        if (size > 0) {
            removeElements(findItems);
            final int startPos = findPos.get(0);
            final int endPos = findPos.get(findPos.size() - 1);
            //try to notify range
            if (endPos - startPos == findPos.size() - 1) {
                notifyItemRangeRemoved(startPos, findPos.size());
            } else {
                // notify
                for (int pos : findPos) {
                    notifyItemRemoved(pos);
                }
            }
        }
    }
}