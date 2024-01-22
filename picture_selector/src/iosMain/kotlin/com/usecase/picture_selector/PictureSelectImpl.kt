package com.usecase.picture_selector

import cocoapods.TZImagePickerController.TZImagePickerController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSHomeDirectory
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
class PictureSelectImpl constructor(private val currentController: UIViewController?) :
    IPictureSelect {

    override fun takePhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        return callbackFlow<Result<List<Media>>> {
            val controller = TZImagePickerController(params.maxImageNum.toLong(), delegate = null)
            controller.didFinishPickingPhotosHandle = { p0, p1, _ ->
                val images = p0?.mapNotNull {
                    it as? UIImage
                }?.mapNotNull { it.asMedia() } ?: emptyList()
//                val phAssetList = p1?.mapNotNull { it as? PHAsset }
//            (p0?.firstOrNull() as? UIImage)?.let {
//                val data = UIImageJPEGRepresentation(it, 0.99)?.bytes
//                // 将 COpaquePointer 转换为 CPointer<ByteVar>
//                val bytePointer: CPointer<ByteVar>? = data?.reinterpret()
//
//            }
                trySendBlocking(Result.success(images))
            }
            currentController?.presentViewController(
                controller,
                true,
                null
            )
            awaitClose {
            }
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    override fun selectPhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        return callbackFlow<Result<List<Media>>> {
            val controller = TZImagePickerController(params.maxImageNum.toLong(), delegate = null)
            controller.didFinishPickingPhotosHandle = { p0, p1, _ ->
                val images = p0?.mapNotNull {
                    it as? UIImage
                }?.mapNotNull { it.asMedia() } ?: emptyList()
//                val phAssetList = p1?.mapNotNull { it as? PHAsset }
//            (p0?.firstOrNull() as? UIImage)?.let {
//                val data = UIImageJPEGRepresentation(it, 0.99)?.bytes
//                // 将 COpaquePointer 转换为 CPointer<ByteVar>
//                val bytePointer: CPointer<ByteVar>? = data?.reinterpret()
//
//            }
                trySendBlocking(Result.success(images))
            }
            currentController?.presentViewController(
                controller,
                true,
                null
            )
            awaitClose {
            }
        }.catch { e ->
            emit(Result.failure(e))
        }
    }

    private fun UIImage?.asMedia(): Media? {
        if (this == null) return null
        val sandboxImagePath = save2Sandbox(image = this, present = 1.0, imageName = "xxx.jpeg")
        return Media("", path = sandboxImagePath, BitmapImpl(this))
    }

    /**
     * 图片保存到沙盒
     */
    private fun save2Sandbox(image: UIImage, present: Double, imageName: String): String {
        val imageData = UIImageJPEGRepresentation(image = image, compressionQuality = present)
        val fullPath = NSHomeDirectory() + "/Images/" + imageName
        imageData?.writeToFile(path = fullPath, atomically = true)
        return fullPath
    }
}