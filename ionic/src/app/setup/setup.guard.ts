import { Injectable } from "@angular/core"
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router"
import { ConfigService } from "../config/config.service"

@Injectable({
    providedIn: "root"
})
export class SetupGuard implements CanActivate {
    constructor(private configService: ConfigService, private router: Router) {}

    async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
        await this.configService.awaitReady()
        const pass = !this.configService.setupCompleted

        if (!pass) {
            await this.router.navigate(["home"])
        }

        return pass
    }
}
