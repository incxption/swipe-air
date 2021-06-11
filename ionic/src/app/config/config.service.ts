import { Injectable } from "@angular/core"
import { Storage } from "@ionic/storage-angular"

@Injectable({
    providedIn: "root"
})
export class ConfigService {
    private ready = false

    public triggerMode: "edge" | "double" = "double"
    public setupCompleted = false

    constructor(private storage: Storage) {
        ;(async () => {
            await this.storage.create()
            await this.load()
            this.ready = true
        })()
    }

    private async load() {
        const config = await this.storage.get("config")
        console.log("config =", config)

        if (config) {
            const { triggerMode, setupCompleted } = config
            this.triggerMode = triggerMode
            this.setupCompleted = setupCompleted
        }
    }

    public async awaitReady() {
        while (!this.ready) {
            await new Promise(resolve => setTimeout(resolve, 100))
        }
    }

    public async save() {
        const { triggerMode, setupCompleted } = this
        const config = { triggerMode, setupCompleted }
        console.log("new config =", config)

        await this.storage.set("config", config)
    }
}
