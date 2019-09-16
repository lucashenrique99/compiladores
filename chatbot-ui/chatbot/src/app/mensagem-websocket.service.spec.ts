import { TestBed } from '@angular/core/testing';

import { MensagemWebsocketService } from './mensagem-websocket.service';

describe('MensagemWebsocketService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MensagemWebsocketService = TestBed.get(MensagemWebsocketService);
    expect(service).toBeTruthy();
  });
});
