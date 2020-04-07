package com.hyrc.lrs.xunsi.utils.vibrator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;

/**
 * @description 作用: 震动工具类
 * @date: 2019/11/21
 * @author: 卢融霜
 */
public class VibratorUtils {
    private static VibratorUtils vibratorUtils;

    public static VibratorUtils getVibrator() {
        if (vibratorUtils == null) {
            vibratorUtils = new VibratorUtils();
        }

        return vibratorUtils;
    }

    public Vibrator vibrator;

    /**
     * 只震动一下
     */
    @SuppressLint("WrongConstant")
    public void setVibratorOnly(Context context, int milli) {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        long[] patern = {0, milli};
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.USAGE_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            vibrator.vibrate(patern, -1, audioAttributes);
        } else {
            vibrator.vibrate(patern, -1);
        }


    }

    /**
     * 结束震动
     */
    public void cancel() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
