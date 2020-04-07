package com.hyrc.lrs.xunsi.activity.main.item

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyrc.lrs.xunsi.R
import com.hyrc.lrs.xunsi.activity.login.LoginActivity
import com.hyrc.lrs.xunsi.activity.main.MainActivity
import com.hyrc.lrs.xunsi.base.LazyLoadingFragment
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper
import com.xuexiang.xui.XUI
import java.util.*

/**
 * @description 作用:
 * @date: 2020/3/27
 * @author: 卢融霜
 */
class MyselfFragment : LazyLoadingFragment(){

    companion object {
        lateinit var fragment: MyselfFragment;
        fun newInstance(): MyselfFragment? {
            val args = Bundle()
            fragment =
                MyselfFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        root_view.findViewById<View>(R.id.btOuLogin).setOnClickListener {
            doOut();
        };
        root_view.findViewById<View>(R.id.btChanSkin).setOnClickListener {
            change();
        };
    }

    private fun change() {
        val state = SharedPreferencesHelper.getPrefInt(activity, "theme", -1) == -1;
        if (state) {
            SharedPreferencesHelper.setPrefInt(activity, "theme", 1);
            XUI.getContext().setTheme(R.style.AppBlueTheme)
        } else {
            SharedPreferencesHelper.setPrefInt(activity, "theme", -1);
            XUI.getContext().setTheme(R.style.AppTheme)
        }

    }

    override fun onFirstVisibleToUser(): Boolean {
        return false;
    }

    override fun onInvisibleToUser() {
    }

    override fun getLayRes(): Int {
        return R.layout.fragment_myself;
    }

    override fun onVisibleToUser() {
        (activity as MainActivity).setTitle(false, "我的");
    }


    /**
     * 登出
     */
    fun doOut() {
        SharedPreferencesHelper.setPrefString(activity, "userId", null)
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        MainActivity().doLogout();
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                activity?.finish()
            }
        }
        val timer = Timer()
        timer.schedule(task, 1 * 1000.toLong())
    }

}