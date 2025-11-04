package com.yash.dev

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun downloader(
    ctx : Context,
    link: String,
    saveFileAt : String ,
    name: String,
    onProgress: (Int) -> Unit,
    onComplete: (Boolean) -> Unit
) : Long{
    val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val req = DownloadManager.Request(link.toUri())
        .setTitle("Downloading $name")
        .setDescription("Please wait...")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(saveFileAt.toUri())

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

fun downloadStatusById(ctx: Context,downloadId : Long, onProgress: (Int) -> Unit, onComplete: (Boolean) -> Unit){
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

fun cancelDownload(ctx: Context, downloadId: Long, onSuccess: (Boolean) -> Unit ){

    val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)

            var found = false
            cursor.use {
                if (it.moveToFirst()) {
                    found = true
                }
            }

            if (found) {
                // Remove cancels the download and deletes partial file
                val removedCount = downloadManager.remove(downloadId)
                withContext(Dispatchers.Main) {
                    onSuccess(removedCount > 0)
                }
            } else {
                // No such download found
                withContext(Dispatchers.Main) {
                    onSuccess(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onSuccess(false)
            }
        }
    }
}
