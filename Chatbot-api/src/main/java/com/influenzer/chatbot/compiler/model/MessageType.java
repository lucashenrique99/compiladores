package com.influenzer.chatbot.compiler.model;

public enum MessageType {
    
    QUESTION_RULE_1,
    QUESTION_RULE_2,
    QUESTION_RULE_3,
    
    ASSIGNMENT_RULE_1,
    ASSIGNMENT_RULE_2,
    ASSIGNMENT_RULE_3,
    ASSIGNMENT_RULE_4,
    ASSIGNMENT_RULE_5,
    ASSIGNMENT_RULE_6,
    ASSIGNMENT_RULE_7,
    
    NOT_RECOGNIZED;
    
    @Override
    public String toString() {
        return name().replaceAll("_", " ");
    }
}
