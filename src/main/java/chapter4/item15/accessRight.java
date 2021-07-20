package chapter4.item15;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class accessRight {

    private static Thing notebook;
    private static Thing electroBike;

    static class Thing{
        private String name;
        private int prices;

        public Thing(String name, int prices) {
            this.name = name;
            this.prices = prices;
        }
    }
    //클래스에서 public static final 배열 필드를 두거나 이 필드를 반환하는 접근자 메서드를 제공해서는 안된다.
    //그렇게 되면 클라이언트에서 그 배열의 내용을 수정할 수 잇게 된다.
    //public static final Thing[] PRIVATE_VALUES = {notebook, electroBike};

    //1번째 방법 : public 배열을 private으로 만들고, public 불변 리스트를 추가하는 것
    private static final Thing[] PRIVATE_VALUES = {notebook, electroBike};
    public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

    //2번째 방법 : 배열을 private으로 만들고 그 복사본을 반환하는 public 메서드를 추가하는 방법(방어적 복사)
    public static final Thing[] values(){
        return PRIVATE_VALUES.clone();
    }

}
