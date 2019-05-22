package com.influenzer.chatbot.compiler.sintatic;

import com.influenzer.chatbot.compiler.model.MessageType;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyntacticObject {
    
    private MessageType type;
    private List<String> tokens;
    private List<SymbolObject> symbols = new ArrayList<>();
    
}
