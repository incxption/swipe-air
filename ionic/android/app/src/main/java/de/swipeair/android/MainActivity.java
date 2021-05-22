package de.swipeair.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.getcapacitor.BridgeActivity;

import de.swipeair.android.plugins.SetupPlugin;
import de.swipeair.android.services.OverlayService;

public class MainActivity extends BridgeActivity {
    public static MainActivity INSTANCE;

    public MainActivity() {
        INSTANCE = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerPlugin(SetupPlugin.class);

        if (Settings.canDrawOverlays(this)) {
            launch();
        } else {
            Intent request = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(request, 1234);
        }
    }

    private void launch() {
        Intent service = new Intent(this, OverlayService.class);
        startService(service);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            launch();
        }
    }
}
