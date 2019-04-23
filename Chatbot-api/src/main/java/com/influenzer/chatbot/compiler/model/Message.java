package com.influenzer.chatbot.compiler.model;

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
    private Integer id;
    private String request;
    private String response;
    
    public Message(String message){
        this.request = message;
    }
    
    public Message(String request, String response){
        this.request = request;
        this.response = response;
    }

}
