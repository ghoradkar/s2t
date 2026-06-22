package com.myhindlab.abkat.adapters

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.myhindlab.abkat.R


class BluetoothDeviceAdapter(
    context: Context,
    resource: Int,
    bluetoothDevices: MutableList<BluetoothDevice>
) : ArrayAdapter<BluetoothDevice>(context, resource, bluetoothDevices) {

    @SuppressLint("MissingPermission")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // convertView which is recyclable view
        var currentItemView = convertView

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context)
                    .inflate(R.layout.devices_dialog_bluetooth_item, parent, false)
        }
        val crrDevicePosition = getItem(position)
        val tvDeviceName = convertView?.findViewById<TextView>(R.id.tvBtItemName)
        val tvDeviceAddress = convertView?.findViewById<TextView>(R.id.tvBtItemAddr)


        if (tvDeviceName != null && tvDeviceAddress != null) {
            tvDeviceName.text = crrDevicePosition?.name ?: "Unknown"
            tvDeviceAddress.text = crrDevicePosition?.address ?: "Unknown"
        }

        return currentItemView!!
    }
}