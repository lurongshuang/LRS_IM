package com.hyrc.lrs.xunsi.activity.group;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.group.adapter.GroupListAdapter;
import com.hyrc.lrs.xunsi.activity.login.LoginActivity;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.hyrc.lrs.xunsi.bean.Group;
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class GroupListActivity extends BaseActivity {
    @BindView(R.id.rllists)
    RecyclerView rllists;

    private String userId;
    private GroupListAdapter adapter;

    @Override
    protected int loadView() {
        return R.layout.activity_group_list;
    }

    @Override
    protected void initData() {
        setTitle(true, "群聊");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rllists.setLayoutManager(linearLayoutManager);
        adapter = new GroupListAdapter(R.layout.item_contact, this);
        rllists.setAdapter(adapter);
        userId = SharedPreferencesHelper.getPrefString(this, "userId", null);
        if (userId == null) {
            openAcitivty(LoginActivity.class);
            this.finish();
        }
        getData();
    }

    private void getData() {
        BmobQuery<Group> bmobQuery = new BmobQuery<>();
        String sql = "select * from Group where groupId in (select groupId from GroupId_with_UserId where userId = '" + userId + "')";
        bmobQuery.doSQLQuery(sql, new SQLQueryListener<Group>() {
            @Override
            public void done(BmobQueryResult<Group> t, BmobException e) {
                if (t != null && t.getResults().size() > 0) {
                    for (Group group : t.getResults()) {
                        adapter.addData(group);
                    }
                }
            }
        });
    }

    @Override
    protected void clearData() {

    }
}
