package com.usecase.picture_selector

import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.luck.picture.lib.basic.PictureSelectionCameraModel
import com.luck.picture.lib.basic.PictureSelectionModel
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


/**
 * @author guoqingshan
 * @date 2024/1/9/009
 * @description
 */
class PictureSelectImpl constructor(
    private val activity: AppCompatActivity,
) : IPictureSelect {
    override fun takePhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        return callbackFlow {
            PictureSelector.create(activity)
                .openCamera(params.asChooseMode())
                .setSandboxFileEngine { context, srcPath, mineType, call ->
                    if (call != null) {
                        val sandboxPath =
                            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                        call.onCallback(srcPath, sandboxPath)
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

    override fun selectPhoto(params: PictureSelectParams): Flow<Result<List<Media>>> {
        return callbackFlow {
            PictureSelector.create(activity)
                .openGallery(params.asChooseMode())
                .setSandboxFileEngine { context, srcPath, mineType, call ->
                    if (call != null) {
                        val sandboxPath =
                            SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                        call.onCallback(srcPath, sandboxPath)
                    }
                }
                .setMaxSelectNum(params.maxImageNum)
                .setMaxVideoSelectNum(params.maxVideoNum)
                .setSelectMaxFileSize(params.maxFileKbSize)
                .setImageEngine(CoilEngine())
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
                Log.e("TAG","error:$e")
                emit(Result.failure(e))
            }
    }

    private fun PictureSelectParams.asChooseMode(): Int {
        require(this.maxImageNum != 0 || this.maxVideoNum != 0)
        return if (this.maxImageNum > 0 && this.maxVideoNum > 0) {
            SelectMimeType.ofAll()
        } else if (this.maxVideoNum > 0) {
            SelectMimeType.ofVideo()
        } else {
            SelectMimeType.ofImage()
        }
    }

    private fun LocalMedia?.asMedia(): Media? {
        if (this == null) {
            return null
        }
        val bitmap = BitmapFactory.decodeFile(this.availablePath)
        return Media(
            name = this.fileName,
            path = this.availablePath,
            preview = BitmapImpl(BitmapImpl.AndroidDelegate(bitmap))
        )
    }
}