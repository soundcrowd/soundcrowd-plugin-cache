/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.tiefensuche.soundcrowd.plugins.cache

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import com.tiefensuche.soundcrowd.plugins.Callback
import com.tiefensuche.soundcrowd.plugins.IPlugin
import org.json.JSONArray
import org.json.JSONObject

class Plugin(private val context: Context) : IPlugin {

    companion object {
        private const val name = "Cache"
    }

    override fun name(): String = name

    override fun mediaCategories(): List<String> = listOf(name)

    @Throws(Exception::class)
    override fun getMediaItems(mediaCategory: String, callback: Callback<JSONArray>) {
        val result = JSONArray()
        val cacheDir = Extension.getIndividualCacheDirectory(context)
        if (cacheDir.exists()) {
            for (file in cacheDir.listFiles()) {
                val obj = JSONObject()
                obj.put(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, file.absolutePath)
                        .put(MediaMetadataCompat.METADATA_KEY_ARTIST, name)
                        .put(MediaMetadataCompat.METADATA_KEY_TITLE, file.name)
                result.put(obj)
            }
        }
        callback.onResult(result)
    }

    override fun getMediaItems(mediaCategory: String, path: String, callback: Callback<JSONArray>) {
        // empty result, browsing is not supported
        callback.onResult(JSONArray())
    }

    override fun getMediaItems(mediaCategory: String, path: String, query: String, callback: Callback<JSONArray>) {
        // empty result, searching is not supported
        callback.onResult(JSONArray())
    }

    @Throws(Exception::class)
    override fun getMediaUrl(url: String, callback: Callback<String>) {
        // pass-though url
        callback.onResult(url)
    }
}
