package com.epro.comp.im.mvp.contract

import com.epro.comp.im.mvp.model.bean.ChatMessage
import com.epro.comp.im.mvp.model.bean.CustomerService
import com.epro.comp.im.mvp.model.bean.MsgBody
import com.mike.baselib.base.IBaseView
import com.mike.baselib.base.IPresenter

interface ChatContract {

    interface View : IBaseView {
        fun <T :MsgBody>onGetMessageListSuccess(messageList:ArrayList<ChatMessage<T>>)
        fun onUploadImageSuccess(sList:ArrayList<String>)
        fun onGetCustomerServiceListSuccess(csList:ArrayList<CustomerService>)
    }

    interface Presenter : IPresenter<View> {
    }
}