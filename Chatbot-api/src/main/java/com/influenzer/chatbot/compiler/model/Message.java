package com.influenzer.chatbot.compiler.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @EqualsAndHashCode.Include
    private UUID id;
    private String request;
    private String response;
    
    public Message(String message){
        this.id = UUID.randomUUID();
        this.request = message;
    }
    
    public Message(String request, String response){
        this.id = UUID.randomUUID();
        this.request = request;
        this.response = response;
    }

}
