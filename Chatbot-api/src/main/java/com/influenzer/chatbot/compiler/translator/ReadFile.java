package com.influenzer.chatbot.compiler.translator;

import com.influenzer.chatbot.compiler.optimizer.Optimizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

public class ReadFile {
    
    private InputStreamReader inputStream;
    private BufferedReader bufferedReader;
    
    public Map<String, Double> readFile(String fileName) throws FileNotFoundException, IOException{
        Map<String, Double> result = new HashMap<>();
        
        InputStream input = new ClassPathResource("static/user_response/" + fileName).getInputStream();
        inputStream = new InputStreamReader(input);
        bufferedReader = new BufferedReader(inputStream);
        
        String line;
        long count = 0;
        while((line = bufferedReader.readLine()) != null){
            String[] words = line.toLowerCase()
                    .split("[,.!;:(){}|<>?\\s+]");
            count += words.length;
            List<String> list = Arrays.asList(words);
            Optimizer.lexiconOptimizer(list);
            list.forEach((word) -> {
                if(result.containsKey(word)){
                    result.replace(word, result.get(word) + 1);
                } else {
                    result.put(word, new Double(1));
                }
            });
        }
        
        // normalize
        final long numDocumentWords = count;
        result.keySet().forEach( (key) -> {
            result.replace(key, result.get(key) / numDocumentWords);
        });

        return result;
    }
    
}
