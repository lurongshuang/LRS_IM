package com.hyrc.lrs.xunsi.activity.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hyrc.lrs.xunsi.R
import com.hyrc.lrs.xunsi.activity.register.model.RegisterModel
import com.hyrc.lrs.xunsi.databinding.Register
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper

class RegisterKolinActivity : AppCompatActivity() {
    private lateinit var register: Register;
    private lateinit var rm: RegisterModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPreferencesHelper.getPrefInt(this, "theme", -1) == -1) {
            setTheme(R.style.AppTheme)
        } else {
            setTheme(R.style.AppBlueTheme)
        }
        setContentView(R.layout.activity_register_kolin)
        register = DataBindingUtil.setContentView(this, R.layout.activity_register_kolin);

        initModel();
    }

    private fun initModel() {
        rm = RegisterModel(this);
        register.regi = rm;
    }
}