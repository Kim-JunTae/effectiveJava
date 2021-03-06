# 4장 클래스와 인터페이스
- 추상화의 기본 단위인 클래스와 인터페이스는 자바 언어의 심장   
  그래서 둘의 설계에 사용하는 강력한 요소가 많다.   
  이번 장에서는 이런 요소를 적절히 활용하여 쓰기 편하고, 견고하며, 유연하게 만드는 방법을 안내한다.
---
### Item15. 클래스와 멤버의 접근 권한을 최소화하라
- 기본 원칙
    - 모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다. -> SW가 올바르게 동작하는 한 항상 가장 낮은 접근 수준을 부여한다.
        - 톱레벨 클래스와 인터페이스 접근 수준 
            - package-private
            - public
        - 맴버(필드, 메서드, 중첩 클래스, 중첩 인터페이스) 접근 수준
            - private : 멤버를 선언한 톱레벨 클래스에서만 접근 가능
            - package-private : 멤버가 소속된 패키지 안의 모든 클래스에서 접근 가능   
                                (접근 제한자를 명시하지 않았을 때 default, 인터페이스 멤버는 public)
            - protected : package-private의 접근 범위를 포함, 이 멤버를 선언한 클래스의 하위 클래스에서도 접근 가능
            - public : 모든 곳에서 접근 가능
            
- public 클래스의 인스턴스 필드는 되도록 public이 아니어야 한다.(Item16)

- public 가변 필드를 갖는 클래스는 일반적으로 스레드 안전하지 않다.

```
//클래스에서 public static final 배열 필드를 두거나 이 필드를 반환하는 접근자 메서드를 제공해서는 안된다.   
//그렇게 되면 클라이언트에서 그 배열의 내용을 수정할 수 있게 된다.
//public static final Thing[] PRIVATE_VALUES = {notebook, electroBike};

//1번째 방법 : public 배열을 private으로 만들고, public 불변 리스트를 추가하는 것
private static final Thing[] PRIVATE_VALUES = {notebook, electroBike};
public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

//2번째 방법 : 배열을 private으로 만들고 그 복사본을 반환하는 public 메서드를 추가하는 방법(방어적 복사)
public static final Thing[] values(){
    return PRIVATE_VALUES.clone();
}
```

- 핵심 정리
    - 프로그램 요소의 접근성은 가능한 최소한으로 하라. 꼭 필요한 것만 골라 최소한의 public API를 설계하자.
      그 외에는 클래스, 인터페이스, 멤버가 의도치 않게 API로 공개되는 일이 없도록 해야 한다.   
      public 클래스는 상수용 public static final 필드 외에는 어떠한 public 필드도 가져서는 안된다.
      public static final 필드가 참조하는 객체가 불변인지 확인하라.   
      
---
### Item16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라
- 패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공
- But package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 하등의 문제가 없다.
- 규칙을 어긴 자바 플랫폼 라이브러리 사례
    -java.awt.package 패키지의 Point와 Dimension 클래스
     
- 핵심 정리
    - public 클래스는 절대 가변 필드를 직접 노출해서는 안된다.    
      불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다.   
      하지만 package-private 클래스나 private 중첩 클래스에서는    
      종종(불변이든 가변이든) 필드를 노출하는 편이 나을 때도 있다.
---
### Item17. 변경 가능성을 최소화하라
- 불변클래스 : 그 인스턴스의 내부 값을 수정할 수 없는 클래스
    - ex) String, 기본 타입의 박싱된 클래스들, BigInteger, BigDecimal

- 불변클래스 만드는 다섯가지 규칙
    - 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.(Setter 메서드)
    - 모든 필드를 final로 선언한다.
        - 시스템이 강제하는 수단을 이용해 설계자의 의도를 명확하게 드러내는 방법
        - 새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장하는데도 필요하다.
    - 모든 필드를 private으로 선언한다.
        - 필드가 참조하는 가변 객체를 클라이언트에서 직접 접근해 수정하는 일을 막아준다.
    - 클래스를 확장할 수 없도록 한다.
            1. final 선언
            2. 
    - 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.
        - 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 해야한다.
        - 이런 필드는 절대 클라이언트가 제공한 객체 참조를 가리키게 해서는 안되며, 접근자 메서드가 그 필드를 그대로 반환해서도 안된다.
        - 생성자, 접근자, readObject 메서드(Item88) 모두에서 방어적 복사를 수행하라.

- ex) 불변 복소수 클래스 Complex 
    - 사칙연산 메서드 : 인스턴스 자신을 수정하지 않고 새로운 인스턴스를 만들어 반환한다. -> 함수형 프로그래밍   
      -> 코드에서 불변이 되는 영역의 비율이 높아진다.
      
- 불변 객체는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.
- 객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다. 
- 불변 객체는 그 자체로 실패 원자성을 제공한다.(Item76)
    - 메서드에서 예외가 발생한 후에도 그 객체는 여전히 (메서드 호출 전과 똑같은) 유효한 상태여야 한다는 성질
- 불변클래스의 단점 : 값이 다르면 반드시 독립된 객체로 만들어야 한다.

- 정리
    - 클래스는 꼭 필요한 경우가 아니면 불변이어야 한다.
    - 불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자. 다른 합당한 이유가 없다면 모든 필드는 private final이어야 한다.
    - 생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.
    - java.util.concurrent 패키지의 CountDownLatch가 이상적인 원칙을 지키고 있다.  
