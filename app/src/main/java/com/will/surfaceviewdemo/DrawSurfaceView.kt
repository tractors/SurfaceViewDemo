package com.will.surfaceviewdemo

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView

class DrawSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var centerX = 0f
    private var centerY = 0f
    private val colors = arrayOf(
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.MAGENTA,
        Color.BLUE,
        Color.GRAY)

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        centerX = event?.x ?:0f
        centerY = event?.y ?:0f

        val canvas = holder.lockCanvas()
        canvas.drawColor(Color.BLACK)
        repeat(3000){
            paint.color = colors.random()
            canvas?.drawCircle(centerX,centerY,it.toFloat() / 5,paint)
        }

        holder.unlockCanvasAndPost(canvas)
        return super.onTouchEvent(event)
    }
}