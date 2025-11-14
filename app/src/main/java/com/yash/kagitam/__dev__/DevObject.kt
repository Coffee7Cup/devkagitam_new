import android.content.Context
import android.util.Log
import com.yash.kagitam.__dev__.extractPluginFromAssets
import com.yash.kagitam.__dev__.readManifest
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import java.io.File

object DevObject {

    private var _manifest: MetaDataPluginEntity? = null

    fun getManifest(context: Context): MetaDataPluginEntity {
        if (_manifest != null) return _manifest!!

        val extractedDir = extractPluginFromAssets(context, "paper.zip", "__dev__")
        val manifestFile = File(extractedDir, "manifest.json")
        Log.d("GM","path of manifest ${manifestFile.absolutePath}")

        _manifest = readManifest(manifestFile)
        return _manifest!!
    }
}
