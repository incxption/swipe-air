import {
    ChangeDetectorRef,
    Component,
    ElementRef,
    ViewChild,
} from "@angular/core";
import Setup from "../../plugins/setup";
import { Platform } from "@ionic/angular";

@Component({
    selector: "app-welcome",
    templateUrl: "welcome.component.html",
    styleUrls: ["welcome.component.scss"],
})
export class WelcomeComponent {
    @ViewChild("slides")
    private slidesRef: ElementRef<HTMLIonSlidesElement>;

    triggerMode: "edge" | "double" = "double";

    sliderOptions = {
        initialSlide: 0,
        allowTouchMove: false,
    };

    constructor(private platform: Platform) {
        this.platform.backButton.subscribeWithPriority(10, () =>
            this.slidePrevious()
        );
    }

    setupOverlay() {
        return Setup.setupOverlay();
    }

    setupAccessibilityService() {
        return Setup.setupAccessibilityService();
    }

    slideNext() {
        this.slidesRef.nativeElement.slideNext();
    }

    slidePrevious() {
        this.slidesRef.nativeElement.slidePrev();
    }

    updateTrigger(event: any) {
        this.triggerMode = event.detail.value;
    }
}
