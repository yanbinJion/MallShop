package com.epro.comp.im.mvp.model

import com.epro.comp.im.mvp.model.bean.*
import com.epro.comp.im.utils.IMBusManager
import com.epro.comp.im.utils.IMConst
import com.mike.baselib.listener.RetryWithDelay
import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ChatModel : BaseModel() {

    fun <T : MsgBody> getMessageList(shopId: String): Observable<GetMessageListBean<T>> {
        return Observable.just(true).flatMap {
            logTools.d("do here1")
            IMBusManager.updateMessageRead(shopId) //全部设为已读
            logTools.d("do here2")
            val messageList = IMBusManager.getMessageList<T>(shopId)
            logTools.d("do here3")
            return@flatMap Observable.just(GetMessageListBean(ErrorStatus.SUCCESS, ErrorStatus.SUCCESS_MSG, messageList as ArrayList<ChatMessage<T>>))
        }.compose(SchedulerUtils.ioToMain())
    }


    fun updateImage(image: File, isCreateThumb: Int): Observable<UploadImageBean> {
        val bodyMap = HashMap<String, RequestBody>()
        bodyMap!!.put("isCreateThumb", RequestBody.create(MediaType.parse("text/plain"), isCreateThumb.toString()))
        val requestBody = RequestBody.create(MediaType.parse("image/png"), image)
        val body = MultipartBody.Part.createFormData("image", image.name, requestBody)
        return getApiSevice().uploadImage(bodyMap, body)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<UploadImageBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    fun getCustomerServiceList(shopId: String): Observable<GetCustomerServiceListBean> {
        return getApiSevice().getCustomerServiceList(shopId)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        if (it.result.list.isNotEmpty()) {
                            if(it.result.type==IMConst.CS_TYPE_MERCH){
                                for(c in it.result.list){
                                    c.shopLogo=it.result.shopLogo
                                    c.avatar=it.result.shopLogo
                                    c.shopId=it.result.shopId
                                    c.shopName=it.result.shopName
                                }
                            }
                        } else {
//                            val cs1 = CustomerService("zhonghua", "", "zhonghua", shopId, "")
//                            val cs2 = CustomerService("id000000055", "", "id000000055", shopId, "")
//                            val cs1= CustomerService()
//                            val cs2= CustomerService()
//                            val cs3 = CustomerService()
//                            cs1.account="zhonghua"
//                            cs1.name="zhonghua"
//                            cs1.shopId=shopId
//
//                            cs2.account="id000000043"
//                            cs2.name="id000000043"
//                            cs2.shopId=shopId
//
//                            cs3.account="jason"
//                            cs3.name="jason"
//                            cs3.shopId=shopId
//                            it.result.list.add(cs3)
//                            //it.result.list.add(cs1)
//                            it.result.list.add(cs2)
                            return@flatMap Observable.error<GetCustomerServiceListBean>(ApiException("没找到客服额",-10000))
                        }
                        IMBusManager.saveCustomerService(it.result.list)
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<GetCustomerServiceListBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }


}