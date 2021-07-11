package chapter2.item3;

import chapter2.item3.enumtype.Elvis_enumType;
import chapter2.item3.field.Elvis_field;
import chapter2.item3.staticfactory.Elvis_staticFactory;

public class SingletonTest {
    public static void main(String[] args) {
        //1. public static final 필드 방식의 싱글턴
        Elvis_field field = Elvis_field.INSTANCE;

        //2. 정적 팩터리 방식의 싱글턴
        Elvis_staticFactory staticFactory = Elvis_staticFactory.getInstance();

        //3. EnumType
        Elvis_enumType enumType = Elvis_enumType.INSTANCE;
    }
}
