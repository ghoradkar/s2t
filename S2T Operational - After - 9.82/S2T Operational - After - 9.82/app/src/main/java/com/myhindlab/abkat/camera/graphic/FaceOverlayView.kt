package com.myhindlab.abkat.camera.graphic

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FaceOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val bgPaint = Paint().apply {
        color = Color.parseColor("#AA000000")
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 42f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val rx = width * 0.28f
        val ry = height * 0.35f

        // Dark background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Clear oval
        val path = Path().apply {
            addOval(
                cx - rx, cy - ry,
                cx + rx, cy + ry,
                Path.Direction.CW
            )
        }

        val clearPaint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        canvas.drawPath(path, clearPaint)

        // Border
        canvas.drawOval(
            cx - rx, cy - ry,
            cx + rx, cy + ry,
            borderPaint
        )

        // Text
        canvas.drawText(
            "Align your face inside the oval",
            cx,
            cy + ry + 80f,
            textPaint
        )
    }
}
