package com.influenzer.chatbot.compiler.sintatic;

import com.influenzer.chatbot.compiler.model.MessageType;
import com.influenzer.chatbot.compiler.model.SymbolObject;
import com.influenzer.chatbot.compiler.model.SymbolType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


/*

Gramática formal:

Pgt ::= Como <servico> <software> ? | Como <servico> <equipamento> ?
Pgt ::= Gostaria <expressao>
Pgt ::= Preciso <expressao>

Attr ::= <substantivo> é <modelo> | <substantivo> esta <modelo> | <categoria-informacao> é <informacao> | <equipamento> e <fabricante> | <software> e <fabricante>
Attr ::= <defeito>
Attr ::= Sim
Attr ::= Não
Attr ::= <substantivo> <defeito>
Attr ::= está com <tipo-defeito>

<substantivo> ::= <equipamento> | <software> | <fabricante>
<expressao> ::= <servico> <equipamento> | <servico> <software> | <equipamento> <defeito>
<adjetivo> ::= <defeito> | <informacao>

<servico> ::= Consertar | Instalar | Saber | Descobrir | Verificar

<defeito> :: não <tipo-defeito> 
<tipo-defeito> ::= Liga | Funciona | Inicializa | Acende | Executa | PROBLEMA

<categoria-informacao> ::= cpf | cnpj | pedido
<informacao> ::= CPF | CNPJ | DATA COMPRA

<fabricante> ::= DELL | APPLE | MICROSOFT | LENOVO | LG | GOOGLE
<software> ::= DRIVER | PROGRAMA | WINDOWS | MACOS | LINUX | UBUNTU
<modelo> ::= BRANCO | PRETO | CINZA | VERMELHO | NUMERO | WINDOWS | MACOS | LINUX | UBUNTU
<equipamento> ::= COMPUTADOR | NOTEBOOK | CELULAR | IMPRESSORA | MOUSE | MONITOR | TELA
 
 */
@Component
public class SyntacticAnalyzer {

    private final List<String> services = Arrays.asList("consert", "instal", "sab", "descobr", "verific");
    private final List<String> equipments = Arrays.asList("comput", "notebook", "celul", "impressor", "mous", "monitor", "tel");
    private final List<String> softwares = Arrays.asList("driv", "program", "windows", "macos", "linux", "ubuntu");
    private final List<String> bugTypes = Arrays.asList("lig", "funcion", "inicializ", "acend", "execut", "problem");
    private final List<String> models = Arrays.asList("branc", "pret", "cinz", "vermelh", "verd", "azul", "windows", "macos", "linux", "ubuntu");
    private final List<String> informationCategory = Arrays.asList("cpf", "cnpj", "pedido");
    private final List<String> manufacturers = Arrays.asList("dell", "apple", "microsoft", "lenovo", "lg", "google");

