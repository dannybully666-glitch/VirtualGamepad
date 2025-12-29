package com.example.virtualgamepad

import android.content.Context
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class JoystickView(context: Context) : View(context) {

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h / 2f
        radius = minOf(w, h) / 2.5f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dx = event.x - centerX
        val dy = event.y - centerY

        val distance = sqrt(dx * dx + dy * dy)
        val clamped = minOf(distance, radius)

        val normX = (dx / radius).coerceIn(-1f, 1f)
        val normY = (dy / radius).coerceIn(-1f, 1f)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                sendAxis(normX, normY)
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                sendAxis(0f, 0f)
            }
        }

        return true
    }

    private fun sendAxis(x: Float, y: Float) {
        val now = System.nanoTime()

        val event = MotionEvent.obtain(
            now,
            now,
            MotionEvent.ACTION_MOVE,
            0f,
            0f,
            0
        )

        event.source = InputDevice.SOURCE_JOYSTICK
        event.setAxisValue(MotionEvent.AXIS_X, x)
        event.setAxisValue(MotionEvent.AXIS_Y, y)

        dispatchGenericMotionEvent(event)
        event.recycle()
    }
}
