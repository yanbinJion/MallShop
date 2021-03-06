package com.epro.comp.im.mvp.presenter

import com.epro.comp.im.mvp.contract.ChatContract
import com.epro.comp.im.mvp.model.ChatModel
import com.epro.comp.im.mvp.model.bean.MsgBody
import com.epro.comp.im.mvp.model.bean.MsgType
import com.epro.comp.im.utils.IMConst
import com.mike.baselib.base.BasePresenter
import com.mike.baselib.net.exception.ErrorStatus
import com.mike.baselib.net.exception.ExceptionHandle
import org.jivesoftware.smackx.filetransfer.FileTransferRequest
import java.io.File

class ChatPresenter : BasePresenter<ChatContract.View>(), ChatContract.Presenter {

    private val model by lazy { ChatModel() }


    fun <T : MsgBody> getMessageList(shopId:String,type: String = IMConst.GET_MESSAGE_LIST) {
        checkViewAttached()
        mRootView?.showLoading(type)
        val disposable = model.getMessageList<T>(shopId)
                .subscribe({ bean ->
                    mRootView?.onGetMessageListSuccess(bean.result)
                    mRootView?.dismissLoading(ErrorStatus.SUCCESS_MSG, ErrorStatus.SUCCESS, type)
                }, { throwable ->
                    //处理异常
                    ExceptionHandle.handleException(throwable)
                    mRootView?.showError(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                    mRootView?.dismissLoading(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                })
        addSubscription(disposable)
    }



    fun uploadImage(image: File, isCreateThumb: Int, type: String = IMConst.UPLOAD_IMAGE) {
        checkViewAttached()
        mRootView?.showLoading(type)
        val disposable = model.updateImage(image, isCreateThumb)
                .subscribe({ bean ->
                    mRootView?.onUploadImageSuccess(bean.result)
                    mRootView?.dismissLoading(ErrorStatus.SUCCESS_MSG, ErrorStatus.SUCCESS, type)
                }, { throwable ->
                    //处理异常
                    ExceptionHandle.handleException(throwable)
                    mRootView?.showError(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                    mRootView?.dismissLoading(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                })
        addSubscription(disposable)
    }
    fun getCustomerServiceList(shopId:String, type: String = IMConst.GET_CUSTOMER_SERVICE_LIST) {
        checkViewAttached()
        mRootView?.showLoading(type)
        val disposable = model.getCustomerServiceList(shopId)
                .subscribe({ bean ->
                    mRootView?.onGetCustomerServiceListSuccess(bean.result.list)
                    mRootView?.dismissLoading(ErrorStatus.SUCCESS_MSG, ErrorStatus.SUCCESS, type)
                }, { throwable ->
                    //处理异常
                    ExceptionHandle.handleException(throwable)
                    mRootView?.showError(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                    mRootView?.dismissLoading(ExceptionHandle.errorMsg, ExceptionHandle.errorCode, type)
                })
        addSubscription(disposable)
    }

}