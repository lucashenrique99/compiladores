package com.influenzer.chatbot.compiler.lexicon;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LexiconObject {
    
    private List<String> symbols = new ArrayList<>();
    private List<String> lexemes = new ArrayList<>();
    
    
}
