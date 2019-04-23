import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {InputTextModule} from 'primeng/inputtext';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import { FormsModule } from '@angular/forms';
import { MensagemService } from './mensagem.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
    FormsModule,
    HttpClientModule,

    ButtonModule,
    CardModule,
    InputTextareaModule,
    InputTextModule,
    ScrollPanelModule
    
  ],
  providers: [
    MensagemService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
