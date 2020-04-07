package com.hyrc.lrs.xunsi.base.newBase.interfaceOnclick;


import com.hyrc.lrs.xunsi.base.newBase.bean.BaseBean;

/**
 * @description 作用: adapter点击事件
 * @date: 2019/11/4
 * @author: 卢融霜
 */
public interface OnAdapterOnclick {
    /**
     * adapter 点击事件
     *
     * @param position 索引
     * @param bean     实体数据
     */
    void onClickListener(int position, BaseBean bean);
}
