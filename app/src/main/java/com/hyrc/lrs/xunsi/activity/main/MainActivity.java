package com.hyrc.lrs.xunsi.activity.main;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.login.LoginActivity;
import com.hyrc.lrs.xunsi.activity.main.item.ChatFragment;
import com.hyrc.lrs.xunsi.activity.main.item.FriendFragment;
import com.hyrc.lrs.xunsi.activity.main.item.MyselfFragment;
import com.hyrc.lrs.xunsi.adapter.MyFragmentPagerAdapter;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.hyrc.lrs.xunsi.utils.net.CustomCallBack;
import com.hyrc.lrs.xunsi.utils.net.NetworkUtils;
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper;
import com.hyrc.lrs.xunsi.view.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.mRadioGroupId)
    RadioGroup radioGroup;
    @BindView(R.id.radio0)
    RadioButton radioButton0;
    @BindView(R.id.radio1)
    RadioButton radioButton1;
    @BindView(R.id.radio2)
    RadioButton radioButton2;

    /**
     * 用户id
     */
    public String userId;
    private String userName;
    private String userURL;
    /**
     * handler
     */
    private Handler handler;

    /**
     * 消息监听
     */
    /**
     * 消息管理
     */
    /**
     * 消息客户端
     */

    /**
     * pageList
     *
     * @return
     */
    private List<Fragment> allFragment;


    @Override
    protected int loadView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        userId = SharedPreferencesHelper.getPrefString(this, "userId", null);
        userName = SharedPreferencesHelper.getPrefString(this, "userName", null);
        userURL = SharedPreferencesHelper.getPrefString(this, "userURL", null);
        if (userId == null) {
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1 * 1000);
            return;
        }
        //初始化page
        initPage();
        //重新计算底部按钮大小
        initButton();
        //im 进行自动登录
        autoLogin();
        //查询好友
        initHandle();
    }

    /**
     * 定时查询好友
     */
    private void initHandle() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 200:
                        initData();
                        break;
                }
            }
        };
    }


    /**
     * 消息自动登录
     */
    private void autoLogin() {
        String url = "http://api-cn.ronghub.com/user/getToken.json";
        NetworkUtils.getInstance().postToken(url, new CustomCallBack() {
            @Override
            public void onSuccess(Call call, String result) throws JSONException {
                Log.d("登录", result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null && jsonObject.getInt("code") == 200) {
                    String token = jsonObject.getString("token");
                    initRY(token);
                }
            }

            @Override
            public void onError(Call call, IOException e) {
            }
        }, "userId", userId, "name", userName, "portraitUri", userURL);
    }

    private void updateUserData() {
        String url = "http://api-cn.ronghub.com/user/refresh.json";
        NetworkUtils.getInstance().postToken(url, new CustomCallBack() {
            @Override
            public void onSuccess(Call call, String result) throws JSONException {
                Log.d("登录", result);
                UserInfo userInfo = new UserInfo(userId, userName, Uri.parse(userURL));
                RongIM.getInstance().setCurrentUserInfo(userInfo);
                RongIM.getInstance().refreshUserInfoCache(userInfo);
            }

            @Override
            public void onError(Call call, IOException e) {
            }
        }, "userId", userId, "name", userName, "portraitUri", userURL);
    }

    private void initRY(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d("登录", "--onTokenIncorrect");
                autoLogin();
            }

            @Override
            public void onSuccess(String s) {
                Log.d("登录", "--onSuccess" + s);
                updateUserData();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("登录", "--onError" + errorCode);
            }
        });

    }


    @Override
    protected void clearData() {

    }


    /**
     * 初始化page
     */
    private void initPage() {
        ChatFragment fragment_Item_1 = ChatFragment.Companion.newInstance();
        FriendFragment fragment_Item_2 = FriendFragment.Companion.newInstance();
        MyselfFragment fragment_Item_3 = MyselfFragment.Companion.newInstance();
        allFragment = new ArrayList<>();
        allFragment.add(fragment_Item_1);
        allFragment.add(fragment_Item_2);
        allFragment.add(fragment_Item_3);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), allFragment));
        viewPager.setCurrentItem(0);
    }

    /**
     * 初始化按钮
     */
    private void initButton() {
        RadioButton rab[] = {radioButton0, radioButton1, radioButton2};
        for (RadioButton radioButton : rab) {
            Drawable drs[] = radioButton.getCompoundDrawables();
            int w = (int) (drs[1].getMinimumWidth() / 5.6);
            int h = (int) (drs[1].getMinimumHeight() / 5.6);
            Rect r = new Rect(0, 0, w, h);
            drs[1].setBounds(r);
            radioButton.setCompoundDrawables(null, drs[1], null, null);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.radio0:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.radio1:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.radio2:
                        viewPager.setCurrentItem(2, false);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * API CALLBACK: rtm event listener
     */
    class MyRtmClientListener implements RtmClientListener {
        @Override
        public void onConnectionStateChanged(final int state, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (state) {
                        case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                            showToast(getString(R.string.reconnecting));
                            break;
                        case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                            showToast(getString(R.string.account_offline));
//                            setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                            finish();
                            break;
                    }
                }
            });
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    String content = message.getText();
//                    MessageUtil.addMessageBean(peerId, content);
//                    ChatFragment.Companion.addMessageView(peerId, content);
                }
            });
        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }
    }

    /**
     * API CALL: logout from RTM server
     */
    public void doLogout() {
        RongIM.getInstance().logout();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mChatManager != null) {
//            mChatManager.unregisterListener(mClientListener);
//        }
    }
}
