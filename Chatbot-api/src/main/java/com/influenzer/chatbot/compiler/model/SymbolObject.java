package com.influenzer.chatbot.compiler.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SymbolObject {
    
    @EqualsAndHashCode.Include
    private String name;
    private SymbolType type;
    private String value;

    public SymbolObject(SymbolType type, String name, String value) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    public SymbolObject(String name) {
        this.name = name;
    }
 
}
