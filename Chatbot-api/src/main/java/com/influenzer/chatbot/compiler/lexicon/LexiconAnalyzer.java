package com.influenzer.chatbot.compiler.lexicon;

import com.influenzer.chatbot.compiler.optimizer.Optimizer;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;

@Component
public class LexiconAnalyzer {

//    Foi utilizado uma biblioteca para a geracao dos lexemas
//    Disponivel em <https://snowballstem.org/>. Acesso dia 10/04/2019
    private final SnowballStemmer stemmer;

    public LexiconAnalyzer() {
        this.stemmer = new portugueseStemmer();
    }

    public Optional<LexiconObject> start(String input) {
        if (input != null && !input.isEmpty()) {

            LexiconObject object = new LexiconObject();

            input = this.removeSpecialCharacter(input);

            List<String> list = this.tokenVerification(input);

            this.removeStopWords(list); // remove stopwords

            this.lexemesGenerate(list); // gera lexemas

            List<String> symbols = this.symbolsTableGenerate(list); // gera tabela de simbolos
            object.setSymbols(symbols);
            object.setLexemes(list);

            return Optional.of(object);
        }

        return Optional.empty();
    }

    private void lexemesGenerate(List<String> list) {
//        for (int i = 0; i < list.size(); i++) { // comprimindo a word
//            String word = list.get(i);
//            this.stemmer.setCurrent(word);
//            this.stemmer.stem();
//            list.set(i, this.stemmer.getCurrent());
//
//        }
        Optimizer.lexiconOptimizer(list);
    }

    private List<String> symbolsTableGenerate(List<String> list) {
        List<String> keywords = Arrays.asList("com", "gost", "precis", "e", "sim", "nao", "esta", "?");

        return list.stream()
                .filter(p -> !keywords.contains(p))
                .sorted((s1, s2) -> s1.compareTo(s2))
                .collect(Collectors.toList());
    }

    private List<String> tokenVerification(String input) {
        List<String> list = new ArrayList<>(1000);

        List<String> array = new ArrayList<>();
        Arrays.asList(input.toLowerCase().split("[\\s+]")) // transforma toda a word pra lowercase para reduzir o alfabeto e então faz a separação considerando os espacos
                .forEach((p) -> {
                    if (p != null && !p.isEmpty()) {
                        array.add(p);
                    }
                });

        array.stream()
                .map((token) -> tokenAnalyzer(token)) // verifica os tokens, identifica e retira os sinais de pontuacao
                .forEachOrdered((retorno) -> {
                    list.addAll(retorno);
                });

        return list;
    }

    private void removeStopWords(List<String> input) {
        input.removeAll(Stopwords.list);
    }

    private String removeSpecialCharacter(String word) {
        if (word != null && !word.isEmpty()) {
            // remove acento de vogais
            word = word.replaceAll("[áàãâ]", "a");
            word = word.replaceAll("[ÀÁÃÂ]", "A");

            word = word.replaceAll("[éèẽê]", "e");
            word = word.replaceAll("[ÉÈẼÊ]", "E");

            word = word.replaceAll("[íìĩî]", "i");
            word = word.replaceAll("[ÍÌĨÎ]", "I");

            word = word.replaceAll("[óòõô]", "o");
            word = word.replaceAll("[ÓÒÕÔ]", "O");

            word = word.replaceAll("[úùũû]", "u");
            word = word.replaceAll("[ÚÙŨÛ]", "U");

            // remove letras especiais
            word = word.replaceAll("ñ", "n");
            word = word.replaceAll("Ñ", "N");

            word = word.replaceAll("ç", "c");
            word = word.replaceAll("Ç", "C");

            word = word.replaceAll("ĉ", "c");
            word = word.replaceAll("Ĉ", "C");

            word = word.replaceAll("ŝ", "s");
            word = word.replaceAll("Ŝ", "S");

            word = word.replaceAll("[\t\n]", " ");
        }

        return word;
    }

