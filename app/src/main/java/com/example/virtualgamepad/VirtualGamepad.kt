package com.example.virtualgamepad

import android.content.Context
import android.hardware.input.VirtualDeviceManager
import android.hardware.input.VirtualInputDevice
import android.hardware.input.VirtualInputDeviceParams
import android.hardware.input.VirtualDeviceParams
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class VirtualGamepad(context: Context) {

    private val device: android.hardware.input.VirtualDevice
    private val inputDevice: VirtualInputDevice

    init {
        val vdm = context.getSystemService(VirtualDeviceManager::class.java)

        val deviceParams = VirtualDeviceParams.Builder()
            .setName("Analog Gamepad")
            .build()

        device = vdm.createVirtualDevice(deviceParams)

        val inputParams = VirtualInputDeviceParams.Builder()
            .setName("Virtual Analog Gamepad")
            .setVendorId(0x045E)
            .setProductId(0x028E)
            .setInputDeviceType(VirtualInputDevice.TYPE_GAMEPAD)
            .build()

        inputDevice = device.createVirtualInputDevice(inputParams)
    }
}
