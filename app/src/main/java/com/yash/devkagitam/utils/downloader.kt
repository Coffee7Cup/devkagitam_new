package com.yash.devkagitam.utils

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import androidx.core.net.toUri
import com.yash.devkagitam.registries.AppRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ZIP_FILES = "zipFiles"

fun downloader(
    link: String,
    saveFileAt : String? = null,
    name: String,
    onProgress: (Int) -> Unit,
    onComplete: (Boolean) -> Unit
) : Long{
    val ctx = AppRegistry.getAppContext()
    val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val req = DownloadManager.Request(link.toUri())
        .setTitle("Downloading $name")
        .setDescription("Please wait...")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(saveFileAt?.toUri() ?: "${ctx.filesDir}/$ZIP_FILES/$name".toUri())

    val downloadId: Long = downloadManager.enqueue(req)

    CoroutineScope(Dispatchers.IO).launch {
        var downloading = true
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val bytesDownloaded = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                val bytesTotal = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                )

                if (bytesTotal > 0) {
                    val progress = ((bytesDownloaded * 100L) / bytesTotal).toInt()
                    withContext(Dispatchers.Main) {
                        onProgress(progress)
                    }
                }

                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloading = false
                        withContext(Dispatchers.Main) { onComplete(true) }
                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloading = false
                        withContext(Dispatchers.Main) { onComplete(false) }
                    }
                }
            }
            cursor.close()
            delay(250)
        }
    }

    return downloadId
}

fun downloadStatusById(downloadId : Long, onProgress: (Int) -> Unit, onComplete: (Boolean) -> Unit){
    val ctx = AppRegistry.getAppContext()
    val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    CoroutineScope(Dispatchers.IO).launch {
        var downloading = true
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val bytesDownloaded = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                val bytesTotal = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                )

                if (bytesTotal > 0) {
                    val progress = ((bytesDownloaded * 100L) / bytesTotal).toInt()
                    withContext(Dispatchers.Main) {
                        onProgress(progress)
                    }
                }

                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloading = false
                        withContext(Dispatchers.Main) { onComplete(true) }
                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloading = false
                        withContext(Dispatchers.Main) { onComplete(false) }
                    }
                }
            }
            cursor.close()
            delay(250)
        }
    }

}
