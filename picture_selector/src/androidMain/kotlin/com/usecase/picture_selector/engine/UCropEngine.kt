package com.usecase.picture_selector.engine

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil3.BitmapImage
import coil3.annotation.ExperimentalCoilApi
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.target
import com.luck.picture.lib.engine.CropFileEngine
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine

class UCropEngine : CropFileEngine {
    @OptIn(ExperimentalCoilApi::class)
    override fun onStartCrop(
        fragment: Fragment?,
        srcUri: Uri?,
        destinationUri: Uri?,
        dataSource: ArrayList<String>?,
        requestCode: Int
    ) {
        // 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
        // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；
        if (srcUri == null || destinationUri == null) return
        val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
        uCrop.setImageEngine(object : UCropImageEngine {
            override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
                if (context == null || imageView == null) return
                val target = ImageRequest.Builder(context)
                    .data(url)
                    .target(imageView)
                    .build()
                context.imageLoader.enqueue(target)
            }

            override fun loadImage(
                context: Context?,
                url: Uri?,
                maxWidth: Int,
                maxHeight: Int,
                call: UCropImageEngine.OnCallbackListener<Bitmap>?
            ) {
                if (context == null) return
                val builder = ImageRequest.Builder(context)
                if (maxWidth > 0 && maxHeight > 0) {
                    builder.size(maxWidth, maxHeight)
                }
                builder.data(url)
                builder.target {
                    call?.onCall((it as? BitmapImage)?.bitmap)
                }
                val request = builder.build()
                context.imageLoader.enqueue(request)
            }
        })
        uCrop.start(fragment?.activity?.baseContext ?: return, fragment, requestCode)
    }
}