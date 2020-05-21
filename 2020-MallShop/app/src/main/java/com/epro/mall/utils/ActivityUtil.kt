package com.epro.mall.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.epro.mall.mvp.model.bean.IMShop
import com.mike.baselib.utils.ext_putJsonExtra

class ActivityUtil {
    companion object {
        fun launch2CompIMChat(activity: Activity, shop: IMShop) {
            activity.startActivity(Intent(activity, Class.forName("com.epro.comp.im.ui.activity.ChatActivity")).ext_putJsonExtra(shop))
        }

        fun launch2CompIMChatRecordList(activity: Activity) {
            activity.startActivity(Intent(activity, Class.forName("com.epro.comp.im.ui.activity.ChatRecordListActivity")))
        }

        fun startIMService(activity: Activity) {
            activity.startService(Intent(activity, Class.forName("com.epro.comp.im.service.IMService")))
        }

        fun stopIMService(activity: Activity) {
            activity.stopService(Intent(activity, Class.forName("com.epro.comp.im.service.IMService")))
        }

        fun createChatRecordListFragment():Fragment{
            return Class.forName("com.epro.comp.im.ui.fragment.ChatRecordListFragment").newInstance() as Fragment
        }

    }
}