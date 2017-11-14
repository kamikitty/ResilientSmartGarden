/* defines the root module of the application along with metadata about the module */

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';
import { NguiAutoCompleteModule } from '@ngui/auto-complete';

//import { customHttpProvider } from './_helpers/index';
import { AlertComponent } from './_directives/index';
import { AuthGuard } from './_guards/index';
import { AlertService, AuthenticationService, UserService } from './_services/index';
import { HomeComponent } from './home/index';
import { AboutComponent } from './about/index';
import { FeaturesComponent } from './features/index';
import { ContactComponent } from './contact/index';
import { GettingStartedComponent } from './getting_started/index';
import { LoginComponent } from './rsg_login/index';
import { RegisterComponent } from './rsg_register/index';
import { GardenComponent, AddGardenComponent } from './garden/index';
import { CompanionParentComponent, CompanionComponent } from './companion/index';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    routing,
    NguiAutoCompleteModule
  ],

  declarations: [
    AppComponent,
    AlertComponent,
    HomeComponent,
    AboutComponent,
    FeaturesComponent,
    ContactComponent,
    GettingStartedComponent,
    LoginComponent,
    RegisterComponent,
    GardenComponent,
    AddGardenComponent,
    CompanionParentComponent,
    CompanionComponent
  ],

  providers: [
    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService
  ],

  bootstrap: [AppComponent]
})

export class AppModule {}
