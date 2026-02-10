package com.simats.prepace.utils;

import com.simats.prepace.model.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MockQuizReviewGenerator {

    public static List<Question> generateMockQuestions(String category, String quizTitle, int totalQuestions, int correctCount) {
        List<Question> questions = new ArrayList<>();
        String key = quizTitle.toLowerCase();
        String catKey = category.toLowerCase();
        
        List<Question> sourcePool = new ArrayList<>();

        // --- TECHNICAL ---
        if (key.contains("java") && !key.contains("script")) {
            sourcePool.add(new Question("Which of these is not a primitive type in Java?", "int", "float", "String", "boolean", 2, "String is an Object (Reference Type), not a primitive."));
            sourcePool.add(new Question("What is the size of an int in Java?", "16 bit", "32 bit", "64 bit", "8 bit", 1, "In Java, an int is always 32-bit signed two's complement integer."));
            sourcePool.add(new Question("Which keyword is used to inherit a class?", "implement", "extends", "inherits", "using", 1, "'extends' is used for class inheritance."));
            sourcePool.add(new Question("Default value of boolean in Java?", "true", "false", "null", "undefined", 1, "The default value for boolean fields is false."));
        } 
        else if (key.contains("script") || key.contains("js")) { // JavaScript
            sourcePool.add(new Question("What is console.log(typeof [])?", "'array'", "'object'", "'list'", "'undefined'", 1, "Arrays are objects in JavaScript."));
            sourcePool.add(new Question("Which symbol is used for comments in JS?", "//", "#", "<!--", "**", 0, "// is used for single line comments."));
            sourcePool.add(new Question("What is NaN?", "Not a Name", "Not a Null", "Not a Number", "New array Name", 2, "NaN stands for Not-a-Number."));
        }
        else if (key.contains("python")) {
            sourcePool.add(new Question("How do you start a for loop in Python?", "for x in y:", "for(x : y)", "forEach x in y", "loop x in y", 0, "Python uses 'for item in iterable:' syntax."));
            sourcePool.add(new Question("Which is immutable?", "List", "Dictionary", "Set", "Tuple", 3, "Tuples are immutable in Python."));
            sourcePool.add(new Question("Output of 2 ** 3?", "5", "6", "8", "9", 2, "** is the exponentiation operator. 2^3 = 8."));
        }
        else if (key.contains("c++") || key.contains("cpp")) {
            sourcePool.add(new Question("Which operator releases memory?", "delete", "remove", "free", "clear", 0, "'delete' operator deallocates memory."));
            sourcePool.add(new Question("Standard input stream in C++?", "cin", "stdin", "cout", "input", 0, "cin is the boolean input stream object."));
        }
        else if (key.contains("html") || key.contains("web")) {
            sourcePool.add(new Question("Smallest heading tag?", "<h1>", "<h6>", "<head>", "<small>", 1, "<h6> is the smallest heading tag."));
            sourcePool.add(new Question("HTML stands for?", "Hyper Text Markup Language", "High Tech Main Link", "Hyper Tool Make Logic", "None", 0, "Hyper Text Markup Language."));
        }

        // --- APTITUDE ---
        else if (key.contains("arithmetic") || catKey.contains("aptitude")) {
            sourcePool.add(new Question("Average of first 5 natural numbers?", "3", "2.5", "3.5", "2", 0, "(1+2+3+4+5)/5 = 15/5 = 3."));
            sourcePool.add(new Question("If A is 20% more than B, B is how much less than A?", "20%", "16.66%", "25%", "15%", 1, "Let B=100, A=120. Diff=20. (20/120)*100 = 16.66%."));
            sourcePool.add(new Question("Speed = 60km/h. Distance in 2 hours?", "100km", "120km", "140km", "110km", 1, "Distance = Speed * Time = 60 * 2 = 120km."));
            sourcePool.add(new Question("LCM of 4 and 6?", "12", "24", "10", "8", 0, "Least Common Multiple of 4 and 6 is 12."));
            sourcePool.add(new Question("Value of 15% of 200?", "20", "25", "30", "35", 2, "15/100 * 200 = 30."));
        }

        // --- REASONING ---
        else if (key.contains("reasoning") || key.contains("logic") || key.contains("pattern")) {
            sourcePool.add(new Question("Find next: 2, 4, 8, 16, ?", "24", "30", "32", "28", 2, "Powers of 2: 32."));
            sourcePool.add(new Question("Odd one out: Apple, Banana, Carrot, Mango", "Apple", "Banana", "Carrot", "Mango", 2, "Carrot is a vegetable, others are fruits."));
            sourcePool.add(new Question("SCD, TEF, UGH, ____, WKL", "VIJ", "VJI", "IJV", "VIK", 0, "First letter +1 (V), Second +2 (I), Third +2 (J)."));
        }

        // --- GENERAL KNOWLEDGE ---
        else if (key.contains("history") || catKey.contains("general") || catKey.contains("gk")) {
            sourcePool.add(new Question("Capital of India?", "Mumbai", "Kolkata", "New Delhi", "Chennai", 2, "New Delhi is the capital."));
            sourcePool.add(new Question("Who wrote 'Discovery of India'?", "Gandhi", "Nehru", "Tagore", "Patel", 1, "Jawaharlal Nehru wrote it."));
            sourcePool.add(new Question("Largest planet?", "Earth", "Mars", "Jupiter", "Saturn", 2, "Jupiter is the largest planet."));
        }

        // --- FALLBACK GENERIC ---
        if (sourcePool.isEmpty()) {
            if (catKey.contains("tech")) {
                sourcePool.add(new Question("What is a variable?", "Specific Value", "Container for data", "Function", "Loop", 1, "Variables store data."));
            } else {
                 sourcePool.add(new Question("Sample Question for " + quizTitle, "A", "B", "C", "D", 0, "Mock explanation."));
            }
        }

        // Fill up to totalQuestions
        Random random = new Random();
        for (int i = 0; i < totalQuestions; i++) {
            Question template = sourcePool.get(i % sourcePool.size());
            
            // Clone to avoid modifying the pool definition if used directly
            Question q = new Question(
                template.getQuestionText() + (i >= sourcePool.size() ? " (Var " + (i+1) + ")" : ""),
                template.getOptionA(), template.getOptionB(), template.getOptionC(), template.getOptionD(),
                template.getCorrectAnswerIndex(),
                template.getExplanation()
            );

            // Determine if this specific question 'instance' should be marked correct or wrong
            // We need exactly 'correctCount' correct answers
            boolean isCorrect = i < correctCount; 
            
            if (isCorrect) {
                 q.setUserAnswerIndex(q.getCorrectAnswerIndex());
            } else {
                 // Pick a wrong answer
                 int wrongIndex = (q.getCorrectAnswerIndex() + 1) % 4;
                 q.setUserAnswerIndex(wrongIndex);
            }
            
            questions.add(q);
        }

        return questions;
    }
}
