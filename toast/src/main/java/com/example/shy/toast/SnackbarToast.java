package com.example.shy.toast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shy on 17-3-21.
 */

public class SnackbarToast extends MyToast {
    SnackbarView mView;

    public SnackbarToast(Context context, View view) {
        super(context, view);
    }

    @NonNull
    public static SnackbarToast make(@NonNull Context context, @NonNull CharSequence text, Long duration) {
        SnackbarView view = (SnackbarView) LayoutInflater.from(context).
                inflate(R.layout.view_toast_layout, null);
        view.getMessageView().setText(text);
        SnackbarToast myToast = new SnackbarToast(context, view);
        myToast.duration = duration;
        myToast.mView = view;
        return myToast;
    }

    @NonNull
    public static SnackbarToast make(@NonNull Context context, @NonNull CharSequence text) {
        return make(context, text, null);
    }


    /**
     * default style {ERROR , WARNING , SUCCESS}
     * @param prompt
     * @return
     */
    public SnackbarToast setPromptThemBackground(Prompt prompt) {
        if (prompt == Prompt.SUCCESS) {
            setBackgroundColor(Prompt.SUCCESS.getBackgroundColor());
            addIcon(Prompt.SUCCESS.getResIcon(), 70, 70);
        } else if (prompt == Prompt.ERROR) {
            setBackgroundColor(Prompt.ERROR.getBackgroundColor());
            addIcon(Prompt.ERROR.getResIcon(), 70, 70);
        } else if (prompt == Prompt.WARNING) {
            setBackgroundColor(Prompt.WARNING.getBackgroundColor());
            addIcon(Prompt.WARNING.getResIcon(), 70, 70);
        }
        return this;
    }
    /**
     * 设置背景色
     * @param colorId
     * @return
     */
    public SnackbarToast setBackgroundColor(int colorId) {
        mView.setBackgroundColor(mContext.getResources().getColor(colorId));
        return this;
    }

    /**
     * 添加图片并设置尺寸
     * @param resource_id
     * @param width
     * @param height
     * @return
     */
    public SnackbarToast addIcon(int resource_id, int width, int height) {
        if(width>0 || height>0){
            final TextView tv = mView.getMessageView();
            tv.setCompoundDrawablesWithIntrinsicBounds(
                    new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable)
                            (mContext.getResources().getDrawable(resource_id))).getBitmap(), width, height, true)),
                    null, null, null);
        }else{
            addIcon(resource_id);
        }
        return this;
    }

    /**
     * 添加图片
     * @param resource_id
     * @return
     */
    public SnackbarToast addIcon(int resource_id) {
        final TextView tv = mView.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(
                mContext.getResources().getDrawable(resource_id), null, null, null);
        return this;
    }

    public SnackbarToast setText(CharSequence text){
        mView.getMessageView().setText(text);
        return this;
    }
}
