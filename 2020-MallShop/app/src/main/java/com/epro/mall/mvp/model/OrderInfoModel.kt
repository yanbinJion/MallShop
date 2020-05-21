package com.epro.mall.mvp.model

import android.app.Activity
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.*
import com.epro.mall.utils.MallBusManager
import com.epro.mall.utils.MallConst
import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.net.exception.ExceptionHandle
import com.mike.baselib.net.exception.PayException
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.utils.ToastUtil
import com.mike.baselib.utils.toast
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class OrderInfoModel : BaseModel() {
    fun createOrder(send: CreateOrderBean.Send): Observable<CreateOrderBean> {
        return getApiSevice().createOrder(send)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        if(TextUtils.isEmpty(it.result.notifyStr )|| TextUtils.isEmpty(it.result.orderSn)){
                            return@flatMap Observable.error<CreateOrderBean>(ApiException("订单创建异常", 100001))
                        }
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<CreateOrderBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }


    fun getShopInfo(shopId: String): Observable<GetShopInfoBean> {
        return getApiSevice().getShopInfo(shopId)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<GetShopInfoBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }
    fun getAddressList(parentId:String): Observable<AddressListBean> {

        return getApiSevice().getAddressList(parentId)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<AddressListBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }



}