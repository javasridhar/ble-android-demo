package com.ble.android.demo.model

import android.bluetooth.BluetoothDevice

data class BtDevice(val name: String?, val address: String, val device: BluetoothDevice) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BtDevice

        if (name != other.name) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + address.hashCode()
        return result
    }
}
