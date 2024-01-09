package com.usecase.picture_selector

import kotlinx.coroutines.flow.Flow

/**
 * @author guoqingshan
 * @date 2024/1/9/009
 * @description
 */
interface IPictureSelect {

    fun takePhoto(): Flow<Result<List<Media>>>

    fun selectPhoto(): Flow<Result<List<Media>>>
}