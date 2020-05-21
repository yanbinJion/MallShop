package com.epro.mall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.epro.mall.utils.MallBusManager
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.net.exception.ExceptionHandle
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.utils.AppContext
import com.mike.baselib.utils.CleanMessageUtil
import com.mike.baselib.utils.DateUtils
import com.mike.baselib.utils.LogTools
import io.reactivex.Observable

class TimeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if ("android.intent.action.TIME_SET" == p1?.action) { //修改时间需要清除内部缓存
            MallBusManager.setTimeDifferenceValue("#")
            getSystemTime()
            CleanMessageUtil.cleanInternalCache(AppContext.getInstance().context)
        }
        if ("android.intent.action.CLOSE_SYSTEM_DIALOGS" == p1?.action) {
            LogTools.debug(p1?.action)
        }
        LogTools.debug(p1?.action)
    }
    private fun getSystemTime() {
        val d = Observable.just(true).flatMap {
            return@flatMap MallApplication.apiService.getSystemTime()
        }.compose(SchedulerUtils.ioToMain()).subscribe({ bean ->
            if (bean.code == ErrorStatus.SUCCESS) {
                val value = DateUtils.dateToStamp(bean.result as String) - System.currentTimeMillis()
                MallBusManager.setTimeDifferenceValue(value.toString())
            } else {

            }
        }, { throwable ->
            //处理异常
            ExceptionHandle.handleException(throwable)
        })
    }

}