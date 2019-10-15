package test;

public class A extends AbstractLifecycle{
    @Override
    public void init() {
        this.value =100;
        System.out.println("this is A");
        System.out.println(this.value);
    }
}
