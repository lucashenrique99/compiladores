package com.influenzer.chatbot.compiler.sintatic;

import com.influenzer.chatbot.compiler.model.MessageType;
import lombok.Data;

@Data
public class SyntaticException extends Exception{
    
    private MessageType type;

    public SyntaticException(String message, MessageType type) {
        super(message);
        this.type = type;
    }
    
    
    
}
