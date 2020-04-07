package com.hyrc.lrs.xunsi.utils.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hyrc.lrs.xunsi.app.App;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by wenda1 on 2017/8/13.
 */

public class NetworkUtils {
    public final String TAG = this.getClass().getSimpleName();
    private static final String CODE1 = "000000";
    private static final String PWD1 = "B98394C6F59BE530AD449ADA20CE4E7C6B8920100929A";
    private static final String CODE = "210000";
    private static final String PWD = "B98394C6F59BE530AD449ADA20CE4E7C6B8931917519CF0F02733F";
    /**
     * WSDL文件的URL.
     */
    private static final int TIME_OUT = 60000 * 3;
    private static NetworkUtils instance;
    private OkHttpClient ohc;

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private static ExecutorService mFixedThreadExecutor = null;

    public NetworkUtils() {
//        getOkHttpClient();
        mFixedThreadExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public static NetworkUtils getInstance() {
        synchronized (NetworkUtils.class) {
            if (instance == null) {
                instance = new NetworkUtils();
            }
        }
        return instance;
    }


    /**
     * * 将线程放入线程池中运行
     *
     * @param runnable 需要在子线程中运行的代码
     */


    public void runOnExecutorServiceThread(Runnable runnable) {
        mFixedThreadExecutor.execute(runnable);
    }

    public static boolean isNetworkConnected() {
        if (App.Companion.getSInstance() != null) {
            @SuppressLint("WrongConstant") ConnectivityManager mConnectivityManager = (ConnectivityManager)
                    App.Companion.getSInstance().getSystemService("connectivity");
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        } else {
        }
        return false;
    }

    public boolean isWifi(Context c) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI");
    }


    private OkHttpClient getOkHttpClient() {
        if (ohc == null) {
            ohc = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(false)
                    .sslSocketFactory(com.lrs.xunsi.utils.net.RxUtils.createSSLSocketFactory())
                    .build();
        }
        return ohc;
    }

