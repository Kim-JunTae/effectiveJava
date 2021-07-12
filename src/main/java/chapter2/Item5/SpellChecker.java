package chapter2.Item5;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SpellChecker {
    private final LexiCon2 dictionary;

    public SpellChecker(LexiCon2 dictionary){
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public static boolean isValid(String word) {throw new UnsupportedOperationException();}

    public static List<String> suggestions(String typo){throw new UnsupportedOperationException();}

}

class LexiCon2 {}
