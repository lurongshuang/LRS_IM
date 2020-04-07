package com.hyrc.lrs.xunsi.base.newBase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.LazyLoadingFragment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.statelayout.CustomStateOptions;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import butterknife.BindView;

public abstract class ListBaseFragment extends LazyLoadingFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout statefulLayout;
    private BaseAdapter adapter;


    @Override
    protected boolean onFirstVisibleToUser() {
        return false;
    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }

    @Override
    protected int getLayRes() {
        return R.layout.listbase_fragment;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
    }

    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        refreshLayout.setEnableAutoLoadMore(false);
        setRefreshData();
        adapter = initAdapter(adapter);
        recyclerView.setAdapter(adapter);
        loadData(adapter);
        /**
         * 下拉刷新
         */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                listonRefresh(refreshLayout, recyclerView);
                clearDatas();
                loadData(adapter);
                finishRefresh();
            }
        });
        /**
         * 上拉加载
         */
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                listOnLoadMore(refreshLayout, recyclerView);
                loadData(adapter);
                finishLoadMore();
            }

        });
    }

    //清理数据
    public void clearDatas() {
        if (recyclerView != null && recyclerView.getChildCount() > 0) {
            recyclerView.removeAllViews();
        }
        if (adapter != null && adapter.getItemCount() > 0) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 配置 上拉 下拉
     */
    public void setRefreshData() {
        Refresh(true);
        LoadMore(true);
    }

    /**
     * 是否启用上拉加载
     */
    public void LoadMore(boolean state) {
        refreshLayout.setEnableLoadMore(state);
    }

    /**
     * 是否启用下拉刷新
     */
    public void Refresh(boolean state) {
        refreshLayout.setEnableRefresh(state);
    }

    /**
     * 刷新完成
     */
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    /**
     * 加载完成
     */
    public void finishLoadMore() {
        refreshLayout.finishLoadMore();
    }

    /**
     * 无更多数据
     */
    public void finishRefreshWithNoMoreData() {
        refreshLayout.finishRefreshWithNoMoreData();
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (statefulLayout != null) {
            statefulLayout.showContent();
        }
    }

    /**
     * 是否停止继续上拉
     */
    public void setNomoreData(boolean state) {
        if (refreshLayout != null) {
            refreshLayout.setNoMoreData(state);
        }
    }

    /**
     * 显示加载中
     *
     * @param message 提示信息
     */
    public void showLoading(String message) {
        if (message != null && statefulLayout != null) {
            statefulLayout.showLoading(message);
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading() {
        if (statefulLayout != null) {
            statefulLayout.showLoading();
        }
    }

    /**
     * 显示暂无数据
     */
    public void showEmpty() {
        if (statefulLayout != null) {
            statefulLayout.showEmpty();
        }
    }

    /**
     * 显示暂无数据
     *
     * @param message 自定义信息
     */
    public void showEmpty(String message) {
        if (message != null && statefulLayout != null) {
            statefulLayout.showEmpty(message);
        }
    }

    /**
     * 显示错误
     *
     * @param clickListener 点击重试按钮
     */
    public void showError(View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null) {
            statefulLayout.showError(clickListener);
        }
    }

    /**
     * 显示错误
     *
     * @param message       自定义提示信息
     * @param clickListener 点击重试按钮
     */
    public void showError(String message, View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null && message != null) {
            statefulLayout.showError(message, clickListener);
        }
    }

    /**
     * 显示错误
     *
     * @param message       自定义提示信息
     * @param buttonText    重试按钮提示信息
     * @param clickListener 点击按钮事件
     */
    public void showError(String message, String buttonText, View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null && message != null && buttonText != null) {
            statefulLayout.showError(message, buttonText, clickListener);
        }
    }

    /**
     * 显示网络离线
     *
     * @param clickListener 点击按钮事件
     */
    public void showOffline(View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null) {
            statefulLayout.showOffline(clickListener);
        }
    }

    /**
     * 显示网络离线
     *
     * @param message       提示消息
     * @param clickListener 点击事件
     */
    public void showOffline(String message, View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null && message != null) {
            statefulLayout.showOffline(message, clickListener);
        }
    }

    /**
     * 显示网络离线
     *
     * @param message       提示消息
     * @param buttonText    按钮显示信息
     * @param clickListener 点击事件
     */
    public void showOffline(String message, String buttonText, View.OnClickListener clickListener) {
        if (statefulLayout != null && clickListener != null && message != null && buttonText != null) {
            statefulLayout.showOffline(message, buttonText, clickListener);
        }
    }

    /**
     * 显示自定义布局
     *
     * @param options 自定义布局
     */
    public void showCustom(final CustomStateOptions options) {
        if (statefulLayout != null && options != null) {
            statefulLayout.showCustom(options);
        }
    }

    /**
     * 下拉刷新
     */
    protected abstract void listonRefresh(RefreshLayout refreshLayout, RecyclerView recyclerView);

    /**
     * 上拉加载
     */
    protected abstract void listOnLoadMore(RefreshLayout refreshLayout, RecyclerView recyclerView);

    /**
     * 初始化adapter
     */
    protected abstract BaseAdapter initAdapter(BaseAdapter adapter);

    /**
     * 加载数据
     */
    protected abstract void loadData(BaseAdapter adapter);
}
