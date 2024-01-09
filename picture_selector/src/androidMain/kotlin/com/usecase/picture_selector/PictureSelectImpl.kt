package com.usecase.picture_selector

import androidx.appcompat.app.AppCompatActivity
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File


/**
 * @author guoqingshan
 * @date 2024/1/9/009
 * @description
 */
class PictureSelectImpl constructor(
    private val activity: AppCompatActivity,
) : IPictureSelect {
    override fun takePhoto(): Flow<Result<List<Media>>> {
        return callbackFlow {
            PictureSelector.create(activity)
                .openCamera(SelectMimeType.ofImage())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>) {
                        val mediaList = result.mapNotNull { localMedia ->
                            localMedia.asMedia()
                        }
                        trySendBlocking(Result.success(mediaList))
                    }

                    override fun onCancel() {
                        close(Throwable("取消选择"))
                    }
                })
            awaitClose {

            }
        }
    }

    override fun selectPhoto(): Flow<Result<List<Media>>> {
        return callbackFlow {
            PictureSelector.create(activity)
                .openSystemGallery(SelectMimeType.ofImage())
                .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>) {
                        val mediaList = result.mapNotNull { localMedia ->
                            localMedia.asMedia()
                        }
                        trySendBlocking(Result.success(mediaList))
                    }

                    override fun onCancel() {
                        close(Throwable("取消选择"))
                    }
                })

            awaitClose {

            }
        }
    }

    private fun LocalMedia?.asMedia(): Media? {
        if (this == null) {
            return null
        }
        val inputStream = File(this.availablePath).inputStream()
        val bitmap = BitmapUtils.getBitmapForStream(inputStream = inputStream, 100) ?: return null
        return Media(
            name = this.fileName,
            path = this.availablePath,
            preview = BitmapImpl(BitmapImpl.AndroidDelegate(bitmap))
        )
    }
}