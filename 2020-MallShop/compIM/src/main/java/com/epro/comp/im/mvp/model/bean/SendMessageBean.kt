package com.epro.comp.im.mvp.model.bean

data class SendMessageBean<T:MsgBody>(override val code: Int, override val message: String, override val result: ChatMessage<T>) : BaseBean<ChatMessage<T>>
