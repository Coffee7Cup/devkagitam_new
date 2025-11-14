package com.yash.kagitam.screens.viewModels

import androidx.lifecycle.ViewModel
import java.io.File

class SettingViewModel : ViewModel() {

    fun getFilesInFolder(path: String): List<File> {
        val givenFolder = File(path)

        if(givenFolder.isDirectory){
            throw Error("this is a directory")
        }

        return givenFolder.listFiles().toList()
    }

    fun getFilesInZipFolder(){

    }

    fun getFilesInPaperFolder(){

    }

    fun deleteFile(path : String){
        File(path).deleteRecursively()
    }
}

