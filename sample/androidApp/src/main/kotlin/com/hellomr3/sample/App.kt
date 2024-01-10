package com.hellomr3.sample

import ActivityManager
import android.app.Application

/**
 * @author guoqingshan
 * @date 2024/1/10/010
 * @description
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ActivityManager.init(this)
    }
}