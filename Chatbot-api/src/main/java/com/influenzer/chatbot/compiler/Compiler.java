package com.influenzer.chatbot.compiler;

import com.influenzer.chatbot.compiler.lexicon.LexiconAnalyzer;
import com.influenzer.chatbot.compiler.lexicon.LexiconObject;
import com.influenzer.chatbot.compiler.model.Message;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import com.influenzer.chatbot.compiler.sintatic.SyntacticAnalyzer;
import com.influenzer.chatbot.compiler.sintatic.SyntacticObject;
import com.influenzer.chatbot.compiler.sintatic.SyntaticException;
import com.influenzer.chatbot.compiler.translator.InvertFileResult;
import com.influenzer.chatbot.compiler.translator.Translator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class Compiler {

    private final LexiconAnalyzer lexicon = new LexiconAnalyzer();
    private final SyntacticAnalyzer syntactic = new SyntacticAnalyzer();
    private final Translator translator;

    private final List<SymbolObject> symbolsTable = new ArrayList<>();

    private final InvertFileResult invertFile;

    public Compiler(InvertFileResult invertFile) {
        this.invertFile = invertFile;
        this.translator = new Translator(invertFile);
    }

    public Compiler() {
        invertFile = null;
        this.translator = null;
    }

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

        this.symbolsTable.addAll(synOptional.get().getSymbols());
        Collections.sort(symbolsTable, (s1, s2) -> s1.getName().compareTo(s2.getName()));

        String answer = this.translator.translate(
                Stream.concat(
                        this.symbolsTable
                                .stream()
                                .map(symbol -> (symbol.getValue() != null) ? symbol.getValue().toLowerCase() : null),
                        this.symbolsTable
                                .stream()
                                .map(symbol -> (symbol.getName()!= null) ? symbol.getName().toLowerCase() : null))
                        .filter( value -> value != null)
                        .collect(Collectors.toSet()));
//        message.setResponse("Correct. " + synOptional.get().getType());
        message.setResponse(answer);

        return Optional.of(message);
    }

}
