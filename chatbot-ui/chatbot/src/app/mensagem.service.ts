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

  iniciarConexao(){
    return this.http.get<string>(`${this.url}/mensagens/key`, {responseType: 'text' as 'json'}).subscribe( key => sessionStorage.setItem('key', key));
  }

  enviarMensagem(mensagem){
    return this.http.post<any>(`${this.url}/mensagens?key=${sessionStorage.getItem('key')}`, mensagem);
  }

  encerrarConexao(){
    return this.http.get<string>(`${this.url}/mensagens/${sessionStorage.getItem('key')}`);
  }
}
