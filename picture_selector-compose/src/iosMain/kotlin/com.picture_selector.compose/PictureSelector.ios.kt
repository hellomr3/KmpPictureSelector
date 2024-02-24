package com.picture_selector.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.usecase.picture_selector.IOSPictureSelect
import com.usecase.picture_selector.IPictureSelect
import platform.UIKit.UIApplication

@Composable
actual fun rememberPictureSelect(): IPictureSelect {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: throw Throwable("加载环境异常")
    return remember(viewController) {
        IOSPictureSelect(currentController = viewController)
    }
}