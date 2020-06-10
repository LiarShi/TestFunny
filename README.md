# TestFunny
用来测试一些有趣功能的安卓Demo。

1.实现了循环和定时拨打电话 

2.营销号内容生成器 

3.监控APP前后台切换状态，并弹广告弹出

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
2018.11.15 更新 实现了APP循环拨打和定时拨打电话功能

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

2020.04.29 更新 增加了营销号内容生成器

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

2020.06.09 更新 增加了 app 前台后台切换监听的功能

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

2020.06.09 18：28 更新 增加了 app 弹出广告的功能（APP切换到后台一段时间，回到前台后会弹出广告，可在首页关闭）

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
