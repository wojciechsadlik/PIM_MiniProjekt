package com.example.canvas_kotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

private const val STROKE_WIDTH = 12f;

class CanvasView: View {
    private lateinit var canvas: Canvas;
    private lateinit var bitmap: Bitmap;
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.canvasBackground, null);
    private val drawColor = ResourcesCompat.getColor(resources, R.color.canvasPaint, null);
    private var motionTouchEventX = 0.0f;
    private var motionTouchEventY = 0.0f;
    private var currentX = 0.0f;
    private var currentY = 0.0f;

    private val paint = Paint().apply {
        color = drawColor;

        isAntiAlias = true;

        style = Paint.Style.STROKE;
        strokeJoin = Paint.Join.ROUND;
        strokeCap = Paint.Cap.ROUND;
        strokeWidth = STROKE_WIDTH;
    }

    private var path = Path();

    constructor(context: Context): super(context);
    constructor(context: Context, attrs: AttributeSet): super(context, attrs);

    public fun clearCanvas() {
        path.reset();

        canvas.drawColor(backgroundColor);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (this::bitmap.isInitialized) {
            bitmap.recycle();
        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        canvas = Canvas(bitmap);

        canvas.drawColor(backgroundColor);
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x;
        motionTouchEventY = event.y;

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart();
            MotionEvent.ACTION_MOVE -> touchMove();
            MotionEvent.ACTION_UP -> touchStop();
        }

        return true;
    }

    private fun touchStart() {
        path.reset();
        path.moveTo(motionTouchEventX, motionTouchEventY);
        currentX = motionTouchEventX;
        currentY = motionTouchEventY;
    }

    private fun touchMove() {
        path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)

        currentX = motionTouchEventX;
        currentY = motionTouchEventY;

        canvas.drawPath(path, paint);

        invalidate();
    }

    private fun touchStop() {
        path.reset();
    }
}