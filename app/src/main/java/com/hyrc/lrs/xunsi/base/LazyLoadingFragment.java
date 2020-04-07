package com.hyrc.lrs.xunsi.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hyrc.lrs.xunsi.activity.splash.SplashActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class LazyLoadingFragment extends Fragment {
    private boolean isCanLoading;
    private boolean isFirstVisibleToUser;
    private boolean isVisibleToUser;
    public View root_view;

    public LazyLoadingFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFirstVisibleToUser = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
        }
        isCanLoading = false;
        isFirstVisibleToUser = true;
        isVisibleToUser = false;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        onVisibleChanged(isVisibleToUser);
    }

    private void onVisibleChanged(boolean isVisibleToUser) {
        if (isCanLoading) {
            if (getUserVisibleHint()) {
                if (isFirstVisibleToUser) {
                    if (onFirstVisibleToUser()) {
                        onVisibleToUser();
                    } else {
                        onVisibleToUser();
                    }
                    isFirstVisibleToUser = false;
                } else {
                    onVisibleToUser();
                    isFirstVisibleToUser = false;
                }
            } else {
                onInvisibleToUser();
            }
        }
    }

    /**
     * 第一次对用户可见
     *
     * @return 第一次可见任然执行 {@link LazyLoadingFragment#onVisibleChanged(boolean)}
     */
    protected abstract boolean onFirstVisibleToUser();

    /**
     * 对用户可见
     */
    protected abstract void onVisibleToUser();

    /**
     * 对用户不可见
     */
    protected abstract void onInvisibleToUser();

    protected abstract int getLayRes();

    protected abstract void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public static Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(getLayRes(), container, false);
            unbinder = ButterKnife.bind(this, root_view);
            init(inflater, container, savedInstanceState);
        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCanLoading = true;
        onVisibleChanged(getUserVisibleHint());
    }

    protected final boolean isFirstVisibleToUser() {
        return isFirstVisibleToUser;
    }

    protected final boolean isCanLoading() {
        return isCanLoading;
    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 提示信息  吐司
     */
    public void showToast(String text) {
        if (getActivity() != null) {
            Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提示信息  吐司
     */
    public void showToast(int id) {
        if (getActivity() != null) {
            Toast.makeText(getActivity().getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        }
    }
}
