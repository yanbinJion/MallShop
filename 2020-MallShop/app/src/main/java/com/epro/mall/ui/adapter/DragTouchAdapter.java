/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epro.mall.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epro.mall.R;
import com.epro.mall.mvp.model.bean.MemberCentreBean;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 触摸即开始拖拽。
 * </p>
 * Created by YanZhenjie on 2016/7/22.
 */
public class DragTouchAdapter extends BaseAdapter<DragTouchAdapter.ViewHolder> {

    private SwipeRecyclerView mMenuRecyclerView;
    private ArrayList<MemberCentreBean> mDataList;

    public DragTouchAdapter(Context context, SwipeRecyclerView menuRecyclerView) {
        super(context);
        Log.e("YB","DragTouchAdapter : "+mDataList);
        this.mMenuRecyclerView = menuRecyclerView;
    }

    @Override
    public void notifyDataSetChanged(ArrayList<MemberCentreBean> dataList) {
        Log.e("YB","notifyDataSetChanged : "+mDataList);
        this.mDataList =dataList ;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getInflater().inflate(R.layout.member_centre_item, parent, false));
        viewHolder.mMenuRecyclerView = mMenuRecyclerView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("YB","onBindViewHolder : "+mDataList);
        holder.setData(mDataList.get(position).getResult());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        TextView tvTitle;
        SwipeRecyclerView mMenuRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.memberId);
            itemView.findViewById(R.id.level).setOnTouchListener(this);
        }

        public void setData(String title) {
            Log.e("YB","setData : "+title);
            this.tvTitle.setText(title);
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mMenuRecyclerView.startDrag(this);
                    break;
                }
            }
            return false;
        }
    }

}