package com.yash.devkagitam.screens.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.yash.dev.downloadStatusById
import com.yash.dev.downloader
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.db.plugins.MetaDataPluginDB
import com.yash.devkagitam.utils.setupPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.FileNotFoundException

const val ZIP_FILES = "zipFiles"

const val DOWNLOADING_SP = "downloading_sp"
fun verifyDownloadedFile(data : GitHubFile){

}

// ---------------------- DATA CLASSES --------------------------

data class GitHubFile(
    val name: String,
    val size: Int,
    val download_url: String,
    val sha: String
)
// ---------------------- VIEWMODELS ----------------------------

// ----------- FACTORY -----------
open class PluginCardViewModelFactory(
    private val data: GitHubFile
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PluginCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PluginCardViewModel(data) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ----------- VIEWMODEL -----------
class PluginCardViewModel(private val data: GitHubFile) : ViewModel() {

    private val appCtx = AppRegistry.getAppContext()
    private val sp = appCtx.getSharedPreferences(DOWNLOADING_SP, Context.MODE_PRIVATE)

    private val installingPapers = sp.all
    var isInstalling by mutableStateOf(installingPapers.contains(data.download_url))
        private set

    var error = mutableStateOf<String?>(null)
    var progress = mutableStateOf<Int?>(null)

    fun startDownload(): Long {
        error.value = null
        progress.value = 0
        var id: Long = 0

        viewModelScope.launch {
            sp.edit {
                putLong(data.download_url, 0)
            }
            isInstalling = true
            try {
                id = downloader(
                    ctx = appCtx,
                    link = data.download_url,
                    saveFileAt = "${appCtx.filesDir}/$ZIP_FILES/${data.name}",
                    name = data.name,
                    onProgress = { pro -> progress.value = pro },
                    onComplete = { success ->
                        if (success) {
                            verifyDownloadedFile(data)
                            CoroutineScope(Dispatchers.Default).launch {
                                try {
                                    val pluginPath = "${appCtx.filesDir}/$ZIP_FILES/${data.name}"
                                    setupPlugin(pluginPath,data.name)
                                } catch (e: FileNotFoundException) {
                                    withContext(Dispatchers.Main) {
                                        error.value = "metaData.json missing in plugin: ${data.name}, ${e.message}"
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        error.value = "Setup failed: ${e.message}"
                                    }
                                }
                            }
                            sp.edit { remove(data.download_url) }
                            isInstalling = false
                        } else {
                            error.value = "Download failed"
                        }
                    }
                )
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }

        return id
    }

    fun alreadyDownloading() {
        error.value = null
        viewModelScope.launch {
            try {
                downloadStatusById(
                    ctx = appCtx,
                    downloadId = sp.getLong(data.download_url,0),
                    onProgress = { pro -> progress.value = pro },
                    onComplete = { success ->
                        if (success) {
                            verifyDownloadedFile(data)
                            CoroutineScope(Dispatchers.Default).launch {
                                try {
                                    val pluginPath = "${appCtx.filesDir}/$ZIP_FILES/${data.name}"
                                    setupPlugin(pluginPath,data.name)
                                } catch (e: FileNotFoundException) {
                                    withContext(Dispatchers.Main) {
                                        error.value = "metaData.json missing in plugin: ${data.name}, ${e.message}"
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        error.value = "Setup failed: ${e.message}"
                                    }
                                }
                            }
                            sp.edit { remove(data.download_url) }
                            isInstalling=false
                        } else {
                            error.value = "Download failed"
                        }
                    }
                )
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun resetError() {
        error.value = null
    }
}


private const val GITHUB_PLUGINS_URL =
    "https://api.github.com/repos/Coffee7Cup/plugins/contents?ref=main"

class InstallScreenViewModel : ViewModel() {

    var plugins by mutableStateOf<List<GitHubFile>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
        private set

    var installedPlugins by mutableStateOf<Set<String>>(emptySet())
        private set

    private var loadedOnce = false

    init {
        loadPlugins()
        viewModelScope.launch { refreshInstalled() }
    }

    private suspend fun refreshInstalled() {
        val dao = MetaDataPluginDB.getDatabase().metaDataPluginDao()
        installedPlugins = dao.getAllPlugins().map { it.name }.toSet()
    }

    fun markInstalled(pluginName: String) {
        viewModelScope.launch { refreshInstalled() }
    }

    fun loadPlugins(force: Boolean = false) {
        if (loadedOnce && !force) return

        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(GITHUB_PLUGINS_URL).build()
                val client = OkHttpClient()

                client.newCall(request).execute().use { res ->
                    if (!res.isSuccessful) throw Exception("HTTP ${res.code}")

                    val json = res.body?.string() ?: throw Exception("Empty body")
                    val gson = Gson()
                    val element = JsonParser.parseString(json)

                    val parsed = if (element.isJsonArray) {
                        gson.fromJson(element, Array<GitHubFile>::class.java)
                            .filter { it.name.endsWith(".zip") }
                    } else {
                        listOf(gson.fromJson(element, GitHubFile::class.java))
                            .filter { it.name.endsWith(".zip") }
                    }

                    withContext(Dispatchers.Main) {
                        plugins = parsed
                        error = null
                        loadedOnce = true
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error = e.message
                    isLoading = false
                }
                Log.e("InstallVM", "Error loading plugins", e)
            }
        }
    }
}
