package com.hyrc.lrs.xunsi.activity.group.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.group.adapter.onInterface.OnAdapterOnclick;
import com.hyrc.lrs.xunsi.base.newBase.BaseAdapter;
import com.hyrc.lrs.xunsi.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 作用:
 * @date: 2020/4/3
 * @author: 卢融霜
 */
public class SelectAdapter extends BaseAdapter<User> {
    private String myId;
    /**
     * 1 为选中 0为未选中
     */
    private Map<String, Integer> map = new HashMap<>();
    private OnAdapterOnclick adapterOnclick;

    public SelectAdapter(int layoutResId, Context context) {
        super(layoutResId, context);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public int getSelectNum() {
        int selectNum = 0;
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            if (m.getValue() == 1) {
                selectNum += 1;
            }
        }
        return selectNum;
    }

    public List<String> getSelectUser() {
        List<String> selectUser = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            if (m.getValue() == 1) {
                selectUser.add(m.getKey());
            }
        }
        selectUser.add(myId);
        return selectUser;
    }

    @Override
    protected void itemInit(BaseViewHolder helper, User item) {
        helper.setText(R.id.item_tv_qz, item.getUserName());
        LinearLayout linearLayout = helper.getView(R.id.linearLayout_it);
        if (item.getUserPhone().equals(myId)) {
            helper.setImageResource(R.id.ivchecnd, R.drawable.rc_voip_icon_checkbox_checked);
        } else {
            int id = map.getOrDefault(item.getUserPhone(), -1);
            if (id == 1) {
                helper.setImageResource(R.id.ivchecnd, R.drawable.rc_voip_icon_checkbox_hover);
                linearLayout.setTag(R.id.rc_audio_state_image, true);
            } else if (id == -1) {
                map.put(item.getUserPhone(), 0);
                helper.setImageResource(R.id.ivchecnd, R.drawable.rc_voip_icon_checkbox_normal);
                linearLayout.setTag(R.id.rc_audio_state_image, false);
            } else {
                helper.setImageResource(R.id.ivchecnd, R.drawable.rc_voip_icon_checkbox_normal);
                linearLayout.setTag(R.id.rc_audio_state_image, false);
            }
        }
        Glide.with(super.Mcontext).load(item.getUserURL()).into((ImageView) helper.getView(R.id.item_iv_qz));
        linearLayout.setTag(R.id.index_list, helper.getAdapterPosition());
        linearLayout.setTag(R.id.gridlist, item.getUserPhone());
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thisId = (String) v.getTag(R.id.gridlist);
                ImageView imageView = v.findViewById(R.id.ivchecnd);
                if (thisId.equals(myId)) {
                    imageView.setImageResource(R.drawable.rc_voip_icon_checkbox_checked);
                    return;
                }
                int position = (int) v.getTag(R.id.index_list);
                boolean state = (boolean) v.getTag(R.id.rc_audio_state_image);
                if (!state) {
                    imageView.setImageResource(R.drawable.rc_voip_icon_checkbox_hover);
                } else {
                    imageView.setImageResource(R.drawable.rc_voip_icon_checkbox_normal);
                }
                linearLayout.setTag(R.id.rc_audio_state_image, !state);
                map.put(thisId, !state ? 1 : 0);
                if (adapterOnclick != null) {
                    adapterOnclick.onClickListener(position, !state);
                }
            }
        });
    }

    public void setUserId(String userId) {
        this.myId = userId;
    }

    public void setAdapterOnclick(OnAdapterOnclick onclick) {
        this.adapterOnclick = onclick;
    }
}
