#10장 예외
- 예외를 제대로 활용한다면 프로그램의 가독성, 신뢰성, 유지보수성이 높아진다.

---
### Item69. 예외는 진짜 예외 상황에만 사용하라
- 예외는 오직 예외 상황에서만 쓰자. 절대 일상적인 제어 흐름용으로 쓰지말자.
    - 표준적이고 쉽게 이해되는 관용구 사용
    - 성능 개선을 목적으로 과하게 머리를 쓴 기법 자제
    - 잘못된 추론을 근거로 성능을 높이려 한 예시 (2배 차이)
    ```jsx
    //코드 69-1 예외를 완전히 잘못 사용한 예 - 따라하지 말 것!
    try {
    	int i = 0;
    	while(true)
    		range[i++].climb();
    } catch(ArrayIndexOutOfBoundsException e) {
    }

    // -> 표준적인 관용구
    for(Mountain m : range)
    	m.climb();
    ```

    1. JVM 구현자 입장에서는 명확한 검사만큼 빠르게 만들어야 할 동기가 약하다(최적화 가능성 낮음)
    2. 코드를 try-catch 블록 안에 넣으면 JVM이 적용할 수 있는 최적화가 제한
    3. 배열을 순회하는 표준 관용구는 앞서 걱정한 중복 검사를 수행하지 X, 
       JVM이 알아서 최적화하여 없애준다.
    
- 잘 설계된 API라면 클라이언트가 정상적인 제어 흐름에서 예외를 사용할 일이 X
    - 특정 상태에서만 호출할 수 있는 
      '상태 의존적' 메서드(next)를 제공하는 클래스(Iterator 인터페이스)는
      '상태 검사' 메서드(hasNext)도 함께 제공해야 한다.
    - 상태 검사 메서드 대신 사용할 수 있는 선택지도 있다.
        - 올바르지 않은 상태일 때 빈 Optional(Item55) 혹은 null 같은 특수한 값을 반환하는 방법
        - 3가지 방법(상태 검사 메서드, Optional, 특정 값) 중 하나를 선택하는 지침들
            - Optional, 특정값
                1. 외부 동기화 없이 여러 스레드가 동시 접근 가능 또는 외부 요인으로 상태 변경 가능 시
                2. 성능이 중요한 상황에서 상태 검사 메서드가 상태 의존적 메서드의 작업 일부를 중복 수행 시
            - 상태 검사 메서드
                1. 그 외의 경우는 상태 검사 메서드가 조금 더 낫다. 가독성, 잘못 사용 시 발견이 쉽다. 상태 검사 메서드 호출을 잊었다면, 상태 의존적 메서드가 예외를 예외를 던져 버그를 확실히 드러낼 것이다.
                2. 반면에 특정 값은 검사하지 않고 지나쳐도 발견하기가 어렵다.
                (Optional은 해당하지 않는다.)

- 핵심 정리
    - 예외는 예외 상황에서 쓸 의도로 설계
    - 정상적 제어 흐름에서는 사용 X
    - 이를 프로그래머에게 강요하는 API를 만들어서는 안 된다.

