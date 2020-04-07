package com.hyrc.lrs.xunsi.utils.net;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by wenda on 2017/5/8.
 */

public class MainHandler extends Handler {
    private static volatile MainHandler instance;

    public static MainHandler getInstance() {
        if (null == instance) {
            synchronized (MainHandler.class) {
                if (null == instance) {
                    instance = new MainHandler();
                }
            }
        }
        return instance;
    }
    private MainHandler() {
        super(Looper.getMainLooper());
    }


    /**
     * 指定在UI线程上运行
     *
     * @param r
     * @return 如果当前线程是UI线程，则返回true
     */
    public static boolean runOnUI(Runnable r) {
        Thread t = Thread.currentThread();
        boolean ui = (t != null && t.getId() == Looper.getMainLooper()
                .getThread().getId());
        if (r == null) {
            return ui;
        }
        if (ui) {
            r.run();
        } else {
            instance.post(r);
        }
        return ui;
    }

}

