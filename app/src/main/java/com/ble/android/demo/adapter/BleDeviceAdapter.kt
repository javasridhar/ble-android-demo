package com.ble.android.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ble.android.demo.R
import com.ble.android.demo.model.BtDevice

class BleDeviceAdapter(private val context: Context, private val itemClickListener: (BtDevice) -> Unit) : RecyclerView.Adapter<BleDeviceAdapter.BleViewHolder>() {

    private var btDevices: MutableList<BtDevice> = mutableListOf()

    inner class BleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bleDeviceName: TextView = itemView.findViewById(R.id.bt_device_name)
        var bleDeviceConnectionStatus: TextView = itemView.findViewById(R.id.bt_device_connection_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleViewHolder {
        return BleViewHolder(LayoutInflater.from(context).inflate(R.layout.ble_device_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: BleViewHolder, position: Int) {
        val btDevice = btDevices[position]
        holder.bleDeviceName.text = btDevice.name
        holder.bleDeviceConnectionStatus.text = btDevice.address
        holder.itemView.setOnClickListener { itemClickListener(btDevice) }
    }

    override fun getItemCount(): Int {
        return btDevices.size
    }

    fun addBtDevice(btDevice: BtDevice) {
        if (!btDevices.contains(btDevice)) {
            btDevices.add(btDevice)
            notifyDataSetChanged()
        }
    }

    fun clearAllBtDevices() {
        btDevices.clear()
        notifyDataSetChanged()
    }
}