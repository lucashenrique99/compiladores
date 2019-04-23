import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MensagemService {

  url: string;

  constructor(
    private http: HttpClient
  ) { 
    this.url = environment.url;
  }

  enviarMensagem(mensagem){
    return this.http.post<any>(`${this.url}/mensagens`, mensagem);
  }
}
