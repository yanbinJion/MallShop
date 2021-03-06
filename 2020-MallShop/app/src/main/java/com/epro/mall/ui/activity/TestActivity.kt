package com.epro.mall.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.epro.mall.R
import com.epro.mall.listener.RefreshHomePageFinishEvent
import com.epro.mall.listener.BigEvent
import com.mike.baselib.activity.BaseSimpleActivity
import com.mike.baselib.utils.DisplayManager
import kotlinx.android.synthetic.main.activity_test.*
import com.mike.baselib.utils.toast
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TestActivity : BaseSimpleActivity() {
    override fun layoutContentId(): Int {
        return R.layout.activity_test
    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun initView() {
        setActivitySize(1,1)
        super.initView()
        btnSure.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(edtSignData.text.toString())) {
                toast(getString(R.string.data_cannot_be_empty))
                return@OnClickListener
            }
            setResult(Activity.RESULT_OK, Intent().putExtra("zfb",edtSignData.text.toString()))
            finish()
        })
        toast("test 启动")
    }

    fun setActivitySize(width:Int,height:Int){
        val window = getWindow()
        val layoutParams = window.getAttributes()
        layoutParams.x = 0
        layoutParams.y = 0
        layoutParams.width = width
        layoutParams.height = height
        //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        if(width==1){
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        }
        window.setBackgroundDrawableResource(R.color.transparent)
        window.setAttributes(layoutParams)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshHomePageFinishEvent(event: BigEvent) {
        setActivitySize(DisplayManager.getScreenWidth()!!, DisplayManager.getScreenHeight()!!)
        toast("toast 变大")
    }
}