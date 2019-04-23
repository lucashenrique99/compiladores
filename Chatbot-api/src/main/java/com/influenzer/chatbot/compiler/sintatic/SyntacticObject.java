package com.influenzer.chatbot.compiler.sintatic;

import com.influenzer.chatbot.compiler.model.MessageType;
import com.influenzer.chatbot.compiler.model.SymbolObject;
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
public class SyntacticObject {
    
    private MessageType type;
    private Boolean isCorrect;
    private List<SymbolObject> symbols = new ArrayList<>();
    
}
