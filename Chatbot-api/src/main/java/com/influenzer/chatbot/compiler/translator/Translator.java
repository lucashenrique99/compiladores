package com.influenzer.chatbot.compiler.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.ClassPathResource;

public class Translator {

    private final InvertFileResult invertFile;

    public Translator(InvertFileResult invertFile) {
        this.invertFile = invertFile;
    }

    public String translate(Set<String> words) {
        String fileName = invertFile.getBestAnswer(words);
        return getAnswer(fileName);
    }

    private String getAnswer(String fileName) {
        try {
            String answer = "Este foi o melhor resultado que eu encontrei com base nas suas ultimas pesquisas:\n";
            InputStreamReader ir = new InputStreamReader(new ClassPathResource("static/user_response/" + fileName).getInputStream());
            BufferedReader bufferedReader = new BufferedReader(ir);

            String line;
            while ((line = bufferedReader.readLine()) != null && answer.length() < 500) {
                answer += line;
            }

            if (answer.length() >= 500) {
                answer += " ...";
            }

            return answer;

        } catch (Exception ex) {
            Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
