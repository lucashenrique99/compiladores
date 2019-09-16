package com.influenzer.chatbot.service;

import com.influenzer.chatbot.compiler.Compiler;
import com.influenzer.chatbot.compiler.translator.InvertFileGenerator;
import com.influenzer.chatbot.compiler.translator.InvertFileResult;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Getter
public class MessageService {

    private final Map<String, Compiler> map = new HashMap<>();
    private final InvertFileResult invertFile;


    public MessageService(){
        InvertFileGenerator generator = new InvertFileGenerator();
        this.invertFile = generator.getInvertFile();
    }

    public String getNewAccessKey(){
        String key = UUID.randomUUID().toString();
        this.map.put(key, new Compiler(this.invertFile));
        return key;
    }

    public Compiler getCompiler(String key){
        Compiler c = this.map.get(key);
        return (c == null) ?  this.map.put(key, new Compiler(this.invertFile)) : c;
    }

    public void removeCompiler(String key){
        this.map.remove(key);
    }

}
