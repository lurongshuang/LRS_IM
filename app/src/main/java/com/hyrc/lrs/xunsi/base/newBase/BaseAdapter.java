package com.hyrc.lrs.xunsi.base.newBase;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyrc.lrs.xunsi.base.newBase.interfaceOnclick.OnAdapterOnclick;

/**
 * create by lrs 2019/10/12
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private BaseViewHolder helper;
    public Context Mcontext;

    public BaseAdapter(int layoutResId, Context context) {
        super(layoutResId);
        this.Mcontext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        itemInit(helper, item);

    }

    //初始化item
    protected abstract void itemInit(BaseViewHolder helper, T item);

    //点击事件
    public OnAdapterOnclick iadapterOnclick;

    public void setAdapterItemOnclick(OnAdapterOnclick adapterItemOnclick) {
        iadapterOnclick = adapterItemOnclick;
    }

}
