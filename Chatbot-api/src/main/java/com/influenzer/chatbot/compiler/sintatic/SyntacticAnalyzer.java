package com.influenzer.chatbot.compiler.sintatic;

import com.influenzer.chatbot.compiler.model.MessageType;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import com.influenzer.chatbot.compiler.model.SymbolType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SyntacticAnalyzer {

    public Optional<SyntacticObject> start(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return Optional.of(new SyntacticObject(MessageType.NOT_RECOGNIZED, Boolean.FALSE, new ArrayList<>()));
        }

        SyntacticObject object = new SyntacticObject();

        if (tokens.get(0).contains("com")) {
            object.setIsCorrect(tokens.stream().anyMatch(p -> p.contains("?")));
            object.setType(MessageType.QUESTION_RULE_1);

        } else if (tokens.get(0).contains("gost")) {
            object.setIsCorrect(tokens.size() > 1);
            object.setType(MessageType.QUESTION_RULE_2);

        } else if (tokens.get(0).contains("precis")) {
            object.setIsCorrect(tokens.size() > 1);
            object.setType(MessageType.QUESTION_RULE_3);

        } else if (tokens.get(0).contains("sim")) {
            object.setIsCorrect(Boolean.TRUE);
            object.setType(MessageType.ASSIGNMENT_RULE_3);

        } else if (tokens.get(0).contains("nao")) {
            object.setIsCorrect(Boolean.TRUE);
            object.setType(MessageType.ASSIGNMENT_RULE_4);

        } else if (tokens.get(0).contains("esta")) {
            object.setIsCorrect(tokens.size() > 1);
            object.setType(MessageType.ASSIGNMENT_RULE_6);

            object.getSymbols().add(new SymbolObject(SymbolType.ADJECTIVE, tokens.get(1)));

        } else if (tokens.contains("e") && !tokens.contains("esta")) {
            object.setIsCorrect(
                    tokens.size() > 2
                    && tokens.get(1).equalsIgnoreCase("e"));
            object.setType(MessageType.ASSIGNMENT_RULE_1);

            if (object.getIsCorrect()) {
                object.getSymbols().add(new SymbolObject(SymbolType.SUBSTANTIVE, tokens.get(0)));
                object.getSymbols().add(new SymbolObject(SymbolType.ADJECTIVE, tokens.get(2)));
            }

        } else if (tokens.contains("esta") && !tokens.contains("e")) {
            object.setIsCorrect(
                    tokens.size() > 2
                    && tokens.get(1).equalsIgnoreCase("esta"));
            object.setType(MessageType.ASSIGNMENT_RULE_5);

            if (object.getIsCorrect()) {
                object.getSymbols().add(new SymbolObject(SymbolType.SUBSTANTIVE, tokens.get(0)));
                object.getSymbols().add(new SymbolObject(SymbolType.ADJECTIVE, tokens.get(2)));
            }

        } else if (tokens.contains("esta") && tokens.contains("e")) {
            if (tokens.size() > 2 && tokens.get(1).equalsIgnoreCase("esta")) {
                object.setType(MessageType.ASSIGNMENT_RULE_5);
                object.setIsCorrect(Boolean.TRUE);

            } else if (tokens.size() > 2 && tokens.get(1).equalsIgnoreCase("e")) {
                object.setType(MessageType.ASSIGNMENT_RULE_1);
                object.setIsCorrect(Boolean.TRUE);

            } else {
                int index1 = tokens.indexOf("esta");
                int index2 = tokens.indexOf("e");

                object.setType((index1 < index2) ? MessageType.ASSIGNMENT_RULE_5 : MessageType.ASSIGNMENT_RULE_1);
                object.setIsCorrect(Boolean.FALSE);
            }

        } else {

            List<String> adjectives = Arrays.asList("bom", "ruim", "problem", "lig");

            if (tokens.stream().anyMatch(p -> adjectives.contains(p))) {
                object.setIsCorrect(Boolean.TRUE);
                object.setType(MessageType.ASSIGNMENT_RULE_2);

                object.getSymbols().add(new SymbolObject(SymbolType.ADJECTIVE, tokens.get(0)));

            } else {
                object.setIsCorrect(Boolean.FALSE);
                object.setType(MessageType.QUESTION_RULE_1);

            }

        }

        return Optional.of(object);
    }

}

/*

Gramática formal:

Pgt ::= Como <expressao> ?
Pgt ::= Gostaria <expressao>
Pgt ::= Preciso <expressao>

Attr ::= <substantivo> é <adjetivo>
Attr ::= <adjetivo>
Attr ::= Sim
Attr ::= Não
Attr ::= <substantivo> está <adjetivo>
Attr ::= está <adjetivo>

<substantivo> ::= ainda não definido
<expressao> ::= ainda não definido
<adjetivo> ::= ainda não definido

<servico> ::= Consertar | Instalar | Saber | Descobrir | Verificar

<defeito> :: não <Tipo-Defeito>
<Tipo-Defeito> ::= Liga | Funciona | Inicializa | Acende | Executa

<Informacao-Pessoal> ::= CPF | CNPJ | DATA COMPRA

<Equipamento> ::= COMPUTADOR | NOTEBOOK | CELULAR | IMPRESSORA | MOUSE | MONITOR | TELA
 
*/
