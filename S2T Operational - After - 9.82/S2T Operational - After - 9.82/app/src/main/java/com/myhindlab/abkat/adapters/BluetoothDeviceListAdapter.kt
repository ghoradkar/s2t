package com.myhindlab.abkat.adapters

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myhindlab.abkat.R

class BluetoothDeviceListAdapter(val deviceList: MutableList<BluetoothDevice>) :
    RecyclerView.Adapter<BluetoothDeviceListAdapter.BluetoothDeviceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.devices_dialog_bluetooth_item, parent, false)
        return BluetoothDeviceViewHolder(view)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {

        val device = deviceList[holder.absoluteAdapterPosition]

        holder.tvDeviceAddress.text = device.address
        holder.tvDeviceName.text = device.name

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }


    inner class BluetoothDeviceViewHolder(itemView: View) : ViewHolder(itemView) {

        val tvDeviceName = itemView.findViewById<TextView>(R.id.tvBtItemName)
        val tvDeviceAddress = itemView.findViewById<TextView>(R.id.tvBtItemAddr)

    }

}