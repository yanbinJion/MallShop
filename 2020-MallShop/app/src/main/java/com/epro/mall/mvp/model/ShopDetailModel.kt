package com.epro.mall.mvp.model

import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.FollowShopBean
import com.epro.mall.mvp.model.bean.GetShopHomeViewBean
import com.epro.mall.mvp.model.bean.GetShopInfoBean
import io.reactivex.Observable

class ShopDetailModel : BaseModel() {
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

    fun followShop(shopId: String): Observable<FollowShopBean> {
        val send = FollowShopBean.Send(shopId)
        return getApiSevice().followShop(send)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<FollowShopBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    fun getShopHomeView(shopId: String): Observable<GetShopHomeViewBean> {
        return getApiSevice().getShopHomeView(shopId)
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<GetShopHomeViewBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }
}