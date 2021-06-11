package de.swipeair.android.plugins

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import de.swipeair.android.MainActivity

@CapacitorPlugin(name = "Setup")
class SetupPlugin : Plugin() {

    private val mainActivity: MainActivity
        get() = MainActivity.INSTANCE

    companion object {
        @JvmStatic
        var overlayCall: PluginCall? = null
            private set
        
        @JvmStatic
        var serviceCall: PluginCall? = null
            private set
    }

    @PluginMethod
    fun setupOverlay(call: PluginCall) {
        overlayCall?.resolve()
        overlayCall = call

        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${mainActivity.packageName}"))
        mainActivity.startActivityForResult(intent, 8801)
    }

    @PluginMethod
    fun setupAccessibilityService(call: PluginCall) {
        serviceCall?.resolve()
        serviceCall = call

        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        mainActivity.startActivityForResult(intent, 8802)
    }
}