package com.hyrc.lrs.xunsi.activity.register;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrc.lrs.xunsi.R;
import com.hyrc.lrs.xunsi.base.BaseActivity;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class PhoneCodeActivity extends BaseActivity {

    @BindView(R.id.tvphone)
    TextView tvphone;
    @BindView(R.id.verText)
    VerifyCodeEditText verText;
    @BindView(R.id.btn_Ver)
    XUIAlphaButton btnVer;
    @BindView(R.id.tvnumber)
    TextView tvnumber;
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
    int numCode = -1;

    @Override
    protected int loadView() {
        return R.layout.activity_phone_code;
    }

    @Override
    protected void initData() {
        setTitle(true, "手机验证");
        String phone = getIntent().getExtras().getString("phone");
        tvphone.setText(phone);
        verText.setOnInputListener(new VerifyCodeEditText.OnInputListener() {
            @Override
            public void onComplete(String input) {
                btnVer.setEnabled(true);
            }

            @Override
            public void onChange(String input) {
                btnVer.setEnabled(false);
            }

            @Override
            public void onClear() {

            }
        });
        showKeyboard(verText.getEditText());
        Timer time = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //发送验证码
                        getNumber();
                    }
                });

            }
        };
        time.schedule(timertask, 1 * 1000);
    }


    @Override
    protected void clearData() {
        Message message = new Message();
        message.what = GOTOMESSAGE;
        handler.sendMessage(message);
    }

    @OnClick(R.id.btn_Ver)
    public void onViewClicked() {
        if (verText.getInputValue().trim().isEmpty()) {
            showToast("请输入验证码");
            return;
        }
        String num = verText.getInputValue().trim();
        if (!num.equals(numCode + "")) {
            showToast("验证码输入有误");
            return;
        }
        openAcitivty(SetPasswordActivity.class, getIntent().getExtras());
        finish();

    }

    @OnClick(R.id.tvnumber)
    public void getNumber() {
        //调用发送验证码
        sendNunber();
        //完成之后  进行倒计时
        beginCountDown();
    }

    private void sendNunber() {
        numCode = (int) ((Math.random() * 9 + 1) * 100000);
        showToast("验证为：" + numCode + "", Toast.LENGTH_LONG);
//        verText.setEtNumber(numCode);
    }

    private void beginCountDown() {
        if (timer == null) {
            timer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int endTime = (int) ((millisUntilFinished / 1000) + 1);
                    tvnumber.setText("重新发送（" + endTime + "s）");
                }

                @Override
                public void onFinish() {
                    NumberFinish();
                }
            };
            tvnumber.setEnabled(false);
            timer.start();
        }
    }

    /**
     * 倒计时完成
     */
    private void NumberFinish() {
        tvnumber.setText("重新发送");
        tvnumber.setEnabled(true);
        Message message = new Message();
        message.what = GOTOMESSAGE;
        handler.sendMessage(message);
    }
}
