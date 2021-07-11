package chapter2.item2.freeze;

public class freeze {
    private int a;
    private int b;
    private boolean frozen;

    public int getA(){
        return this.a;
    }

    public int getB(){
        return this.b;
    }

    public synchronized void setA(final int a){
        checkNotFrozen();
        this.a = a;
    }

    public synchronized void setB(final int b){
        checkNotFrozen();
        this.b = b;
    }

    public boolean isFrozen(){
        return this.frozen;
    }

    public synchronized void freeze(){
        this.frozen = true;
    }

    private void checkNotFrozen(){
        if(this.frozen) throw new RuntimeException();
    }

}
