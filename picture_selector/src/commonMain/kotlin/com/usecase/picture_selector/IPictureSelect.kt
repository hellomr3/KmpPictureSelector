package com.usecase.picture_selector

import kotlinx.coroutines.flow.Flow

/**
 * @author guoqingshan
 * @date 2024/1/9/009
 * @description
 */
interface IPictureSelect {

    fun takePhoto(params: PictureSelectParams):Flow<Result<Media?>>

    fun selectPhoto(params: PictureSelectParams): Flow<Result<List<Media>>>
}