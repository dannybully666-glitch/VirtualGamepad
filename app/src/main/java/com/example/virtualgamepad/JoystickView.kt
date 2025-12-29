package com.example.virtualgamepad

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class JoystickView(context: Context) : View(context) {

    private val basePaint = Paint().apply {
        color = Color.GRAY
        alpha = 120
        isAntiAlias = true
    }

    private val knobPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private var centerX = 0f
    private var centerY = 0f
    private var knobX = 0f
    private var knobY = 0f
    private var radius = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h / 2f
        knobX = centerX
        knobY = centerY
        radius = w.coerceAtMost(h) / 2.5f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, basePaint)
        canvas.drawCircle(knobX, knobY, radius / 2, knobPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dx = event.x - centerX
        val dy = event.y - centerY
        val distance = sqrt(dx * dx + dy * dy)

        if (event.action != MotionEvent.ACTION_UP) {
            if (distance < radius) {
                knobX = event.x
                knobY = event.y
            } else {
                val ratio = radius / distance
                knobX = centerX + dx * ratio
                knobY = centerY + dy * ratio
            }
        } else {
            knobX = centerX
            knobY = centerY
        }

        invalidate()
        return true
    }
}
