package com.will.surfaceviewdemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class BubbleSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) ,CoroutineScope by MainScope(), LifecycleObserver {
    @Volatile
    private var isRunning = false
    var deviation = 50f
        set(value) {
            field = value
            paint.pathEffect = ComposePathEffect(CornerPathEffect(50f),DiscretePathEffect(30f,deviation/2))
        }

    private val colors = arrayOf(
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.MAGENTA,
        Color.BLUE,
        Color.GRAY)

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private val bubblesList = mutableListOf<Bubble>()
    private var job: Job? = null
    private var centerX = 0f
    private var centerY = 0f
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
//        pathEffect = ComposePathEffect(CornerPathEffect(50f),DiscretePathEffect(30f,deviation/2))//组合离散效果
        //pathEffect = DiscretePathEffect(30f,20f)
    }

    private data class Bubble(val x:Float,val y:Float,val color:Int,var radius:Float)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        val x = (event?.x) ?:0f
        val y = (event?.y) ?:0f
        val color = colors.random()
        val bubble = Bubble(x,y,color,1f)
        bubblesList.add(bubble)
        if (bubblesList.size > 20) bubblesList.removeAt(0)
        return super.onTouchEvent(event)
    }

    //    init {
//        CoroutineScope(Dispatchers.Default).launch {
//            while (true){
//                if (holder.surface.isValid){
//                    val canvas = holder.lockCanvas()
//                    canvas.drawColor(Color.BLACK)
//                    bubblesList.toList().filter {
//                        it.radius < 3000
//                    }.forEach {
//                        paint.color = it.color
//                        canvas.drawCircle(it.x,it.y,it.radius,paint)
//                        it.radius += 10f
//                    }
//                    if (holder.surface.isValid)
//                    holder.unlockCanvasAndPost(canvas)
//                }
//            }
//        }

    private fun createJob() {
        job?.cancel()
        job = launch(Dispatchers.Default){
            while (isRunning){
                if (holder.surface.isValid){
                    val canvas = holder.lockCanvas()
                    canvas?.drawColor(ContextCompat.getColor(context,R.color.black))
                    bubblesList.toList().filter {
                        it.radius < 3000
                    }.forEach {
                        paint.color = it.color
                        canvas?.drawCircle(it.x,it.y,it.radius,paint)
                        it.radius += 10f
                    }
                    if (holder.surface.isValid)
                        holder?.unlockCanvasAndPost(canvas)
                }
            }
        }
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume(){
        isRunning = true
        createJob()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause(){
        isRunning = false
        job?.cancel()
    }
}