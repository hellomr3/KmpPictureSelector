package com.usecase.picture_selector

import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapImpl constructor(
    private val delegate: Delegate,
) : Bitmap {
    constructor(platformBitmap: android.graphics.Bitmap) : this(AndroidDelegate(platformBitmap))

    val platformBitmap: android.graphics.Bitmap get() = delegate.getAndroidBitmap()

   override  fun toByteArray(): ByteArray {
        return delegate.getByteArray()
    }

    override fun toBase64(): String {
        val byteArray = toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    override fun toBase64WithCompress(maxSize: Int): String {
        val bitmap = delegate.getResized(maxSize)
        return bitmap.toBase64()
    }

    interface Delegate {
        fun getAndroidBitmap(): android.graphics.Bitmap
        fun getByteArray(): ByteArray
        fun getResized(maxSize: Int): Bitmap
    }

    class AndroidDelegate(private val bitmap: android.graphics.Bitmap) : Delegate {
        override fun getAndroidBitmap(): android.graphics.Bitmap {
            return bitmap
        }

        override fun getByteArray(): ByteArray {
            val byteArrayOutputStream = ByteArrayOutputStream()
            @Suppress("MagicNumber")
            bitmap.compress(
                android.graphics.Bitmap.CompressFormat.PNG,
                100,
                byteArrayOutputStream
            )
            return byteArrayOutputStream.toByteArray()
        }

        override fun getResized(maxSize: Int): Bitmap {
            val compressedBitmap = BitmapUtils.getResizedBitmap(bitmap, maxSize)
            return BitmapImpl(compressedBitmap)
        }
    }
}