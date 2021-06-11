import { NgModule } from "@angular/core"
import { BrowserModule } from "@angular/platform-browser"
import { RouteReuseStrategy } from "@angular/router"

import { IonicModule, IonicRouteStrategy } from "@ionic/angular"
import { StatusBar, StatusBarStyle } from "@capacitor/status-bar"
import { BarcodeScanner } from "@ionic-native/barcode-scanner/ngx"

import { AppComponent } from "./app.component"
import { AppRoutingModule } from "./app-routing.module"
import { ConfigService } from "./config/config.service"
import { IonicStorageModule } from "@ionic/storage-angular"

@NgModule({
    declarations: [AppComponent],
    entryComponents: [],
    imports: [
        BrowserModule,
        IonicModule.forRoot({
            rippleEffect: true,
            mode: "ios"
        }),
        IonicStorageModule.forRoot(),
        AppRoutingModule
    ],
    providers: [
        { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
        BarcodeScanner,
        ConfigService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
    constructor() {
        ;(async () => {
            await StatusBar.setOverlaysWebView({ overlay: true })
            await StatusBar.setStyle({ style: StatusBarStyle.Light })
        })()
    }
}
