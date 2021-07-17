# 3장 모든 객체의 공통 메서드
- Object는 객체를 만들 수 있는 구체 클래스이지만 기본적으로는 상속해서 사용하도록 설계되었다.   
  Object에서 final이 아닌 메서드(equals, hashCode, toString, clone, finalize)는   
  모두 재정의(overriding)를 염두해 두고 설계된 것이라 재정의 시 지켜야 하는 일반 규약이 명확히 정의되어 있다.
- final이 아닌 Object 메서드들을 언제 어떻게 재정의해야 하는지를 다룬다.
- Comparable.compareTo의 경우 성격이 비슷하여 같이 다룬다.
  
---
### Item10. equals는 일반 규약을 지켜 재정의하라
- equals 메서드는 재정의하기에 까다롭다.(곳곳에 함정이!)

- equals를 재정의하지 않는 것이 최선인 경우
    1. 각 인스턴스가 본질적으로 고유하다.
        - 값 표현 X, 동작하는 개체를 표현하는 클래스
        - ex) Thread가 좋은 예, Object의 equals 메서드는 이러한 클래스에 딱 맞게 구현되었다.
        
    2. 인스턴스의 '논리적 동치성(logical equality)'을 검사할 일이 없다.
        - ex) java.util.regex.Pattern은 equals를 재정의해서 두 pattern의 인스턴스가 같은 정규표현식을 나타내는지를 검사   
              즉, 논리적 동치성을 검사하는 방식
        - 설계자가 클라이언트에서 이 방식을 원하지 않거나 애초에 필요하지 않다고 판단할 수 있다.
            
    3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.
        - ex) 대부분의 Set 구현체는 AbstractSet이 구현한 eqauls를 상속받아 쓰고,   
              List 구현체들은 AbstractList로부터, Map 구현체들은 AbstractMap으로 부터 상속받아 그대로 쓴다.
              
    4. 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다.
        - 철저하게 위험을 회피하기 위해서 equals가 호출되지 않도록 구현하는 방법
        ```
           @Override public boolean equals(Object o) {
               throw new AssertionError(); //호출 금지!
           }   
        ```

