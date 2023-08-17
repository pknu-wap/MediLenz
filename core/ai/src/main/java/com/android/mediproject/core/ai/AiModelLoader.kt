package com.android.mediproject.core.ai

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.android.mediproject.core.ai.classification.MedicineClassifierImpl
import com.android.mediproject.core.ai.detection.MedicineDetectorImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AiModelLoader : ContentProvider() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(): Boolean {
        GlobalScope.launch(Dispatchers.IO) {
            context?.run {
                Log.d("wap", "AiModelLoader onCreate")
                MedicineClassifierImpl.initialize(this)
                MedicineDetectorImpl.initialize(this)
                Log.d("wap", "AiModelLoader onCreate done")
            }
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? =
        null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

}
