package com.hyrc.lrs.xunsi.activity.register;

import android.view.View;
import android.widget.EditText;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.hyrc.lrs.xunsi.bean.User;
import com.hyrc.lrs.xunsi.view.LoadBaseDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_register_password)
    EditText et_register_password;
    @BindView(R.id.et_register_name)
    EditText et_register_name;
    @BindView(R.id.et_register_url)
    EditText et_register_url;
    private String phone;
    private LoadBaseDialog loadBaseDialog;

    @Override
    protected int loadView() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initData() {
        setTitle(true, "设置密码");
        phone = getIntent().getExtras().getString("phone", null);
        if (phone == null || phone.isEmpty()) {
            showToast("数据异常");
            finish();
        }
        loadBaseDialog = new LoadBaseDialog(this);
    }

    @Override
    protected void clearData() {
        loadBaseDialog = null;
        phone = null;
        et_register_name = null;
        et_register_password = null;
    }

    @OnClick(R.id.btn_comit)
    public void onSumit(View view) {
        if (et_register_url.getText().toString().trim().isEmpty()) {
            showToast("请输入头像地址");
            return;
        }
        if (et_register_name.getText().toString().trim().isEmpty()) {
            showToast("请输入昵称");
            return;
        }
        if (et_register_password.getText().toString().trim().isEmpty()) {
            showToast("请输入密码");
            return;
        }
        if (et_register_password.getText().toString().trim().length() < 6) {
            showToast("密码至少六位");
            return;
        }

        User user = new User(
                et_register_name.getText().toString(),
                et_register_password.getText().toString(),
                phone,
                et_register_url.getText().toString()
        );
        loadBaseDialog.show();
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                loadBaseDialog.dismiss();
                if (e == null) {
                    showToast("注册成功");
                    finish();
                } else {
                    showToast("注册失败" + e.getMessage());
                }
            }
        });

    }
}
