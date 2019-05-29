package com.influenzer.chatbot.compiler.translator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvertFileResult {

    private Map<String, Map<String, Long>> files;
    private Map<String, Long> idf;

    public InvertFileResult() {
        files = new HashMap<>();
        idf = new HashMap<>();
    }

    public String getBestAnswer(Set<String> words) {
        ResultList result = new ResultList(null, Long.MIN_VALUE);
        Random r = new Random();
        files.keySet().forEach(key -> {
            Double value = getInvertFileValue(words, files.get(key)) ;
            if (value > result.value) {
                result.value = value;
                result.fileName = key;
            } else if (value == result.value) {

                long randomNumber = r.nextInt(50);
                if (randomNumber % 2 == 0) {
                    result.value = value;
                    result.fileName = key;
                }

            }
        });

        return result.fileName;
    }

    private Double getInvertFileValue(Set<String> words, Map<String, Long> wordsMapTF) {
        final int numFiles = 12;
        return words
                .stream()
                .mapToDouble((word) -> wordsMapTF.getOrDefault(word, new Long(0)) * (Math.log( numFiles / this.idf.getOrDefault(word, new Long(1)))) )
                .average().getAsDouble();
    }

    private class ResultList {

        String fileName;
        double value;

        public ResultList(String fileName, double value) {
            this.fileName = fileName;
            this.value = value;
        }

    }

}
