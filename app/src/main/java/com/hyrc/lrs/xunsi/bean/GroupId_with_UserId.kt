package com.hyrc.lrs.xunsi.bean

import cn.bmob.v3.BmobObject

/**
 * @description 作用: 群组 用户关系表
 * @date: 2020/4/1
 * @author: 卢融霜
 */
class GroupId_with_UserId() : BmobObject() {
    constructor(groupId: String, userId: String) : this() {
        this.groupId = groupId;
        this.userId = userId;
    }

    lateinit var groupId: String;
    lateinit var userId: String;
}