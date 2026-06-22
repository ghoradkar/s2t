package com.myhindlab.abkat.camera.graphic

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

class FaceGuideGraphic(
    overlay: GraphicOverlay<*>
) : GraphicOverlay.Graphic(overlay) {

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

//    override fun draw(canvas: Canvas) {
//        val cx = canvas.width / 2f
//        val cy = canvas.height / 2f
//        val rx = canvas.width * 0.28f
//        val ry = canvas.height * 0.35f
//
//        // Dark background
//        canvas.drawRect(
//            0f,
//            0f,
//            canvas.width.toFloat(),
//            canvas.height.toFloat(),
//            bgPaint
//        )
//
//        // Clear oval (face cut-out)
//        val clearPath = Path().apply {
//            addOval(
//                cx - rx,
//                cy - ry,
//                cx + rx,
//                cy + ry,
//                Path.Direction.CW
//            )
//        }
//
//        bgPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//        canvas.drawPath(clearPath, bgPaint)
//        bgPaint.xfermode = null
//
//        // Oval border
//        canvas.drawOval(
//            cx - rx,
//            cy - ry,
//            cx + rx,
//            cy + ry,
//            borderPaint
//        )
//
//        // Instruction text
//        canvas.drawText(
//            "Align your face inside the oval",
//            cx,
//            cy + ry + 80f,
//            textPaint
//        )
//    }

    override fun draw(canvas: Canvas) {
        val p = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 12f
        }

        canvas.drawRect(
            100f,
            100f,
            canvas.width - 100f,
            canvas.height - 100f,
            p
        )
    }

}
