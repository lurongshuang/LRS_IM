package com.hyrc.lrs.xunsi.bean

import kotlin.properties.Delegates

/**
 * @description 作用: 置顶
 * @date: 2020/3/24
 * @author: 卢融霜
 */
class TopUser() {
    constructor(userId: String, userName: String, userURL: Int) : this() {
        this.userId = userId;
        this.userName = userName;
        this.userURL = userURL;
    }

    /**
     * 用户名
     */
    lateinit var userName: String;

    /**
     * id
     */
    lateinit var userId: String;

    /**
     * 头像地址
     */
    var userURL by Delegates.notNull<Int>();


}