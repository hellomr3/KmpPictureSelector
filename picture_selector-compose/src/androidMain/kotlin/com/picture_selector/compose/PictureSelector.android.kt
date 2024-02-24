package com.picture_selector.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.usecase.picture_selector.AndroidPictureSelect
import com.usecase.picture_selector.IPictureSelect

@Composable
actual fun rememberPictureSelect():IPictureSelect {
    val context = LocalContext.current
    return remember(context) {
        AndroidPictureSelect(activity = context as ComponentActivity)
    }
}