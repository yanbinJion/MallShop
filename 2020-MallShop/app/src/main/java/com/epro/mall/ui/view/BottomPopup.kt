package com.epro.mall.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.epro.mall.R
import razerdp.basepopup.BasePopupWindow

class BottomPopup(context: Context):BasePopupWindow(context) {

    override fun onCreateContentView(): View {
         val view = LayoutInflater.from(context).inflate(R.layout.bottom_popup,null)
        return view
    }
}