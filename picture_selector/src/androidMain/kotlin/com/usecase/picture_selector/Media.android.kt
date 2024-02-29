package com.usecase.picture_selector

import java.io.File

actual fun Media.fileSize(): Long {
    val file = File(path)
    if (file.exists() && file.isFile) {
        return file.length()
    }
    return 0L
}