    public okhttp3.Call get(String url, CustomCallBack customCallBack) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            return null;
        }
        String keys = "&KEY=" + getKeyPaw(getFunName(url));
        url += keys;
        customCallBack.setUrl(url);
        //添加请求秘钥
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .method("GET", null)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);
        call.enqueue(customCallBack);
        return call;
    }

    public okhttp3.Call post(String url, CustomCallBack customCallBack, String... strings) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            customCallBack.onError(null, new IOException());
            return null;
        }
        customCallBack.setUrl(url);
        FormBody.Builder builder = new FormBody.Builder();
        String key = null;
        String value = null;
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 == 0) {
                key = strings[i];
            } else {
                value = strings[i];
                if (value == null) {
                    value = "";
                }
                //添加请求秘钥
                builder.add(key, value);
            }
        }
        builder.add("KEY", getKeyPaw(url.substring(url.lastIndexOf("/") + 1)));
        RequestBody formBody = builder.build();
        String Nonce = getNonce();
        String Timestamp = getTimestamp();
        String Signature = getSignature(Nonce, Timestamp);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);
        call.enqueue(customCallBack);
        return call;
    }

    public okhttp3.Call postToken(String url, CustomCallBack customCallBack, String... strings) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            customCallBack.onError(null, new IOException());
            return null;
        }
        customCallBack.setUrl(url);
        FormBody.Builder builder = new FormBody.Builder();
        String key = null;
        String value = null;
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 == 0) {
                key = strings[i];
            } else {
                value = strings[i];
                if (value == null) {
                    value = "";
                }
                //添加请求秘钥
                builder.add(key, value);
            }
        }
        builder.add("KEY", getKeyPaw(url.substring(url.lastIndexOf("/") + 1)));
        RequestBody formBody = builder.build();
        String Nonce = getNonce();
        String Timestamp = getTimestamp();
        String Signature = getSignature(Nonce, Timestamp);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("App-Key", "8luwapkv84hyl")
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature", Signature)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);
        call.enqueue(customCallBack);
        return call;
    }

    public okhttp3.Call postToken(String url, CustomCallBack customCallBack, FormBody.Builder builder) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            customCallBack.onError(null, new IOException());
            return null;
        }
        customCallBack.setUrl(url);
        builder.add("KEY", getKeyPaw(url.substring(url.lastIndexOf("/") + 1)));
        RequestBody formBody = builder.build();
        String Nonce = getNonce();
        String Timestamp = getTimestamp();
        String Signature = getSignature(Nonce, Timestamp);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("App-Key", "8luwapkv84hyl")
                .addHeader("Nonce", Nonce)
                .addHeader("Timestamp", Timestamp)
                .addHeader("Signature", Signature)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);
        call.enqueue(customCallBack);
        return call;
    }

    private String getSignature(String Nonce, String Timestamp) {
        String appSecret = "emjkt9urIh";
        return SHA1.encode(appSecret + Nonce + Timestamp);
    }


    private String getTimestamp() {
        //获取当前时间
        return System.currentTimeMillis() + "";

    }

    private String getNonce() {
        Random random = new Random();
        return random.nextInt(15) + "";
    }

    public okhttp3.Call post1(String url, CustomCallBack customCallBack, String... strings) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            return null;
        }
        customCallBack.setUrl(url);
        FormBody.Builder builder = new FormBody.Builder();
        String key = null;
        String value = null;
        for (int i = 0; i < strings.length; i++) {
            if (i % 2 == 0) {
                key = strings[i];
            } else {
                value = strings[i];
                if (value == null) {
                    value = "";
                }
                //添加请求秘钥
                builder.add(key, value);
            }
        }
        builder.add("KEY", getKeyPaw1(url.substring(url.lastIndexOf("/") + 1)));
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);

        call.enqueue(customCallBack);
        return call;
    }

    public okhttp3.Call postValue(String url, CustomCallBack customCallBack, String... strings) {
        if (!isNetworkConnected()) {
            ToastUtils.makeToast("网络未连接");
            return null;
        }
        customCallBack.setUrl(url);
        FormBody.Builder builder = new FormBody.Builder();
        String value = "";
        for (int i = 0; i < strings.length; i++) {
            if (i == strings.length - 1) {
                value += strings[i];
            } else {
                value += strings[i] + ",";
            }
        }
        builder.add("USINFO", value);
        //添加请求秘钥
        builder.add("KEY", getKeyPaw(url.substring(url.lastIndexOf("/") + 1)));
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        okhttp3.Call call = getInstance().getOkHttpClient().newCall(request);
        call.enqueue(customCallBack);
        return call;
    }


    private String getFunName(String url) {
        String funName = "";
        if (url.lastIndexOf("?") > 0) {
            funName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
        } else {
            funName = url.substring(url.lastIndexOf("/") + 1);
        }
        return funName;
    }

    public static String getKeyPaw(String funName) {
        String key = null;
        String value = CODE + "," + PWD + "," + funName;
        key = Base64.encode(value.getBytes(StandardCharsets.UTF_8));
        return key;
    }

    public static String getKeyPaw1(String funName) {
        String key = null;
        String value = CODE1 + "," + PWD1 + "," + funName;
        key = Base64.encode(value.getBytes(StandardCharsets.UTF_8));
        return key;
    }

    public static String getKeyPaw(String funName, String id, String bookType) {
        String key = null;
        String value = CODE1 + "," + PWD1 + "," + funName + "," + id + "," + bookType;
        key = Base64.encode(value.getBytes(StandardCharsets.UTF_8));
        return key;
    }

    public static String getKeyPaw(String funName, String id, int startPage, int endPage, String BookType) {
        String key = null;
        String value = CODE1 + "," + PWD1 + "," + funName + "," + id + "," + startPage + "," + endPage + "," + BookType;
        key = Base64.encode(value.getBytes(StandardCharsets.UTF_8));
        return key;
    }
}