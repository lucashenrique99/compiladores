package com.influenzer.chatbot.compiler.optimizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;

public class Optimizer {

    private static final Map<String, String> DICTIONARY = new HashMap<>();
    private static final SnowballStemmer STEMMER = new portugueseStemmer();

    private Optimizer() {}

    public static void lexiconOptimizer(List<String> tokens) {
        if (tokens != null && !tokens.isEmpty()) {
            if(DICTIONARY.isEmpty()){
                buildHasmap();
            }
            
            for (int i = 0; i < tokens.size(); i++) {
                String token = wordCompression(tokens.get(i));
                tokens.set(i, DICTIONARY.getOrDefault(token, token));
            }
        }
    }

    private static void buildHasmap() {
        DICTIONARY.put(wordCompression("drive"), wordCompression("driver"));

        DICTIONARY.put(wordCompression("software"), wordCompression("programa"));
        DICTIONARY.put(wordCompression("aplicativo"), wordCompression("programa"));
        DICTIONARY.put(wordCompression("app"), wordCompression("programa"));

        DICTIONARY.put(wordCompression("win"), wordCompression("windows"));
        DICTIONARY.put(wordCompression("window"), wordCompression("windows"));

        DICTIONARY.put(wordCompression("mac"), wordCompression("macos"));
        DICTIONARY.put(wordCompression("mec"), wordCompression("macos"));
        DICTIONARY.put(wordCompression("maco"), wordCompression("macos"));

        DICTIONARY.put(wordCompression("debian"), wordCompression("linux"));
        DICTIONARY.put(wordCompression("fedora"), wordCompression("linux"));
        DICTIONARY.put(wordCompression("linu"), wordCompression("linux"));

        DICTIONARY.put(wordCompression("ubuntu"), wordCompression("ubuntu"));
        DICTIONARY.put(wordCompression("ubunt"), wordCompression("ubuntu"));
        DICTIONARY.put(wordCompression("obuntu"), wordCompression("ubuntu"));

        DICTIONARY.put(wordCompression("pc"), wordCompression("computador"));

        DICTIONARY.put(wordCompression("note"), wordCompression("notebook"));
        DICTIONARY.put(wordCompression("notebooc"), wordCompression("notebook"));

        DICTIONARY.put(wordCompression("cel"), wordCompression("celular"));
        DICTIONARY.put(wordCompression("mobile"), wordCompression("celular"));
        DICTIONARY.put(wordCompression("telefone"), wordCompression("celular"));
        DICTIONARY.put(wordCompression("tel"), wordCompression("celular"));

        DICTIONARY.put(wordCompression("printer"), wordCompression("impressora"));
        DICTIONARY.put(wordCompression("multifuncional"), wordCompression("impressora"));
        DICTIONARY.put(wordCompression("xerox"), wordCompression("impressora"));

        DICTIONARY.put(wordCompression("mousi"), wordCompression("mouse"));
        DICTIONARY.put(wordCompression("mause"), wordCompression("mouse"));

        DICTIONARY.put(wordCompression("tv"), wordCompression("monitor"));
        
        DICTIONARY.put(wordCompression("ordem"), wordCompression("pedido"));
        DICTIONARY.put(wordCompression("compra"), wordCompression("pedido"));

    }

    private static String wordCompression(String word) {
        STEMMER.setCurrent(word);
        STEMMER.stem();
        return STEMMER.getCurrent();
    }

}
