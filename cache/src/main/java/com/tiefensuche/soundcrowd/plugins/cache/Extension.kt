/*
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.tiefensuche.soundcrowd.plugins.cache

import android.content.Context
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import android.preference.PreferenceManager
import android.support.v4.media.MediaMetadataCompat
import com.danikula.videocache.HttpProxyCacheServer
import com.tiefensuche.soundcrowd.database.MetadataDatabase
import com.tiefensuche.soundcrowd.extensions.UrlResolver
import com.tiefensuche.soundcrowd.plugins.Callback
import org.json.JSONObject
import java.io.File

class Extension(private val context: Context, private val pluginContext: Context) : UrlResolver {

    private var cache: HttpProxyCacheServer

    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val cacheSize = prefs.getString("cache_size", "512").toLong() * 1024 * 1024
        cache = HttpProxyCacheServer.Builder(context)
                .cacheDirectory(getIndividualCacheDirectory(context))
                .maxCacheSize(cacheSize)
                .build()
    }

    override fun getMediaUrl(metadata: JSONObject, callback: Callback<JSONObject>) {

        // local or online media
        if (!metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).startsWith("http")) {
            callback.onResult(metadata)
            return
        }


        MetadataDatabase.getInstance(context).addMediaItem(metadata)
        callback.onResult(metadata.put(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, cache.getProxyUrl(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID), metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI), true)))
    }

    companion object {
        private val INDIVIDUAL_DIR_NAME = "music"

        /**
         * Returns individual application cache directory (for only video caching from Proxy). Cache directory will be
         * created on SD card *("/Android/data/[app_package_name]/cache/video-cache")* if card is mounted .
         * Else - Android defines cache directory on device's file system.
         *
         * @param context Application context
         * @return Cache [directory][File]
         */
        internal fun getIndividualCacheDirectory(context: Context): File {
            val cacheDir = getCacheDirectory(context, true)
            return File(cacheDir, INDIVIDUAL_DIR_NAME)
        }

        /**
         * Returns application cache directory. Cache directory will be created on SD card
         * *("/Android/data/[app_package_name]/cache")* (if card is mounted and app has appropriate permission) or
         * on device's file system depending incoming parameters.
         *
         * @param context        Application context
         * @param preferExternal Whether prefer external location for cache
         * @return Cache [directory][File].<br></br>
         * **NOTE:** Can be null in some unpredictable cases (if SD card is unmounted and
         * [Context.getCacheDir()][android.content.Context.getCacheDir] returns null).
         */
        private fun getCacheDirectory(context: Context, preferExternal: Boolean): File? {
            var appCacheDir: File? = null
            val externalStorageState = try {
                Environment.getExternalStorageState()
            } catch (e: NullPointerException) { // (sh)it happens
                ""
            }

            if (preferExternal && MEDIA_MOUNTED == externalStorageState) {
                appCacheDir = getExternalCacheDir(context)
            }
            if (appCacheDir == null) {
                appCacheDir = context.cacheDir
            }
            if (appCacheDir == null) {
                val cacheDirPath = "/data/data/" + context.packageName + "/cache/"
                appCacheDir = File(cacheDirPath)
            }
            return appCacheDir
        }

        private fun getExternalCacheDir(context: Context): File? {
            val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
            val appCacheDir = File(File(dataDir, context.packageName), "cache")
            if (!appCacheDir.exists()) {
                if (!appCacheDir.mkdirs()) {
                    return null
                }
            }
            return appCacheDir
        }
    }
}
