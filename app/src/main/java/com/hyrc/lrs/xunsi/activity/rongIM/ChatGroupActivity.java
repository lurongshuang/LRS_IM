package com.hyrc.lrs.xunsi.activity.rongIM;

import android.net.Uri;

import androidx.fragment.app.FragmentManager;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ChatGroupActivity extends BaseActivity {

    @Override
    protected int loadView() {
        return R.layout.activity_main4;
    }

    @Override
    protected void initData() {
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragement = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        String title = getIntent().getData().getQueryParameter("title");
        String targetId = getIntent().getData().getQueryParameter("targetId");
        String mConversationType = getIntent().getData().getQueryParameter("type");
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation")
                .appendQueryParameter("title", title)
                .appendPath(Conversation.ConversationType.GROUP.getName())
                .appendQueryParameter("targetId", targetId).build();
        fragement.setUri(uri);



        setTitle(true, title);
        getRemoteHistoryMessages(targetId);
    }

    private void getRemoteHistoryMessages(String targetId) {
        /**
         * 获取服务端历史消息。
         */
//        RongIM.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, 0, 20, new RongIMClient.ResultCallback<List<Message>>() {
//            @Override
//            public void onSuccess(List<Message> messages) {
//                Log.e("","");
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode e) {
//                Log.e("","");
//            }
//        });
    }

    @Override
    protected void clearData() {

    }
}
