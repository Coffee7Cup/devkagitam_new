package com.yash.kagitam.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

val activeDownloads = ConcurrentHashMap<String, Call>()

fun downloaderOkhttp(
    link: String,
    saveFileAt: String,
    onProgress: (Int) -> Unit,
    onComplete: (Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient()
        val request = Request.Builder().url(link).build()

        val call = client.newCall(request)
        activeDownloads[link] = call

        try {
            val response = call.execute()
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) { onComplete(false) }
                activeDownloads.remove(link)
                throw Error("No response")
            }

            val body = response.body ?: run {
                withContext(Dispatchers.Main) { onComplete(false) }
                activeDownloads.remove(link)
                throw Error("No response body")
            }

            val contentLength = body.contentLength()
            val inputStream = body.byteStream()
            val outFile = File(saveFileAt)
            outFile.parentFile?.mkdirs()

            val sink = outFile.sink().buffer()
            val buffer = ByteArray(8 * 1024)
            var bytesRead: Int
            var totalBytesRead = 0L
            var lastProgress = 0

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                sink.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead

                val progress = ((totalBytesRead * 100) / contentLength).toInt()
                if (progress != lastProgress) {
                    lastProgress = progress
                    withContext(Dispatchers.Main) { onProgress(progress) }
                }

                // Check for cancellation
                if (call.isCanceled()) {
                    sink.close()
                    outFile.delete()
                    withContext(Dispatchers.Main) { onComplete(false) }
                    activeDownloads.remove(link)
                    return@launch
                }
            }

            sink.close()
            withContext(Dispatchers.Main) { onComplete(true) }
        } catch (e: IOException) {
            if (!call.isCanceled()) {
                withContext(Dispatchers.Main) { onComplete(false) }
            }
        } finally {
            activeDownloads.remove(link)
        }
    }
}

/**
 * Cancels a download associated with the given link.
 */
fun cancelOkhttpDownload(
    link: String,
    savedFileLoc : String,
    onComplete: (Boolean) -> Unit,
) {
    val call = activeDownloads.remove(link)
    if (call != null && !call.isCanceled()) {
        val file = File(savedFileLoc)
        file.deleteRecursively()
        call.cancel()
        onComplete(true)
    } else {
        onComplete(false)
        throw Error("Failed to cancel")
    }
}
