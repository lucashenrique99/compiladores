package com.influenzer.chatbot.compiler;

import com.influenzer.chatbot.compiler.lexicon.LexiconAnalyzer;
import com.influenzer.chatbot.compiler.lexicon.LexiconObject;
import com.influenzer.chatbot.compiler.model.Message;
import com.influenzer.chatbot.compiler.model.MessageType;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import com.influenzer.chatbot.compiler.sintatic.SyntacticAnalyzer;
import com.influenzer.chatbot.compiler.sintatic.SyntacticObject;
import com.influenzer.chatbot.compiler.templates.MessageTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Compiler {

    @Autowired
    private LexiconAnalyzer lexicon;

    @Autowired
    private SyntacticAnalyzer syntactic;

    private final List<SymbolObject> symbolsTable = new ArrayList<>();

    public Optional<Message> compile(Message message) {
        // lexicon
        Optional<LexiconObject> lexOptional = this.lexicon.start(message.getRequest());
        if (!lexOptional.isPresent()) {
            return Optional.empty();
        }

        // syntactic
        Optional<SyntacticObject> synOptional = this.syntactic.start(lexOptional.get().getLexemes());
        if (!synOptional.isPresent()) {
            return Optional.empty();
        }

        if (synOptional.get().getIsCorrect()) {
            message.setResponse("");
            lexOptional.get().getLexemes().forEach((lexeme) -> {
                message.setResponse(message.getResponse() + " " + lexeme);
            });

            this.symbolsTable.addAll(synOptional.get().getSymbols());
            Collections.sort(symbolsTable, (s1, s2) -> s1.getValue().compareTo(s2.getValue()));

        } else {
            MessageType type = synOptional.get().getType();
            if (type == MessageType.QUESTION_RULE_1) {
                message.setResponse(MessageTemplate.INTERROGATION_MISSING);
            } else if (type.name().contains("QUESTION")) {
                message.setResponse(MessageTemplate.EXPRESSION_MISSING);
            } else if (type.name().contains("ASSIGNMENT")) {
                message.setResponse(MessageTemplate.INCOMPLETE);
            } else {
                message.setResponse(MessageTemplate.NOT_RECOGNIZED);
            }
        }

        return Optional.of(message);
    }

}
