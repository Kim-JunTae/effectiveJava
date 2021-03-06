# 9장 일반적인 프로그래밍 원칙
- 자바 언어의 핵심 요소에 집중하는 장
    - 지역변수 : '블록' 내에서 선언된 변수, 선언된 블록 내에서만 유효하며 블록이 종료되면 메모리(stack 영역)에서 사라집니다.
    - 제어구조 : 프로그램의 제어구조를 말하는 듯?
        - 기본적인 제어구조 : 순차구조, 선택구조(if else), 반복구조(for, while), 건너뜀 구조(break,  continue, goto)
    - 라이브러리 : 공통으로 사용될 수 있는 특정한 기능들을 모듈화한 것, 대상 환경에서 바로 실행될 수 있는 형태로 제공, 실제 실행되어 기능을 수행하는 단편화된 프로그램
        - API : 프로그래밍 언어에서 라이브러리를 사용할 수 있도록 소스코드 수준에서 인터페이스를 노출시킨 것이 API
    - 데이터 타입 : 프로그래밍 언어에서 사용할 수 있는 데이터(숫자, 문자열, Boolean 등)
        - ex) 자바스크립트
        - 원시타입 : boolean, null, undefined, number, string, symbol
        - 객체타입 : object
    - 리플렉션 : 구체적인 클래스 타입을 알지 못해도, 그 클래스의 메소드, 타입, 변수들을 접근할 수 있도록 해주는 자바 API
        - [자바 리플렉션이란?](https://dublin-java.tistory.com/53)
        - [Java 리플렉션](https://velog.io/@ptm0304/Java-%EC%9E%90%EB%B0%94-%EB%A6%AC%ED%94%8C%EB%A0%89%EC%85%98)
    - 네이티브 메서드 :
    - 최적화 : 
    - 명명 규칙 :
    
---
### Item57. 지역변수의 범위를 최소화하라
- 핵심정리
    1. 가장 처음 쓰일 때 선언하기
    2. 선언과 동시에 초기화(try-catch문에서는 예외)
    3. 반복문을 활용하자(for문 vs while문, 변수 범위를 최소화)
    4. 메서드를 작게 유지하고 한 가지 기능에 집중(기능별로 쪼개기)
    
---
### Item58. 전통적인 for문 보다는 for-each문을 사용하라
- for-each문을 사용할 수 없는 상황
    - 파괴적인 필터링(destructive filtering)
        - 컬렉션을 순회하면서 선택된 원소를 제거해야한다면 반복자의 remove 메서드를 호출해야 한다.
        - 자바 8부터는 Collection의 removeIf 메서드를 사용해 컬렉션을 명시적으로 순회하는 일을 피할 수 있다.
    - 변형(transforming)
        - 리스트나 배열을 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 한다면 리스트의 반복자나 배열의 인덱스를 사용해야 한다.
    - 병렬 반복(parallel iteration)
        - 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해 엄격하고 명시적으로 제어해야 한다.    
    
- 핵심정리
    - 전통적인 for문과 비교했을 때 for-each문은 명료하고, 유연하고, 버그를 예방해준다.   
      성능저하도 없다. 가능한 모든 곳에서 for-each를 사용하자.

---
### Item59. 라이브러리를 익히고 사용하라
- 예시 : random 메서드
    - 자바 7부터는 ThreadLocalRandom으로 대체하면 대부분 잘 작동한다.(3.6배 빠름)
    - 포크-조인 풀이나 병렬 스트림에서는 SplittableRandom을 사용하라.
- 자바 프로그래머라면 적어도 java.lang, java.util, java.io와 그 하위 패키지들에는 익숙해져야 한다.
    
- 핵심정리
    - 아주 특별한 나만의 기능이 아니라면 누군가 이미 라이브러리로 구현한 것을 사용하자.   
      일반적으로 라이브러리의 코드는 품질이 좋고, 점차 개선될 가능성이 크다.
      -> 규모의 경제

---
### Item60. 정확한 답이 필요하다면 float와 double은 피하라
- float와 double은 과학과 공학 계산용으로 설계되어, 이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 '근사치'로 계산하도록 세심하게 설계되었다.
  그러므로 정확한 결과를 필요할 때에는 사용하면 안된다.
- 정확한 계산(금융계산)에는 BigDecimal, int 혹은 long을 사용해야한다.
  
- 핵심정리
    - 정확한 답이 필요한 계산에는 float나 double을 피하라.   
      소수점 추적은 시스템에 맡기고, 코딩 시 불편함이나 성능 저하를 신경쓰지 않겠다면 BigDecimal을 사용하라.
      BigDecimal이 제공하는 어덟가지 반올림 모드를 이용하자. 법으로 정해진 반올림을 수행해야하는 비즈니스 계산에서 편리하다.
    - 성능이 중요하고 소수점을 직접 추적할 수 있고, 숫자가 너무 크지 않다면 int나 long을 사용하라.   
      숫자를 아홈자리 십진수 -> int, 열여덟자리 십진수 -> long, 열여덟자리 넘어가면 BigDecimal을 사용하자.
    
---
### Item61. 박싱된 기본 타입보다는 기본 타입을 사용하라
- 기본타입과 박싱된 기본타입의 주된 차이
    - 기본 타입은 값만 가지고 있으나, 박싱된 기본타입은 값에 더해 식별성이란 속성을 가진다.
      박싱된 기본 타입의 두 인스턴스는 값이 값아도 서로 다르다고 식별될 수 있다.
    - 기본 타입의 값은 언제나 유효하나, 박싱된 기본타입은 유효하지 않은 값, 즉 null을 가질 수 있다.
    - 기본 타입이 박싱된 기본 타입보다 시간과 메모리 사용면에서 더 효율적이다.
 
- 박싱된 기본 타입에 == 연산자를 사용하면 오류가 일어난다. 같은 객체를 비교하는게 아니기 때문이다.
  == 연산자가 아닌 Comparator.naturalOrder()을 사용하자.

- 기본 타입과 박싱된 기본 타입을 혼용한 연산에서는 박싱된 기본 타입의 박싱이 자동으로 풀린다.

- 박싱된 기본 타입의 사용 시기
    - 컬렉션의 원소, 키, 값으로 사용할 때
    - 매개변수화 타입이나 매개변수화 메서드의 타입 매개변수로 사용할 때
    - 리플렉션을 통해 메서드를 호출할 때
     
- 핵심정리
    - 오토박싱이 박싱된 기본타입을 사용할 때의 번거로움을 줄여주지만, 그 위험까지 없애주지는 않는다.
      언박싱 과정에서 NPE를 던질 수 있다.
    
---
### Item62. 다른 타입이 적절하다면 문자열 사용을 피하라
- 문자열은 다른 값 타입을 대신하기에 적합하지 않다. (받은 데이터가 수치형이라면 적당한 수치 타입~)
- 문자열은 열거 타입을 대신하기에 적합하지 않다. (item34)
- 문자열은 혼합 타입을 대신하기에 적합하지 않다. (여러 요소가 혼합된 데이터를 하나의 문자열을 하나의 문자열로 표현하는 것은 대체로 좋지 않은 생각이다. item24)
- 문자열은 권한을 표현하기에 적합하지 않다. (Key 클래스)

- 핵심정리
    - 더 적합한 데이터 타입이 있거나 새로 작성할 수 있다면, 문자열 사용 X

---
### Item63. 문자열 연결은 느리니 주의하라
- 문자열 연결 연산자(+)로 문자열 n개를 잇는 시간은 n의 2승에 비례한다. (문자열은 불변이라서 그렇다. item17)
- 성능을 포기하고 싶지 않다면 String 대신 StringBuilder를 사용하자.
- 핵심정리
    - 성능에 신경써야 한다면, 많은 문자열을 연결할 때는 문자열 연결 연산자(+)를 피하자. 대신 StringBuilder의 append 메서드를 사용하라.   
      문자 배열을 사용하거나, 문자열을 연결하지않고 하나씩 처리하는 방법도 있다. 
    
---
### Item64. 객체는 인터페이스를 사용해 참조하라
- item51 : 매개변수 타입으로 클래스가 아니라 인터페이스를 사용하라.의 확장
- 적합한 인터페이스만 있다면 매개변수뿐만 아니라 반환값, 변수, 필드를 전부 인터페이스 타입으로 선언하라.
- 인터페이스를 타입으로 사용하는 습관을 길러두면 프로그램이 훨씬 유연해질 것이다.
- 적합한 인터페이스가 없다면 당연히 클래스로 참조해야한다.(String, BigInteger)
- 적합한 인터페이스가 없다면 클래스의 계층구조 중 필요한 기능을 만족하는 가장 덜 구체적인(상위의) 클래스를 타입으로 사용하자.
        
---
### Item65. 리플렉션보다는 인터페이스를 사용하라
- 리플렉션 기능(java.lang.reflect)을 이용하면 프로그램에서 임의의 클래스에 접근할 수 있다.
    - Class 객체가 주어지면, 그 클래스의 생성자, 메서드, 필드 인스턴스를 가져올 수 있고
      인스턴스를 통해 멤버 이름, 필드타입, 메서드 시그니처 등을 가져올 수 있다.
      
- 단점
    - 컴파일타임 타입 검사가 주는 이점을 하나도 누릴 수 없다. 예외검사도 마찬가지.      
    - 코드가 지저분해지고 장황해진다.
    - 성능이 떨어진다.

- 리플렉션은 아주 제한된 형태로만 사용해야 그 단점을 피하고 이점을 취할 수 있다.
- 리플렉션은 인스턴스 생성에만 쓰고, 이렇게 만든 인스턴스는 인터페이스나 상위 클래스로 참조해 사용하자.
    
---
### Item66. 네이티브 메서드는 신중히 사용하라
- 자바 네이티브 인터페이스(JNI)는 자바 프로그램이 네이티브 메서드를 호출하는 기술이다.
    - 네이티브 메서드란 C나 C++ 같은 네이티브 프로그래밍 언어로 작성한 메서드를 말한다.
    - 전통적으로 네이티브 메서드의 주요 쓰임은 세가지이다.
        - 레지스트리 같은 플랫폼 특화 기능
        - 네이티브 코드로 작성된 기존 라이브러리 사용
        - 성능 개선을 목적으로 성능에 결정적인 영향을 주는 영역만 네이티브 언어로 작성

- 성능을 개선할 목적으로 네이티브 메서드를 사용하는 것은 거의 권장하지 않는다.        
- 단점
    - 네이티브 언어가 안전하지 않으므로(item50) 네이티브 메서드를 사용하는 애플리케이션도 메모리 훼손 오류로부터 더이상 안전하지 않다.
    - 다양한 단점들(이식성, 디버깅, 메모리, 추적)
        
---
### Item67. 최적화는 신중히 하라
- 빠른 프로그램보다는 좋은 프로그램을 작성하라.
- 성능을 제한하는 설계를 피하라.
- API를 설계할 때 성능에 주는 영향을 고려하라.

- 핵심정리
    - 빠른 프로그램을 작성하려 안달하지 말자. 좋은 프로그램을 작성하다 보면 성능은 따라오기 마련이다.
    - 하지만 시스템을 설계할 때, 특히 API, 네트워크 프로토콜, 영구 저장용 데이터 포맷을 설계할 때는 성능을 염두하자.
    - 충분히 빠르면 OK, 그렇지 않다면 프로파일러를 사용하여 문제의 원인을 찾아 최적화를 수행하자.
    
    
---
### Item68. 일반적으로 토용되는 명명 규칙을 따르라
- 핵심정리
    - 표준 명명 규칙 사용
    - 철자 규칙은 직관적, 문법 규칙은 복잡하고 느슨하다.