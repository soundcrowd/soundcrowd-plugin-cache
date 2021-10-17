/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.tiefensuche.soundcrowd.plugins.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaMetadataCompat
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import com.tiefensuche.soundcrowd.database.MetadataDatabase
import com.tiefensuche.soundcrowd.plugins.Callback
import com.tiefensuche.soundcrowd.plugins.IPlugin

class Plugin(private val appContext: Context, context: Context) : IPlugin {

    companion object {
        private const val name = "Cache"
    }

    private var database = MetadataDatabase(appContext)
    private val icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.plugin_icon)
    private val cacheSizePreference = EditTextPreference(appContext)

    init {
        cacheSizePreference.key = context.getString(R.string.cache_size_key)
        cacheSizePreference.title = context.getString(R.string.cache_size_title)
        cacheSizePreference.summary = context.getString(R.string.cache_size_summary)
        cacheSizePreference.dialogTitle = context.getString(R.string.cache_size_title)
        cacheSizePreference.dialogMessage = context.getString(R.string.cache_size_dialog_message)
    }

    override fun name(): String = name

    override fun mediaCategories(): List<String> = listOf(name)

    override fun preferences(): List<Preference> = listOf(cacheSizePreference)

    @Throws(Exception::class)
    override fun getMediaItems(mediaCategory: String, callback: Callback<List<MediaMetadataCompat>>, refresh: Boolean) {
        val result = mutableListOf<MediaMetadataCompat>()
        val cacheDir = Extension.getIndividualCacheDirectory(appContext)
        if (cacheDir.exists()) {
            for (file in cacheDir.listFiles()) {
                database.getMediaItem(file.name.replace(".download", ""))?.let {
                    result.add(MediaMetadataCompat.Builder(it).putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, file.absolutePath).build())
                }
            }
        }
        callback.onResult(result)
    }

    override fun getIcon(): Bitmap = icon
}
