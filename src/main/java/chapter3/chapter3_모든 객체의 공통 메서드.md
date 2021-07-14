# 3장 모든 객체의 공통 메서드
- Object는 객체를 만들 수 있는 구체 클래스이지만 기본적으로는 상속해서 사용하도록 설계되었다.   
  Object에서 final이 아닌 메서드(equals, hashCode, toString, clone, finalize)는   
  모두 재정의(overriding)를 염두해 두고 설계된 것이라 재정의 시 지켜야 하는 일반 규약이 명확히 정의되어 있다.
- final이 아닌 Object 메서드들을 언제 어떻게 재정의해야 하는지를 다룬다.
- Comparable.compareTo의 경우 성격이 비슷하여 같이 다룬다.
  
---
### Item10. equals는 일반 규약을 지켜 재정의하라

---
### Item11. equals를 재정의하려거든 hashCode도 재정의하라

---
### Item12. toString을 항상 재정의하라

---
### Item13. clone 재정의는 주의해서 진행하라

---
### Item14. Comparable을 구현할지 고려하라