package com.example.wbvideo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import com.example.wbvideo.ijk.IjkPlayer
import com.example.wbvideo.system.SystemMediaPlayer

import java.util.ArrayList


class WBVideosActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private var mControlsView: View? = null

    private var mVideos: ArrayList<IjkPlayer>? = null
    private var mSysVideos: ArrayList<SystemMediaPlayer>? = null
    private var mUrls: ArrayList<String>? = null
    private val hwaccl = false
    private val mVisible: Boolean = false

    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        mControlsView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mHideRunnable = Runnable { hide() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hwaccl) {
            setContentView(R.layout.activity_videos)
        } else {
            setContentView(R.layout.activity_ijkvideos)
        }
        initVideosUrl()
        initViews()
        mControlsView = findViewById(R.id.fullscreen_content_controls)
    }

    private fun initViews() {
        if (hwaccl) {
            initSysVideos()
        } else {
            initijkVieos()
        }
    }

    private fun initSysVideos() {
        mSysVideos = ArrayList()

        val v1 = findViewById<SystemMediaPlayer>(R.id.video1)
        mSysVideos!!.add(v1)
        Log.d(TAG, "v1 source = " + mUrls!![0])
        v1.setPath(mUrls!![0])

        val v2 = findViewById<SystemMediaPlayer>(R.id.video2)
        mSysVideos!!.add(v2)
        Log.d(TAG, "v2 source = " + mUrls!![1])
        v2.setPath(mUrls!![1])
        v2.mute()

        val v3 = findViewById<SystemMediaPlayer>(R.id.video3)
        mSysVideos!!.add(v3)
        Log.d(TAG, "v3 source = " + mUrls!![2])
        v3.setPath(mUrls!![2])
        v3.mute()

        val v4 = findViewById<SystemMediaPlayer>(R.id.video4)
        mSysVideos!!.add(v4)
        Log.d(TAG, "v4 source = " + mUrls!![3])
        v4.setPath(mUrls!![3])
        v4.mute()

        val v5 = findViewById<SystemMediaPlayer>(R.id.video5)
        mSysVideos!!.add(v5)
        Log.d(TAG, "v5 source = " + mUrls!![4])
        v5.setPath(mUrls!![4])
        v5.mute()

        val v6 = findViewById<SystemMediaPlayer>(R.id.video6)
        mSysVideos!!.add(v6)
        Log.d(TAG, "v6 source = " + mUrls!![5])
        v6.setPath(mUrls!![5])
        v6.mute()
    }

    private fun initijkVieos() {
        mVideos = ArrayList()

        val v1 = findViewById<IjkPlayer>(R.id.video1)
        mVideos!!.add(v1)
        Log.d(TAG, "v1 source = " + mUrls!![0])
        v1.setPath(mUrls!![0])

        val v2 = findViewById<IjkPlayer>(R.id.video2)
        mVideos!!.add(v2)
        Log.d(TAG, "v2 source = " + mUrls!![1])
        v2.setPath(mUrls!![1])
        v2.mute()

        val v3 = findViewById<IjkPlayer>(R.id.video3)
        mVideos!!.add(v3)
        Log.d(TAG, "v3 source = " + mUrls!![2])
        v3.setPath(mUrls!![2])
        v3.mute()

        val v4 = findViewById<IjkPlayer>(R.id.video4)
        mVideos!!.add(v4)
        Log.d(TAG, "v4 source = " + mUrls!![3])
        v4.setPath(mUrls!![3])
        v4.mute()

        val v5 = findViewById<IjkPlayer>(R.id.video5)
        mVideos!!.add(v5)
        Log.d(TAG, "v5 source = " + mUrls!![4])
        v5.setPath(mUrls!![4])
        v5.mute()

        val v6 = findViewById<IjkPlayer>(R.id.video6)
        mVideos!!.add(v6)
        Log.d(TAG, "v6 source = " + mUrls!![5])
        v6.setPath(mUrls!![5])
        v6.mute()
    }

    private fun initVideosUrl() {
        mUrls = ArrayList()
        val urls = resources.getStringArray(R.array.array_urls)
        for (str in urls) {
            val str1 = getString(R.string.default_web) + str + "_HD.mp4"
            mUrls!!.add(getUrl(str1))
            Log.d(TAG, "initVideosUrl item = $str")
        }
        return
    }

    private fun getUrl(mUrlString: String): String {
        return if (testNetWork) {
            mUrlString
        } else {
            App.getProxy(this).getProxyUrl(mUrlString, true)
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    companion object {

        private val TAG = "WBVideosActivity"
        private val AUTO_HIDE = true
        private val AUTO_HIDE_DELAY_MILLIS = 3000
        private val UI_ANIMATION_DELAY = 300
        var testNetWork = true
    }


}
