package com.influenzer.chatbot.compiler;

import com.influenzer.chatbot.compiler.lexicon.LexiconAnalyzer;
import com.influenzer.chatbot.compiler.lexicon.LexiconObject;
import com.influenzer.chatbot.compiler.model.Message;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import com.influenzer.chatbot.compiler.sintatic.SyntacticAnalyzer;
import com.influenzer.chatbot.compiler.sintatic.SyntacticObject;
import com.influenzer.chatbot.compiler.sintatic.SyntaticException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class Compiler {

    private final LexiconAnalyzer lexicon = new LexiconAnalyzer();

    private final SyntacticAnalyzer syntactic = new SyntacticAnalyzer();

    private final List<SymbolObject> symbolsTable = new ArrayList<>();

    public Optional<Message> compile(Message message) {
        // lexicon
        Optional<LexiconObject> lexOptional = this.lexicon.start(message.getRequest());
        if (!lexOptional.isPresent()) {
            return Optional.empty();
        }

        // syntactic
        Optional<SyntacticObject> synOptional;
        try {
            synOptional = this.syntactic.start(lexOptional.get().getLexemes());

        } catch (SyntaticException ex) {
            message.setResponse(ex.getType() + ": " + ex.getMessage());
            return Optional.of(message);
        }
        if (!synOptional.isPresent()) {
            return Optional.empty();
        }

        message.setResponse("Correct. " + synOptional.get().getType());

        this.symbolsTable.addAll(synOptional.get().getSymbols());
        Collections.sort(symbolsTable, (s1, s2) -> s1.getName().compareTo(s2.getName()));

        return Optional.of(message);
    }

}
