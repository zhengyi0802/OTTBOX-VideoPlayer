package com.example.wbvideo

import android.app.Application
import android.content.Context

import com.danikula.videocache.HttpProxyCacheServer


/**
 * Created by yyl on 2017/11/30.
 */

class App : Application() {


    private var proxy: HttpProxyCacheServer? = null

    /**
     * 注意被app 安全软件禁网后 127.0.0.1 的getLocalPort 会被限制权限 而闪退
     *
     */
    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

    companion object {

        fun getProxy(context: Context): HttpProxyCacheServer {
            val app = context.applicationContext as App
            return if (app.proxy == null) app.proxy = app.newProxy() else app.proxy
        }
    }
}
