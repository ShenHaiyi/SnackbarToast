package com.example.shy.snackbartoast;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.example.shy.toast.MyToast;
import com.example.shy.toast.Prompt;
import com.example.shy.toast.SnackbarToast;
import com.example.shy.toast.SnackbarView;
import com.example.shy.toast.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] a = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5};
        for (int i = 0; i < a.length; i++) {
            findViewById(a[i]).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                SnackbarToast.make(this, "ERROR", 3000l)
                        .setPromptThemBackground(Prompt.ERROR)
                        .show();
                break;
            case R.id.button2:
                SnackbarToast.make(this, "SUCCESS", 3000l)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                break;
            case R.id.button3:
                SnackbarToast.make(this, "WARNING", 3000l)
                        .setPromptThemBackground(Prompt.WARNING)
                        .show();
                break;
            case R.id.button4:
                SnackbarView myView = (SnackbarView) LayoutInflater.from(this).
                    inflate(R.layout.view_toast_layout, null);
                MyToast myToast = new MyToast(this, myView);
                mParams(myToast.getWindowParams());
                myToast.show();
                break;
            case R.id.button5:
                SnackbarToast s = SnackbarToast.make(this, "WARNING", 3000l)
                        .addIcon(R.mipmap.ic_launcher_round);
                s.show();
                s.setText("消息提醒...");
                break;
        }
    }
    private void mParams(LayoutParams mParams) {
        mParams.width = LayoutParams.MATCH_PARENT;//宽
        mParams.height = Util.getStatusHeight(this) + Util.getActionBarHeight(this);//高
        //TYPE_SYSTEM_OVERLAY 系统覆盖窗口/TYPE_SYSTEM_ALERT 系统窗口/TYPE_TOAST 瞬态通知
        //TYPE_PHONE 来电窗口/TYPE_SYSTEM_ERROR 内部系统错误窗口 /TYPE_APPLICATION 普通应用程序窗口
//        mParams.type = LayoutParams.TYPE_TOAST;//窗口类型 部分窗口需要悬浮窗口权限,没有权限会报错
        mParams.flags = LayoutParams.FLAG_LAYOUT_IN_SCREEN//将窗口放置在整个屏幕中，忽略边框周围的装饰（如状态栏）
                | LayoutParams.FLAG_FULLSCREEN//隐藏所有屏幕装饰（如状态栏）
//                | LayoutParams.FLAG_SECURE//禁止截屏
                | LayoutParams.FLAG_NOT_FOCUSABLE;//此窗口将永远不会得到关键输入焦点
        mParams.format = PixelFormat.TRANSPARENT;//位图格式 支持透明
        mParams.windowAnimations = com.example.shy.toast.R.style.Animation;//窗口动画
        mParams.x = 0;//窗口位置的偏移量
        mParams.y = 0;
//        mParams.alpha = 0.5f;//窗口的透明度
    }
}
