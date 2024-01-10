package com.usecase.picture_selector

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import java.io.File
import java.io.InputStream


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
                .setSandboxFileEngine { context, srcPath, mineType, call ->
                    if (call != null) {
                        val sandboxPath =
                            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                        call.onCallback(srcPath, sandboxPath);
                    }
                }
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
            .catch { e ->
                emit(Result.failure(e))
            }
    }

    override fun selectPhoto(): Flow<Result<List<Media>>> {
        return callbackFlow {
            PictureSelector.create(activity)
                .openSystemGallery(SelectMimeType.ofImage())
                .setSandboxFileEngine { context, srcPath, mineType, call ->
                    if (call != null) {
                        val sandboxPath =
                            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                        call.onCallback(srcPath, sandboxPath)
                    }
                }
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
            .catch { e ->
                emit(Result.failure(e))
            }
    }

    private fun LocalMedia?.asMedia(): Media? {
        if (this == null) {
            return null
        }
        val bitmap = BitmapUtils.getBitmapForStream(inputStream = File(this.availablePath).inputStream(), 100) ?: return null
        return Media(
            name = this.fileName,
            path = this.availablePath,
            preview = BitmapImpl(BitmapImpl.AndroidDelegate(bitmap))
        )
    }
}