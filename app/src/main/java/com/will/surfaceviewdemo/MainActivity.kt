package com.will.surfaceviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),SeekBarAndText.SongTimeCallBack,SeekBarAndText.OnSeekBarAndTextChangeListener{

    private var seekBar : SeekBarAndText? = null
    private var progress : Int?=0
    //private var drawView : BubbleSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(drawView)
        //drawView = findViewById(R.id.drawView)
        seekBar = findViewById(R.id.seek_bar)

        seekBar?.setOnSeekBarChangeListener(this)
        seekBar?.progress = 50
        seekBar?.setSongTimeCallBack(this)
    }

    override fun getSongTime(progress: Int): String {

        Log.e(TAG, "progress:$progress")
        return this.progress.toString()
    }

    override fun getDrawText(): String {
        return this.progress.toString()
    }

    override fun onProgress(seekBar: SeekBar, progress: Int, indicatorOffset: Float) {
        Log.e(TAG, "onProgress progress:$progress")
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromuser: Boolean) {
        this.progress = progress
        drawView?.deviation = progress.toFloat()
        Log.e(TAG, "onProgressChanged progress:$progress")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

}