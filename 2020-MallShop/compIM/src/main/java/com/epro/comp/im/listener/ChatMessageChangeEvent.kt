package com.epro.comp.im.listener

import com.epro.comp.im.mvp.model.bean.ChatMessage
import com.epro.comp.im.mvp.model.bean.MsgBody

class ChatMessageChangeEvent<T:MsgBody> {
    var message: ChatMessage<T>?=null
}