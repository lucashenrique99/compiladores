package com.influenzer.chatbot.compiler.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvertFileResult {

    private Map<String, Map<String, Double>> files;
    private Map<String, Long> df;

    public InvertFileResult() {
        files = new HashMap<>();
        df = new HashMap<>();
    }

    public String getBestAnswer(Set<String> words) {
        Result result = new Result(null, 0);
        Random r = new Random();
        List<String> listWords = new ArrayList<>(words);
        List<Double> queryArray = listWords
                .stream()
                .map((word) -> Math.log(12 / this.df.getOrDefault(word, new Long(12))))
                .collect(Collectors.toList());

        files.keySet().forEach(key -> {
            List<Double> fileArray = getInvertFileValue(listWords, files.get(key));
            Double value = similarCalculator(fileArray, queryArray);
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

    private List<Double> getInvertFileValue(List<String> words, Map<String, Double> wordsMapTF) {
        final int numFiles = 12;
        return words
                .stream()
                .map((word) -> wordsMapTF.getOrDefault(word, new Double(0)) * (Math.log(numFiles / this.df.getOrDefault(word, new Long(numFiles)))))
                .collect(Collectors.toList());
    }

    private double similarCalculator(List<Double> array1, List<Double> array2) {
        double numerator = 0;
        for (int i = 0; i < array1.size(); i++) {
            numerator += array1.get(i) * array2.get(i);
        }
        double denominator
                = Math.sqrt(array1.stream().mapToDouble((value) -> value * value).sum())
                * Math.sqrt(array2.stream().mapToDouble((value) -> value * value).sum());

        return (denominator == 0) ? 0 : numerator / denominator;
    }

    private class Result {

        String fileName;
        double value;

        public Result(String fileName, double value) {
            this.fileName = fileName;
            this.value = value;
        }

    }

}
