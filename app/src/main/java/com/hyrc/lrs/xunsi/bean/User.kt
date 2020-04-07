package com.hyrc.lrs.xunsi.bean

import cn.bmob.v3.BmobObject

/**
 * @description 作用: 用户表
 * @date: 2020/3/24
 * @author: 卢融霜
 */
class User() : BmobObject() {
    constructor(userName: String, passWord: String, userPhone: String, userURL: String) : this() {
        this.userName = userName;
        this.userPhone = userPhone;
        this.passWord = passWord;
        this.userURL = userURL;
    }

    /**
     * 用户名
     */
    lateinit var userName: String;

    /**
     * 密码
     */
    lateinit var passWord: String;

    /**
     * 手机号
     */
    lateinit var userPhone: String;

    /**
     * 头像地址
     */
    lateinit var userURL: String;


}