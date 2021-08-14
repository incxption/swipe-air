import { CapacitorConfig } from "@capacitor/cli"

const config: CapacitorConfig = {
    appId: "io.ionic.starter",
    appName: "SwipeAir",
    webDir: "www",
    bundledWebRuntime: false,
    server: {
        url: "http://192.168.178.66:4200",
        cleartext: true
    }
}

export default config
