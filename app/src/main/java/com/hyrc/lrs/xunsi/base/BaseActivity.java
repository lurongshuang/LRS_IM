package com.hyrc.lrs.xunsi.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gyf.barlibrary.ImmersionBar;
import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.splash.SplashActivity;
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper;
import com.hyrc.lrs.xunsi.view.FontIconView;
import com.hyrc.lrs.xunsi.view.LoadBaseDialog;
import com.xuexiang.xui.widget.statelayout.CustomStateOptions;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public abstract class BaseActivity extends AppCompatActivity {
    onPermissionCallBack permissionCallBack;
    //权限集合
    public static String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    public LoadBaseDialog loadBaseDialog;

    private String BId = "com.lrs.im";
    private myBroadCast myBroadCast;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.getPrefInt(this, "theme", -1) == -1) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppBlueTheme);
        }
        if (savedInstanceState != null) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setContentView(loadView());
        ButterKnife.bind(this);
        loadBaseDialog = new LoadBaseDialog(this);
        if (getPermossion()) {
            initData();
            //退出监听
            myBroadCast = new myBroadCast();
            IntentFilter intentFilter = new IntentFilter(BId);
            registerReceiver(myBroadCast, intentFilter);

//            ImmersionBar.with(this)
//                    .statusBarDarkFont(false, 1.0f)
//                    .statusBarColorTransformEnable(false)
//                    .statusBarColor(R.color.colorPrimary)
//                    .fitsSystemWindows(true)
//                    .init();
        }
    }

    /**
     * 获取权限
     */
    public boolean getPermossion() {
        return true;
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        clearData();
        if (myBroadCast != null) {
            unregisterReceiver(myBroadCast);
        }
        super.onDestroy();
    }

    public void exitApp() {
        Intent intent = new Intent(BId);
        intent.putExtra("close", 1);
        sendBroadcast(intent);
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    protected abstract int loadView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 清楚数据
     */
    protected abstract void clearData();

    /**
     * 提示信息  吐司
     */
    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 提示信息  吐司
     */
    public void showToast(String text, int type) {
        Toast.makeText(getApplicationContext(), text, type).show();
    }

    /**
     * 提示信息  吐司
     */
    public void showToast(int id) {
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
    }


    /**
     * 跳转activity
     */
    public void openAcitivty(Class activity) {
        openAcitivty(activity, null);
    }

    /**
     * 跳转activity
     */
    public void openAcitivty(Class activity, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    //申请回调
    public interface onPermissionCallBack {
        //获取权限成功
        void onSuccess();

        //禁止
        void choiceProhibit();

        //禁止不再询问
        void choiceProhibitNotAsking();
    }

    /**
     * 使用默认权限集合
     */
    public void requetPermission(onPermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
        if (ContextCompat.checkSelfPermission(BaseActivity.this, String.valueOf(permission)) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this, permission, 1);
        } else {
            //权限全部通过
            this.permissionCallBack.onSuccess();
        }
    }


    /**
     * 使用自定义权限集合  或者验证指定权限是够通过
     */
    public void requetPermission(String[] perm, onPermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
        if (ContextCompat.checkSelfPermission(BaseActivity.this, String.valueOf(perm)) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this, perm, 1);
        } else {
            //权限全部通过
            this.permissionCallBack.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    //选择了“始终允许”
                    if (i == permissions.length - 1) {
                        permissionCallBack.onSuccess();
                    }
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, permissions[i])) {
                        //用户选择了禁止不再询问
                        this.permissionCallBack.choiceProhibitNotAsking();
                    } else {
                        //选择禁止
                        this.permissionCallBack.choiceProhibit();
                    }
                }
            }
        }
    }

    /**
     * 设置状态栏
     *
     * @param isShowLeftRes 是否显示 左侧按钮
     * @param name          标题
     */
    public void setTitle(boolean isShowLeftRes, String name) {
        TextView textView = findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(name);
        }
        FontIconView fontIconView = findViewById(R.id.iv_leftIcon);
        if (fontIconView != null) {
            fontIconView.setVisibility(isShowLeftRes ? View.VISIBLE : View.GONE);
            fontIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        FontIconView tv_rightText = findViewById(R.id.tv_rightText);
        if (tv_rightText != null) {
            tv_rightText.setVisibility(View.GONE);
        }
    }

    /**
     * 设置状态栏
     *
     * @param isShowLeftRes 是否显示 左侧按钮
     * @param name          标题
     */
    public void setTitle(boolean isShowLeftRes, String name, String right) {
        TextView textView = findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(name);
        }
        FontIconView fontIconView = findViewById(R.id.iv_leftIcon);
        if (fontIconView != null) {
            fontIconView.setVisibility(isShowLeftRes ? View.VISIBLE : View.GONE);
            fontIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        FontIconView tv_rightText = findViewById(R.id.tv_rightText);
        if (tv_rightText != null) {
            tv_rightText.setVisibility(View.VISIBLE);
            tv_rightText.setText(right);
        }
    }

    /**
     * 设置状态栏
     *
     * @param isShowLeftRes 是否显示 左侧按钮
     * @param name          标题
     */
    public void setTitle(boolean isShowLeftRes, String name, String right, View.OnClickListener rightOnclick) {
        TextView textView = findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(name);
        }
        FontIconView fontIconView = findViewById(R.id.iv_leftIcon);
        if (fontIconView != null) {
            fontIconView.setVisibility(isShowLeftRes ? View.VISIBLE : View.GONE);
            fontIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        FontIconView tv_rightText = findViewById(R.id.tv_rightText);
        if (tv_rightText != null) {
            tv_rightText.setVisibility(View.VISIBLE);
            tv_rightText.setText(right);
            tv_rightText.setOnClickListener(rightOnclick);
        }
    }

    /**
     * 设置状态栏
     *
     * @param isShowLeftRes 是否显示 左侧按钮
     * @param name          标题
     */
    public void setTitle(boolean isShowLeftRes, String name, String right, View.OnClickListener rightOnclick, String rightTitle, View.OnClickListener rightViewOnclick) {
        TextView textView = findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(name);
        }
        FontIconView fontIconView = findViewById(R.id.iv_leftIcon);
        if (fontIconView != null) {
            fontIconView.setVisibility(isShowLeftRes ? View.VISIBLE : View.GONE);
            fontIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        if (right != null && rightOnclick != null) {
            FontIconView tv_rightText = findViewById(R.id.tv_rightText);
            if (tv_rightText != null) {
                tv_rightText.setVisibility(View.VISIBLE);
                tv_rightText.setText(right);
                tv_rightText.setOnClickListener(rightOnclick);
            }
        }
        if (rightTitle != null && rightViewOnclick != null) {
            TextView tv_rightTitle = findViewById(R.id.tv_rightTextTitle);
            if (tv_rightTitle != null) {
                tv_rightTitle.setVisibility(View.VISIBLE);
                tv_rightTitle.setText(rightTitle);
                tv_rightTitle.setOnClickListener(rightViewOnclick);
            }
        }
    }

    public interface SearchOnclickListener {
        void OnSearch(String text);
    }


    public void showKeyboard(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示内容
     */
    public void showContent(int id) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null) {
            statefulLayout.showContent();
        }
    }

    /**
     * 显示加载中
     *
     * @param message 提示信息
     */
    public void showLoading(int id, String message) {
        StatefulLayout statefulLayout = findViewById(id);
        if (message != null && statefulLayout != null) {
            statefulLayout.showLoading(message);
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading(int id) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null) {
            statefulLayout.showLoading();
        }
    }

    /**
     * 显示暂无数据
     */
    public void showEmpty(int id) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null) {
            statefulLayout.showEmpty();
        }
    }

    /**
     * 显示暂无数据
     *
     * @param message 自定义信息
     */
    public void showEmpty(int id, String message) {
        StatefulLayout statefulLayout = findViewById(id);
        if (message != null && statefulLayout != null) {
            statefulLayout.showEmpty(message);
        }
    }

    /**
     * 显示错误
     *
     * @param clickListener 点击重试按钮
     */
    public void showError(int id, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
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
    public void showError(int id, String message, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
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
    public void showError(int id, String message, String buttonText, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null && clickListener != null && message != null && buttonText != null) {
            statefulLayout.showError(message, buttonText, clickListener);
        }
    }

    /**
     * 显示网络离线
     *
     * @param clickListener 点击按钮事件
     */
    public void showOffline(int id, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
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
    public void showOffline(int id, String message, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
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
    public void showOffline(int id, String message, String buttonText, View.OnClickListener clickListener) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null && clickListener != null && message != null && buttonText != null) {
            statefulLayout.showOffline(message, buttonText, clickListener);
        }
    }

    /**
     * 显示自定义布局
     *
     * @param options 自定义布局
     */
    public void showCustom(int id, final CustomStateOptions options) {
        StatefulLayout statefulLayout = findViewById(id);
        if (statefulLayout != null && options != null) {
            statefulLayout.showCustom(options);
        }
    }

    public class myBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int close = intent.getIntExtra("close", 0);
            if (close == 1) {
                finish();
            }
        }
    }
}
