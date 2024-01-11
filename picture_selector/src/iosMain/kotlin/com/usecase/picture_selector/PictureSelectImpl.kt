package com.usecase.picture_selector

import cocoapods.TZImagePickerController.TZImagePickerController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import platform.UIKit.UIImage
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
class PictureSelectImpl constructor(private val currentController: UIViewController?) :
    IPictureSelect {

    override fun takePhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        return callbackFlow<Result<List<Media>>> {
            val controller = TZImagePickerController(9, delegate = null)
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
            val controller = TZImagePickerController(9, delegate = null)
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
        return Media("", "", BitmapImpl(this))
    }
}