---
### Item18. 상속보다는 컴포지션을 사용하라
- 상속 : 다른 패키지의 구체 클래스를 상속하는 일은 위험하다. 
    - 클래스가 다른 클래스를 확장하는 구현 상속 v (이 책에서의 상속)
    - 클래스가 인터페이스를 구현하거나 인터페이스가 다른 인터페이스를 확장하는 인터페이스 상속과는 무관하다.

- 메서드 호출과 달리 상속은 캡슐화를 깨뜨린다. (= 상위 클래스가 어떻게 구현되느냐에 따라 하위 클래스의 동작에 이상이 생길 수 있다.)

- 컴포지션 : 기존 클래스가 새로운 클래스의 구성요소로 쓰이는 것
     
- 핵심 정리
    - 상속은 강력하지만 캡슐화를 해친다는 문제가 있다. 상속은 상위 클래스와 하위 클래스가 순수한 is-a 관계일 때만 써야한다.   
    is-a 관계일 때도 안심할 수만은 없는 게, 하위 클래스의 패키지가 상위 클래스와 다르고,   
    상위 클래스가 확장을 고려해 설계되지 않았다면 여전히 문제가 될 수 있다.   
    상속의 취약점을 피하려면 상속 대신 컴포지션과 전달을 사용하자. 특히 래퍼 클래스로 구현할 적당한 인터페이스가 있다면 더욱 그렇다.   
    래퍼 클래스는 하위 클래스보다 견고하고 강력하다.

- [Link](https://www.geeksforgeeks.org/favoring-composition-over-inheritance-in-java-with-examples/)    
---
### Item19. 상속을 고려해 설계하고 문서화하라. 그러지 않다면 상속을 금지하라

- 핵심 정리
    - 상속용 클래스를 설계하기란 결코 만만치 않다. 클래스 내부에서 스스로를 어떻게 사용하는지(자기사용 패턴) 모두 문서로 남겨야 하며,   
    일단 문서화한 것은 그 클래스가 쓰이는 한 반드시 지켜야 한다. 그러지 않으면 그 내부 구현 방식을 믿고 활용하던 하위 클래스를 오동작하게 만들 수 있다.   
    다른 이가 효율 좋은 하위 클래스를 만들 수 있도록 일부 메서드를 protected로 제공해야 할 수도 있다. 그러니 클래스를 확장해야 할 명확한 이유가   
    떠오르지 않으면 상속을 금지하는 편이 나을 것이다. 상속을 금지하려면 클래스를 final로 선언하거나 생성자 모두를 외부에서 접근할 수 없도록 만들면 된다.
---
### Item20. 추상 클래스보다는 인터페이스를 우선하라

- 핵심 정리
    - 일반적으로 다중 구현 타입으로는 인터페이스가 가장 적합하다. 복잡한 인터페이스라면 구현하는 수고를 덜어주는 골격 구현을 함께 제공하는 방법을 꼭 고려해보자.   
      골격 구현은 '가능한 한' 인터페이스의 디폴트 메서드로 제공하여 그 인터페이스를 구현한 모든 곳에서 활용하는 것이 좋다.   
      '가능한 한'이라고 한 이유는, 인터페이스에 걸려있는 구현상의 제약 때문에 골격 구현을 추상 클래스로 제공하는 경우가 더 흔하기 때문이다.
---
### Item21. 인터페이스는 구현하는 쪽을 생각해 설계하라
- 디폴트 메서드 잘 설계해서 만들자.

- 핵심 정리

---
### Item22. 인터페이스는 타입을 정의하는 용도로만 사용하라

- 핵심 정리
    - 인터페이스는 타입을 정의하는 용도로만 사용해야 한다.   
      상수 공개용 수단으로 사용하지 말자.
---
### Item23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라

- 핵심 정리
    - 태그 달린 클래스를 써야 하는 상황은 거의 없다. 새로운 클래스를 작성하는데 태그 필드가 등장한다면 태그를 없애고 계층구조로 대체하는 방법을 생각해보자.   
      기존 클래스가 태그 필드를 사용하고 있다면 계층 구조로 리팩터링하는 걸 고민해보자.
---
### Item24. 멤버 클래스는 되도록 static으로 만들라

- 핵심 정리
    - 중첩 클래스에는 네 가지가 있으며, 각각의 쓰임이 다르다. 메서드 밖에서도 사용해야 하거나 메서드 안에 정의하기엔 너무 길다면 멤버 클래스로 만든다.
      멤버 클래스의 인스턴스 각각이 바깥 인스턴스를 참조한다면 비정적으로, 그렇지 않으면 정적으로 만들자. 중첩 클래스가 한 메서드 안에서만 쓰이면서, 그 인스턴스를 생성하는 지점이 단 한 곳이고
      해당 타입으로 쓰기에 적합한 클래스나 인터페이스가 이미 있다면 익명 클래스로 만들고, 그렇지 않으면 지역 클래스로 만들자.

---
### Item25. 톱레벨 클래스는 한 파일에 하나만 담으라

- 핵심 정리
    - 교훈은 명확하다. 소스 파일 하나에는 반드시 톱레벨 클래스(혹은 톱레벨 인터페이스)를 하나만 담자. 이 규칙만 따른다면 컴파일러가 한 클래스에 대한 정의를 여러개 만들어 내는 일은 사라진다.   
      소스 파일을 어떤 순서로 컴파일하든 바이너리 파일이나 프로그램의 동작이 달라지는 일은 결코 일어나지 않을 것이다.