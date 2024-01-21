package com.usecase.picture_selector.engine

import android.content.Context
import android.net.Uri
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.util.ArrayList

class CompressEngine(private val ignoreKb:Int) :CompressFileEngine {
    override fun onStartCompress(
        context: Context?,
        source: ArrayList<Uri>?,
        call: OnKeyValueResultCallbackListener?
    ) {

        Luban.with(context)
            .load(source)
            .ignoreBy(ignoreKb)
            .setCompressListener(object : OnNewCompressListener {
                override fun onStart() {

                }

                override fun onSuccess(source: String?, compressFile: File?) {
                    call?.onCallback(source, compressFile?.absolutePath)
                }

                override fun onError(source: String?, e: Throwable?) {
                    call?.onCallback(source, null)
                }

            })
            .launch()
    }

}