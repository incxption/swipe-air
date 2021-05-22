import { registerPlugin } from "@capacitor/core";

export interface SetupPlugin {
    setupOverlay(): Promise<void>;
    setupAccessibilityService(): Promise<void>;
}

const Setup = registerPlugin<SetupPlugin>("Setup");

export default Setup;
