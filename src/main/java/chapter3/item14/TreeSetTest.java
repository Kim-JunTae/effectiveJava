package chapter3.item14;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        BigDecimal b1 = new BigDecimal("1.0");
        BigDecimal b2 = new BigDecimal("1.00");

        HashSet<BigDecimal> hashSet = new HashSet<>();
        hashSet.add(b1);
        hashSet.add(b2);

        TreeSet<BigDecimal> treeSet = new TreeSet<>();
        treeSet.add(b1);
        treeSet.add(b2);

        System.out.println("b1.equals(b2) " + b1.equals(b2));
        System.out.println("b2.equals(b1) " + b2.equals(b1));

        System.out.println(b1.compareTo(b2));
        System.out.println(b2.compareTo(b1));

        System.out.println(hashSet);
        System.out.println(treeSet);

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };

        Comparable comparable = new Comparable() {
            @Override
            public int compareTo(Object o) {
                return 0;
            }
        };

//        FakeBigDecimal fakeBigDecimal1 = new FakeBigDecimal(new BigDecimal("1.0"));
//        FakeBigDecimal fakeBigDecimal2 = new FakeBigDecimal(new BigDecimal("1.00"));
//
//        HashSet<FakeBigDecimal> fakeHashSet = new HashSet<>();
//
//        fakeHashSet.add(fakeBigDecimal1);
//        fakeHashSet.add(fakeBigDecimal2);
//
//        System.out.println(fakeHashSet);
    }

    static class FakeBigDecimal {
        BigDecimal bigDecimal;

        public FakeBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        @Override
        public int hashCode() {
            return 7;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FakeBigDecimal that = (FakeBigDecimal) o;
            return bigDecimal.equals(that.bigDecimal);
        }


    }
}
