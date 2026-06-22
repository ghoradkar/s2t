package com.myhindlab.abkat.bioland.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.bioland.entity.DeviceInfoDetailEntity;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<DeviceInfoDetailEntity, BaseViewHolder> {

    public DeviceListAdapter(@Nullable List<DeviceInfoDetailEntity> data) {
        super(R.layout.adapter_devicelist_item, data);

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, DeviceInfoDetailEntity item)
    {

        int bgColor = ContextCompat.getColor(mContext, R.color.color_white);

        //1.设置背景
        viewHolder.setBackgroundColor(R.id.ll_devicelist_item, bgColor);

        //2.设置内容
        String tempString;


        //2.1 deviceName
        TextView tvDeviceName = viewHolder.getView(R.id.tv_devicename);

        tempString = item.getDeviceName();
        tvDeviceName.setText(tempString);

        //2.2 Mac
        TextView tvMacAddress = viewHolder.getView(R.id.tv_macaddress);

        tempString = item.getMacAddress();
        tvMacAddress.setText(tempString);

    }
}