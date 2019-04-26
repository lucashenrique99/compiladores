import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ScrollPanel } from 'primeng/scrollpanel';
import { MensagemService } from './mensagem.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit, OnDestroy {

  isDigitando: boolean;
  conversa: Mensagem[];

  constructor(private mensagemService: MensagemService) { }

  ngOnInit() {
    this.conversa = [];
    this.mensagemService.iniciarConexao();
  }

  ngOnDestroy() {
    this.mensagemService.encerrarConexao().subscribe();
  }

  ngAfterViewInit(){
    setTimeout(()=> {
      this.conversa.push( new Mensagem('bot', 'Olá, meu nome é Cefebot e eu sou seu assistente virtual. Em que posso lhe ajudar?'));
    }, 1000)
  }

  enviarMensagem(form: NgForm, scrollPanel: ScrollPanel) {
    this.adicionarMensagem('user', form.value.mensagem, scrollPanel);

    this.isDigitando = true;
    this.mensagemService.enviarMensagem(form.value.mensagem)
      .subscribe(
        (resposta: any) => {
          setTimeout(()=>{ // simular digitação
            this.isDigitando = false;
            this.adicionarMensagem('bot', resposta.response, scrollPanel);

          }, 500)
        },
        (error: HttpErrorResponse) => {
          this.isDigitando = false;
            this.adicionarMensagem('bot', "Algo de errado não está certo. Aguarde só um momento que vou verificar!", scrollPanel);
        }
        )

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
