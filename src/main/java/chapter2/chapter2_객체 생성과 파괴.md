# 2장 객체 생성과 파괴
- 객체를 만들어야 할 때와 만들지 말아야 할 때를 구분하는 법
- 올바른 객체 생성 방법과 불필요한 생성을 피하는 방법
- 제때 파괴됨을 보장하고 파괴 전에 수행해야 할 정리 작업을 관리하는 요령
---
### Item1. 생성자 대신 정적 팩터리 메서드를 고려하라
- 클라이언트가 클래스의 인스턴스를 얻는 방법
    1. public 생성자
    2. 정적 팩터리 메서드 (static factory method)
- 아래와 같은 장단점을 확인하여 인스턴스 생성 시 정적 팩터리 메서드를 고려해보자.
- 장점
    1. 이름을 가질 수 있다.
    2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
        - 불변 클래스(item17)는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 피할 수 있다.
        - 플라이웨이트 패턴(Flyweight pattern) : 비슷한 기법
        - 인스턴스 통제(instance-controlled) 클래스 : 반복되는 요청에 같은 객체를 반환하는 식으로 정적 팩터리 방식의 클래스는 언제 어느 인스턴스를 살아 있게 할지를 철저히 통제할 수 있다.
    
    3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
        - 유연성 : 구현클래스를 공개하지 않고도 그 객체를 반환할 수 있어 API를 작게 유지할 수 있다.
        - 예) java.util.Collections
        
    4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
        - 반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다. 
        - 예) EnumSet
        
    5. 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
        - 서비스 제공자 프레임워크(service provider framework)를 만드는 근간이 된다.
        - 대표적인 서비스 제공자 프레임워크 : JDBC(Java Database Connectivity)
        - 서비스 제공자 프레임워크 : 서비스의 구현체
            - 3개의 핵심 컴포넌트로 이뤄진다.
            1. 서비스 인터페이스(service interface) : 구현체의 동작을 정의
            2. 제공자 등록 API(provider registration API) : 제공자가 구현체를 등록할 때 사용
            3. 서비스 접근 API(service access API) : 클라이언트가 서비스의 인스턴트를 얻을 때 사용
            + 네번째 컴포넌트
                서비스 제공자 인터페이스(service provider interface) : 서비스 인터페이스의 인스턴스를 생성하는 팩터리 객체를 설명해준다.
        - 예) JDBC
            - Connection : 서비스 인터페이스
            - DriverManager.registerDriver : 제공자 등록 API
            - DriverManager.getConnection : 서비스 접근 API
            - Driver : 서비스 제공자 인터페이스
        - 심화학습 : 서비스 제공자 프레임워크 패턴의 변형
            1. 브리지 패턴(Bridge Pattern)
            2. 의존 객체 주입(dependency injection, 의존성 주입) 프레임워크 - Item5
            3. java.util.ServiceLoader (자바6) : 범용 서비스 제공자 프레임워크 : 직접 프레임워크 만들 필요가 없어짐

- 단점
    1. 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
    2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
        - 생성자처럼 API 설명에 명확히 들어나지 않으니 사용자는 정적 팩터리 메서드 방식 클래스를 인스턴스화할 방법을 알아내야 한다.
        - API 문서를 잘 써놓자.
        - 메서드 이름도 잘 알려진 규칙으로 사용하자.
            - from      : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
            - of        : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
            - valueOf   : from과 of의 더 자세한 버전
            - instance 혹은 getInstance   : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않는다.
            - create 혹은 newInstance     : instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환은 보장한다.
            - getType   : getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. "Type"은 팩터리 메서드가 반환할 객체의 타입이다.
            - newType   : newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. "Type"은 팩터리 클래스가 반환할 객체의 타입이다.
            - type      : getType과 newType의 간결한 버전 
    
