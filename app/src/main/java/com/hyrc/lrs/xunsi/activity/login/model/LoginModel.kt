package com.hyrc.lrs.xunsi.activity.login.model

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import com.hyrc.lrs.xunsi.activity.main.MainActivity
import com.hyrc.lrs.xunsi.activity.register.RegisterActivity
import com.hyrc.lrs.xunsi.bean.User
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper
import com.hyrc.lrs.xunsi.view.LoadBaseDialog
import java.util.*


/**
 * @description 作用:
 * @date: 2020/3/24
 * @author: 卢融霜
 */
class LoginModel(content: Activity) : BaseObservable() {

    private var content: Activity = content;
    private lateinit var loadBaseDialog: LoadBaseDialog;
    /**
     * 手机号
     */
    val userPhone: ObservableField<String> = ObservableField();
    /**
     * 密码
     */
    val passWord: ObservableField<String> = ObservableField();

    /**
     * 登录
     */
    fun onLogin(v: View) {
        if (userPhone.get() == null || userPhone.get().toString().isEmpty()) {
            toastString("请输入账号");
            return;
        }
        if (passWord.get() == null || passWord.get().toString().isEmpty()) {
            toastString("请输入密码");
            return;
        }
        loadBaseDialog = LoadBaseDialog(content);
        loadBaseDialog.show();
        val bmobQuery = BmobQuery<User>();
        val sql =
            "select * from User where userPhone = '" + userPhone.get() + "' and passWord = '" + passWord.get() + "'";
        bmobQuery.doSQLQuery(sql, object : SQLQueryListener<User>() {
            override fun done(t: BmobQueryResult<User>?, e: BmobException?) {
                loadBaseDialog.dismiss();
                if (t != null && t.results.size > 0) {
                    var user: User = t.results[0];
                    toastString("登录成功：欢迎您：" + user.userName);
                    SharedPreferencesHelper.setPrefString(content, "userId", userPhone.get());
                    SharedPreferencesHelper.setPrefString(content, "userName", user.userName);
                    SharedPreferencesHelper.setPrefString(content, "userURL", user.userURL);
                    val intent = Intent(content, MainActivity().javaClass)
//                    intent.putExtra(MessageUtil.INTENT_EXTRA_USER_ID, userPhone.get())
                    content.startActivity(intent);
                    val task: TimerTask = object : TimerTask() {
                        override fun run() {
                            content.finish();
                        }
                    }
                    val timer = Timer()
                    timer.schedule(task, 1 * 1000)
                } else {
                    toastString("用户名密码错误，请重试");
                }
            }
        });
    }

    /**
     * 去注册
     */
    fun onToRegister(v: View) {
        val intent = Intent(content, RegisterActivity().javaClass);
        content.startActivity(intent)
    }

    /**
     * toast
     */
    fun toastString(text: String) {
        Toast.makeText(content, text, Toast.LENGTH_SHORT).show();
    }


}