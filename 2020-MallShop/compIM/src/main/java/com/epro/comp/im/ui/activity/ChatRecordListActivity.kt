package com.epro.comp.im.ui.activity

import android.content.Context
import android.content.Intent
import com.epro.comp.im.R
import com.epro.comp.im.service.IMService
import com.epro.comp.im.ui.fragment.ChatRecordListFragment
import com.mike.baselib.activity.BaseSimpleActivity


class ChatRecordListActivity : BaseSimpleActivity(){
    override fun layoutContentId(): Int {
        return R.layout.activity_chat_recordlist
    }

    companion object {
        const val TAG = "getChatRecordList"
        const val FOR_CHAT_RESULT = 11
        fun launch(context: Context) {
            launchWithStr(context, "")
        }

        fun launchWithStr(context: Context, str: String) {
            context.startActivity(Intent(context, ChatRecordListActivity::class.java).putExtra(TAG, str))
        }
    }

    override fun initData() {

    }

    override fun initView() {
        super.initView()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContent,ChatRecordListFragment()).commitAllowingStateLoss()
    }
    override fun initListener() {
    }

}


