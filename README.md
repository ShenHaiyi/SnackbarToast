# SnackbarToast
一个自定义Toast,实现了Snackbar在顶部弹出的效果，具有Toast的属性，它可以不受权限限制显示在状态栏之上，同时可以显示在应用程序外部。<br>
```java
//显示一个错误提示
SnackbarToast.make(context, "ERROR", 2000l)//三个参数分别是上下文,显示文本和显示时间
        .setPromptThemBackground(Prompt.ERROR)
        .show();
//显示一个成功提示
SnackbarToast.make(context, "SUCCESS", 2000l)
        .setPromptThemBackground(Prompt.SUCCESS)
        .show();
//自定义
SnackbarToast.make(this, "自定义内容", 2000l)
        .addIcon(R.mipmap.ic_launcher_round)//添加一个图片
        .setBackgroundColor(R.color.colorAccent);//设置一个背景色
        .setButton("确定", new SnackbarToast.OnClick() {
            @Override
            public void onClick(View v, MyToast toast) {
                toast.cancel();
            }
        })
        .show();
//显示警告提示
SnackbarToast.make(context, "WARNING", 2000l)
        .setPromptThemBackground(Prompt.WARNING)
        .show();
//显示一个自定义View
SnackbarView myView = (SnackbarView) LayoutInflater.from(this).
    inflate(R.layout.view_toast_layout, null);
MyToast myToast = new MyToast(context, myView);
mParams(myToast.getWindowParams());
myToast.show();
```
![](https://github.com/ShenHaiyi/SnackbarToast/blob/master/screenshot.png)
