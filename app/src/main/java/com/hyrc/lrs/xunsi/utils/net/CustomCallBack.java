package com.hyrc.lrs.xunsi.utils.net;


import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by a on 2017/9/13.
 */

public abstract class CustomCallBack implements Callback {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if (e != null) {
            e.printStackTrace();
        }
        MainHandler.getInstance().post(() -> onError(call, e));
    }

    @Override
    public void onResponse(final Call call, Response response) throws IOException {
        if (response != null && response.body() != null) {
            final String result = response.body().string();
            MainHandler.getInstance().post(() -> {
                try {
                    onSuccess(call, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else {
            MainHandler.getInstance().post(() -> onError(call, new IOException("response or body = null")));
        }
    }

    public abstract void onSuccess(Call call, String result) throws JSONException;

    public abstract void onError(Call call, IOException e);
}
