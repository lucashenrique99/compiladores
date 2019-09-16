import { Component, OnInit, AfterViewInit, OnDestroy, Input, ViewChild } from '@angular/core';
import { MensagemService } from '../mensagem.service';
import { NgForm } from '@angular/forms';
import { ScrollPanel } from 'primeng/scrollpanel';
import { HttpErrorResponse } from '@angular/common/http';
import { MensagemWebsocketService } from '../mensagem-websocket.service';

@Component({
  selector: 'app-chat-view',
  templateUrl: './chat-view.component.html',
  styleUrls: ['./chat-view.component.css']
})
export class ChatViewComponent implements OnInit, AfterViewInit, OnDestroy {

  isDigitando: boolean;
  conversa: Mensagem[];

  @Input() useWebSocket: boolean;
  @ViewChild('scroll') scroll: ScrollPanel;

  constructor(
    private mensagemService: MensagemService,
    private mensagemWebSocketService: MensagemWebsocketService) { }

  ngOnInit() {
    this.conversa = [];
    this.mensagemService.iniciarConexao();

    if (this.useWebSocket) {
      this.mensagemWebSocketService.handleOnSubscribe()
        .subscribe(
          (resposta: any) => {
            console.log(resposta)
            setTimeout(() => { // simular digitação
              this.isDigitando = false;
              this.adicionarMensagem('bot', resposta.response, this.scroll);

            }, 500)
          }
        )
    }
  }

  ngOnDestroy() {
    this.mensagemService.encerrarConexao().subscribe();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.conversa.push(new Mensagem('bot', 'Olá, meu nome é Cefebot e eu sou seu assistente virtual. Em que posso lhe ajudar?'));
    }, 1000)
  }

  enviarMensagem(form: NgForm, scrollPanel: ScrollPanel) {
    this.adicionarMensagem('user', form.value.mensagem, scrollPanel);

    this.isDigitando = true;
    if (this.useWebSocket) {
      this.mensagemWebSocketService.sendMessage(form.value.mensagem);

    } else {
      this.mensagemService.enviarMensagem(form.value.mensagem)
        .subscribe(
          (resposta: any) => {
            setTimeout(() => { // simular digitação
              this.isDigitando = false;
              this.adicionarMensagem('bot', resposta.response, scrollPanel);

            }, 500)
          },
          (error: HttpErrorResponse) => {
            this.isDigitando = false;
            this.adicionarMensagem('bot', "Algo de errado não está certo. Aguarde só um momento que vou verificar!", scrollPanel);
          }
        )
    }
    form.reset();
  }

  private adicionarMensagem(autor: string, mensagem: string, scrollPanel: ScrollPanel) {
    const msg = new Mensagem(autor, mensagem);
    this.conversa.push(msg);

    setTimeout(() => {
      scrollPanel.scrollTop(this.conversa.length * 300);
    }, 100)
  }
}

class Mensagem {

  autor: string;
  conteudo: string;

  constructor(autor: string, conteudo: string) {
    this.autor = autor;
    this.conteudo = conteudo;
  }

}
