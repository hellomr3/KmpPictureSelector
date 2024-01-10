package com.hellomr3.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.usecase.picture_selector.BitmapImpl
import com.usecase.picture_selector.BitmapUtils
import com.usecase.picture_selector.Media
import com.usecase.picture_selector.PictureSelectParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pictureSelect
import java.io.ByteArrayInputStream

/**
 * @author guoqingshan
 * @date 2024/1/10/010
 * @description
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btnSelectPicture).setOnClickListener {
            lifecycleScope.launch {
                val media = pictureSelect.selectPhoto(params = PictureSelectParams(maxImageNum = 3, maxVideoNum = 1))
                    .firstOrNull()?.getOrNull()?.firstOrNull()
                displayImage(media)
            }
        }
        findViewById<View>(R.id.btnTakePicture).setOnClickListener {
            lifecycleScope.launch {
                val media = pictureSelect.takePhoto(params = PictureSelectParams(maxImageNum = 1))
                    .firstOrNull()?.getOrNull()?.firstOrNull()
                displayImage(media)
            }
        }
    }

    private fun displayImage(media: Media?) {
        media ?: return
        findViewById<ImageView>(R.id.img).setImageBitmap(
            (media.preview as BitmapImpl).platformBitmap
        )
    }
}