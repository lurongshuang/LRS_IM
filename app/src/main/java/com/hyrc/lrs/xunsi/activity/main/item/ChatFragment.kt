package com.hyrc.lrs.xunsi.activity.main.item

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import com.hyrc.lrs.xunsi.R
import com.hyrc.lrs.xunsi.activity.main.MainActivity
import com.hyrc.lrs.xunsi.base.LazyLoadingFragment
import com.hyrc.lrs.xunsi.bean.GroupId_with_UserId
import com.hyrc.lrs.xunsi.bean.User
import io.rong.imkit.RongIM
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Group
import io.rong.imlib.model.UserInfo
import java.util.ArrayList


/**
 * @description 作用:
 * @date: 2020/3/26
 * @author: 卢融霜
 */
class ChatFragment : LazyLoadingFragment() {

    companion object {
        lateinit var fragment: ChatFragment;
        fun newInstance(): ChatFragment? {
            val args = Bundle()
            fragment =
                ChatFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun init(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        (activity as MainActivity).setTitle(false, "消息");
        var mConversationListFragment: ConversationListFragment = ConversationListFragment();
        val uri = Uri.parse("rong://" + activity?.getApplicationInfo()?.packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
            .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")
            .appendQueryParameter(
                Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(),
                "true"
            )
            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
            .build();
        mConversationListFragment.setUri(uri);
        val manager: FragmentManager? = activity?.getSupportFragmentManager();
        val transaction: FragmentTransaction? = manager?.beginTransaction();
        transaction?.replace(R.id.conversationlist, mConversationListFragment);
        transaction?.commit();
        RongIM.setUserInfoProvider({ userId -> findUserById(userId) }, true);
        RongIM.setGroupInfoProvider({ groupId -> findGroupById(groupId) }, true);
        RongIM.getInstance().setGroupMembersProvider { s, iGroupMemberCallback ->
            findGroupList(s, iGroupMemberCallback);
        }

    }

    //根据群组id 查询用户
    private fun findGroupList(groupId: String, cb: RongIM.IGroupMemberCallback) {
        var bmq = BmobQuery<User>();
        val sql =
            "select * from User where userPhone in (select userId from GroupId_with_UserId where groupId = '$groupId')";
        val list: MutableList<UserInfo> = ArrayList();
        bmq.doSQLQuery(sql, object : SQLQueryListener<User>() {
            override fun done(t: BmobQueryResult<User>?, e: BmobException?) {
                if (t != null && t.results.size > 0) {
                    for (u in t.results) {
                        list.add(UserInfo(u.userPhone, u.userName, Uri.parse(u.userURL)));
                    }
                    if (list?.size > 0) {
                        cb.onGetGroupMembersResult(list);
                    }
                }
            }
        });
    }

    //根据群组id 查询群组
    private fun findGroupById(groupId: String): Group? {
        var bmobQuery = BmobQuery<com.hyrc.lrs.xunsi.bean.Group>();
        var sql = "select * from Group where groupId = '$groupId'";
        bmobQuery.doSQLQuery(sql, object : SQLQueryListener<com.hyrc.lrs.xunsi.bean.Group>() {
            override fun done(
                t: BmobQueryResult<com.hyrc.lrs.xunsi.bean.Group>?,
                e: BmobException?
            ) {
                if (t != null && t.results.size > 0) {
                    val group = Group(
                        t.results[0].groupId,
                        t.results[0].groupName,
                        Uri.parse(t.results[0].groupUrl)
                    );
                    RongIM.getInstance().refreshGroupInfoCache(group);
                }
            }

        })
        return null;
    }

    //根据id 查询用户
    private fun findUserById(userId: String): UserInfo? {
        var infoIt = UserInfo(userId, "", Uri.parse(""));
        val bmobQuery = BmobQuery<User>();
        val sql = "select * from User where userPhone = '$userId'";
        bmobQuery.doSQLQuery(sql, object : SQLQueryListener<User>() {
            override fun done(t: BmobQueryResult<User>?, e: BmobException?) {
                if (t != null && t.results.size > 0) {
                    infoIt.name = t.results[0].userName;
                    infoIt.portraitUri = Uri.parse(t.results[0].userURL);
                    RongIM.getInstance().refreshUserInfoCache(infoIt);
                } else {
                    showToast("查询失败" + e?.message);
                }
            }
        });
        return infoIt;
    }

    override fun onFirstVisibleToUser(): Boolean {
        // 第一次对用户可见
        return true;
    }

    override fun onInvisibleToUser() {
        //对用户不可见
    }

    override fun getLayRes(): Int {
        return R.layout.fragment_chat
    }

    override fun onVisibleToUser() {
        (activity as MainActivity).setTitle(false, "消息");
    }
}