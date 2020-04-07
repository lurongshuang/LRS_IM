package com.hyrc.lrs.xunsi.app

import android.app.Activity
import android.app.Application
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.bmob.v3.Bmob
import com.hyrc.lrs.xunsi.activity.rongIM.MyExtensionModule
import com.xuexiang.xui.XUI
import io.rong.imkit.DefaultExtensionModule
import io.rong.imkit.IExtensionModule
import io.rong.imkit.RongExtensionManager
import io.rong.imkit.RongIM
import io.rong.imkit.widget.provider.SightMessageItemProvider
import io.rong.message.SightMessage
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import me.jessyan.autosize.unit.Subunits
import me.jessyan.autosize.utils.ScreenUtils


/**
 * @description 作用:
 * @date: 2020/3/24
 * @author: 卢融霜
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //初始化云存储
        Bmob.initialize(this, "1ad4db4503319e703ac4127b78827df5");
        //初始化IM
        RongIM.init(this, "8luwapkv84hyl");
        //传递用户信息
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        //显示新消息提醒
        RongIM.getInstance().enableNewComingMessageIcon(true);
        //显示未读消息数目
        RongIM.getInstance().enableUnreadMessageIcon(true);
        //支持小视频
        setMyExtensionModule();
        RongIM.registerMessageType(SightMessage::class.java);
        RongIM.registerMessageTemplate(SightMessageItemProvider());
        //语音输入
//        Recognizer.setAppId(null);
        sInstance = this
        XUI.init(this);
        XUI.debug(true);
        //布局auto
        initAutoSize()
    }

    private fun initAutoSize() {
        AutoSizeConfig.getInstance()

            //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
            .setCustomFragment(true)
            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
            //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
            .setExcludeFontScale(true)
            //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
            //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
//                .setPrivateFontScale(0.8f)
            //屏幕适配监听器
            .setOnAdaptListener(object : ViewPager.OnAdapterChangeListener, onAdaptListener {
                override fun onAdapterChanged(
                    viewPager: ViewPager,
                    oldAdapter: PagerAdapter?,
                    newAdapter: PagerAdapter?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onAdaptBefore(target: Any?, activity: Activity?) {
                    //                    //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
//                    //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
//                    //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                    AutoSizeConfig.getInstance()
                        .setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                    AutoSizeConfig.getInstance()
                        .setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                }

                override fun onAdaptAfter(target: Any?, activity: Activity?) {
                    //                    AutoSizeLog.d(
//                        String.format(
//                            Locale.ENGLISH,
//                            "%s onAdaptAfter!",
//                            target.getClass().getName()
//                        )
//                    );
                }
            })

            //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
//                .setLog(false)
            //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
            //AutoSize 会将屏幕总高度减去状态栏高度来做适配
            //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
            //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
//                .setUseDeviceSize(true)

            //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)

            //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())
            .unitsManager
            .setSupportDP(true)
            .setSupportSP(true)
            .supportSubunits = Subunits.NONE;

        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this)
    }

    /**
     * 小视频
     */
    fun setMyExtensionModule() {
        val moduleList =
            RongExtensionManager.getInstance().extensionModules
        var defaultModule: IExtensionModule? = null
        if (moduleList != null) {
            for (module in moduleList) {
                if (module is DefaultExtensionModule) {
                    defaultModule = module
                    break
                }
            }
            if (defaultModule != null) {
                //清理现有module
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                //添加新的module
                RongExtensionManager.getInstance().registerExtensionModule(MyExtensionModule());
            }
        }
    }

    /**
     * 修饰静态变量
     */
    companion object {
        var sInstance: App? = null
        fun getsInstance(): App? {
            return sInstance;
        }
    }
}