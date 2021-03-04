package com.saldivar.pruebatecnica.helper

import android.app.Application
import android.content.Context

class MyAplicationClass : Application() {

    companion object {
        var ctx: Context? = null
        var comentarioEnProgreso:String ?=null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        comentarioEnProgreso =""
    }
}