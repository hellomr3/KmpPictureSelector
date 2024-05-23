package com.usecase.picture_selector.delegate

import com.usecase.picture_selector.PictureSelectParams
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
    val params: PictureSelectParams,
    val result: (UIImage?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
    private val pickerController: UIImagePickerController = UIImagePickerController().apply {
        sourceType =
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        allowsEditing = params.isCrop
        cameraDevice =
            UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear
        delegate = this@MyUIImagePickerControllerDelegate
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val originImage = with(didFinishPickingMediaWithInfo) {
            this[UIImagePickerControllerEditedImage] as? UIImage
                ?: this[UIImagePickerControllerOriginalImage] as? UIImage
        }
        picker.dismissModalViewControllerAnimated(true)
        /* 你的照片处理逻辑 */
        result(originImage)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
    }

    fun takePhoto() {
        viewController.presentViewController(pickerController, true, null)
    }
}