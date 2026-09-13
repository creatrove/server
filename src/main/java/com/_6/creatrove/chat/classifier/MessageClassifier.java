package com._6.creatrove.chat.classifier;

public interface MessageClassifier {
    ClassificationResult classify(String content);
}