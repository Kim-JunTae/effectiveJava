package chapter2.item3.enumtype;

// 열거 타입 방식의 싱글턴 - 바람직한 방법 (25쪽)
public enum Elvis_enumType {
    INSTANCE;

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    // 이 메서드는 보통 클래스 바깥(다른 클래스)에 작성해야 한다!
    public static void main(String[] args) {
        Elvis_enumType elvisEnumType = Elvis_enumType.INSTANCE;
        elvisEnumType.leaveTheBuilding();
    }
}