- 핵심 정리
    - 정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하는 것이 좋다.    
      그렇다고 하더라도 정적 팩터리를 사용하는 게 유리한 경우가 더 많으므로   
      무작정 public 생성자를 제공하던 습관이 있다면 고치자.
---
### Item2. 생성자에 매개변수가 많다면 빌더를 고려하라
- static 팩터리 메서드와 public 생성자 모두 매개변수가 많을 때 불편해진다.
- 해결책1 : 점층적 생성자 패턴(telescoping constructor pattern) : 생성자를 늘려나가는 방식
    - 예) NutritionFacts 클래스
    - NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27); //매개변수 갯수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다.

- 해결책2 : 자바빈즈 패턴(JavaBeans pattern)
    - 매개변수가 없는 생성자로 객체를 만든 후, 세터(setter) 메서드를 호출해 원하는 매개변수의 값을 설정하는 방식
    
    - 단점
        1. 객체 하나를 만들려면 메서드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(Consistency)이 무너진 상태에 놓이게 된다.
        2. 불변 클래스(Item17)로 만들지 못한다.
        3. 스레드 안정성을 얻으려면 프로그래머가 추가 작업을 해줘야 한다. -> setter를 사용하기 때문
    
    - 단점을 완화하려면? Freeze : Object.freeze() : 객체를 read만 가능하게, write는 불가능함    
                                                 -> java에는 따로 메서드는 없고 구현해야한다.   
        [자바스크립트의 예시](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/freeze)
    
- 해결책3 : 빌더 패턴(Builder pattern)
    - 만들려는 객체를 바로 만들지 않고 클라이언트는 빌더에 필수적인 매개변수를 주면서 호출해 Builder 객체를 얻은 다음 
      빌더 객체가 제공하는 세터와 비슷한 메서드를 사용해서 부가적인 필드를 채워넣고   
      최종적으로 build 메서드를 호출해서 만들려는 객체를 생성한다.
    
    - 명명된 선택적 매개변수(named optional parameters)를 흉내낸 것 (파이썬, 스칼라)
    - 각각의 메서드에서 유효성 검사를 할 수 있다.
    - 잘못된 매개변수를 최대한 일찍 발견하려면 빌더의 생성자와 메서드에서 입력 매개변수를 검사하고
    - build 메서드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식을 검사하자.
    - 공격에 대비해 이런 불변식을 보장하려면 빌더로부터 매개변수를 복사한 후 해당 객체 필드들도 검사해야 한다.(Item50)
    - 검사해서 잘못된 점을 발견하면 어떤 매개변수가 잘못되었는지 자세히 알려주는 메시지를 담아 IllegalArgumentException을 던지면 된다.(Item75)
    
    - 빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기 좋다. hierarchical builder
        - 공변 변환 타이핑(covariant return typing) :  protected abstract T self();

    - [Lombok의 빌더](https://projectlombok.org/features/Builder)
        - self()와 같은 기능까지 제공하지는 않을 것 같다.
        
- 핵심 정리
    - 생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는 게 더 낫다.  
    매개변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그렇다.    
    빌더는 점층적인 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고, 자바빈즈보다 훨씬 안전하다.       
---
### Item3. private 생성자나 열거(Enum) 타입으로 싱글턴임을 보증하라
- 싱글턴(singleton) : 인스턴스를 오직 하나만 생성할 수 있는 클래스
    - 예) 함수(Item24)와 같은 무상태(stateless) 객체나 설계상 유일해야 하는 시스템 컴포넌트

- 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.

