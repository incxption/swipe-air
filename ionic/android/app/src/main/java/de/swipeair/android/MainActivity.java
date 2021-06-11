package de.swipeair.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

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
        launch();
    }

    public void launch() {
        if (Settings.canDrawOverlays(this)) {
            Intent service = new Intent(this, OverlayService.class);
            startService(service);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 8801) {
            SetupPlugin.getOverlayCall().resolve();
            launch();
        } else if (requestCode == 8802) {
            SetupPlugin.getServiceCall().resolve();
        }
    }
}
