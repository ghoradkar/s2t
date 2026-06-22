package com.myhindlab.abkat.bioland.adapter;


import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhindlab.abkat.R;

import java.util.List;

public class ContentListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ContentListAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_contentlist_item, data);

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item)
    {

        int bgColor = ContextCompat.getColor(mContext, R.color.color_white);

        //1.设置背景
        viewHolder.setBackgroundColor(R.id.ll_contentlist_item, bgColor);

        //2.设置内容
        String tempString;


        //2.1 deviceName
        TextView tvContent = viewHolder.getView(R.id.tv_content);

        tvContent.setText(item);



    }
}