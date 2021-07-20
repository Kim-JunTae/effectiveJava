# 4장 클래스와 인터페이스
- 추상화의 기본 단위인 클래스와 인터페이스는 자바 언어의 심장   
  그래서 둘의 설계에 사용하는 강력한 요소가 많다.   
  이번 장에서는 이런 요소를 적절히 활용하여 쓰기 편하고, 견고하며, 유연하게 만드는 방법을 안내한다.
---
### Item15. 클래스와 멤버의 접근 권한을 최소화하라
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

- 핵심 정리

---
### Item18. 상속보다는 컴포지션을 사용하라

- 핵심 정리

---
### Item19. 상속을 고려해 설계하고 문서화하라. 그러지 않다면 상속을 금지하라

- 핵심 정리

---
### Item20. 추상 클래스보다는 인터페이스르 우선하라

- 핵심 정리

---
### Item21. 인터페이스는 구현하는 쪽을 생각해 설계하라

- 핵심 정리

---
### Item22. 인터페이스는 타입을 정의하는 용도로만 사용하라

- 핵심 정리

---
### Item23. 태그 달린 클래스보다는 클래스 계층구조를 활용하라

- 핵심 정리

---
### Item24. 멤버 클래스는 되도록 static으로 만들라

- 핵심 정리

---
### Item25. 톱레벨 클래스는 한 파일에 하나만 담으라

- 핵심 정리
