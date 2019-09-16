package com.influenzer.chatbot.resource;

import com.influenzer.chatbot.compiler.Compiler;
import com.influenzer.chatbot.compiler.model.Message;
import com.influenzer.chatbot.compiler.translator.InvertFileGenerator;
import com.influenzer.chatbot.compiler.translator.InvertFileResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.influenzer.chatbot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagens")
public class MessagesResource {

    @Autowired
    private MessageService messageService;

    @GetMapping("/key")
    public ResponseEntity<String> getAccessKey(){
        String key = this.messageService.getNewAccessKey();
        return ResponseEntity.ok(key);
    }

    @PostMapping
    public ResponseEntity<Message> receiveMessages(@RequestBody String message, @RequestParam String key) {
        Compiler compiler = this.messageService.getCompiler(key);
        if(compiler == null){
            return ResponseEntity.notFound().build();
        }
        
        Optional<Message> msg = compiler.compile(new Message(message));

        return ResponseEntity.ok(msg.get());
    }
    
    @DeleteMapping("/{key}")
    public ResponseEntity finishConnection(@PathVariable String key){
        this.messageService.removeCompiler(key);
        return ResponseEntity.noContent().build();
    }

}
