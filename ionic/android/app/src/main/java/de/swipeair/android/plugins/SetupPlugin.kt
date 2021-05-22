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

    @PluginMethod
    fun setupOverlay(call: PluginCall) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${mainActivity.packageName}"))
        mainActivity.startActivity(intent)

        call.resolve()
    }

    @PluginMethod
    fun setupAccessibilityService(call: PluginCall) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        mainActivity.startActivity(intent)

        call.resolve()
    }
}