package com.usecase.picture_selector

import cocoapods.TZImagePickerController.TZImagePickerController
import com.usecase.picture_selector.delegate.MyUIImagePickerControllerDelegate
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSLog
import platform.Foundation.date
import platform.Foundation.writeToFile
import platform.Photos.PHAsset
import platform.Photos.PHAssetResource
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIViewController

// 需要缓存代理类，否则无法回调结果
private val cacheDelegate = atomic<MyUIImagePickerControllerDelegate?>(null)

@OptIn(ExperimentalForeignApi::class)
class IOSPictureSelect constructor(private val currentController: UIViewController?) :
    IPictureSelect {

    private var params: PictureSelectParams? = null

    override fun takePhoto(params: PictureSelectParams): Flow<Result<Media?>> {
        this.params = params
        return callbackFlow<Result<Media?>> {
            // 先检查相机权限
            if (!checkCameraPermission()) {
                throw Throwable("没有相机权限")
            }
            val delegate = MyUIImagePickerControllerDelegate(
                viewController = currentController!!,
                params = params
            ) {
                trySendBlocking(Result.success(asMedia(it, null)))
            }
            cacheDelegate.getAndSet(delegate)
            cacheDelegate.value?.takePhoto()
            awaitClose {
                cacheDelegate.getAndSet(null)
            }
        }.catch { e ->
            emit(Result.failure(e))
        }
            .onEach {
                NSLog(it.toString())
            }
    }

    override fun selectPhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        this.params = params
        return callbackFlow<Result<List<Media>>> {
            val controller = TZImagePickerController()
            controller.maxImagesCount = params.maxImageNum.toLong()
            controller.allowCrop = params.isCrop
            controller.allowTakePicture = params.allowTakePicture
            controller.didFinishPickingPhotosHandle = { p0, p1, p2 ->
                val images = handleCallback(p0, p1, p2)
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

    private fun handleCallback(p0: List<*>?, p1: List<*>?, p3: Boolean): List<Media> {
        return p0?.zip(p1 ?: emptyList())?.mapNotNull {
            val image = it.first as? UIImage
            val path = it.second as? PHAsset
            asMedia(image, path)
        } ?: emptyList()
    }

    private fun asMedia(image: UIImage?, path: PHAsset?): Media? {
        if (image == null) return null
        val fileName =
            path?.getOriginFileName() ?: "photo_${NSDate.date().timeIntervalSinceReferenceDate}.png"
        val sandboxImagePath = save2Sandbox(image = image, imageName = fileName)
        return Media(fileName, path = sandboxImagePath, IOSBitmap(sandboxImagePath))
    }

    /**
     * 图片保存到沙盒
     */
    private fun save2Sandbox(image: UIImage, imageName: String): String {
        val imageData = if (params?.isCompress == true) compressImage(
            this.params?.maxFileKbSize ?: (1 * 1024 * 1024), image = image
        ) else UIImagePNGRepresentation(image)
        // 注意这里NSHomeDirectory动态变化
        val fullPath = NSHomeDirectory() + "/Documents/" + imageName
        imageData?.writeToFile(path = fullPath, atomically = true)
        return fullPath
    }

    /**
     * 处理图片压缩
     */
    private fun compressImage(maxLength: Long, image: UIImage): NSData? {
        var compression = 1.0f
        var data: NSData =
            UIImageJPEGRepresentation(image = image, compressionQuality = compression.toDouble())
                ?: return null
        if (data.length() < maxLength.toUInt()) {
            return data
        }
        var max = 1f
        var min = 0f
        repeat(6) {
            compression = (max + min) / 2f
            data = UIImageJPEGRepresentation(
                UIImage(data),
                compressionQuality = compression.toDouble()
            )
                ?: return null
            if (data.length() < (maxLength * 0.9).toUInt()) {
                min = compression
            } else if (data.length() > maxLength.toUInt()) {
                max = compression
            } else {
                return@repeat
            }
        }
        return data
    }

    private fun PHAsset.getOriginFileName(): String? {
        val resource =
            PHAssetResource.assetResourcesForAsset(this).firstOrNull() as? PHAssetResource
        return resource?.originalFilename
    }

}