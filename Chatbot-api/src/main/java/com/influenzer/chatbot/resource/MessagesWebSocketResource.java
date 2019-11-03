package com.influenzer.chatbot.resource;

import com.influenzer.chatbot.compiler.Compiler;
import com.influenzer.chatbot.compiler.model.Message;
import com.influenzer.chatbot.resource.request.MessageWebSocket;
import com.influenzer.chatbot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class MessagesWebSocketResource {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/ws/mensagens/{key}")
    @SendTo("/ws/mensagens/{key}")
    public Object handleOnReceiveMessage(
            @DestinationVariable String key,
            @Payload MessageWebSocket request) {

        Compiler compiler = this.messageService.getCompiler(key);
        if (compiler == null) {
            return new Message("", "Não encontrei seu chat");
        }

        Optional<Message> msg = compiler.compile(new Message(request.getMessage()));
        return msg.get();
    }


}
