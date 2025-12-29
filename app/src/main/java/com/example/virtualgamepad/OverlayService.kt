package com.example.virtualgamepad

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.view.View
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var joystickView: JoystickView

    override fun onCreate() {
        super.onCreate()

        makeForeground()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        joystickView = JoystickView(this)

        // 🔴 DEBUG: force visible background
        joystickView.setBackgroundColor(0x55FF0000)

        val params = WindowManager.LayoutParams(
            500,
            500,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 0

        Log.d("OverlayService", "Adding joystick overlay")
        windowManager.addView(joystickView, params)
    }

    private fun makeForeground() {
        val channelId = "overlay_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Virtual Gamepad Overlay",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("VirtualGamepad")
            .setContentText("Overlay running")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(joystickView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
