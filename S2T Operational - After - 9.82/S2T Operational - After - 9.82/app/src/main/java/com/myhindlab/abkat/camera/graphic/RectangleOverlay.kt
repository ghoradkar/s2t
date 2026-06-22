package com.myhindlab.abkat.camera.graphic

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import com.myhindlab.abkat.camera.utils.CameraUtils

class RectangleOverlay(
    private val overlay: GraphicOverlay<*>,
    private val face: Face,
    private val rect: Rect,
) : GraphicOverlay.Graphic(overlay) {
    private val FACE_POSITION_RADIUS = 10.0f
    private val ID_TEXT_SIZE = 70.0f
    private val ID_Y_OFFSET = 80.0f
    private val ID_X_OFFSET = -70.0f
    private val BOX_STROKE_WIDTH = 5.0f
    private val COLOR_CHOICES = intArrayOf(
        Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW
    )
    private var currentColorIndex = 0

    private var facePositionPaint: Paint? = null
    private var idPaint: Paint? = null
    private var boxPaint: Paint? = null


    init {
        currentColorIndex =
            (currentColorIndex + 1) % COLOR_CHOICES.size
        val selectedColor: Int =
            COLOR_CHOICES.get(currentColorIndex)

        facePositionPaint = Paint()
        facePositionPaint!!.setColor(selectedColor)

        idPaint = Paint()
        idPaint!!.setColor(selectedColor)
        idPaint!!.setTextSize(ID_TEXT_SIZE)

        boxPaint = Paint()
        boxPaint!!.setColor(selectedColor)
        boxPaint!!.setStyle(Paint.Style.STROKE)
        boxPaint!!.setStrokeWidth(BOX_STROKE_WIDTH)
    }

    override fun draw(canvas: Canvas) {
        val rect = CameraUtils.calculateRect(
            overlay,
            rect.height().toFloat(),
            rect.width().toFloat(),
            face.boundingBox
        )

        boxPaint?.let { canvas.drawRect(rect, it) }


        // Draws a circle at the position of the detected face, with the face's track id below.
        val x = translateX(face.boundingBox.centerX().toFloat())
        val y = translateY(face.boundingBox.centerY().toFloat())
        facePositionPaint?.let {
            canvas.drawCircle(
                x,
                y,
                FACE_POSITION_RADIUS,
                it
            )
        }
        idPaint?.let {
            canvas.drawText(
                "id: " + face.trackingId,
                x + ID_X_OFFSET,
                y + ID_Y_OFFSET,
                it
            )
        }


        // Draws a bounding box around the face.
        val xOffset = scaleX(face.boundingBox.width() / 2.0f)
        val yOffset = scaleY(face.boundingBox.height() / 2.0f)
        val left = x - xOffset
        val top = y - yOffset
        val right = x + xOffset
        val bottom = y + yOffset
        boxPaint?.let { canvas.drawRect(left, top, right, bottom, it) }

        val contour = face.allContours
        for (faceContour in contour) {
            for (point in faceContour.points) {
                val px = translateX(point.x)
                val py = translateY(point.y)
                idPaint?.let {
                    canvas.drawCircle(
                        px,
                        py,
                        FACE_POSITION_RADIUS,
                        it
                    )
                }
            }
        }
        boxPaint?.let { canvas.drawRect(rect, it) }

        if (face.leftEyeOpenProbability != null) {
            canvas.drawText(
                "left eye: " + String.format("%.2f", face.leftEyeOpenProbability),
                x + ID_X_OFFSET * 6,
                y,
                idPaint!!
            )
        }
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
        if (leftEye != null) {
            canvas.drawCircle(
                translateX(leftEye.position.x),
                translateY(leftEye.position.y),
                FACE_POSITION_RADIUS,
                idPaint!!
            )
        }
    }

}