import { Injectable } from '@angular/core';
import { filter, map } from 'rxjs/operators';
import { IMessage } from '@stomp/stompjs';
import { RxStompService } from '@stomp/ng2-stompjs';

@Injectable({
  providedIn: 'root'
})
export class MensagemWebsocketService {

  constructor(
    private rxStompService: RxStompService
  ) { }

  handleOnSubscribe() {
    const key = 'key-sd';

    return this.rxStompService
      .watch(`/ws/mensagens/${key}`)
      // .watch(`/ws/mensagens`)
      .pipe(
        filter((message: IMessage) => message &&
         !Array('SEND').includes(message.command)),
        map<IMessage, any>((message: IMessage) => JSON.parse(message.body)),
        filter( message => message && message.response)
      );
  }

  sendMessage(content: string) {
    const key =  'key-sd';
    const body = { message: content };
    this.rxStompService
      .publish({
        // destination: `/ws/mensagens`,
        destination: `/ws/mensagens/${key}`,
        body: JSON.stringify(body)
      });
  }

}
