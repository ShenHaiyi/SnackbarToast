package com.example.shy.toast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shy on 17-3-21.
 */

public class MyToast {
    /**
     * 获取当前Android系统版本
     */
    static int currentapiVersion = Build.VERSION.SDK_INT;
    /**
     * 一直显示,执行{@link #cancel()}关闭
     */
    public static final long LENGTH_INDEFINITE = 0;
    /**
     * 显示2000毫秒
     */
    static final long LENGTH_SHORT = 2000;

    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    Long duration = LENGTH_SHORT;
    boolean hasReflectException = false;
    Context mContext;
    Toast mToast;
    View mView;

    public MyToast(Context context, View view) {
        if (context == null)
            throw new NullPointerException("context can't be null");
        this.mContext = context;
        this.mView = view;
        initTN();
    }

    /**
     * 开启Toast
     */
    public final void show() {
        MyToast.pool.execute(new Thread() {
            @Override
            public void run() {
                super.run();
                if (hasReflectException) {// 反射过程异常时则使用源生Toast
                    Toast t = new Toast(mContext);
                    t.setView(mView);
                    t.setDuration(Toast.LENGTH_SHORT);
                    t.show();
                    // 重新获取反射对象
                    initTN();
                    return;
                }
                showToast();
                if (duration == null) return;
                try {
                    sleep(duration);
                    hideToast();
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                new Handler(mContext.getMainLooper())
//                        .postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }, duration);
            }
        });

    }

    /**
     * 关闭Toast
     */
    public void cancel() {
        hideToast();
    }

    public void setView(View view) {
        this.mView = view;
        setMinHeight(0, 0);
    }

    @NonNull
    public static MyToast make(@NonNull Context context, @NonNull View view, Long duration) {
        MyToast myToast = new MyToast(context, view);
        myToast.duration = duration;
        return myToast;
    }

    @NonNull
    public static MyToast make(@NonNull Context context, @NonNull View view) {
        return make(context, view, null);
    }

    /* 反射内容 */
    Object mTN;
    Field mTNf, mParams1;
    Method showMethod, hideMethod;
    WindowManager.LayoutParams mParams;

    /**
     * 通过反射获得mTN下的show和hide方法
     */
    private void initTN() {
        mToast = new Toast(mContext);
        try {
            mTNf = Toast.class.getDeclaredField("mTN");
            mTNf.setAccessible(true);
            mTN = mTNf.get(mToast);
            mParams1 = mTN.getClass().getDeclaredField("mParams");
            mParams1.setAccessible(true);
            mParams = (WindowManager.LayoutParams) mParams1.get(mTN);
            showMethod = mTN.getClass().getDeclaredMethod("show",
                    new Class<?>[0]);
            hideMethod = mTN.getClass().getDeclaredMethod("hide",
                    new Class<?>[0]);
            mParams();
            setMinHeight(0, 0);
            mToast.setView(mView);
            mToast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);//显示位置
            hasReflectException = false;
        } catch (NoSuchFieldException e) {
            hasReflectException = true;
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            hasReflectException = true;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            hasReflectException = true;
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获得的show方法显示指定View
     */
    private void showToast() {
        try {
            // 高版本需要再次手动设置mNextView属性
            if (currentapiVersion > 10) {
                Field mNextView = mTN.getClass().getDeclaredField("mNextView");
                mNextView.setAccessible(true);
                mNextView.set(mTN, mView);
            }
            showMethod.invoke(mTN, new Object[0]);
            hasReflectException = false;
        } catch (Exception e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        }
    }

    /**
     * 通过反射获得的hide方法隐藏指定View
     */
    private void hideToast() {
        try {
            hideMethod.invoke(mTN, new Object[0]);
            hasReflectException = false;
        } catch (Exception e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        }
    }

    /**
     * 设置弹窗属性
     */
    private void mParams() {
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;//宽
        mParams.height = Util.getStatusHeight(mContext) + Util.getActionBarHeight(mContext);//高
        //TYPE_SYSTEM_OVERLAY 系统覆盖窗口/TYPE_SYSTEM_ALERT 系统窗口/TYPE_TOAST 瞬态通知
        //TYPE_PHONE 来电窗口/TYPE_SYSTEM_ERROR 内部系统错误窗口 /TYPE_APPLICATION 普通应用程序窗口
//        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//窗口类型 部分窗口需要悬浮窗口权限
        mParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN//将窗口放置在整个屏幕中，忽略边框周围的装饰（如状态栏）
                | WindowManager.LayoutParams.FLAG_FULLSCREEN//隐藏所有屏幕装饰（如状态栏）
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//此窗口将永远不会得到关键输入焦点
        mParams.format = PixelFormat.TRANSPARENT;//位图格式 支持透明
        mParams.windowAnimations = R.style.Animation;//窗口动画
        mParams.x = 0;//窗口位置的偏移量
        mParams.y = 0;
//        mParams.alpha = 0.5f;//窗口的透明度
    }

    public void setMinHeight(int stateBarHeight, int actionBarHeight) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        if (stateBarHeight > 0 || actionBarHeight > 0) {
            mView.setPadding(0, stateBarHeight, 0, 0);
            mParams.height = stateBarHeight + actionBarHeight;
        } else {
            mView.setPadding(0, Util.getStatusHeight(mContext), 0, 0);
            mParams.height = Util.getStatusHeight(mContext) | WindowManager.LayoutParams.WRAP_CONTENT;
        }
    }

    public int getYOffset() {
        return mToast.getYOffset();
    }

    public View getView() {
        return mView;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
    }

    public float getHorizontalMargin() {
        return mToast.getHorizontalMargin();
    }

    public float getVerticalMargin() {
        return mToast.getVerticalMargin();
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    public int getGravity() {
        return mToast.getGravity();
    }

    public int getXOffset() {
        return mToast.getXOffset();
    }

    public WindowManager.LayoutParams getWindowParams() {
//        mToast.getWindowParams();
        return mParams;
    }
}
