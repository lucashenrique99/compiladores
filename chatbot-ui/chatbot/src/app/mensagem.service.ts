import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MensagemService {

  url: string;
  key: string;

  constructor(
    private http: HttpClient
  ) { 
    this.url = environment.url;
  }

  iniciarConexao(){
    return this.http.get<string>(`${this.url}/mensagens/key`).subscribe( key => this.key = key);
  }

  enviarMensagem(mensagem){
    return this.http.post<any>(`${this.url}/mensagens?key=${this.key}`, mensagem);
  }

  encerrarConexao(){
    return this.http.get<string>(`${this.url}/mensagens/${this.key}`);
  }
}
