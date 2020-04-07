package com.hyrc.lrs.xunsi.activity.rongIM;

import android.net.Uri;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;

import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;

public class JHChatActivity extends BaseActivity {

    @Override
    protected int loadView() {
        return R.layout.activity_j_h_chat;
    }

    @Override
    protected void initData() {
//        String title = getIntent().getData().getQueryParameter("title");
//        String targetId = getIntent().getData().getQueryParameter("targetId");
        SubConversationListFragment mSubConversationListFragment = new SubConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("subconversationlist")
//                .appendQueryParameter("title", title)
//                .appendQueryParameter("targetId", targetId)
                .appendQueryParameter("type", Conversation.ConversationType.GROUP.getName())
                .build();
        mSubConversationListFragment.setUri(uri);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.subconversationlist, mSubConversationListFragment);
        transaction.commit();
        setTitle(true, "群组");
    }

    @Override
    protected void clearData() {

    }
}
