package com.influenzer.chatbot.compiler.translator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertFileGenerator {

    private final ReadFile reader = new ReadFile();

    public InvertFileResult getInvertFile() {
        InvertFileResult result = new InvertFileResult();

        for (int i = 0; i < 12; i++) {
            try {
                String name = "file_" + i;

                Map<String, Double> tfFile = reader.readFile(name);
                result.getFiles().put(name, tfFile);
                result.setDf(getDF(result.getFiles().values()));

            } catch (IOException e) { // if the project dont contains the file, just ignore it

            }
        }

        return result;
    }
    
    private Map<String, Long> getDF(Collection<Map<String, Double>> collections){
        Map<String, Long> map = new HashMap<>();
        Set<String> possibleTerms = new HashSet<>();
        collections.forEach( item -> {
            possibleTerms.addAll(item.keySet());
        });
        
        possibleTerms.forEach( term -> {
            Long count = collections.stream()
                    .mapToLong( tfFile -> tfFile.containsKey(term) ? 1 : 0)
                    .reduce(0, (result, accumulator) -> result + accumulator);
            map.put(term, count);
        });
        
        return map;
    }

}
