package com.hellomr3.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.usecase.picture_selector.BitmapUtils
import com.usecase.picture_selector.Media
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
                val media = pictureSelect.selectPhoto().firstOrNull()?.getOrNull()?.firstOrNull()
                displayImage(media)
            }
        }
        findViewById<View>(R.id.btnTakePicture).setOnClickListener {
            lifecycleScope.launch {
                val media = pictureSelect.takePhoto().firstOrNull()?.getOrNull()?.firstOrNull()
                displayImage(media)
            }
        }
    }

    private fun displayImage(media: Media?) {
        media ?: return
        Log.e("TAG", "$media")
        findViewById<ImageView>(R.id.img).setImageBitmap(
            BitmapUtils.getBitmapForStream(
                ByteArrayInputStream(media.preview.toByteArray()),
                100
            )
        )
    }
}