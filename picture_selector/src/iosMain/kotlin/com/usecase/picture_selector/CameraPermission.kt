package com.usecase.picture_selector

import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType

/**
 * 用于判断是否有拍照权限
 */
fun checkCameraPermission(): Boolean {
    val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
    if (status == AVAuthorizationStatusRestricted || status == AVAuthorizationStatusDenied) {
        return false
    }
    return true;
}