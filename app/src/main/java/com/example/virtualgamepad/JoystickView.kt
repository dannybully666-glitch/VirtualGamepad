package com.example.virtualgamepad

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class JoystickView(context: Context) : View(context) {

    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var knobRadius = 0f

    private var knobX = 0f
    private var knobY = 0f

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(120, 0, 0, 0)
    }

    private val knobPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(200, 255, 255, 255)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h / 2f
        baseRadius = minOf(w, h) / 3f
        knobRadius = baseRadius / 2.5f
        knobX = centerX
        knobY = centerY
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint)
        canvas.drawCircle(knobX, knobY, knobRadius, knobPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dx = event.x - centerX
        val dy = event.y - centerY
        val distance = sqrt(dx * dx + dy * dy)

        val clampedDistance = minOf(distance, baseRadius)
        val scale = if (distance > 0) clampedDistance / distance else 0f

        knobX = centerX + dx * scale
        knobY = centerY + dy * scale

        val normX = (knobX - centerX) / baseRadius
        val normY = (knobY - centerY) / baseRadius

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> sendAxis(normX, normY)

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                knobX = centerX
                knobY = centerY
                sendAxis(0f, 0f)
            }
        }

        invalidate()
        return true
    }

    private fun sendAxis(x: Float, y: Float) {
        val now = System.currentTimeMillis()

        val properties = MotionEvent.PointerProperties().apply {
            id = 0
            toolType = MotionEvent.TOOL_TYPE_FINGER
        }

        val coords = MotionEvent.PointerCoords().apply {
            setAxisValue(MotionEvent.AXIS_X, x)
            setAxisValue(MotionEvent.AXIS_Y, y)
        }

        val event = MotionEvent.obtain(
            now,
            now,
            MotionEvent.ACTION_MOVE,
            1,
            arrayOf(properties),
            arrayOf(coords),
            0,
            0,
            1f,
            1f,
            0,
            0,
            InputDevice.SOURCE_JOYSTICK,
            0
        )

        dispatchGenericMotionEvent(event)
        event.recycle()
    }
}
