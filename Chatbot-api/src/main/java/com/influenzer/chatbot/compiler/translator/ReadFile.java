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
    
    public Map<String, Long> readFile(String fileName) throws FileNotFoundException, IOException{
        Map<String, Long> result = new HashMap<>();
        
        InputStream input = new ClassPathResource("static/user_response/" + fileName).getInputStream();
        inputStream = new InputStreamReader(input);
        bufferedReader = new BufferedReader(inputStream);
        
        String line;
        while((line = bufferedReader.readLine()) != null){
            String[] words = line.toLowerCase()
                    .split("[,.!;:(){}|<>?\\s+]");
            List<String> list = Arrays.asList(words);
            Optimizer.lexiconOptimizer(list);
            list.forEach((word) -> {
                if(result.containsKey(word)){
                    result.replace(word, result.get(word) + 1);
                } else {
                    result.put(word, new Long(1));
                }
            });
        }
        
        return result;
    }
    
}
