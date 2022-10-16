package com.ble.android.demo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ble.android.demo.adapter.BleDeviceAdapter
import com.ble.android.demo.api.ApiInterface
import com.ble.android.demo.model.BtApiData
import com.ble.android.demo.model.BtDevice
import com.ble.android.demo.utils.SharedPreference
import com.ble.android.demo.viewmodel.MainRepository
import com.ble.android.demo.viewmodel.MainViewModel
import com.ble.android.demo.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    private val REQUEST_ENABLE_BT: Int = 100
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bleDeviceAdapter: BleDeviceAdapter
    lateinit var btRecyclerView: RecyclerView
    lateinit var viewModel: MainViewModel
    lateinit var apiInterface: ApiInterface
    lateinit var mainRepository: MainRepository

    private val BT_PERMISSION_REQUEST_CODE = 1
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btRecyclerView = findViewById(R.id.ble_devices)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        apiInterface = ApiInterface.getInstance()
        mainRepository = MainRepository(apiInterface)

        bleDeviceAdapter = BleDeviceAdapter(this) {
            // Pair and Connect
            pairAndConnectWithRemoteDevice(it.device)

            // send Raw Data To Server
            Toast.makeText(this, "Sending raw data...", Toast.LENGTH_LONG).show()
            val fcmToken = SharedPreference(this).getFcmToken()
            val rawData = BtApiData(rawData = "${it.name},${it.address}", fcmToken = fcmToken)
            viewModel.postRawData(rawData)
        }
        btRecyclerView.adapter = bleDeviceAdapter

        viewModel = ViewModelProvider(this, MainViewModelFactory(mainRepository)).get(MainViewModel::class.java)
        viewModel.btApiData.observe(this) {
            Toast.makeText(this, "${it.rawData} successfully posted to server", Toast.LENGTH_LONG).show()
            Log.i(TAG, "${it.id}, ${it.rawData} successfully posted to server")
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, "Error=> $it", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Error=> $it")
        }

        checkPermissions()
    }

    private fun pairAndConnectWithRemoteDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "BT permission is not enabled in pair request")
            return
        }

        // Pair and Connect with remote device
        device.createBond()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT), BT_PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN), BT_PERMISSION_REQUEST_CODE)
            // Start Scanning
            isBTEnabled()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isPermissionsAllowed = true
        when (requestCode) {
            BT_PERMISSION_REQUEST_CODE -> {
                grantResults.forEach {
                    if (it == PackageManager.PERMISSION_DENIED) {
                        isPermissionsAllowed = false
                    }
                }
                if (isPermissionsAllowed) {
                    // Start Scanning
                    isBTEnabled()
                } else {
                    Toast.makeText(this, "App won't work without permissions", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isBTEnabled() {
        if (!bluetoothAdapter.isEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermissions()
                return
            }
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            discoverDevices()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            discoverDevices()
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action!!
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkPermissions()
                        return
                    }
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    val deviceBondState = device.bondState

                    Log.i(TAG, "Name=>$deviceName, Address=>$deviceHardwareAddress, Bond=>$deviceBondState")
                    bleDeviceAdapter.addBtDevice(BtDevice(deviceName, deviceHardwareAddress, device))
                } else -> {
                    Log.i(TAG, "No Bluetooth device available")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun discoverDevices() {
        bleDeviceAdapter.clearAllBtDevices()
        val bondedDevices = bluetoothAdapter.bondedDevices
        bondedDevices?.forEach {
            bleDeviceAdapter.addBtDevice(BtDevice(it.name, it.address, it))
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "BT Scan permission is not enabled")
            checkPermissions()
            return
        }
        bluetoothAdapter.startDiscovery()
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }
}