- 만드는 방식
    1. public static 멤버가 final 필드인 방식
        - 장점
            - 해당 클래스가 싱글턴임이 API에 명백히 드러난다.
            - 간결함
    2. 정적 팩터리 메서드를 public static 멤버로 제공
        - 장점
            - API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다.
            - 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다는 점(Item30)
            - 정적 팩터리의 메서드 참조를 공급자로 사용할 수 있다는 점(Item43, 44)
    
    * 1,2는 직렬화하려면 모든 인스턴스 필드를 일시적(transient)이라고 선언하고?     
      readResolve 메서드를 제공해야한다.(Item89)   
    -> 이렇게 하지 않으면 직렬화된 인스턴스가 역직렬화할 때마다 새로운 인스턴스가 만들어진다.
    
    3. 원소가 하나인 열거타입을 선언하는 것
        - public 필드 방식과 비슷하지만 더 간결하고, 추가 노력없이 직렬화할 수 있다.
        - 대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법
        - 단, 만들려는 싱글턴이 Enum외의 클래스를 상속해야 한다면 X   
        (열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.)
---        
### Item4. 인스턴스화를 막으려거든 private 생성자를 사용하라
- 단순히 정적 메서드와 정적 필드만을 담은 클래스를 만들고 싶을 때 -> 객체 지향적인 사고는 X, But 나름의 쓰임새가 있다.
    1. 기본타입 값이나 배열 관련 메서드들을 모음
    2. 특정 인터페이스를 구현하는 객체를 생성해주는 정적(혹은 팩터리) 메서드를 모음
    3. final 클래스와 관련한 메서드들을 모음

- 정적 멤버만 담은 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 게 아니다.   
  하지만 생성자를 명시하지 않으면 컴파일러가 자동으로 기본 생성자를 만들어준다.   
  즉, 매개변수를 받지 않는 public 생성자가 만들어진다.(사용자는 자동/의도적으로 만들어졌는지 알 수 없다.)
    
- 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.    
- 이러한 인스턴스화를 막는 방법
    - 컴파일러가 기본 생성자를 만드는 경우는 오직 명시된 생성자가 없을 때 뿐이니   
      private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.   
      명시적 생성자가 private이니 클래스 밖에서는 접근할 수 없다.

    - 꼭 Assertion Error를 던질 필요는 없지만, 클래스 안에서 실수라도 생성자를 호출하지 않도록 해준다.
    - 생성자가 존재하지만 호출하지 않는 이유에 대한 적절한 주석을 달아두도록 하자.
    - 생성자 호출이 불가능하므로 상속이 불가능하다.
    
---
### Item5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라
- 많은 클래스가 하나 이상의 자원에 의존한다.
- 예) SpellChecker - 사전(자원)에 의존하는 클래스

- 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.
- 클래스(SpellChecker)가 여러 자원 인스턴스를 지원해야 하며, 클라이언트가 원하는 자원(dictionary)을 사용해야 한다.   
    -> 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식 : 의존 객체 주입의 한 형태!!

- 의존 객체 주입은 생성자, 정적 팩터리(Item1), 빌더(Item2) 모두에 똑같이 응용할 수 있다.

- 변형으로 생성자에 자원 팩터리를 넘겨주는 방식이 있다.
    - 팩터리 메서드 패턴
    - 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.
    - 예) 자바 8에서 소개한 Supplier<T> 인터페이스

- 핵심정리
    - 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.     
    이 자원들을 클래스가 직접 만들게 해서도 안된다. 대신 필요한 자원을(혹은 그 자원을 만들어주는 팩터리를) 생성자에(혹은 정적 팩터리나 빌더에) 넘겨주자.   
    의존 객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선해준다.
---
### Item6. 불필요한 객체 생성을 피하라
- 똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다.   
  특히 불변 객체(Item17)는 언제든 재사용할 수 있다.

- 문자열 객체 생성
    - 하지 말아야 할 극단적인 예 : String s = new String("bikini"); 
    - 올바른 방법 : String s = "bikini";
    - 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용하여 같은 객체를 재사용하게 한다.

- static 팩토리 메서드 사용하기
    - 자바 9에서 deprecated된 Boolean(String) 대신 Boolean.valueOf(String) 팩터리 메서드(Item1)를 사용할 수 있다.
    - 생성자는 반드시 새로운 객체를 만들어야 하지만 팩터리 메서드는 그렇지 않다.