    private List<String> tokenAnalyzer(String token) {
        if (token != null && !token.isEmpty()) {

            if (this.dateAnalyser(token)
                    || this.cpfAnalyzer(token)
                    || this.cnpjAnalyzer(token)
                    || this.numberAnalyzer(token)) {
                return Arrays.asList(token);
            } else if (this.isQuestion(token)) {

                List<String> aux = new ArrayList<>();
                Arrays.asList(token.split("[,.!;:(){}|<>]"))
                        .stream()
                        .filter((word) -> (word != null && word.contains("?")))
                        .forEachOrdered((word) -> {
                            final int index = word.indexOf("?");
                            aux.add(word.substring(0, index));
                            aux.add("?");
                            if (index < word.length() - 1) {
                                aux.add(word.substring(index + 1));
                            }
                        });
                return aux;
            }

            return Arrays.asList(token.split("[,.!;:(){}|<>]"));
        }
        return null;
    }

    private boolean isQuestion(String token) {
        if (token != null && !token.isEmpty()) {
            return token.contains("?");
        }

        return false;
    }

    private boolean numberAnalyzer(String token) {
        String[] array = {};
        if (token.contains(",")) {
            array = token.split("[,]");
        } else if (token.contains(".")) {
            array = token.split("[.]");
        }

        if (array.length == 2) {
            try {
                Float.parseFloat(array[0]);
                Float.parseFloat(array[1]);
                return true;

            } catch (NumberFormatException e) {
            }
        }

        return false;
    }

    private boolean cpfAnalyzer(String token) {
        String[] array = {token};
        if (token.contains(".") && token.contains("-")) {
            array = token.split("[.-]");
        }

        if (array.length == 1) {
            if (array[0].length() != 11) {
                return false;
            }
            try {
                Long.parseLong(array[0]);
                return this.isCPF(token);
            } catch (NumberFormatException e) {
            }
        } else if (array.length == 4) {
            try {
                // verifica o tamanho
                if (array[0].length() != 3) {
                    return false;
                }
                if (array[1].length() != 3) {
                    return false;
                }
                if (array[2].length() != 3) {
                    return false;
                }
                if (array[3].length() != 2) {
                    return false;
                }

                Integer.parseInt(array[0]);
                Integer.parseInt(array[1]);
                Integer.parseInt(array[2]);
                return this.isCPF(token.replaceAll("[.-]", ""));

            } catch (NumberFormatException e) {
            }
        }

        return false;
    }

    private boolean cnpjAnalyzer(String token) {
        String[] array = {token};
        if (token.contains(".") && token.contains("-")) {
            array = token.split("[.-/]");
        }

        if (array.length == 1) {
            if (array[0].length() != 14) {
                return false;
            }
            try {
                Long.parseLong(array[0]);
                return this.isCNPJ(token);
            } catch (NumberFormatException e) {
            }
        } else if (array.length == 5) {
            try {
                // verifica o tamanho
                if (array[0].length() != 2) {
                    return false;
                }
                if (array[1].length() != 3) {
                    return false;
                }
                if (array[2].length() != 3) {
                    return false;
                }
                if (array[3].length() != 4) {
                    return false;
                }
                if (array[4].length() != 2) {
                    return false;
                }

                Integer.parseInt(array[0]);
                Integer.parseInt(array[1]);
                Integer.parseInt(array[2]);
                Integer.parseInt(array[3]);
                Integer.parseInt(array[4]);
                return this.isCNPJ(token.replaceAll("[.-/]", ""));

            } catch (NumberFormatException e) {
            }
        }

        return false;
    }

    private boolean dateAnalyser(String token) {
        String[] array = {};
        if (token.contains("/")) {
            array = token.split("[/]");
        } else if (token.contains(".")) {
            array = token.split("[.]");
        }

        if (array.length == 2) { // dia e mes
            try {
                LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(array[1]), Integer.parseInt(array[0]));
                return true;

            } catch (NumberFormatException | DateTimeException e) {

            }

        } else if (array.length == 3) { // dia/mes/ano
            try {
                if (array[2].length() == 2) { // ano com dois digitos -> transforma para 20YY ou 19YY
                    int ano = Integer.parseInt(array[2]);
                    if (ano > 50 && ano < 99) { // SUPOSIÇÃO
                        array[2] = "19" + array[2];
                    } else {
                        array[2] = "20" + array[2];
                    }
                }
                LocalDate.of(Integer.parseInt(array[2]), Integer.parseInt(array[1]), Integer.parseInt(array[0]));
                return true;

            } catch (NumberFormatException | DateTimeException e) {

            }
        }

