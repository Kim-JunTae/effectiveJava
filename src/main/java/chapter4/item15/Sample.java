package chapter4.item15;

import lombok.Data;

import java.awt.*;

@Data
public class Sample {
    private String name;

    public Sample(String name) {
        this.name = name;
    }

    private class Inner{
        public int num;

        public Inner(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public static void main(String[] args) {
        Sample sample = new Sample("김진우(수염O)");
        sample.name = "김진우(수염2일차)";

        System.out.println(sample);
    }

}