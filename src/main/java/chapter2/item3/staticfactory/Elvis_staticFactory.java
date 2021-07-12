package chapter2.item3.staticfactory;

// 코드 3-2 정적 팩터리 방식의 싱글턴 (24쪽)
public class Elvis_staticFactory {
    private static final Elvis_staticFactory INSTANCE = new Elvis_staticFactory();

    private Elvis_staticFactory() { }

    public static Elvis_staticFactory getInstance() { return new Elvis_staticFactory(); }

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }

    // 이 메서드는 보통 클래스 바깥(다른 클래스)에 작성해야 한다!
    public static void main(String[] args) {
        Elvis_staticFactory elvisStaticFactory = Elvis_staticFactory.getInstance();
        elvisStaticFactory.leaveTheBuilding();
    }

    //싱글턴임을 보장해주는 readResolve 메서드
    private Object readResolve(){
        //'진짜' Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡긴다.
        return INSTANCE;
    }
}
