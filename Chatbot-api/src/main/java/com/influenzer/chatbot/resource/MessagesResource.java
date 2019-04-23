package com.influenzer.chatbot.resource;

import com.influenzer.chatbot.compiler.Compiler;
import com.influenzer.chatbot.compiler.model.Message;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagens")
public class MessagesResource {

    @Autowired
    private Compiler compilador;

    @PostMapping
    public ResponseEntity<Message> receberMensagem(@RequestBody String message) {

        Optional<Message> msg = this.compilador.compile(new Message(message));

        return ResponseEntity.ok(msg.get());
    }

}
