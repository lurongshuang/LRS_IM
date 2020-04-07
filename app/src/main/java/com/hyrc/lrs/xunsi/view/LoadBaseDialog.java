package com.hyrc.lrs.xunsi.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.hyrc.lrs.xunsi.R;

/**
 * @description 作用:
 * @date: 2020/3/30
 * @author: 卢融霜
 */
public class LoadBaseDialog extends Dialog {
    public LoadBaseDialog(@NonNull Context context) {
        super(context, R.style.TransparentDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.load_indicator_view);
    }

    public void setMsg(String text) {

    }

}
