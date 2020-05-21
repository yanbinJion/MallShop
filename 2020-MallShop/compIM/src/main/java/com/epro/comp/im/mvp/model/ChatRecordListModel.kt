package com.epro.comp.im.mvp.model

import android.text.TextUtils
import com.epro.comp.im.mvp.model.bean.GetChatRecordListBean
import com.epro.comp.im.utils.IMBusManager
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

class ChatRecordListModel : BaseModel() {
    fun getChatRecordList(shopId:String): Observable<GetChatRecordListBean> {
        return Observable.just(true).flatMap {
            if(!TextUtils.isEmpty(shopId)){
                //收到的所有消息标记为已读
                IMBusManager.updateMessageRead(shopId)
            }
            val data = GetChatRecordListBean.Result(IMBusManager.getChatRecords())
            return@flatMap Observable.just(GetChatRecordListBean(ErrorStatus.SUCCESS, ErrorStatus.SUCCESS_MSG, data))
        }.compose(SchedulerUtils.ioToMain())
    }
}