package com.influenzer.chatbot.compiler.model;

import java.util.UUID;
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
    private UUID id;
    private SymbolType type;
    private String name;
    private String value;

    public SymbolObject(SymbolType type, String name) {
        this.type = type;
        this.name = name;
        this.id = UUID.randomUUID();
    }

    public SymbolObject(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
 
}
