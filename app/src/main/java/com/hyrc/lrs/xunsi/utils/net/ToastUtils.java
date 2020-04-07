package com.hyrc.lrs.xunsi.utils.net;

import android.widget.Toast;

import com.hyrc.lrs.xunsi.app.App;


public class ToastUtils {
	private static boolean isToastShow = true;
	public static void makeToast(String text){
		if(isToastShow){
			Toast.makeText(App.Companion.getSInstance(), text, Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void makeToast(String text, boolean isShort){
		if(isToastShow){
			if(isShort){
				Toast.makeText(App.Companion.getSInstance(), text, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(App.Companion.getSInstance(), text, Toast.LENGTH_LONG).show();
			}
		}
	}
}
