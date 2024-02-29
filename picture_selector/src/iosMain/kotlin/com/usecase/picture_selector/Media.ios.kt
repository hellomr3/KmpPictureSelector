package com.usecase.picture_selector

import platform.Foundation.NSFileManager

actual fun Media.fileSize(): Long {
    val fileManager = NSFileManager()
    if (fileManager.fileExistsAtPath(path = path)) {
        return fileManager.fileAttributesAtPath(
            path = this.path,
            traverseLink = true
        )?.size?.toLong() ?: 0L
    }
    return 0L
}