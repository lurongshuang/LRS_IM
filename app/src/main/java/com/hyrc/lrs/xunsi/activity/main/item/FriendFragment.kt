package com.hyrc.lrs.xunsi.activity.main.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import com.hyrc.lrs.xunsi.R
import com.hyrc.lrs.xunsi.activity.group.GroupListActivity
import com.hyrc.lrs.xunsi.activity.group.SelectActivity
import com.hyrc.lrs.xunsi.activity.main.MainActivity
import com.hyrc.lrs.xunsi.activity.main.item.adapter.ContactAdapter
import com.hyrc.lrs.xunsi.base.LazyLoadingFragment
import com.hyrc.lrs.xunsi.bean.TopUser
import com.hyrc.lrs.xunsi.bean.User
import com.hyrc.lrs.xunsi.utils.net.ToastUtils
import com.hyrc.lrs.xunsi.utils.pinyin.PinyinUtils
import com.hyrc.lrs.xunsi.view.DropView
import com.hyrc.lrs.xunsi.view.IndexView
import com.xuexiang.xui.adapter.simple.AdapterItem
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup.OnPopupItemClickListener
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import java.util.*


/**
 * @description 作用:
 * @date: 2020/3/27
 * @author: 卢融霜
 */
class FriendFragment : LazyLoadingFragment(), IndexView.OnWordsChangeListener,
    ContactAdapter.OnClickListener {
    private var userId: String? = null;

    //    private lateinit var handler: Handler;
    private var list: MutableList<User> = ArrayList();
    private var topList: MutableList<TopUser> = ArrayList();
    private lateinit var adapter: ContactAdapter;
    private lateinit var rllist: RecyclerView;
    private lateinit var layoutManager: LinearLayoutManager;
    private lateinit var drop_view: DropView;
    private lateinit var index_list: IndexView;


    companion object {
        lateinit var fragment: FriendFragment;
        fun newInstance(): FriendFragment? {
            val args = Bundle()
            fragment =
                FriendFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
//        initHandle();
        initData();
        //初始化 RecyclerView
        rllist = root_view.findViewById(R.id.contact_rv);
        drop_view = root_view.findViewById(R.id.drop_view);
        index_list = root_view.findViewById(R.id.index_list);
        layoutManager = LinearLayoutManager(activity);
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        adapter = ContactAdapter(activity);
        adapter.setOnClickListener(this);
        rllist.layoutManager = layoutManager;
        rllist.adapter = adapter;

        index_list.setDropView(drop_view);
        index_list.setOnWordsChangeListener(this);
    }

    override fun onFirstVisibleToUser(): Boolean {
        return false;
    }

    override fun onInvisibleToUser() {
//        handler.removeCallbacksAndMessages(null);
    }

    override fun getLayRes(): Int {
        return R.layout.fragment_friend;
    }

    override fun onVisibleToUser() {
        userId = (activity as MainActivity).userId;
        (activity as MainActivity).setTitle(false, "好友", getString(R.string.icontianjia)) {
            val list: MutableList<AdapterItem> =
                ArrayList()
            list.add(
                AdapterItem(
                    "发起群聊",
                    resources.getDrawable(R.drawable.rc_default_group_portrait)
                )
            );
            list.add(
                AdapterItem(
                    "添加好友",
                    resources.getDrawable(R.drawable.rc_ext_realtime_default_avatar)
                )
            );
            list.add(
                AdapterItem(
                    "帮助反馈",
                    resources.getDrawable(R.drawable.rc_cs_default_portrait)
                )
            );
            val mMenuPopup: XUISimplePopup<*> = XUISimplePopup<XUISimplePopup<*>>(activity, list)
                .create(OnPopupItemClickListener { adapter, item, position ->
                    when (position) {
                        0 -> {
                            openActivity(SelectActivity().javaClass)
                        }
                    }
                    ToastUtils.makeToast(
                        item.title.toString()
                    )
                })
            mMenuPopup.showDown((activity as MainActivity).findViewById(R.id.tv_rightText));
        };
//        handler.sendEmptyMessageDelayed(200, 10000);
    }

//    private fun initHandle() {
//        userId = (activity as MainActivity).userId;
//        handler = object : Handler() {
//            override fun handleMessage(msg: Message?) {
//                super.handleMessage(msg)
//                when (msg?.what) {
//                    200 -> {
//                        initData();
//                    }
//                }
//            }
//        }
//        initData();
//    }

    private fun initData() {
        userId = (activity as MainActivity).userId;
        val bmobQuery = BmobQuery<User>();
        val sql = "select * from User where userPhone != '$userId'";
        bmobQuery.doSQLQuery(sql, object : SQLQueryListener<User>() {
            override fun done(t: BmobQueryResult<User>?, e: BmobException?) {
                if (t != null && t.results.size > 0) {
                    if (rllist.childCount > 0) {
                        rllist.removeAllViews();
                    }
                    if (list?.size > 0) {
                        list.clear();
                    }
                    adapter?.clearData();
                    for (u: User in t.results) {
                        list.add(u);
                    }
                    //初始化 置顶数据
                    if (topList.size > 0) {
                        topList.clear()
                    }
                    topList.add(TopUser("1", "新的朋友", R.drawable.rc_ext_realtime_default_avatar));
                    topList.add(TopUser("2", "群聊", R.drawable.rc_default_group_portrait));
                    topList.add(TopUser("3", "公众号", R.drawable.rc_cs_default_portrait));
                    //标星数据

                    //调整顺序
                    list = initDataOrder(list);
                    adapter.setData(topList, null, list);
//                    handler.sendEmptyMessageDelayed(200, 10000);
                } else {
                    showToast("查询失败" + e?.message);
                }
            }
        });
    }

    private fun initDataOrder(list: MutableList<User>): MutableList<User> {
        //                    adapter.addData(list);
        val orderList = arrayOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "j",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z",
            "#"
        );
        var newList: MutableList<User> = ArrayList();
        for (str in orderList) {
            for ((index, u: User) in list.withIndex()) {
                if (str == PinyinUtils.getSurnameFirstLetter(u.userName)) {
                    newList.add(list[index])
                }
            }
        }
        return newList;
    }

    override fun wordsChange(words: String?) {
        layoutManager.scrollToPositionWithOffset(adapter.getFirstWordListPosition(words), 0)
    }

    override fun onClick(view: View?, position: Int, type: Int) {
        when (type) {
            ContactAdapter.TYPE_ITEM -> {
                val frId: String = list.get(position).userPhone;
                val name: String = list.get(position).userName;
                //聊天界面
                RongIM.getInstance()
                    .startConversation(activity, Conversation.ConversationType.PRIVATE, frId, name);
            }
            ContactAdapter.TYPE_TOP -> {
                if (position == 1) {
                    openActivity(GroupListActivity().javaClass)
                } else {
                    ToastUtils.makeToast("功能开发中")
                }
                //置顶
            }
            ContactAdapter.TYPE_START -> {
                //标星
            }
        }

    }

    override fun onLongClick(view: View?, position: Int, type: Int) {
        TODO("Not yet implemented")
    }
}