- 무거운 객체
    - 만드는데 메모리나 시간이 오래 걸리는 객체 == 비싼 객체를 반복적으로 만들어야 한다면 캐시해두고 재사용할 수 있는지 고려하는 것이 좋다.
    - 예) 정규표현식 Roman 숫자
        - 미리 필요한 정규표현식을 표현하는 Pattern 인스턴스를 클래스 초기화(정적 초기화) 과정에서    
          직접 생성해서 캐싱해두고 나중에 isRomanNumeral 메서드가 호출될 때마다 재사용한다.
        - isRomanNumeral 메서드가 호출되지 않을 때 문제가 있지만, PASS

- 어댑터(뷰)
    - 실제 작업은 뒷단 객체에 위임하고, 자신은 제2의 인터페이스 역할을 해주는 객체다. 뒷단 객체만 관리하면 된다.
      즉, 뒷단 객체 외에는 관리할 상태가 없으므로 뒷단 객체 하나당 한 개의 어댑터만 만들어지면 충분하다. 
    - 예) UsingKeySet
        - 호출할 때마다 새로운 객체가 나올 것 같지만 사실 같은 객체를 리턴하기 때문에 리턴 받은 Set 타입의 객체를 변경하면 그 뒤에 있는 Map 객체를 변경하게 된다.
        
- 오토박싱
    - 프리미티브 타입과 박스 타입을 섞어 쓸 수 있게 해주고 박싱과 언박싱을 자동으로 해준다.
    - 오토박싱은 프리미티브 타입과 박스 타입의 경계가 안보이게 해주지만 없애주는 것은 아니다.
    - 박싱된 기본 타입보다는 기본타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.
    
---
### Item7. 다 쓴 객체 참조를 해제하라
- 가비지 컬렉션 언어에서는 (의도치 않게 객체를 살려두는) 메모리 누수를 찾기가 아주 까다롭다.   
  객체 참조 하나를 살려두면 가비지 컬렉터는 그 객체뿐만 아니라 그 객체가 참조하는 모든 객체..)를 회수해가지 못한다.

- 해법
    1. 해당 참조를 다 썻을 때 null 처리(참조 해제)한다. -> 예외적인 경우일 때만 사용한다.
    2. 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 것

- 자기 메모리를 직접 관리하는 클래스라면 프로그래머는 항시 메모리 누수에 주의해야 한다.

- 캐시 역시 메모리 누수를 일으키는 주범이다.(WeakHashMap을 사용해 캐시를 만들자.)
 
- 핵심 정리
    메모리 누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다.    
    이런 누수는 철저한 코드 리뷰나 힙 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다.    
    그래서 이런 종류의 문제는 예방법을 익혀두는 것이 매우 중요하다.

---
### Item8. finalizer와 cleaner 사용을 피하라
- 자바는 두 가지 객체 소멸자를 제공한다.
    - finalizer : 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.
    - cleaner : finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불필요하다.

- finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.(즉시 수행된다는 보장이 없다.)
- 상태를 영구적으로 수정하는 작업에서는 절대  finalizer나 cleaner에 의존해서는 안 된다.

- 핵심 정리
    cleaner(자바 8까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이티브 자원 회수용으로만 사용하자.   
    물론 이런 경우라도 불확실성과 성능 저하에 주의해야 한다.
    
---
### Item9. try-finally 보다는 try-with-resources를 사용하라
- 자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야하는 자원이 많다.
    - InputStream, OutputStream, java.sql.Connection 등
    
- 전통적으로 자원이 제대로 닫힘을 보장하는 수단으로 try - finally가 쓰였다.   
  예외가 발생하거나 메서드에서 반환되는 경우를 포함해서 말이다.
  
- 핵심 정리
    꼭 회수해야 하는 자원을 다룰 때는 try-finally말고, try-with-resources를 사용하자.   
    예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. try-finally로 작성하면   
    실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다.
