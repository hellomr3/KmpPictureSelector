package com.usecase.picture_selector.delegate

import platform.Foundation.NSLog
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraDevice
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class MyUIImagePickerControllerDelegate(
    val viewController: UIViewController,
    val result: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
    private val pickerController: UIImagePickerController

    init {

        pickerController = UIImagePickerController().apply {
//                mediaTypes = listOf(kUTTypeImage)
            sourceType =
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            allowsEditing = true
            cameraDevice =
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear
//                cameraFlashMode = UIImagePickerControllerCameraFlashModeAuto
            delegate = this@MyUIImagePickerControllerDelegate
        }
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val info = didFinishPickingMediaWithInfo
        val originImage = info[UIImagePickerControllerEditedImage] as? UIImage
            ?: info[UIImagePickerControllerOriginalImage] as? UIImage
        picker.dismissModalViewControllerAnimated(true)
        /* 你的照片处理逻辑 */
        result(originImage)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController): Unit {
        NSLog("取消拍摄")
        picker.dismissModalViewControllerAnimated(true)
    }

    fun takePhoto() {
        viewController.presentViewController(pickerController, true, null)
    }
}