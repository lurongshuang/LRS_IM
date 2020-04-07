package com.hyrc.lrs.xunsi.bean

import cn.bmob.v3.BmobObject

/**
 * @description 作用: 群组表
 * @date: 2020/4/1
 * @author: 卢融霜
 */
class Group() : BmobObject() {
    constructor(groupId: String, groupName: String, groupUrl: String) : this() {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupUrl = groupUrl;
    }

    lateinit var groupId: String;
    lateinit var groupName: String;
    lateinit var groupUrl: String;
}