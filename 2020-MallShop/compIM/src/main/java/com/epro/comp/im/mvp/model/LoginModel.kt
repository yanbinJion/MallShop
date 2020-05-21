package com.epro.comp.im.mvp.model

import com.mike.baselib.net.exception.ApiException
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.rx.scheduler.SchedulerUtils
import com.epro.comp.im.mvp.model.bean.LoginBean
import com.epro.comp.im.mvp.model.bean.User
import com.epro.comp.im.smack.SmackManager
import io.reactivex.Observable

class LoginModel : BaseModel() {
    fun login(username: String, password: String): Observable<LoginBean> {
        return Observable.just(true).flatMap {
            val result = SmackManager.getInstance().login(username, password)
            if (result) {
                return@flatMap Observable.just(LoginBean(ErrorStatus.SUCCESS, ErrorStatus.SUCCESS_MSG, User(username,username,password,"")))
            } else {
                return@flatMap Observable.error<LoginBean>(ApiException("登录失败", ErrorStatus.API_ERROR))
            }
        }.compose(SchedulerUtils.ioToMain())
    }

}
