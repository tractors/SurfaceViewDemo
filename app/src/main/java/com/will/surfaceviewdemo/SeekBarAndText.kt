package com.will.surfaceviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar

@SuppressLint("AppCompatCustomView")
class SeekBarAndText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SeekBar(context, attrs, defStyleAttr) {
    // 画笔
    private val paint = TextPaint().apply {
        color = Color.parseColor("#99000000")
        isAntiAlias = true
        textSize = sp2px(6f)
    }

    // 进度文字位置信息
    private val mProgressTextRect = Rect()
    // 滑块按钮宽度
    private val mThumbWidth : Int = dp2px(60f)
    //进度监听
    private var onSeekBarAndTextChangeListener : OnSeekBarAndTextChangeListener? = null
    //对外提供的接口用于返回当前要画的时间
    private var songTimeCallBack: SongTimeCallBack? = null

    init {
        // 如果不设置padding，当滑动到最左边或最右边时，滑块会显示不全
        setPadding(mThumbWidth / 2, 0, mThumbWidth / 2, 0)
        // 设置滑动监听
        this.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                seekBar?.let { onSeekBarAndTextChangeListener?.onProgressChanged(it,progress,fromUser) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let { onSeekBarAndTextChangeListener?.onStartTrackingTouch(it) }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let { onSeekBarAndTextChangeListener?.onStopTrackingTouch(it) }
            }

        })
    }


    @Synchronized
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var progressText ="" //要画的文字
        if (songTimeCallBack != null){
            //将要画的时间对外提供
            progressText = songTimeCallBack!!.getDrawText()

        }

        //画滑块
        paint.getTextBounds(progressText,0,progressText.length,mProgressTextRect)
        // 进度百分比
        var progressRatio : Float = progress.toFloat() /max
        val thumbOffset : Float = (mThumbWidth - mProgressTextRect.width()) /2 - mThumbWidth * progressRatio
        val thumbX : Float = width * progressRatio + thumbOffset
        val thumbY : Float = height /2f + mProgressTextRect.height() /2f
        val indicatorOffset : Float = width * progressRatio - mThumbWidth /2 - mThumbWidth * progressRatio
        if (progressRatio > 0){
            //画文字
            canvas?.drawText(progressText,thumbX,thumbY,paint)
            //paint.getTextBounds(progressText,0,progressText.length,mProgressTextRect)
            //滑块移动
            mProgressTextRect.offsetTo(thumbX.toInt(),thumbY.toInt())
        } else{
            canvas?.drawText(progressText,((mThumbWidth - mProgressTextRect.width())/2).toFloat(),thumbY,paint)
        }
    }
    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,resources.displayMetrics)
    }

    private fun dp2px(dp:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,resources.displayMetrics).toInt()
    }


    /**
     * 设置监听
     */
    fun setOnSeekBarChangeListener(listener:OnSeekBarAndTextChangeListener){
        this.onSeekBarAndTextChangeListener = listener
    }

    /**
     * 进度监听
     */
    interface OnSeekBarAndTextChangeListener{
        /**
         * 进度
         */
        fun onProgress(seekBar:SeekBar,progress:Int,indicatorOffset:Float)

        /**
         * 进度监听回调
         */
        fun onProgressChanged(seekBar: SeekBar,progress: Int,fromuser:Boolean)

        /**
         * 开始拖动
         */
        fun onStartTrackingTouch(seekBar: SeekBar)

        /**
         * 停止拖动
         */
        fun onStopTrackingTouch(seekBar: SeekBar)
    }


    interface SongTimeCallBack{
        fun getSongTime(progress: Int):String

        fun getDrawText():String
    }

    fun setSongTimeCallBack(songTimeCallBack:SongTimeCallBack){
        this.songTimeCallBack = songTimeCallBack
    }
}