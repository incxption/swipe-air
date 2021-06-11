import { Component, ElementRef, ViewChild } from "@angular/core"
import Setup from "../../plugins/setup"
import { Platform, ToastController } from "@ionic/angular"
import { BarcodeScanner } from "@ionic-native/barcode-scanner/ngx"
import { ConfigService } from "../config/config.service"

@Component({
    templateUrl: "setup.page.html",
    styleUrls: ["setup.page.scss"]
})
export class SetupPage {
    @ViewChild("slides")
    private slidesRef: ElementRef<HTMLIonSlidesElement>

    triggerMode: "edge" | "double" = "double"

    permissionOverlay: boolean = false
    permissionAccessibility: boolean = false

    sliderOptions = {
        initialSlide: 0,
        allowTouchMove: false
    }

    constructor(
        private platform: Platform,
        private barcodeScanner: BarcodeScanner,
        private toastController: ToastController,
        private configService: ConfigService
    ) {
        this.platform.backButton.subscribeWithPriority(10, () => this.slidePrevious())
    }

    async setupOverlay() {
        await Setup.setupOverlay()
        this.permissionOverlay = true
    }

    async setupAccessibilityService() {
        await Setup.setupAccessibilityService()
        this.permissionAccessibility = true
    }

    async finishSetup() {
        this.configService.setupCompleted = true
        this.configService.triggerMode = this.triggerMode
        await this.configService.save()
    }

    async scanQRCode() {
        try {
            const result = await this.barcodeScanner.scan({
                prompt: "Scan the SwipeAir QR-Code on your PC",
                resultDisplayDuration: 0,
                formats: "QR_CODE"
            })

            if (result.cancelled) throw new Error("scan_cancelled")

            await this.finishSetup()
        } catch (e) {
            console.error(e)
            await this.toastController
                .create({
                    message: "Failed to scan QR code",
                    duration: 2000,
                    color: "danger"
                })
                .then(toast => toast.present())
        }
    }

    slideNext = () => this.slidesRef.nativeElement.slideNext()
    slidePrevious = () => this.slidesRef.nativeElement.slidePrev()

    updateTrigger = (event: any) => (this.triggerMode = event.detail.value)

    permissionButtonColor = (granted: boolean) => (granted ? "success" : "danger")
    permissionIconName = (granted: boolean) => (granted ? "checkmark-circle" : "alert-circle")
}
