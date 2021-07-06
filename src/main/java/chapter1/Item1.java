package chapter1;

/**
 * 아이템 1 : 생성자 대신 정적 팩토리 메서드를 고려하라
 */
public class Item1 {
    /**
     * 클라이언트가 클래스의 인스턴스를 얻는 전통적인 수단은 public 생성자
     * + 클래스는 생성자와 별도로 정적 팩토리 메서드를 제공 -> 그 클래스의 인스턴스 타입을 반환하는 단순한 정적 메서드
     *
     */

    //예시1
    public static Boolean valueOf(boolean b){
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 정적 팩토리 메서드가 생성자 보다 좋은 장점 다섯가지
     * 1. 이름을 가질 수 있다.
     *      ex) 생성자 : BigInteger(int, int, Random) vs 정적 팩토리 메서드 BigInteger.probablePrime
     *          '값이 소수인 BigInteger를 반환한다'의 의미를 더 잘 설명하는 것은?
     *          한 클래스에 시그니처가 같은 생성자가 여러 개 필요할 것 같으면, 생성자를 정적 패터리 메서드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주자
     *
     * 2. 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.
     *      ex) 
     */

}
