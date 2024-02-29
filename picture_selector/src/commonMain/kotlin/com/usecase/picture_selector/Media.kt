package com.usecase.picture_selector

data class Media(
    val name: String,
    val path: String,
    val preview: Bitmap,
)

expect fun Media.fileSize():Long