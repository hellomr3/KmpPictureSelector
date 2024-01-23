package com.usecase.picture_selector

import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File

class AndroidBitmap constructor(
    private val sandboxPath: String,
) : Bitmap {

    override fun toByteArray(): ByteArray {
        val file = File(sandboxPath)
        // 有内存风险
        return file.readBytes()
    }

    override fun toBase64(): String {
        val byteArray = toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    override fun toBase64WithCompress(maxSize: Int): String {
        val bitmap = BitmapFactory.decodeFile(sandboxPath)
        val compressedBitmap = BitmapUtils.getResizedBitmap(bitmap, maxSize)
        val byteArrayOutputStream = ByteArrayOutputStream()
        compressedBitmap.compress(
            android.graphics.Bitmap.CompressFormat.PNG,
            100,
            byteArrayOutputStream
        )
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
    }
}
