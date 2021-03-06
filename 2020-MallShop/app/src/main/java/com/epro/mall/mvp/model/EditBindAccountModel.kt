package com.epro.mall.mvp.model

import com.mike.baselib.listener.RetryWithDelay
import com.epro.mall.mvp.model.bean.CheckBindAccountBean
import com.epro.mall.mvp.model.bean.EditBindAccountBean
import com.epro.mall.mvp.model.bean.GetUserVfBean
import com.epro.mall.mvp.model.bean.GetVfBean
import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

class EditBindAccountModel : BaseModel() {

    fun getEditBindAccount(account:String,code:String,authType:String): Observable<EditBindAccountBean> {
        return getApiSevice().editBindAccount(EditBindAccountBean.Send(account,code,authType))
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<EditBindAccountBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取验证码
     */
    fun getVfCode(account:String,type:Int): Observable<GetVfBean> {
        return getApiSevice().getVfcode(GetVfBean.Send(account,type))
                .flatMap {
                    if(it.code== ErrorStatus.SUCCESS){
                        return@flatMap  Observable.just(it)
                    }else{
                        return@flatMap Observable.error<GetVfBean>(ApiException(it.message,it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    //获取验证码2
    fun getUserVfCode(authType:String,account:String,type:Int): Observable<GetUserVfBean> {
        return getApiSevice().getUserVfcode(GetUserVfBean.Send(authType,account,type))
                .flatMap {
                    if(it.code== ErrorStatus.SUCCESS){
                        return@flatMap  Observable.just(it)
                    }else{
                        return@flatMap Observable.error<GetUserVfBean>(ApiException(it.message,it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

    //检查账号
    fun checkBindAccount(account:String,code:String,authType:String): Observable<CheckBindAccountBean> {
        return getApiSevice().checkBindAccount(CheckBindAccountBean.Send(account,code,authType))
                .flatMap {
                    if (it.code == ErrorStatus.SUCCESS) {
                        return@flatMap Observable.just(it)
                    } else {
                        return@flatMap Observable.error<CheckBindAccountBean>(ApiException(it.message, it.code))
                    }
                }
                .retryWhen(RetryWithDelay())
                .compose(SchedulerUtils.ioToMain())
    }

}