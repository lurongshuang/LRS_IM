package com.hyrc.lrs.xunsi.activity.group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.group.adapter.SelectAdapter;
import com.hyrc.lrs.xunsi.activity.group.adapter.onInterface.OnAdapterOnclick;
import com.hyrc.lrs.xunsi.activity.login.LoginActivity;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.hyrc.lrs.xunsi.bean.User;
import com.hyrc.lrs.xunsi.utils.sharedpreferences.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class SelectActivity extends BaseActivity implements OnAdapterOnclick {
    @BindView(R.id.rvlist)
    RecyclerView rvlist;
    @BindView(R.id.tvSelectNum)
    TextView tvSelectNum;
    @BindView(R.id.tvOk)
    TextView tvOk;

    private SelectAdapter adapter;
    private String userId;

    @Override
    protected int loadView() {
        return R.layout.activity_select;
    }

    @Override
    protected void initData() {
        setTitle(true, "选择联系人");
        userId = SharedPreferencesHelper.getPrefString(this, "userId", null);
        if (userId == null) {
            openAcitivty(LoginActivity.class);
            this.finish();
        }
        adapter = new SelectAdapter(R.layout.item_select, this);
        adapter.setUserId(userId);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rvlist.setLayoutManager(manager);
        //添加Android自带的分割线
        rvlist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvlist.setAdapter(adapter);
        adapter.setAdapterOnclick(this);
        getData();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelectNum() > 0) {
                    Bundle build = new Bundle();
                    build.putStringArrayList("selectUser", (ArrayList<String>) adapter.getSelectUser());
                    openAcitivty(AddGroupActivity.class, build);
                    SelectActivity.this.finish();
                }
            }
        });
    }

    private List<User> list = null;

    /**
     * 获取数据库数据
     */
    private void getData() {
        BmobQuery bmobQuery = new BmobQuery<User>();
        String sql = "select * from User";
        bmobQuery.doSQLQuery(sql, new SQLQueryListener<User>() {
            @Override
            public void done(BmobQueryResult<User> t, BmobException e) {
                if (t != null && t.getResults().size() > 0) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    if (list.size() > 0) {
                        list.clear();
                    }
                    list = t.getResults();
                    adapter.addData(list);
                }
            }
        });
    }

    @Override
    protected void clearData() {

    }

    @Override
    public void onClickListener(int position, boolean state) {
        Log.e("", "");
        int num = adapter.getSelectNum();
        if (num > 0) {
            tvSelectNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvOk.setTextColor(getResources().getColor(R.color.colorPrimary));

        } else {
            tvSelectNum.setTextColor(getResources().getColor(R.color.rc_gray));
            tvOk.setTextColor(getResources().getColor(R.color.rc_gray));
        }
        tvSelectNum.setText("已选择" + num + "人");
    }
}
