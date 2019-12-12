package com.example.wbvideo1.system


import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

import java.io.IOException

class SystemMediaPlayer : SurfaceView {

    private var mSurfaceHolder: SurfaceHolder? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mContext: Context? = null
    private var isMuted = false
    private var isPlaying = false
    private var loopFlag = false
    private var time: Long = 0

    var isLoop: Boolean
        get() {
            Log.d(TAG, "isLoop!")
            return loopFlag
        }
        set(loop) {
            Log.d(TAG, "setLoop!")
            loopFlag = loop
            return
        }


    private val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceCreated!")
            mMediaPlayer!!.setDisplay(holder)
            return
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            Log.d(TAG, "surfaceChanged!")
            return
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            Log.d(TAG, "surfaceDestroyed!")
            mMediaPlayer = null
            return
        }
    }

    private val onPreparedListener = MediaPlayer.OnPreparedListener { mp ->
        Log.d(TAG, "onPrepared!")
        mp.start()
        return@OnPreparedListener
    }

    private val onCompletionListener = MediaPlayer.OnCompletionListener { mp ->
        Log.d(TAG, "onCompletion!")
        if (loopFlag) {
            mp.reset()
            mp.start()
        } else {
            release()
        }
        return@OnCompletionListener
    }

    private val onBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent ->
        Log.d(TAG, "onBufferingUpdate!")
        return@OnBufferingUpdateListener
    }

    private val onErrorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        Log.d(TAG, "onError!")
        false
    }

    private val onInfoListener = MediaPlayer.OnInfoListener { mp, what, extra ->
        Log.d(TAG, "onInfo!")
        false
    }

    private val onTimedTextListener = MediaPlayer.OnTimedTextListener { mp, text ->
        Log.d(TAG, "onTimedText!")
        return@OnTimedTextListener
    }

    private val onVideoSizeChangedListener = MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
        Log.d(TAG, "onVideoSizeChanged!")
        return@OnVideoSizeChangedListener
    }

    private val onSeekCompleteListener = MediaPlayer.OnSeekCompleteListener {
        Log.d(TAG, "onSeekComplete!")
        return@OnSeekCompleteListener
    }

    constructor(context: Context) : super(context) {
        Log.d(TAG, "SystemMediaPlayer!")
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        Log.d(TAG, "SystemMediaPlayer!")
        mContext = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        Log.d(TAG, "SystemMediaPlayer!")
        mContext = context
        init()
    }

    private fun init() {
        Log.d(TAG, "init!")
        mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer!!.setOnBufferingUpdateListener(onBufferingUpdateListener)
            mMediaPlayer!!.setOnCompletionListener(onCompletionListener)
            mMediaPlayer!!.setOnErrorListener(onErrorListener)
            mMediaPlayer!!.setOnInfoListener(onInfoListener)
            mMediaPlayer!!.setOnPreparedListener(onPreparedListener)
            mMediaPlayer!!.setOnTimedTextListener(onTimedTextListener)
            mMediaPlayer!!.setOnSeekCompleteListener(onSeekCompleteListener)
            mMediaPlayer!!.setOnVideoSizeChangedListener(onVideoSizeChangedListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loopFlag = false
        mSurfaceHolder = holder
        mSurfaceHolder!!.addCallback(callback)
        return
    }

    fun setSpeed(speed: Float) {
        Log.d(TAG, "setSpeed!")
        //mMediaPlayer.setSpeed(speed);
        return
    }

    fun setPath(path: String) {
        Log.d(TAG, "setPath!")
        time = System.currentTimeMillis()
        try {
            mMediaPlayer!!.setDataSource(mContext!!, Uri.parse(path))
            mMediaPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        isPlaying = false
        return
    }

    fun start(audioflag: Boolean) {
        Log.d(TAG, "start!")
        mMediaPlayer!!.start()
        if (audioflag == false) {
            mMediaPlayer!!.setVolume(0f, 0f)
            isMuted = true
        } else {
            isMuted = false
        }
        isPlaying = true
        return
    }

    fun mute() {
        Log.d(TAG, "mute!")
        mMediaPlayer!!.setVolume(0f, 0f)
        isMuted = true
        return
    }

    fun setVolume(vol: Float) {
        Log.d(TAG, "setVolume!")
        mMediaPlayer!!.setVolume(vol, vol)
        isMuted = false
        return
    }

    fun isMuted(): Boolean {
        Log.d(TAG, "isMuted!")
        return isMuted
    }

    fun isPlaying(): Boolean {
        Log.d(TAG, "isPlaying!")
        return isPlaying
    }

    fun onResume() {
        Log.d(TAG, "onResume!")
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
            isPlaying = true
        }
        return
    }

    fun onPause() {
        Log.d(TAG, "onPause!")
        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
            isPlaying = false
        }
        return
    }

    fun release() {
        Log.d(TAG, "release!")
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        return
    }

    companion object {

        private val TAG = "SystemMediaPlayer"
    }

}
