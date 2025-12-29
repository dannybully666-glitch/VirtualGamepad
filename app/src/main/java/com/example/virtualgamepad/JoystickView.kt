package com.example.virtualgamepad

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class JoystickView(context: Context) : View(context) {

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(120, 0, 0, 0)
    }

    private val knobPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(200, 255, 255, 255)
    }

    private var centerX = 0f
    private var centerY = 0f
    private var baseRadius = 0f
    private var knobRadius = 0f

    private var knobX = 0f
    private var knobY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = w / 2f
        centerY = h / 2f

        baseRadius = minOf(w, h) / 2.5f
        knobRadius = baseRadius / 2f

        knobX = centerX
        knobY = centerY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Base circle
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint)

        // Knob
        canvas.drawCircle(knobX, knobY, knobRadius, knobPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dx = event.x - centerX
        val dy = event.y - centerY
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                if (distance < baseRadius) {
                    knobX = event.x
                    knobY = event.y
                } else {
                    val ratio = baseRadius / distance
                    knobX = centerX + dx * ratio.toFloat()
                    knobY = centerY + dy * ratio.toFloat()
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                knobX = centerX
                knobY = centerY
                invalidate()
            }
        }
        return true
    }
}
