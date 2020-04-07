package com.hyrc.lrs.xunsi.activity.splash.countdown;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.activity.main.MainActivity;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import java.util.Random;

import butterknife.BindView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CountDownActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bntClose)
    RoundButton bntClose;
    @BindView(R.id.ivWecome)
    ImageView ivWecome;

    private String[] images = {"",
            "https://ww1.sinaimg.cn/large/0065oQSqgy1ftwcw4f4a5j30sg10j1g9.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqly1ftu6gl83ewj30k80tites.jpg",
            "http://ww1.sinaimg.cn/large/0065oQSqgy1ftt7g8ntdyj30j60op7dq.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqgy1ftrrvwjqikj30go0rtn2i.jpg"
            , "https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqly1ftdtot8zd3j30ju0pt137.jpg",
            "http://ww1.sinaimg.cn/large/0073sXn7ly1ft82s05kpaj30j50pjq9v.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqly1ft5q7ys128j30sg10gnk5.jpg",
            "https://ww1.sinaimg.cn/large/0065oQSqgy1ft4kqrmb9bj30sg10fdzq.jpg",
            "http://ww1.sinaimg.cn/large/0065oQSqly1ft3fna1ef9j30s210skgd.jpg",
            "http://ww1.sinaimg.cn/large/0065oQSqly1fszxi9lmmzj30f00jdadv.jpg"
    };
    private static final int NOT_NOTICE = 2;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    CountDownTimer timer;
    public final int GOTOMESSAGE = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == GOTOMESSAGE) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        }
    };


    @Override
    protected int loadView() {
        //去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏顶部状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //在你的activity的onCreate方法下添加下列代码（如果是自定义的BaseActivity的话，就在你自定义的Activity下添加，这样的话只要其他的继承你的BaseActivity，只需要改一次就可以）
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        return R.layout.activity_count_down;
    }

    private void beginCountDown() {
        Random random = new Random();
//        String url = images[random.nextInt(12)];
//        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585313239519&di=7900cd3df0491a909beb216a5d5492a7&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2055163815%2C3776562514%26fm%3D214%26gp%3D0.jpg";
        Glide.with(this).load(R.drawable.start).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                goToNext();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(ivWecome);
        if (timer == null) {
            timer = new CountDownTimer(3 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //millisUntilFinished / 1000
                    int endTime = (int) ((millisUntilFinished / 1000) + 1);
                    bntClose.setText("跳过 " + endTime);
                }

                @Override
                public void onFinish() {
                    goToNext();
                }
            };
            timer.start();
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void clearData() {

    }

    private void goToNext() {
        Message message = new Message();
        message.what = GOTOMESSAGE;
        handler.sendMessage(message);
        openAcitivty(MainActivity.class);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bntClose:
                goToNext();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean getPermossion() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //小于6
            return true;
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        bntClose.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(this, String.valueOf(permission)) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permission, 1);
        } else {
            beginCountDown();
        }
        requetPermission(new onPermissionCallBack() {
            @Override
            public void onSuccess() {
                beginCountDown();
            }

            @Override
            public void choiceProhibit() {
                //选择禁止
                if (alertDialog != null && alertDialog.isShowing()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CountDownActivity.this);
                builder.setTitle(R.string.perTitle)
                        .setMessage(R.string.perText)
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(CountDownActivity.this,
                                        permission, 1);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }

            @Override
            public void choiceProhibitNotAsking() {
                if (alertDialog != null && alertDialog.isShowing()) {
                    return;
                }
                //用户选择了禁止不再询问
                AlertDialog.Builder builder = new AlertDialog.Builder(CountDownActivity.this);
                builder.setTitle(R.string.perTitle)
                        .setMessage(R.string.perText)
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (mDialog != null && mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, NOT_NOTICE);
                            }
                        });
                mDialog = builder.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
            }
        });
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOT_NOTICE) {
            //由于不知道是否选择了允许所以需要再次判断
            getPermossion();
        }
    }

}
