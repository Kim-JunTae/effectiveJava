package chapter2.Item5;

import java.util.List;

public class SpellCheckerByStatic {
    private static final LexiCon dictionary = new KoreanDictionary();

    private SpellCheckerByStatic(){
        //Noninstantialbe
    }

    public static boolean isValid(String word) {throw new UnsupportedOperationException();}

    public static List<String> suggestions(String typo){throw new UnsupportedOperationException();}
}