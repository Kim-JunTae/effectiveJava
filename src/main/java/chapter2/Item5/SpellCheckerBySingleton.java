package chapter2.Item5;

import java.util.*;


//Singleton을 잘못 사용한 예
public class SpellCheckerBySingleton {
    private static final LexiCon dictionary = new KoreanDictionary();

    private SpellCheckerBySingleton(){
        //Noninstantialbe
    }

    public static SpellCheckerBySingleton INSTANCE = new SpellCheckerBySingleton();

    public static boolean isValid(String word) {throw new UnsupportedOperationException();}

    public static List<String> suggestions(String typo){throw new UnsupportedOperationException();}

}

