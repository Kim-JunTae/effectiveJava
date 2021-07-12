package chapter2.Item1;

import java.util.Collections;
import java.util.EnumSet;

/**
 * 아이템 1 : 생성자 대신 정적 팩토리 메서드를 고려하라
 */
public class Babo {
    String name;

    String address;

    public Babo() {
    }

    public Babo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Babo(String name) {
        this.name = name;
    }

    //1. 반환될 객체의 특성을 쉽게 묘사할 수 있다.
    public static Babo withName(String name){
        return new Babo(name);
    }

    //2. 하나의 시그니처로는 생성자 하나만 만들 수 있기 때문에 불가
//    public Babo(String address){
//        this.address = address;
//    }

    public static Babo withAddress(String address){
        Babo babo = new Babo();
        babo.address = address;
        return babo;
    }

    //3. 반드시 새로운 객체를 만들 필요가 없다.
    private static final Babo IS_ME = new Babo();

    public static Babo getBabo(){
        return IS_ME;
    }

    //서비스 접근 API
    public static Babo getBabo(boolean flag){

        //추가 -  서비스인터페이스, 제공자 등록 API

        return flag ? new Babo() : new UnderBabo();
    }

    public static void main(String[] args) {
        Babo babo = new Babo("jun");

        Babo babo1 = Babo.withName("jun");

        Babo babo2 = Babo.getBabo();

        Babo babo3 = Babo.getBabo(false); // 하위 타입 얻음

        EnumSet<Color> colors = EnumSet.allOf(Color.class);

        //???? 머지
        EnumSet<Color> blueAndWhite = EnumSet.of(Color.BLUE, Color.WHITE);
    }

    //Collections

    //하위 타입
    static class UnderBabo extends Babo{
    }

    enum Color {
        RED, BLUE, WHITE
    }

    //java.util.ServiceLoader;
}
