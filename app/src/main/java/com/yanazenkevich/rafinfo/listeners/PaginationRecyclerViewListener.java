//package com.yanazenkevich.rafinfo.listeners;
//
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.leverx.espresa.module.BaseLoaderPagination;
//
///**
// * Created by aliaksandr.tabunou on 3/20/2018.
// */
//
//public abstract class PaginationRecyclerViewListener extends RecyclerView.OnScrollListener {
//
//    private static final int PAGE_SIZE = 10;
//    private final int pageSize;
//
//    private final LinearLayoutManager layoutManager;
//    private final LoaderManager loaderManager;
//    private final int loaderId;
//
//    public PaginationRecyclerViewListener(LinearLayoutManager layoutManager, LoaderManager loaderManager, int loaderId) {
//        this(layoutManager,loaderManager,loaderId,PAGE_SIZE);
//    }
//
//    public PaginationRecyclerViewListener(LinearLayoutManager layoutManager, LoaderManager loaderManager, int loaderId, int pageSize) {
//        super();
//        this.layoutManager = layoutManager;
//        this.loaderManager = loaderManager;
//        this.loaderId = loaderId;
//        this.pageSize = pageSize;
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//        final int visibleItemCount = layoutManager.getChildCount();
//        final int totalItemCount = layoutManager.getItemCount();
//        final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
//
//            Loader<?> loader = loaderManager.getLoader(loaderId);
//
//            BaseLoaderPagination oldLoader = (BaseLoaderPagination) loader;
//            if (oldLoader != null) {
//                if (oldLoader.loadNext()) {
//                    showProgressPagination(true);
//                }
//            }
//        }
//    }
//
//    protected abstract void showProgressPagination(boolean show);
//}
