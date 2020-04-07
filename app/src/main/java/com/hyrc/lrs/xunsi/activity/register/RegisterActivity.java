package com.hyrc.lrs.xunsi.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_register_ph)
    EditText etRegisterPh;
    @BindView(R.id.btn_register)
    XUIAlphaButton btnRegister;
    @BindView(R.id.tvuserTreaty)
    TextView tvuserTreaty;

    @Override
    protected int loadView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        setTitle(true, "用户注册");
        showKeyboard(etRegisterPh);
    }

    @OnClick(R.id.btn_register)
    public void register(View view) {
        if (etRegisterPh.getText().toString().trim().isEmpty()) {
            showToast("请输入手机号码");
            return;
        }
        if (etRegisterPh.getText().toString().trim().length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone", etRegisterPh.getText().toString());
        openAcitivty(PhoneCodeActivity.class, bundle);
        finish();
    }

    @Override
    protected void clearData() {

    }
}
