/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.tiefensuche.soundcrowd.plugins.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaMetadataCompat
import com.tiefensuche.soundcrowd.database.MetadataDatabase
import com.tiefensuche.soundcrowd.plugins.Callback
import com.tiefensuche.soundcrowd.plugins.IPlugin
import org.json.JSONArray
import org.json.JSONObject

class Plugin(private val appContext: Context, context: Context) : IPlugin {

    companion object {
        private const val name = "Cache"
    }

    private val icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.plugin_icon)

    override fun name(): String = name

    override fun mediaCategories(): List<String> = listOf(name)

    override fun preferences() = JSONArray().put(JSONObject().put("key", "cache_size")
            .put("name", "Cache size").put("description", "Maximal cache size for the LRU cache"))

    @Throws(Exception::class)
    override fun getMediaItems(mediaCategory: String, callback: Callback<JSONArray>, refresh: Boolean) {
        val result = JSONArray()
        val cacheDir = Extension.getIndividualCacheDirectory(appContext)
        if (cacheDir.exists()) {
            for (file in cacheDir.listFiles()) {
                MetadataDatabase.getInstance(appContext).getMediaItem(file.name.replace(".download", ""))?.let {
                    it.put(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, file.absolutePath)
                    result.put(it)
                }
            }
        }
        callback.onResult(result)
    }

    override fun getMediaItems(mediaCategory: String, path: String, callback: Callback<JSONArray>, refresh: Boolean) {
        // empty result, browsing is not supported
        callback.onResult(JSONArray())
    }

    override fun getMediaItems(mediaCategory: String, path: String, query: String, callback: Callback<JSONArray>, refresh: Boolean) {
        // empty result, searching is not supported
        callback.onResult(JSONArray())
    }

    @Throws(Exception::class)
    override fun getMediaUrl(metadata: JSONObject, callback: Callback<JSONObject>) {
        // pass-though url
        callback.onResult(metadata)
    }

    @Throws(Exception::class)
    override fun favorite(id: String, callback: Callback<Boolean>) {
        // not supported
        callback.onResult(false)
    }

    override fun getIcon() = icon
}
