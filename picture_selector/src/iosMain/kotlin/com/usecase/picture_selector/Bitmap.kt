package com.usecase.picture_selector

import kotlinx.cinterop.*
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.dataWithContentsOfFile
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

class IOSBitmap(
    private val sandboxPath: String
) : Bitmap {

    @OptIn(ExperimentalForeignApi::class)
    override fun toByteArray(): ByteArray {
        val imageData = NSData.dataWithContentsOfFile(path = sandboxPath)
            ?: throw IllegalArgumentException("image data is null")
        val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
        val length = imageData.length
        val data: CPointer<ByteVar> = bytes.reinterpret()
        return ByteArray(length.toInt()) { index -> data[index] }
    }

    override fun toBase64(): String {
        val imageData = NSData.dataWithContentsOfFile(path = sandboxPath)
            ?: throw IllegalArgumentException("image data is null")
        return imageData.base64EncodedStringWithOptions(0L.toULong())
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun toBase64WithCompress(maxSize: Int): String {
        val image = UIImage(sandboxPath)
        val imageSize = image.size.useContents { this }
        val scale = minOf(maxSize / imageSize.width, maxSize / imageSize.height)

        if (scale > 1) return toBase64()

        val newWidth = imageSize.width * scale
        val newHeight = imageSize.height * scale

        UIGraphicsBeginImageContextWithOptions(CGSizeMake(newWidth, newHeight), false, 0.0)
        image.drawInRect(CGRectMake(0.0, 0.0, newWidth, newHeight))
        val newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        val imageData = UIImageJPEGRepresentation(newImage!!, COMPRESSION_QUALITY)
            ?: throw IllegalArgumentException("image data is null")

        return imageData.base64EncodedStringWithOptions(0L.toULong())
    }

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}