import { NgModule } from "@angular/core"
import { PreloadAllModules, RouterModule, Routes } from "@angular/router"
import { SetupPage } from "./setup/setup.page"
import { SetupGuard } from "./setup/setup.guard"
import { HomeComponent } from "./home/home.component"

const routes: Routes = [
    {
        path: "setup",
        component: SetupPage,
        canActivate: [SetupGuard]
    },
    {
        path: "home",
        component: HomeComponent
    },
    {
        path: "",
        pathMatch: "full",
        redirectTo: "setup"
    }
]

@NgModule({
    imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })],
    exports: [RouterModule]
})
export class AppRoutingModule {}