- equals를 재정의해야 할 때
    - 객체 식별성(object identity; 두 객체가 물리적으로 같은가)이 아니라 논리적 동치성을 확인해야 하는데,   
      상위 클래스의 equals가  논리적 동치성을 비교하도록 재정의되지 않았을 때다.
      
    - 주로 값 클래스들이 이에 해당한다. - Integer, String 같은 값을 표현하는 클래스들
    - 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스(Item1)나,    
      Enum(Item34)의 경우 재정의하지 않아도 된다.
    - equals 메서드를 재정의할 때는 반드시 일반 규약을 따라야 한다.   
        
        - Object 명세에 적힌 규약
            - equals 메서드는 동치관계(equivalence relation)를 구현하며, 다음을 만족한다.
            1. 반사성(reflexivity) : null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다.
            2. 대칭성(symmetry) : null이 아닌 모든 참조값 x,y에 대해, x.equals(y)가 true면 y.equals(x)도 true다.
            3. 추이성(transitivity) : null이 아닌 모든 참조값 x,y,z에 대해   
               x.equals(y)각 true이고, y.equals(z)도 true면 x.equals(z)도 true이다.
            4. 일관성(consistency) : null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면   
               항상 true를 반환하거나 항상 false를 반환한다.
            5. null-아님 : null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.
        
        - Object 명세에서 말하는 동치 관계 : 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산
        1. 반사성 : 객체는 자기 자신과 같아야 한다.
        2. 대칭성 : 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다.
           ```java
              import java.util.Objects;//잘못된 코드 - 대칭성 위배
              public final class CaseInsensitiveString {
                   private final String s;
                   
                   public CaseInsensitiveString(String s) {
                       this.s = Objects.requireNonNull(s);
                   }
                   
                   //대칭성 위배!
                   @Override public boolean equals(Object o){
                       if(o instanceof CaseInsensitiveString)
                           return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
                       if(o instanceof String) //한 방향으로만 작동한다.
                           return s.equalsIgnoreCase((String) o);
                       return false;
                   }   
                   //... 생략
              }
           ```
           - 위의 예제코드에서 CaseInsensitiveString의 equals는 순진하게 일반 문자열과도 비교를 시도한다.
           - String의 equals는 일반 String만 알고 있기 때문에 둘의 equals 반환값이 다르다. -> 대칭성 위배
           
           - equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 없다.
           ```
               //해결책 : CaseInsensitiveString의 equals를 String과도 연동하겠다는 허황된 꿈을 버린다.
               @Override public boolean equals(Object o){
                    return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
               }
           ``` 
           
        3. 추이성 : 첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세 번째 객체도 같아야 한다는 뜻이다.
            - 상위 클래스에 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보자. equals 비교에 영향을 주는 정보를 추가한 것이다.
            - ex) Point.class, ColorPoint.class, SmellPoint.class
            - 구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.(객체 지향적 추상화의 이점을 포기하지 않는 한)
            - equals 안의 instanceof 검사를 getClass 검사로 바꾸면 가능할까? -> 리스코프 치환 원칙 위배
                - 같은 구현 클래스의 객체와 비교할 때만 true 반환
                - ex) Point.class, CounterPoint.class, Date.class, Timestamp.class
                -> 실수
        
        4. 일관성 : 두 객체가 같다면 앞으로도 영원히 같아야 한다.
            - 클래스가 불변이든 가변이든 equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.
            - ex) java.net.URL의 equals는 주어진 URL과 매핑된 호스트의 IP 주소를 이용해 비교한다.
                  호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데 그 결과가 항상 같다고 보장할 수 없다. -> 실수
            - 해결책 : equals는 항시 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다.
        
        5. 'null 아님' : 모든 객체가 null과 같지 않아야 한다.
        
    - 정리 - 단계별 양질의 equals 메서드 구현 방법
        1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
        2. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
        3. 입력을 올바른 타입으로 형 변환한다. (2번에서 했음)
        4. 입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다.
        5. 모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환한다.
    
        - 추가적인 내용
            - float와 double을 제외한 기본 타입 필드는 == 연산자로 비교   
              참조 타입 필드는 각각 equals 메서드로   
              float와 double 필드는 각각 정적 메서드인 Float.compare(float, float)와 Double.compare(double, double)로 비교
            
            - 때론 null도 정상 값으로 취급하는 참조 타입 필드도 있다.    
              이런 필드는 정적 메서드인 Objects.equals(Object, Object)로 비교해 NullPointException 발생을 예방하자.
            
            - 비교하기 복잡한 필드를 가진 클래스도 있다.(앞에서 만든 CaseInsensitiveString.class)   
              그 필드의 표준형을 저장해둔 후 표준형끼리 비교하면 훨씬 경제적이다. 불변클래스에 적합하며, 가변객체라면 값이 바뀔때마다 표준형을 최신상태로 갱신해야한다.
              
            - 어떤 필드를 먼저 비교하느냐가 성능을 좌우하기도 한다. 
            
    - 재점검용 질문 : 대칭적인가? 추이성이 있는가? 일관적인가?    
    
    - 주의사항
        1. equals를 재정의할 땐 hashCode도 반드시 재정의하자(Item11)
        2. 너무 복잡하게 해결하려 들지 말자.
        3. Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.
    
    - AutoValue 프레임워크 : equals, hashCode를 작성하고 테스트하는 일을 대신해주는 오픈소스
        
    - 핵심정리
        - 꼭 필요한 경우가 아니면 equals를 재정의하지 말자. 많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다.   
          재정의해야할 때는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.
        
---
### Item11. equals를 재정의하려거든 hashCode도 재정의하라

---
### Item12. toString을 항상 재정의하라

---
### Item13. clone 재정의는 주의해서 진행하라

---
### Item14. Comparable을 구현할지 고려하라