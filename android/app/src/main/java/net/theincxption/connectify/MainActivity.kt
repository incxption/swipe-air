package net.theincxption.connectify

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import net.theincxption.connectify.service.OverlayService

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Settings.canDrawOverlays(this)) {
            launch()

//            val request = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            startActivity(request)
        } else {
            val request = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(request, 1234)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234) {
            launch()
        }
    }

    private fun launch() {
        Log.w("Connectify", "launch: launching overlay service")
        val service = Intent(this, OverlayService::class.java)
        startService(service)
    }
}