package com.hyrc.lrs.xunsi.activity.group;

import android.view.View;
import android.widget.EditText;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.hyrc.lrs.xunsi.bean.Group;
import com.hyrc.lrs.xunsi.bean.GroupId_with_UserId;
import com.hyrc.lrs.xunsi.utils.net.CustomCallBack;
import com.hyrc.lrs.xunsi.utils.net.NetworkUtils;
import com.hyrc.lrs.xunsi.utils.net.ToastUtils;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.FormBody;

public class AddGroupActivity extends BaseActivity {
    @BindView(R.id.et_group_id)
    EditText et_group_id;
    @BindView(R.id.et_group_name)
    EditText et_group_name;
    @BindView(R.id.et_group_url)
    EditText et_group_url;
    @BindView(R.id.btn_comit)
    XUIAlphaButton btn_comit;

    private List<String> list;

    @Override
    protected int loadView() {
        return R.layout.activity_add_group;
    }

    @Override
    protected void initData() {
        list = getIntent().getExtras().getStringArrayList("selectUser");
        setTitle(true, "设置群信息");
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_group_id.getText().toString().trim().isEmpty()) {
                    showToast("请输入群id");
                    return;
                }
                if (et_group_name.getText().toString().trim().isEmpty()) {
                    showToast("请输入群昵称");
                    return;
                }
                if (et_group_url.getText().toString().trim().isEmpty()) {
                    showToast("请输入群头像地址");
                    return;
                }
                FormBody.Builder builder = new FormBody.Builder();
                for (String value : list) {
                    builder.add("userId", value);
                }
                builder.add("groupId", et_group_id.getText().toString());
                builder.add("groupName", et_group_name.getText().toString());
                String url = "http://api-cn.ronghub.com/group/create.json";
                //添加到融云数据库
                NetworkUtils.getInstance().postToken(url, new CustomCallBack() {
                    @Override
                    public void onSuccess(Call call, String result) throws JSONException {
                        //添加到后端云 新建群组
                        Group group = new Group();
                        group.setGroupId(et_group_id.getText().toString());
                        group.setGroupName(et_group_name.getText().toString());
                        group.setGroupUrl(et_group_url.getText().toString());
                        group.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                //添加到后端云 添加群组关系
                                for (String userId : list) {
                                    GroupId_with_UserId gu = new GroupId_with_UserId();
                                    gu.setGroupId(et_group_id.getText().toString());
                                    gu.setUserId(userId);
                                    gu.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            ToastUtils.makeToast("添加成功");
                                            AddGroupActivity.this.finish();
                                        }
                                    });
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, IOException e) {
                        ToastUtils.makeToast("添加失败");
                    }
                }, builder);

            }
        });

    }

    @Override
    protected void clearData() {

    }
}
