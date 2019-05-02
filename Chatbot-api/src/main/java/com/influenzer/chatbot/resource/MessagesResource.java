package com.influenzer.chatbot.resource;

import com.influenzer.chatbot.compiler.Compiler;
import com.influenzer.chatbot.compiler.model.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

    private final Map<String, Compiler> map = new HashMap<>();
    
    @GetMapping("/key")
    public ResponseEntity<String> getAcessKey(){
        String key = UUID.randomUUID().toString();
        map.put(key, new Compiler());
        return ResponseEntity.ok(key);
    }

    @PostMapping
    public ResponseEntity<Message> reciveMessages(@RequestBody String message, @RequestParam String key) {
        Compiler compiler = this.map.get(key);
        if(compiler == null){
            return ResponseEntity.notFound().build();
        }
        
        Optional<Message> msg = compiler.compile(new Message(message));

        return ResponseEntity.ok(msg.get());
    }
    
    @DeleteMapping("/{key}")
    public ResponseEntity finishConnection(@PathVariable String key){
        this.map.remove(key);
        return ResponseEntity.noContent().build();
    }

}