    public Optional<SyntacticObject> start(List<String> tokens) throws SyntaticException {
        if (tokens == null || tokens.isEmpty()) {
            throw new SyntaticException("Nenhuma regra foi reconhecida", MessageType.NOT_RECOGNIZED);
        }

        SyntacticObject object = new SyntacticObject();

        while (!tokens.isEmpty()) {

            // todos os marcadores iniciais devem estar de acordo com as respectivas regras
            if (tokens.get(0).equalsIgnoreCase("com")) { // question - rule 1

                int index = 1;
                while (index < (tokens.size()) && !this.services.contains(tokens.get(index))) {
                    tokens.remove(index);
                }

                if (index >= (tokens.size())) {
                    throw new SyntaticException("Serviço esperado", MessageType.QUESTION_RULE_1);
                }

                index++; // passa para o proximo token
                while (index < (tokens.size()) && (!this.equipments.contains(tokens.get(index)) && !this.softwares.contains(tokens.get(index)))) {
                    tokens.remove(index);
                }

                if ((tokens.size()) <= index) {
                    throw new SyntaticException("Equipamento ou software esperado", MessageType.QUESTION_RULE_1);
                }

                index++; // passa para o proximo token
                while (index < (tokens.size()) && !tokens.get(index).contains("?")) {
                    tokens.remove(index);
                }
                if (index >= (tokens.size())) {
                    throw new SyntaticException("Ponto de interrogação esperado", MessageType.QUESTION_RULE_1);
                }

                // index neste momento sera o caractere ?
                cleanTokensList(tokens, index - 1);

                object.setType(MessageType.QUESTION_RULE_1);
                object.setSymbols(tokens.stream().map( token -> new SymbolObject(token)).collect(Collectors.toList()));

                return Optional.of(object);

            } else if (tokens.get(0).equalsIgnoreCase("gost")) {

                int index = 1;
                while (index < (tokens.size()) && (!this.services.contains(tokens.get(index)) && !this.equipments.contains(tokens.get(index)))) {
                    tokens.remove(index);
                }

                if ((tokens.size()) <= index) {
                    throw new SyntaticException("Serviço ou equipamento esperado", MessageType.QUESTION_RULE_2);
                }
                boolean isServico = this.services.contains(tokens.get(index));

                index++;
                int tempIndex = index;
                List<String> tempTokens = new ArrayList<>(tokens);
                while (index < (tokens.size()) && isServico && (!this.equipments.contains(tokens.get(index)) && !this.softwares.contains(tokens.get(index)))) {
                    tokens.remove(index);
                }

                if ((tokens.size()) <= index && isServico) {
                    throw new SyntaticException("Software ou equipamento esperado", MessageType.QUESTION_RULE_2);
                } else if (isServico) {
                    cleanTokensList(tokens, index);
                    object.setType(MessageType.QUESTION_RULE_2);
                    return Optional.of(object);
                }

                index = tempIndex; // restaura o index para iniciar uma busca por bug. Tratava se de um equipamento
                tokens = tempTokens;
                while (index < (tokens.size() - 1)
                        && (!tokens.get(index).equalsIgnoreCase("nao") || !this.bugTypes.contains(tokens.get(index + 1)))) {
                    tokens.remove(index);
                }

                if ((tokens.size() - 1) <= index) {
                    throw new SyntaticException("Defeito esperado", MessageType.QUESTION_RULE_2);
                }

                cleanTokensList(tokens, index);
                object.setType(MessageType.QUESTION_RULE_2);
                object.setSymbols(tokens.stream().map( token -> new SymbolObject(token)).collect(Collectors.toList()));
                return Optional.of(object);

            } else if (tokens.get(0).equalsIgnoreCase("precis")) {

                int index = 1;
                while (index < (tokens.size()) && (!this.services.contains(tokens.get(index)) && !this.equipments.contains(tokens.get(index)))) {
                    tokens.remove(index);
                }

                if ((tokens.size()) <= index) {
                    throw new SyntaticException("Serviço ou equipamento esperado", MessageType.QUESTION_RULE_3);
                }
                boolean isServico = this.services.contains(tokens.get(index));

                index++;
                int tempIndex = index;
                List<String> tempTokens = new ArrayList<>(tokens);
                while (index < (tokens.size()) && isServico && (!this.equipments.contains(tokens.get(index)) && !this.softwares.contains(tokens.get(index)))) {
                    tokens.remove(index);
                }

                if ((tokens.size()) <= index && isServico) {
                    throw new SyntaticException("Software ou equipamento esperado", MessageType.QUESTION_RULE_3);
                } else if (isServico) {
                    cleanTokensList(tokens, index);
                    object.setType(MessageType.QUESTION_RULE_3);
                    return Optional.of(object);
                }

                index = tempIndex; // restaura o index para iniciar uma busca por bug. Tratava se de um equipamento
                tokens = tempTokens;
                while (index < (tokens.size() - 1)
                        && (!tokens.get(index).equalsIgnoreCase("nao") || !this.bugTypes.contains(tokens.get(index + 1)))) {
                    tokens.remove(index);
                }

                if ((tokens.size() - 1) <= index) {
                    throw new SyntaticException("Defeito esperado", MessageType.QUESTION_RULE_3);
                }

                cleanTokensList(tokens, index);
                object.setType(MessageType.QUESTION_RULE_3);
                object.setSymbols(tokens.stream().map( token -> new SymbolObject(token)).collect(Collectors.toList()));
                return Optional.of(object);

            } else if (tokens.get(0).equalsIgnoreCase("esta")) {

                int index = 1;
                while (index < (tokens.size() - 1) && (!tokens.get(index).equalsIgnoreCase("com") || !this.bugTypes.contains(tokens.get(index + 1)))) {
                    tokens.remove(index);
                }

                if ((tokens.size() - 1) <= index) {
                    throw new SyntaticException("Defeito esperado", MessageType.ASSIGNMENT_RULE_6);
                }

                cleanTokensList(tokens, index);
                object.setType(MessageType.ASSIGNMENT_RULE_6);

                object.getSymbols().add(new SymbolObject(SymbolType.DEFECT, SymbolType.DEFECT.toString(), tokens.get(index)));
                return Optional.of(object);

            }

            if (tokens.size() >= 2
                    && (tokens.get(1).equalsIgnoreCase("e") || tokens.get(1).equalsIgnoreCase("esta"))
                    && (this.equipments.contains(tokens.get(0)) || this.softwares.contains(tokens.get(0)) || this.manufacturers.contains(tokens.get(0)) || this.informationCategory.contains(tokens.get(0)))) { // assignment - rule 1

                if (tokens.size() < 3) {
                    throw new SyntaticException("Informação, equipamento, fabricante ou modelo esperado", MessageType.ASSIGNMENT_RULE_1);
                }

                int index = 1;

                while (index < (tokens.size())) {

                    if (this.informationCategory.contains(tokens.get(0)) && (tokens.get(index).length() == 11 // cpf
                            || tokens.get(index).length() == 14 // cnpj
                            || tokens.get(index).contains("/") // data
                            || tokens.get(index).contains("."))) {

                        cleanTokensList(tokens, index);
                        object.setType(MessageType.ASSIGNMENT_RULE_1);
                        object.getSymbols().add(new SymbolObject(SymbolType.INFORMATION, tokens.get(0), tokens.get(index)));
                        return Optional.of(object);
                    }

                    if (this.equipments.contains(tokens.get(0)) || this.softwares.contains(tokens.get(0)) || this.manufacturers.contains(tokens.get(0))
                            && (this.models.contains(tokens.get(index)))) {
                        cleanTokensList(tokens, index);
                        object.setType(MessageType.ASSIGNMENT_RULE_1);
                        object.getSymbols().add(new SymbolObject(SymbolType.MODEL, tokens.get(0), tokens.get(index)));
                        return Optional.of(object);
                    }

                    if (this.equipments.contains(tokens.get(0)) || this.softwares.contains(tokens.get(0))
                            && this.manufacturers.contains(tokens.get(index))) {
                        cleanTokensList(tokens, index);
                        object.setType(MessageType.ASSIGNMENT_RULE_1);
                        object.getSymbols().add(new SymbolObject(SymbolType.MANUFACTURER, tokens.get(0), tokens.get(index)));
                        return Optional.of(object);
                    }

                    tokens.remove(index);
                }

                throw new SyntaticException("Informação ou modelo esperado", MessageType.ASSIGNMENT_RULE_1);
            }

            if (isInformation(tokens) // information
                    || isBug(tokens)) { // defeito

                if (tokens.size() > 1
                        && isInformation(tokens)) {
                    cleanTokensList(tokens, 1);
                } else if (tokens.size() > 2) {
                    cleanTokensList(tokens, 2);
                }

                object.setType(MessageType.ASSIGNMENT_RULE_2);
                object.getSymbols().add(new SymbolObject(SymbolType.INFORMATION, SymbolType.INFORMATION.toString(), (tokens.size() == 1) ? tokens.get(0) : tokens.get(1)));
                return Optional.of(object);
            }

            if (this.equipments.contains(tokens.get(0)) || this.softwares.contains(tokens.get(0)) || this.manufacturers.contains(tokens.get(0))) {

                int index = 1;
                while (index < (tokens.size() - 1)) {

                    if (tokens.get(index).equalsIgnoreCase("nao") && this.bugTypes.contains(tokens.get(index + 1))) {

                        cleanTokensList(tokens, index + 1);
                        object.setType(MessageType.ASSIGNMENT_RULE_5);
                        object.getSymbols().add(new SymbolObject(SymbolType.DEFECT, tokens.get(0), tokens.get(index + 1)));
                        return Optional.of(object);
                    }

                    tokens.remove(index);
                }

                throw new SyntaticException("Defeito esperado", MessageType.ASSIGNMENT_RULE_5);
            }

            // nos casos onde os marcadores iniciais não foram encontrados, será feita uma nova tentativa de identificar possiveis erros
            if (tokens.get(0).equalsIgnoreCase("e")) {

                if (tokens.size() == 1) {
                    throw new SyntaticException("Equipamento ou software esperado", MessageType.ASSIGNMENT_RULE_1);
                }

                if (tokens.get(1).length() == 11 // cpf
                        || tokens.get(1).length() == 14 // cnpj
                        || tokens.get(1).contains("/") // data
                        || tokens.get(1).contains(".")
                        || this.models.contains(tokens.get(1))) {
                    throw new SyntaticException("Equipamento ou software esperado", MessageType.ASSIGNMENT_RULE_1);
                }
            }

            if (this.bugTypes.contains(tokens.get(0))) {
                throw new SyntaticException("Equipamento ou software esperado", MessageType.ASSIGNMENT_RULE_5);
            }

            // nenhum dos casos foi identificado, logo, testa se as menores regras
            if (tokens.get(0).contains("sim")) {
                cleanTokensList(tokens, 1);
                object.setType(MessageType.ASSIGNMENT_RULE_3);
                return Optional.of(object);

            } else if (tokens.get(0).contains("nao")) {
                cleanTokensList(tokens, 1);
                object.setType(MessageType.ASSIGNMENT_RULE_4);
                return Optional.of(object);

            }

            tokens.remove(0);
        }

        throw new SyntaticException("Nenhuma regra foi reconhecida", MessageType.NOT_RECOGNIZED);
    }

    private void cleanTokensList(List<String> tokens, int finalIndex) {
        if (!tokens.isEmpty() && finalIndex < tokens.size()) {
            int i = tokens.size() - 1;
            while (tokens.size() > (finalIndex + 1)) {
                tokens.remove(i);
                i--;
            }
        }
    }

    private boolean isInformation(List<String> tokens) {
        return (tokens.get(0).length() == 11 || tokens.get(0).length() == 14 || tokens.get(0).contains("/") || tokens.get(0).contains("."));
    }

    private boolean isBug(List<String> tokens) {
        return (tokens.size() >= 2 && tokens.get(0).equalsIgnoreCase("nao") && this.bugTypes.contains(tokens.get(1)));
    }

}
