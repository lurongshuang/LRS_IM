package com.hyrc.lrs.xunsi.activity.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gyf.barlibrary.ImmersionBar
import com.hyrc.lrs.xunsi.R
import com.hyrc.lrs.xunsi.activity.login.model.LoginModel
import com.hyrc.lrs.xunsi.databinding.Login
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper


class LoginActivity : AppCompatActivity() {
    private lateinit var login: Login;
    private lateinit var lm: LoginModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPreferencesHelper.getPrefInt(this, "theme", -1) == -1) {
            setTheme(R.style.AppTheme)
        } else {
            setTheme(R.style.AppBlueTheme)
        }


        login = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initModel();
        //设置沉浸色为白色
        ImmersionBar.with(this).statusBarColor(R.color.white).fitsSystemWindows(true).init();

    }

    private fun initModel() {
        lm = LoginModel(this);
        login.ln = lm;
    }
}