        return false;
    }

    private boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000")
                || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0         
                // (48 eh a posicao de '0' na tabela ASCII)         
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    private boolean isCNPJ(String CNPJ) {
// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
                || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333")
                || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
                || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777")
                || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999")
                || (CNPJ.length() != 14)) {
            return (false);
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

// "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
// Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
// converte o i-ésimo caractere do CNPJ em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

// Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

// Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

}

class Stopwords {

//    Lista de stopwords
//    Disponível em <https://gist.github.com/alopes/5358189>. Acesso em 10/4/2019
    static List<String> list = Arrays.asList(
            "a",
            "ao",
            "aos",
            "aquela",
            "aquelas",
            "aquele",
            "aqueles",
            "aquilo",
            "as",
            "ate",
            "boa",
            "bom",
//            "com",
//            "como",
            "da",
            "das",
            "de",
            "dela",
            "delas",
            "dele",
            "deles",
            "depois",
            "dia",
            "do",
            "dos",
            //            "e",
            "ela",
            "elas",
            "ele",
            "eles",
            "em",
            "entre",
            "era",
            "eram",
            "eramos",
            "essa",
            "essas",
            "esse",
            "esses",
            //            "esta",
            "estamos",
            "estao",
            "estas",
            "estava",
            "estavam",
            "estavamos",
            "este",
            "esteja",
            "estejam",
            "estejamos",
            "estes",
            "esteve",
            "estive",
            "estivemos",
            "estiver",
            "estivera",
            "estiveram",
            "estiveramos",
            "estiverem",
            "estivermos",
            "estivesse",
            "estivessem",
            "estivessemos",
            "estou",
            "eu",
            "foi",
            "fomos",
            "for",
            "fora",
            "foram",
            "foramos",
            "forem",
            "formos",
            "fosse",
            "fossem",
            "fossemos",
            "fui",
            "ha",
            "haja",
            "hajam",
            "hajamos",
            "hao",
            "havemos",
            "havia",
            "hei",
            "houve",
            "houvemos",
            "houver",
            "houvera",
            "houveram",
            "houveramos",
            "houverao",
            "houverei",
            "houverem",
            "houveremos",
            "houveria",
            "houveriam",
            "houveriamos",
            "houvermos",
            "houvesse",
            "houvessem",
            "houvessemos",
            "isso",
            "isto",
            "ja",
            "lhe",
            "lhes",
            "mais",
            "mas",
            "me",
            "mesmo",
            "meu",
            "meus",
            "minha",
            "minhas",
            "muito",
            "na",
            //            "nao",
            "nas",
            "nem",
            "no",
            "noite",
            "nos",
            "nossa",
            "nossas",
            "nosso",
            "nossos",
            "num",
            "numa",
            "o",
            "os",
            "ou",
            "para",
            "pela",
            "pelas",
            "pelo",
            "pelos",
            "por",
            "qual",
            "quando",
            "que",
            "quem",
            "sao",
            "se",
            "seja",
            "sejam",
            "sejamos",
            "sem",
            "ser",
            "sera",
            "serao",
            "serei",
            "seremos",
            "seria",
            "seriam",
            "seriamos",
            "seu",
            "seus",
            "so",
            "somos",
            "sou",
            "sua",
            "suas",
            "tambem",
            "tarde",
            "te",
            "tem",
            "temos",
            "tenha",
            "tenham",
            "tenhamos",
            "tenho",
            "ter",
            "tera",
            "terao",
            "terei",
            "teremos",
            "teria",
            "teriam",
            "teriamos",
            "teu",
            "teus",
            "teve",
            "tinha",
            "tinham",
            "tinhamos",
            "tive",
            "tivemos",
            "tiver",
            "tivera",
            "tiveram",
            "tiveramos",
            "tiverem",
            "tivermos",
            "tivesse",
            "tivessem",
            "tivessemos",
            "tu",
            "tua",
            "tuas",
            "um",
            "uma",
            "voce",
            "voces",
            "vos"
    );

}
