package com.hyrc.lrs.xunsi.activity.register.model

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.hyrc.lrs.xunsi.bean.User

/**
 * @description 作用:
 * @date: 2020/3/24
 * @author: 卢融霜
 */
class RegisterModel(content: Activity) {
    private var content: Activity = content;

    /**
     * 用户名
     */
    val userName: ObservableField<String> = ObservableField();
    /**
     * 手机号
     */
    val userPhone: ObservableField<String> = ObservableField();
    /**
     * 密码
     */
    val passWord: ObservableField<String> = ObservableField();

    /**
     * 注册
     */
    fun onRegisterUser(v: View) {
        if (userName.get() == null || userName.get().toString().trim().isEmpty()) {
            Toast.makeText(content, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passWord.get() == null || passWord.get().toString().trim().isEmpty()) {
            Toast.makeText(content, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPhone.get() == null || userPhone.get().toString().trim().isEmpty()) {
            Toast.makeText(content, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        val user = User(
            userName.get().toString(),
            passWord.get().toString(),
            userPhone.get().toString(),
            "http://img5.imgtn.bdimg.com/it/u=2471723103,4261647594&fm=26&gp=0.jpg"
        );
        user.save(object : SaveListener<String>() {
            override fun done(objectId: String, e: BmobException?) {
                if (e == null) {
                    Toast.makeText(
                        content,
                        "注册成功",
                        Toast.LENGTH_SHORT
                    ).show();
                    content.finish();
                } else {
                    Toast.makeText(
                        content,
                        "注册失败" + e.message,
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}