---
### Item70. 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라 
- 자바의 문제 상황을 알리는 타입(Throwable)
    - 검사 예외
    - 런타임 예외
    - 에러
    - [Java | 예외 처리의 3가지 방식](https://zuyo.tistory.com/895)
    - [자바의 예외의 종류 3가지](https://velog.io/@jsj3282/%EC%9E%90%EB%B0%94%EC%9D%98-%EC%98%88%EC%99%B8%EC%9D%98-%EC%A2%85%EB%A5%98-3%EA%B0%80%EC%A7%80)

- 호출하는 쪽에서 복구하리라 여겨지는 상황이면 검사 예외를 사용하라
    - 검사와 비검사 예외를 구분하는 기본 규칙
    - 검사 예외를 던지면 호출자가 그 예외를 catch로 잡아 처리하거나 더 바깥으로 전파하도록 강제하게 된다.
    - 비검사 Throwable : 런타인 예외와 에러

- 프로그래밍 오류를 나타낼 때는 런타임 예외를 사용하자
    - 대부분 전제조건 위배(API 명세에 기록된 제약을 지키지 않음)로 인한 발생
    - 복구가능하다면 검사예외, 그렇지 않다면 런타임 예외
    - 확신하기 어렵다면 비검사 예외

- 구현하는 비검사 throwable은 모두 RuntimeException의 하위 클래스여야 한다.
    - Error는 상속 X, throw 문으로 직접 던지는 일도 없어야 한다.(AssertionError 예외)

- Exception, RuntimeException, Error를 상속하지 않는 throwable은 만들 수 있지만 X

- 핵심 정리
    - 복구할 수 있는 상황이면 → 검사 예외
    - 프로그래밍 오류라면 → 비검사 예외
    - 확실하지 않다면 → 비검사 예외
    - throwable은 정의 X
    - **검사 예외라면 복구에 필요한 정보를 알려주는 메서드도 제공하자!**

---
### Item71. 필요없는 검사 예외 사용은 피하라 
- 결과를 코드로 반환하거나 비검사 예외를 던지는 것과 달리
  검사 예외는 발생한 문제를 프로그래머가 처리하여 안전성을 높이게끔 해준다.
- 검사 예외를 던지는 메서드는 스트림 안에서 직접 사용할 수 없기 때문에(Item 45~48)
  자바 8부터는 부담이 더욱 커졌다.
    - 무슨 얘기였는지 다시 확인
- 검사 예외를 회피하는 가장 쉬운 방법은 적절한 결과 타입을 담은 옵셔널을 반환하는 것이다.(Item 55) 검사 예외를 던지는 대신 단순히 빈 옵셔널을 반환하면 된다. 단점으로는 예외가 발생한 이유를 알려주는 부가 정보를 담을 수 없다는 것이다.
- 다른 방법으로는 검사 예외를 던지는 메서드를 2개로 쪼개 비검사 예외로 바꿀 수 있다.
  "아름답진 않지만 유연하다"라고 설명하고 있다.
    ```jsx
    //검사 예외를 던지는 메서드 - 리팩터링 전
    try {
    	obj.action(args);
    } catch (TheCheckedException e) {
    	... // 예외상황에 대처한다.
    }

    //상태 검사 메서드와 비검사 예외를 던지는 메서드 - 리팩터링 후
    if(obj.actionPermitted(args)){
    	obj.action(args);
    } else {
    	... // 예외상황에 대처한다.
    }
    ```
- 핵심 정리
    - 검사예외를 남용하면 고통, 필요한 곳에만 사용하여 프로그램 안전성을 높이자.

---
### Item72. 표준 예외를 사용하라 
- 자바 라이브러리는 대부분 API에서 쓰기에 충분한 수의 예외를 제공한다.
- 표준 예외를 재사용했을 때의 장점
    - 본인의 API를 다른 사람들이 사용하기 쉬워진다.(익숙하기 때문)
    - 가독성 UP(낯선 예외 사용 X)
    - 예외 클래스 수가 적어 메모리 사용량 Down, 클래스 적재 시간 단축
    
- 널리 재사용되는 예외들 : 상세한 내용은 책을 반복적으로 읽자
    - IllegalArgumentException : 허용되지 않는 값이 인수로 건네졌을 때(null은 따로 NPE로 처리)
    - IllegalStateException : 객체가 메서드를 수행하기에 적절하지 않은 상태일 때
    - NullPointerException : null을 허용하지 않은 메서드에 null을 건넸을 때
    - IndexOutOfBoundsException : 허용하지 않는 동시 수정이 발견됐을 때
    - UnsupportedOperationException : 호출한 메서드를 지원하지 않을 때
    
- Exception, RuntimeException, Throwable, Error는 직접 재사용하지 말자.   
  이 예외들은 다른 예외들의 상위 클래스이므로, 안정적으로 테스트할 수 없다.   
  (여러 성격의 예외들을 포괄하기 때문)
  
- 상황에 부합한다면 항상 표준 예외를 재사용하자.  
  API 문서를 참고해서 그 예외가 어떤 상황에서 던져지는지 꼭 확인하자.  
  예외의 이름뿐만 아니라 예외가 던져지는 맥락도 부합할 때만 재사용한다.
  
- 더 많은 정보를 제공하길 원한다면 표준 예외를 확장해도 좋다.

- 단 에외는 직렬화할 수 있다는 사실을 기억하자(12장 직렬화)   
  (직렬화에는 많은 부담이 따르니?) 이 사실만으로도 나만의 예외를 새로 만들지 않아야 할 근거

---
### Item73. 추상화 수준에 맞는 예외를 던저라 
- 예외 번역(Exception translation)
    상위 계층에서는 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꿔 던져야 한다.
    ```
    // 예외 번역 예시
    try {
    	... // 저수준 추상화를 이용한다.
    } catch(LowerLevelException e) {
    	// 추상화 수준에 맞게 번역한다.
    	throw new HigherLevelException(...);
    }

    // List<E> 인터페이스의 get 메서드 명시 확인? 없는데... 이렇게 되어있나보다.
    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (index < 0 || index >= size())
     */
    E get(int index);

    public E get (int index) {
    	ListIterator<E> i = listIterator(index);
    	try{
    	
    	} catch (NoSuchElementException e) {
    		throw new IndexOutOfBoundsException("인덱스 : " + index);
    	}
    }
    ```

- 예외 연쇄(exception chaining)
    예외를 번역할 때 저수준 예외가 디버깅에 도움이 된다면 예외 연쇄를 사용하는 것이 좋다.
    문제의 근본 원인(cause)인 저수준 예외를 고수준 예외에 실어보내는 방식이다.
    ```
    // 예외 연쇄
    try {
    	... // 저수준 추상화를 이용한다.
    } catch (LowerLevelException cause) {
    	// 저수준 예외를 고수준 예외에 실어 보낸다.
    	throw new HigherLevelException(cause);
    }

    // 예외 연쇄용 생성자
    class HigherLevelException extends Exception {
    	HigherLevelException(Throwable cause){
    		super(cause);
    	}
    }
    ```

    - 대부분의 표준 예외는 예외 연쇄용 생성자를 갖추고 있다.   
      그렇지 않더라도 Throwable의 initCause 메서드를 이용해 원인을 직접 못박을 수 있다.
    
- 무턱대고 에외를 전파하는 것보다 예외 번역이 우수한 방법이지만 남용해서는 안된다.   
  가능하다면 저수준 메서드가 반드시 성공하도록 하자.
- 그밖에 차선책
- 핵심 정리
    - 아래 계층의 예외를 예방하거나 스스로 처리할 수 없고,   
      그 예외를 상위 계층에 그대로 노출하기 곤란하다면 예외 번역을 사용하라.   
      이때 예외 연쇄를 이용하면 상위 계층에는 맥락에 어울리는 고수준 예외를 던지면서   
      근본 원인도 함께 알려주어 오류를 분석하기에 좋다(아이템 75).

---
### Item74. 메서드가 던지는 모든 예외를 문서화하라 
- 검사 예외는 항상 따로따로 선언하고,   
  각 예외가 발생하는 상황을 자바독의 @throws 태그를 사용하여 정확히 문서화 하자.
- 메서드가 던질 수 있는 예외를 각각 @throw 태그로 문서화하되,   
  비검사 예외는 메서드 선언의 throws 목록에 넣지 말자.
- 한 클래스에 정의된 많은 메서드가 같은 이유로 같은 예외를 던진다면   
  그 예외를 (각각의 메서드가 아닌) 클래스 설명에 추가하는 방법도 있다.

---
### Item75. 예외의 상세 메시지에 실패 관련 정보를 담으라  
- 예외를 잡지 못해 프로그램이 실패하면 자바 시스템은 그 예외의 스택 추적(stack trace) 정보를 자동으로 출력한다.
    - 스택 추적은 예외 객체의 toString 메서드를 호출해 얻는 문자열, 보통은 예외의 클래스 이름 뒤에 상세 메시지가 붙는 형태
    - 사후 분석을 위해 실패 순간의 상황을 정확히 포착해 예외의 상세 메시지를 담아야 한다.

- 실패 순간을 포착하려면 발생한 예외에 관여된 모든 매개변수와 필드의 값을 실패 메시지에 담아야한다.
    - 예시) IndexOutOfBoundsException
        - 범위의 최솟값, 최댓값, 그 범위를 벗어났다는 인덱스의 값

- 아이템 70 : 접근자 메서드를 적절히 제공하는 것이 좋다
     
---
### Item76. 가능한 한 실패 원자적으로 만들라 
- 실패 원자적 : 호출된 메서드가 실패하더라도 해당 객체는 메서드 호출 전 상태를 유지해야 한다.
- 메서드를 실패 원자적으로 만드는 방법
    1. 불변객체로 설계
    2. 가변객체의 메서드인 경우 작업 수행에 앞서 매개변수의 유효성을 검사한다.
    3. 실패할 가능성이 있는 모든 코드를 객체의 상태를 바꾸는 코드보다 앞에 매치하는 방법
    4. 객체의 임시 복사본에서 작업을 수행한 다음 작업이 성공적으로 완료되면 원래 객체와 교체
    5. 작업 도중 발생하는 실패를 가로채는 복구 코드를 작성하여 작업 전 상태로 되돌리는 방법
    
- 일반적으로 권장되는 덕목이지만 항상 달성할 수 있는 것은 아니다.(스레드)
- 항상 실패원자적으로 해야되는 것도 아니다. 달성하기 위한 비용이나 복잡도가 큰 연산도 있기 때문이다.

---
### Item77. 예외를 무시하지 말라 
- catch 블록을 비워두면 예외가 존재할 이유가 없어진다.(예외가 발생해도 무시된다)
- 예외를 무시해야 할 때
    - FileInputStream을 닫을 때
    - 예외를 무시하기로 했다면 catch 블록 안에 그렇게 결정한 이유를 주석으로 남기고 예외 변수의 이름도 ignored로 바꿔놓도록 하자.
