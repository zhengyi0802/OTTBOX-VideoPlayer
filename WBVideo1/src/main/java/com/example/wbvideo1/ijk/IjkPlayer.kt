package com.example.wbvideo1.ijk

import android.content.Context
import android.graphics.SurfaceTexture
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.TextureView

import java.io.IOException

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * Created by Administrator on 2017/2/22/022.
 */

class IjkPlayer : TextureView, TextureView.SurfaceTextureListener {

    var isMuted = false
        private set
    var isPlaying = false
        private set

    private var mMediaPlayer: IjkMediaPlayer? = null

    internal var time: Long = 0

    private val mCompletionListener = IMediaPlayer.OnCompletionListener { mp ->
        mp.reset()
        mp.start()
    }

    private val mInfoListener = IMediaPlayer.OnInfoListener { mp, arg1, arg2 ->
        when (arg1) {
            IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:")
            IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                Log.i(TAG, "11111   打开时间是 time=" + (System.currentTimeMillis() - time))

                Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:")
            }
            IMediaPlayer.MEDIA_INFO_BUFFERING_START -> Log.d(TAG, "MEDIA_INFO_BUFFERING_START:")
            IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                Log.i(TAG, "22222   打开时间是 time=" + (System.currentTimeMillis() - time))
                Log.d(TAG, "MEDIA_INFO_BUFFERING_END:")
            }
            IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH -> Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: $arg2")
            IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:")
            IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE -> Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:")
            IMediaPlayer.MEDIA_INFO_METADATA_UPDATE -> Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:")
            IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE -> Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:")
            IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT -> Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:")
            IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED ->
                //   mVideoRotationDegree = arg2;
                Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: $arg2")
            IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START -> Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:")
        }
        true
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    internal fun init(context: Context) {
        if (isInEditMode) return
        mMediaPlayer = IjkMediaPlayer()
        surfaceTextureListener = this

        //     mMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())

        // mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 10)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 0)

        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "max-buffer-size", 8*1024*1024)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 300)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "max-fps", 100)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "live_start_index", -4)
        mMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "mediacodec-all-videos", 1)

        mMediaPlayer!!.setOnInfoListener(mInfoListener)
        mMediaPlayer!!.setOnMediaCodecSelectListener { iMediaPlayer, s, i, i1 -> null }
        mMediaPlayer!!.setOnPreparedListener { iMediaPlayer ->
            Log.d(TAG, "onPrepared:")
            iMediaPlayer.start()
        }
        mMediaPlayer!!.setOnBufferingUpdateListener { iMediaPlayer, i -> Log.i(TAG, "buffing=$i") }
        mMediaPlayer!!.isLooping = true
    }

    fun setSpeed(speed: Float) {
        mMediaPlayer!!.setSpeed(speed)
    }

    fun setPath(path: String) {
        time = System.currentTimeMillis()
        try {
            mMediaPlayer!!.setDataSource(context, Uri.parse(path))
            mMediaPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        isPlaying = false
        //mMediaPlayer.start();
        return
    }

    fun start(audioflag: Boolean) {
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
        mMediaPlayer!!.setVolume(0f, 0f)
        isMuted = true
    }

    fun setVolume(vol: Float) {
        mMediaPlayer!!.setVolume(vol, vol)
        isMuted = false
    }

    fun onResume() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
            isPlaying = true
        }
    }

    fun onPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
            isPlaying = false
        }
    }

    fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        if (isInEditMode) return
        Log.d(TAG, "onSurfaceTextureAvailable:")
        mMediaPlayer!!.setSurface(Surface(surface))
        onResume()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        onPause()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    companion object {

        private val TAG = "IjkPlayer"
    }
}
