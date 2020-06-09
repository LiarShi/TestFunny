# TestCall
用来写一些有趣或者测试某些功能的安卓Demo，实现了APP循环拨打和定时拨打电话，营销号内容生成器

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
2018.11.15 更新 实现了APP循环拨打和定时拨打电话功能

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

2020.04.29 更新 增加了营销号内容生成器

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

2020.06.09 更新 增加了增加了 app 前台后台切换监听的功能

添加了 ForegroundCallbacks类，用于监听app前台后台切换

在自定义Application的onCreate中添加监听方法

JAVA:

/** 前台后台切换监听 */


private void initAppStatusListener() {
    ForegroundCallbacks.init(this).addListener(new ForegroundCallbacks.Listener() {
        @Override
        public void onBecameForeground() {
            ToastUtils.showShort(get(),"++++App进入前台++++");
        }
        @Override
        public void onBecameBackground() {
            ToastUtils.showShort(get(),"----App退至后台----");
        }
    });
}

这个方法搬运了CSDN博主「火炎焱燚-」代码
原文链接：https://blog.csdn.net/zheng_jiao/java/article/details/94357414

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
