package com.google.firebase.samples.apps.mlkit.kotlin.barcodescanning

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.samples.apps.mlkit.common.GraphicOverlay

class BarcodeGraphic(overlay: GraphicOverlay, barcode: FirebaseVisionBarcode) :
    GraphicOverlay.Graphic(overlay) {

    companion object {
        private const val TEXT_COLOR = Color.WHITE
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
    }

    private var rectPaint: Paint
    private var barcodePaint: Paint
    val barcode: FirebaseVisionBarcode?
    private var rect: RectF? = null

    init {
        this.barcode = barcode

        rectPaint = Paint()
        rectPaint.color = TEXT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH

        barcodePaint = Paint()
        barcodePaint.color = TEXT_COLOR
        barcodePaint.textSize = TEXT_SIZE
    }

    /**
     * Draws the barcode block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas) {
        if (barcode == null) {
            throw IllegalStateException("Attempting to draw a null barcode.")
        }

        // Draws the bounding box around the BarcodeBlock.
        rect = RectF(barcode.boundingBox).apply {
            left = translateX(left)
            top = translateY(top)
            right = translateX(right)
            bottom = translateY(bottom)
            canvas.drawRect(this, rectPaint)

            // Renders the barcode at the bottom of the box.
            barcode.rawValue?.let { value ->
                canvas.drawText(value, left, bottom, barcodePaint)
            }
        }
    }

    override fun contains(x: Float, y: Float) = rect?.contains(x, y) ?: false
}