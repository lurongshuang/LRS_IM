package com.hyrc.lrs.xunsi.activity.group.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.newBase.BaseAdapter;
import com.hyrc.lrs.xunsi.bean.Group;

import io.rong.imkit.RongIM;

/**
 * @description 作用:
 * @date: 2020/4/3
 * @author: 卢融霜
 */
public class GroupListAdapter extends BaseAdapter<Group> {
    public GroupListAdapter(int layoutResId, Context context) {
        super(layoutResId, context);
    }

    @Override
    protected void itemInit(BaseViewHolder helper, Group item) {
        if (helper.getAdapterPosition() == 0) {
            helper.setGone(R.id.item_head_tv, true);
            helper.setGone(R.id.item_head_top, true);
            helper.setText(R.id.item_head_tv, "群聊");
        } else {
            helper.setGone(R.id.item_head_tv, false);
            helper.setGone(R.id.item_head_top, false);
            helper.setGone(R.id.item_head_bottom, false);
        }
        Glide.with(super.Mcontext).load(item.getGroupUrl()).into((ImageView) helper.getView(R.id.item_iv));
        helper.setText(R.id.item_tv, item.getGroupName());
        if (helper.getAdapterPosition() == getData().size() - 1) {
            helper.setGone(R.id.item_bottom, false);
        }
        LinearLayout item_cl = helper.getView(R.id.item_cl);
        item_cl.setTag(R.id.gridlist, item);
        item_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group it = (Group) v.getTag(R.id.gridlist);
                RongIM.getInstance().startGroupChat(GroupListAdapter.super.mContext, it.getGroupId(), it.getGroupName());
            }
        });
    }
}
