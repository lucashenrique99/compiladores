package com.influenzer.chatbot.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SymbolObject {
    
    private SymbolType type;
    private String value;
    
}
